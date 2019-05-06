package edu.msoe.ncir.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import edu.msoe.ncir.udp.UDPClient;

/**
 * Activity for selecting a device to connect to.
 */
public class DeviceSelectActivity extends AppCompatActivity {

    private DeviceViewModel myDeviceViewModel;
    public static final int NEW_DEVICE_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_DEVICE_ACTIVITY_REQUEST_CODE = 2;

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
                String ipaddr = adapter.getSelectedIpaddr();
                if(deviceID >= 0 && ipaddr.length() > 0) {
                    // TODO: Fix next line of code so doesn't crash!
                    Log.d("ipaddr: ", ipaddr);
                    UDPClient.getInstance().buildConnection(ipaddr, 44444);
                    openRemoteSelect(deviceID);
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Cannot make the connection. Retry.",
                            Toast.LENGTH_LONG).show();
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

        FloatingActionButton fabEdit = findViewById(R.id.fabEdit);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deviceID = adapter.getSelectedID();
                if(deviceID >= 0) {
                    String name = myDeviceViewModel.getDevice(deviceID).getValue().getName();
                    // Open the activity to add new connection
                    Intent intent = new Intent(DeviceSelectActivity.this, EditDeviceActivity.class);
                    intent.putExtra("DEVICE_NAME", name);
                    intent.putExtra("DEVICE_ID", deviceID);
                    startActivityForResult(intent, EDIT_DEVICE_ACTIVITY_REQUEST_CODE);
                }
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
            String name = data.getStringExtra(NewDeviceActivity.EXTRA_NAME);
            String ipaddr = data.getStringExtra(NewDeviceActivity.EXTRA_IPADDR);
            Device device = new Device(name, ipaddr);
            myDeviceViewModel.insert(device);
        } else if(requestCode == EDIT_DEVICE_ACTIVITY_REQUEST_CODE) {
            String ipaddr = data.getStringExtra(EditDeviceActivity.EXTRA_REPLY);
            int deviceID = data.getIntExtra(EditDeviceActivity.EXTRA_REPLY_ID, -1);
            if(deviceID != -1) {
                Device device = myDeviceViewModel.getDevice(deviceID).getValue();
                // TODO: change the ipaddr on the device (dont worry too much about this)
            } else {
                Log.d("Device Edit", "Error for a random reason!");
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
