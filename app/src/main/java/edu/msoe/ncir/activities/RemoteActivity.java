package edu.msoe.ncir.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import edu.msoe.ncir.R;
import edu.msoe.ncir.adapters.ButtonListAdapter;
import edu.msoe.ncir.adapters.SignalListAdapter;
import edu.msoe.ncir.database.ButtonViewModel;
import edu.msoe.ncir.database.SignalViewModel;
import edu.msoe.ncir.models.Button;
import edu.msoe.ncir.models.Signal;

/**
 * The Remote where buttons are displayed
 */
public class RemoteActivity extends AppCompatActivity {

    private ButtonViewModel myButtonViewModel;
    private SignalViewModel mySignalViewModel;
    private int remoteID = -1;
    private int deviceID = -1;
    public static final int NEW_SIGNAL_ACTIVITY_REQUEST_CODE = 1;
    public static final int NEW_BUTTON_ACTIVITY_REQUEST_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ButtonListAdapter adapter = new ButtonListAdapter(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final SignalListAdapter signalAdapter = new SignalListAdapter(this);

        // The remote and device ID's passed from previous activities
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            remoteID = extras.getInt("REMOTE_ID");
            deviceID = extras.getInt("DEVICE_ID");
        } else {
            finish();
        }

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(RemoteActivity.this, NewSignalActivity.class);
                    startActivityForResult(intent, NEW_SIGNAL_ACTIVITY_REQUEST_CODE);
            }
        });

        FloatingActionButton fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RemoteActivity.this, NewButtonActivity.class);
                intent.putExtra("DEVICE_ID", deviceID);
                startActivityForResult(intent, NEW_BUTTON_ACTIVITY_REQUEST_CODE);
            }
        });

        myButtonViewModel = ViewModelProviders.of(this).get(ButtonViewModel.class);
        myButtonViewModel.getButtons(remoteID).observe(this, new Observer<List<Button>>() {
            @Override
            public void onChanged(@Nullable final List<Button> buttons) {
                // Update the cached copy of buttons in adapter
                adapter.setButtons(buttons);
            }
        });

        mySignalViewModel = ViewModelProviders.of(this).get(SignalViewModel.class);
        mySignalViewModel.getSignals(deviceID).observe(this, new Observer<List<Signal>>() {
            @Override
            public void onChanged(@Nullable final List<Signal> signals) {
                // Update the cached copy of buttons in adapter
                adapter.setSignals(signals);
                signalAdapter.setSignals(signals);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_SIGNAL_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra(NewSignalActivity.EXTRA_REPLY_NAME);
                int index = data.getIntExtra(NewSignalActivity.EXTRA_REPLY_INDEX, -1);
                Signal signal = new Signal(deviceID, name, index);
                mySignalViewModel.insert(signal);
                Toast.makeText(
                        getApplicationContext(),
                        "New signal saved.",
                        Toast.LENGTH_LONG).show();
                Log.d("Signal", name + " with index " + index);
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "Error occurred, not saved.",
                        Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == NEW_BUTTON_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Button button = new Button(remoteID,
                    data.getIntExtra(NewButtonActivity.EXTRA_REPLY_ID, -1),
                    data.getStringExtra(NewButtonActivity.EXTRA_REPLY_NAME));
            if(button.getSignalID() != -1) {
                myButtonViewModel.insert(button);
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        R.string.add_button_error,
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
