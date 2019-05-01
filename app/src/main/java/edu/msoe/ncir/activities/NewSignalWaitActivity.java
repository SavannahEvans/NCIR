package edu.msoe.ncir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import edu.msoe.ncir.R;
import edu.msoe.ncir.udp.UDPClient;

public class NewSignalWaitActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.name.REPLY";
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_signal_wait);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabBack);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                programSignal(name);
            }
        }, 1000L);
    }

    /**
     * Handles the workflow for adding a new signal
     * @param name name of the signal
     */
    private void programSignal(String name) {
        boolean response = initSignalProgram(name);
        Intent replyIntent = new Intent();
        if(response) {
            int signalID = recordSignal(name);
            if (signalID == -1) {
                cancel();
            } else {
                replyIntent.putExtra(EXTRA_REPLY, signalID);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        } else {
            cancel();
        }
    }

    /**
     * This function sends to add command and determines
     * if the device is ready to record.
     * @param name This is the name of the signal
     * @return This is true if it is ready to record
     */
    private boolean initSignalProgram(String name) {
        boolean retVal = false;

        UDPClient.getInstance().send(("add_button," + name)); // send add command
        String response = UDPClient.getInstance().receive(); // wait for response (blocking)
        // Determine if the system is ready
        if(response.equalsIgnoreCase("\r\nready_to_record\r\n"))
        {
            retVal = true;
        }

        return retVal;
    }

    /**
     * This function receives a confirmation that the button was programmed
     * and the hardware index it was given.
     * @param name This is the name of the signal
     * @return This is the signal hardware index or -1 if it failed
     */
    private int recordSignal(String name) {
        int retVal = -1;

        // Wait for a confirmation (blocking)
        String response = UDPClient.getInstance().receive();

        // Determine if the button was recorded properly
        response = response.substring(2, response.length() - 2);
        String[] args = response.split(",");
        if(args.length == 3 && args[0].equalsIgnoreCase("button_saved") && args[1].equalsIgnoreCase(name)) {
            retVal = Integer.parseInt(args[2]);
        }

        return retVal;
    }

    private void cancel() {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }
}
