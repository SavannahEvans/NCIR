package edu.msoe.ncir.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.msoe.ncir.R;

public class NewDeviceActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "com.example.android.name.REPLY_NAME";
    public static final String EXTRA_IPADDR = "com.example.android.name.REPLY_IPADDR";
    private EditText myEditNameView;
    private EditText myEditIpaddrView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_device);

        myEditNameView = findViewById(R.id.edit_name);
        myEditIpaddrView = findViewById(R.id.edit_ipaddr);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(myEditNameView.getText()) ||
                        TextUtils.isEmpty(myEditIpaddrView.getText()) ) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String name = myEditNameView.getText().toString();
                    String ipaddr = myEditIpaddrView.getText().toString();
                    replyIntent.putExtra(EXTRA_NAME, name);
                    replyIntent.putExtra(EXTRA_IPADDR, ipaddr);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
