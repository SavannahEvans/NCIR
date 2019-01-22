package edu.msoe.ncir;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class RemoteSelectActivity extends AppCompatActivity {

    private RemoteViewModel myRemoteViewModel;
    public static final int NEW_REMOTE_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RemoteSelectActivity.this, NewRemoteActivity.class);
                startActivityForResult(intent, NEW_REMOTE_ACTIVITY_REQUEST_CODE);
            }
        });

        FloatingActionButton fabNext = (FloatingActionButton) findViewById(R.id.fabNext);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to remote page
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final RemoteListAdapter adapter = new RemoteListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myRemoteViewModel = ViewModelProviders.of(this).get(RemoteViewModel.class);
        myRemoteViewModel.getAllRemotes().observe(this, new Observer<List<Remote>>() {
            @Override
            public void onChanged(@Nullable final List<Remote> remotes) {
                // Update the cached copy of remotes in adapter
                adapter.setRemotes(remotes);
            }
        });
//        String[] remotes = {"Full", "Simple", "Kids"};
//        ListAdapter remoteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, remotes);
//        ListView remotesListView = (ListView) findViewById(R.id.remotesListView);
//        remotesListView.setAdapter(remoteAdapter);

//        // On clicking a remote make a button pop up
//        remotesListView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onItemClick(View parent, View view, int position, long id) {
//                String remote = String.valueOf(parent.getItemAtPosition(position));
//
//            }
//        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_REMOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Remote remote = new Remote("Test", data.getStringExtra(NewRemoteActivity.EXTRA_REPLY));
            myRemoteViewModel.insert(remote);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
