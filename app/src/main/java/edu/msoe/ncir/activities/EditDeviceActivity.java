package edu.msoe.ncir.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.msoe.ncir.R;

public class EditDeviceActivity extends AppCompatActivity {

    private String deviceName;
    private String networkName;
    private String networkPassword;
    private int deviceID;
    private EditText myEditNetworkView;
    private EditText myEditNetworkPasswordView;

    private int NEW_NETWORK_WAIT_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_REPLY = "com.example.android.name.REPLY";
    public static final String EXTRA_REPLY_ID = "com.example.android.name.REPLY_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_device);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabBack);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        Bundle extras = getIntent().getExtras();
        deviceName = extras.getString("DEVICE_NAME");
        deviceID = extras.getInt("DEVICE_ID");

        myEditNetworkView = findViewById(R.id.edit_network);
        myEditNetworkPasswordView = findViewById(R.id.edit_password);

        final Button button = findViewById(R.id.button_continue);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(EditDeviceActivity.this, EditDeviceWaitActivity.class);
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(myEditNetworkView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                    finish();
                } else {
                    networkName = myEditNetworkView.getText().toString();
                    networkPassword = myEditNetworkPasswordView.getText().toString();
                    // TODO: Clean up these variable names
                    intent.putExtra("NETWORK_NAME", networkName);
                    intent.putExtra("DEVICE_NAME", deviceName);
                    intent.putExtra("NETWORK_PASSWORD", networkPassword);
                    startActivityForResult(intent, NEW_NETWORK_WAIT_ACTIVITY_REQUEST_CODE);
                }
            }
        });
    }

    private void cancel() {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent replyIntent = new Intent();
        if (requestCode == NEW_NETWORK_WAIT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String ipaddr = data.getStringExtra(EditDeviceWaitActivity.EXTRA_REPLY);
            replyIntent.putExtra(EXTRA_REPLY, ipaddr);
            replyIntent.putExtra(EXTRA_REPLY_ID, deviceID);
            setResult(RESULT_OK, replyIntent);
        } else {
            // TODO: Set toast to try again
            // setResult(RESULT_CANCELED, replyIntent);
        }
        finish();
    }
}
