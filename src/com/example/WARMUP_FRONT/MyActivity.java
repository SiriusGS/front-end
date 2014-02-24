package com.example.WARMUP_FRONT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;

public class MyActivity extends Activity {
    public final static String USERNAME = "com.example.WARMUP_FRONT.USERNAME";
    public final static String COUNT = "com.example.WARMUP_FRONT.COUNT";
    TextView message;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        message = (TextView) findViewById(R.id.textView);

    }
    /** Called when the user clicks the Send button */
    public void login(View view){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new GetResponse().execute("https://boiling-stream-9655.herokuapp.com/users/login");
        } else {
            message.setText("No network connection.");
        }}
    public void addUser(View view) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new GetResponse().execute("https://boiling-stream-9655.herokuapp.com/users/add");
        } else {
            message.setText("No network connection .");
        }
    }
    public void clearText() {
        ((EditText) findViewById(R.id.username)).setText("");
        ((EditText) findViewById(R.id.password)).setText("");
    }

    private class GetResponse extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            try {
                EditText new_uname = (EditText) findViewById(R.id.username);
                EditText new_passw = (EditText) findViewById(R.id.password);
                String username = new_uname.getText().toString();
                String password = new_passw.getText().toString();

                DefaultHttpClient https = new DefaultHttpClient();
                HttpPost httpp = new HttpPost(urls[0]);

                JSONObject holder = new JSONObject();
                holder.put("password", password);
                holder.put("user", username);
                StringEntity str = new StringEntity(holder.toString());
                httpp.setEntity(str);
                httpp.setHeader("Accept", "application/json");
                httpp.setHeader("Content-type", "application/json");
                ResponseHandler responseHandler = new BasicResponseHandler();

                String response = (String) https.execute(httpp, responseHandler);
                return username + "||" + response;

            } catch (Exception e) {
                e.printStackTrace();
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }


        @Override
        protected void onPostExecute(String result) {
            try{
                clearText();

                String name = result.split("\\|\\|")[0];
                String jresult = result.split("\\|\\|")[1];
                JSONObject jo = new JSONObject(jresult);
                if (jo.getInt("errCode") == 1) {
                    message.setText("Please enter your credentials below");
                    Intent intent = new Intent(getApplicationContext(), DisplayMessageActivity.class);
                    intent.putExtra(USERNAME, name);
                    intent.putExtra(COUNT, jo.getInt("count"));

                    startActivity(intent);
                } else if (jo.getInt("errCode") == -1) {
                    message.setText("Forget your password? Please try again.");
                } else if (jo.getInt("errCode") == -2) {
                    message.setText("Oh, this user name already exists. Please try again.");
                } else if (jo.getInt("errCode") == -3) {
                    message.setText("The user name should not be empty and <= 128 characters. Please try again.");
                } else if (jo.getInt("errCode") == -4) {
                    message.setText("The password should be <= 128 characters long. Please try again.");
                } else {
                    message.setText("unexpected errCode");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




}
