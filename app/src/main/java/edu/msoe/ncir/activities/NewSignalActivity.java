package edu.msoe.ncir.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.msoe.ncir.R;

public class NewSignalActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY_NAME = "com.example.android.name.REPLY_NAME";
    public static final String EXTRA_REPLY_INDEX = "com.example.android.name.REPLY_INDEX";

    private EditText myEditNameView;
    private int NEW_SIGNAL_WAIT_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_signal);

        myEditNameView = findViewById(R.id.edit_name);

        final Button button = findViewById(R.id.button_continue);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(NewSignalActivity.this, NewSignalWaitActivity.class);
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(myEditNameView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                    finish();
                } else {
                    String name = myEditNameView.getText().toString();
                    intent.putExtra("name", name);
                    startActivityForResult(intent, NEW_SIGNAL_WAIT_ACTIVITY_REQUEST_CODE);
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent replyIntent = new Intent();
        if (requestCode == NEW_SIGNAL_WAIT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            int index = data.getIntExtra(NewSignalWaitActivity.EXTRA_REPLY, -1);
            String name = myEditNameView.getText().toString();
            replyIntent.putExtra(EXTRA_REPLY_NAME, name);
            replyIntent.putExtra(EXTRA_REPLY_INDEX, index);
            setResult(RESULT_OK, replyIntent);
        } else {
            setResult(RESULT_CANCELED, replyIntent);
        }
        finish();
    }
}
