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
                    EditText et_un = (EditText) findViewById(R.id.username);
                    EditText et_pw = (EditText) findViewById(R.id.password);
                    String un = et_un.getText().toString();
                    String pw = et_pw.getText().toString();

                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(urls[0]);

                    JSONObject holder = new JSONObject();
                    holder.put("password", pw);
                    holder.put("user", un);
                    StringEntity se = new StringEntity(holder.toString());
                    httppost.setEntity(se);
                    httppost.setHeader("Accept", "application/json");
                    httppost.setHeader("Content-type", "application/json");
                    ResponseHandler responseHandler = new BasicResponseHandler();

                    String response = (String) httpclient.execute(httppost, responseHandler);
                    return un + "||" + response;

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
                        message.setText("Invalid username and password combination. Please try again.");
                    } else if (jo.getInt("errCode") == -2) {
                        message.setText("This user name already exists. Please try again.");
                    } else if (jo.getInt("errCode") == -3) {
                        message.setText("The user name should not be empty and should be at most 128 characters long. Please try again.");
                    } else if (jo.getInt("errCode") == -4) {
                        message.setText("The password should be at most 128 characters long. Please try again.");
                    } else {
                        message.setText("unexpected errCode");
                    }

                } catch (Exception e) {
                    message.setText("unexpected error");
                }
            }
        }




    }

