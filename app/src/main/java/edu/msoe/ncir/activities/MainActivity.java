package edu.msoe.ncir.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.msoe.ncir.R;
import edu.msoe.ncir.udp.UDPClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openDeviceSelect();
            }
        }, 2000L);
    }

    public void openDeviceSelect() {
        Intent intent = new Intent(this, DeviceSelectActivity.class);
        startActivity(intent);
    }
}
