package edu.msoe.ncir.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import edu.msoe.ncir.R;

public class EditDeviceWaitActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.name.REPLY";
    private String deviceName;
    private String networkName;
    private String networkPassword;
    private String password;
    private String ipaddr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_device_wait);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabBack);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        Bundle extras = getIntent().getExtras();
        deviceName = extras.getString("DEVICE_NAME");
        networkName = extras.getString("NETWORK_NAME");
        networkPassword = extras.getString("NETWORK_PASSWORD");

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                provisionBoard();
                Intent replyIntent = new Intent();
                if (ipaddr != null) {
                    replyIntent.putExtra(EXTRA_REPLY, ipaddr);
                    setResult(RESULT_OK, replyIntent);
                    finish();
                } else {
                    cancel();
                }
            }
        }, 1000L);
    }

    private void provisionBoard() {
        // TODO: MAKE CONNECTION HERE, set ipaddr for the return
    }

    private void cancel() {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }
}
