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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.msoe.ncir.R;
import edu.msoe.ncir.adapters.DeviceListAdapter;
import edu.msoe.ncir.database.DeviceViewModel;
import edu.msoe.ncir.database.SignalViewModel;
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

        myDeviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
        myDeviceViewModel.getAllDevices().observe(this, new Observer<List<Device>>() {
            @Override
            public void onChanged(@Nullable final List<Device> devices) {
                // Update the cached copy of devices in adapter
                adapter.setDevices(devices);
            }
        });

        FloatingActionButton fabNext = findViewById(R.id.fabNext);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deviceID = adapter.getSelectedID();
                String ipaddr = adapter.getSelectedIpaddr();
                if(deviceID >= 0 && ipaddr.length() > 0) {
                    // TODO: Fix next line of code so doesn't crash!
                    Log.d("ipaddr: ", ipaddr);
                    connectToDevice(ipaddr, deviceID);
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
                    Device device = myDeviceViewModel.getDevice(deviceID).getValue();
                    if(device != null) {
                        String name = device.getName();
                        // Open the activity to add new connection
                        Intent intent = new Intent(DeviceSelectActivity.this, EditDeviceActivity.class);
                        intent.putExtra("DEVICE_NAME", name);
                        intent.putExtra("DEVICE_ID", deviceID);
                        startActivityForResult(intent, EDIT_DEVICE_ACTIVITY_REQUEST_CODE);
                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "Error",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void openRemoteSelect(int deviceID) {
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
                device.setIpaddr(ipaddr);
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

    /**
     * Updates the specified device by pulling information from that device and updating the database
     * @param deviceID
     */
    private void connectToDevice(String ipaddr, int deviceID) {
        Boolean successful = UDPClient.getInstance().buildConnection(ipaddr, 44444);
        if(successful) {
            updateDevice(deviceID);
        }
    }

    /**
     * Recieves data in the format ‘[button_name],[button_index]\r\n’
     * Updates the phone app to match the device
     */
    private void updateDevice(int deviceID) {
        // Get items from the device
        UDPClient.getInstance().send(("button_refresh")); // send refresh command
        String response = UDPClient.getInstance().receive(); // wait for response (blocking)
        Pattern pattern = Pattern.compile("(.*),(\\d+)");
        Matcher matcher = pattern.matcher(response);
        while (matcher.find( )) {
            Log.d("Matcher found", (matcher.group(1) + " " + matcher.group(2)));

            // Update the signals to match
            SignalViewModel signalViewModel = ViewModelProviders.of(this).get(SignalViewModel.class);
            int signalIndex = Integer.parseInt(matcher.group(2));
            String signalName = matcher.group(1);
            signalViewModel.update(deviceID, signalIndex, signalName);
        }
    }
}
