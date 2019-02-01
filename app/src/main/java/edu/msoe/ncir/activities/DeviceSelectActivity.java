package edu.msoe.ncir.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import edu.msoe.ncir.R;
import edu.msoe.ncir.adapters.DeviceListAdapter;
import edu.msoe.ncir.database.DeviceViewModel;
import edu.msoe.ncir.models.Device;

/**
 * Activity for selecting a device to connect to.
 */
public class DeviceSelectActivity extends AppCompatActivity {

    private DeviceViewModel myDeviceViewModel;
    public static final int NEW_DEVICE_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_select);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final DeviceListAdapter adapter = new DeviceListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fabNext = findViewById(R.id.fabNext);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deviceID = adapter.getSelectedID();
                if(deviceID >= 0) {
                    openRemoteSelect(deviceID);
                }
            }
        });

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the activity to add new connection
                Intent intent = new Intent(DeviceSelectActivity.this, NewDeviceActivity.class);
                startActivityForResult(intent, NEW_DEVICE_ACTIVITY_REQUEST_CODE);
            }
        });



        myDeviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
        myDeviceViewModel.getAllDevices().observe(this, new Observer<List<Device>>() {
            @Override
            public void onChanged(@Nullable final List<Device> devices) {
                // Update the cached copy of devices in adapter
                adapter.setDevices(devices);
            }
        });
    }

    public void openRemoteSelect(int deviceID) {
        Intent intent = new Intent(this, RemoteSelectActivity.class);
        intent.putExtra("DEVICE_ID", deviceID);
        startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_DEVICE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Device device = new Device(data.getStringExtra(NewRemoteActivity.EXTRA_REPLY));
            myDeviceViewModel.insert(device);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
