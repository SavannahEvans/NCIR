package edu.msoe.ncir.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.List;

import edu.msoe.ncir.R;

import edu.msoe.ncir.adapters.SignalListAdapter;
import edu.msoe.ncir.database.SignalViewModel;
import edu.msoe.ncir.models.Signal;

/**
 * Add a new button to the current remote
 */
public class NewButtonActivity extends AppCompatActivity {

    private SignalViewModel mySignalViewModel;
    public static final String EXTRA_REPLY_ID = "com.example.android.id.REPLY";
    public static final String EXTRA_REPLY_NAME = "com.example.android.name.REPLY";
    private int deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_button);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            deviceID = extras.getInt("DEVICE_ID");
        } else {
            finish();
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final SignalListAdapter adapter = new SignalListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final Button button = findViewById(R.id.button_add);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();

                String name = adapter.getSelectedName();
                int id = adapter.getSelectedID();

                replyIntent.putExtra(EXTRA_REPLY_ID, id);
                replyIntent.putExtra(EXTRA_REPLY_NAME, name);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });

        mySignalViewModel = ViewModelProviders.of(this).get(SignalViewModel.class);
        mySignalViewModel.getSignals(deviceID).observe(this, new Observer<List<Signal>>() {
            @Override
            public void onChanged(@Nullable final List<Signal> signals) {
                // Update the cached copy of remotes in adapter
                adapter.setSignals(signals);
            }
        });
    }
}
