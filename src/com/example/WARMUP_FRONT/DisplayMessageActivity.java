package com.example.WARMUP_FRONT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Gongshi on 2/18/14.
 */
public class DisplayMessageActivity extends Activity {
    String ERR_BAD_CREDENTIALS = "-1";
    String ERR_BAD_USER_EXISTS = "-2";
    String ERR_BAD_USERNAME = "-3";
    String ERR_BAD_PASSWORD = "-4";
    String MAX_PASSWORD_LENGTH = "128";
    String MAX_USERNAME_LENGTH = "128";
    String SUCCESS = "1";
    TextView new_username;
    TextView new_count;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the message from the intent
        Intent intent = getIntent();
        String username = intent.getStringExtra(MyActivity.USERNAME);
        int count = intent.getIntExtra(MyActivity.COUNT, 1);
        // Create the text view
        setContentView(R.layout.newpage);

        new_username = (TextView) findViewById(R.id.textView);
        new_count = (TextView) findViewById(R.id.textView1);
        new_username.setText(username);
        new_count.setText("You have logged in " + Integer.toString(count) + " times");
    }


        public void logout(View view) {
            this.finish();

        }

        }

