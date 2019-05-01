package edu.msoe.ncir.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import edu.msoe.ncir.R;
import edu.msoe.ncir.models.Remote;
import edu.msoe.ncir.adapters.RemoteListAdapter;
import edu.msoe.ncir.database.RemoteViewModel;

public class RemoteSelectActivity extends AppCompatActivity {

    private RemoteViewModel myRemoteViewModel;
    public static final int NEW_REMOTE_ACTIVITY_REQUEST_CODE = 1;
    private int deviceID = -1;
    private boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        connected = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_select);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final RemoteListAdapter adapter = new RemoteListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deviceID != -1) {
                    Intent intent = new Intent(RemoteSelectActivity.this, NewRemoteActivity.class);
                    startActivityForResult(intent, NEW_REMOTE_ACTIVITY_REQUEST_CODE);
                }
            }
        });

        FloatingActionButton fabNext = (FloatingActionButton) findViewById(R.id.fabNext);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int remoteID = adapter.getSelectedID();
                if (remoteID >= 0) {
                    openRemote(remoteID);
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            deviceID = extras.getInt("DEVICE_ID");
        }

        myRemoteViewModel = ViewModelProviders.of(this).get(RemoteViewModel.class);
        myRemoteViewModel.getRemotes(deviceID).observe(this, new Observer<List<Remote>>() {
            @Override
            public void onChanged(@Nullable final List<Remote> remotes) {
                // Update the cached copy of remotes in adapter
                adapter.setRemotes(remotes);
            }
        });
    }

    public void openRemote(int remoteID) {
        Intent intent = new Intent(this, RemoteActivity.class);
        if(remoteID != -1) {
            intent.putExtra("REMOTE_ID", remoteID);
            intent.putExtra("DEVICE_ID", deviceID);
            startActivity(intent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_REMOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Remote remote = new Remote(deviceID, data.getStringExtra(NewRemoteActivity.EXTRA_REPLY));
            myRemoteViewModel.insert(remote);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
