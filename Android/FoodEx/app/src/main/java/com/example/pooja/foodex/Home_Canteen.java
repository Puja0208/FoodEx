package com.example.pooja.foodex;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Home_Canteen extends AppCompatActivity {
    static JSONObject json= null;
    static String s = "";
    Button menu_btn, order_details, manage_acnt,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__canteen);
        new CanteenDetails().execute();

        menu_btn = (Button) findViewById(R.id.menu_btn);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Home_Canteen.this, Menu_Page.class);
                startActivity(j);
            }
        });

        order_details = (Button) findViewById(R.id.order_details);
        order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Home_Canteen.this, PendingOrder.class);
                startActivity(j);
            }
        });

        manage_acnt = (Button) findViewById(R.id.manage_acnt);
        manage_acnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Home_Canteen.this, CanteenAccount.class);
                startActivity(j);
            }
        });

        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Logout().execute();
            }
        });
    }

    private class CanteenDetails extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {
            try{
                Log.i("URL", com.example.pooja.foodex.url.base_url);
                URL url=new URL(com.example.pooja.foodex.url.base_url+"/CanteenHome");
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                BufferedReader reader= new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder reply = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    reply.append(line);
                }
                reader.close();
                s = reply.toString();
                Log.i("S", s);
                try {
                    json = new JSONObject(s);
                    if(!(json.getString("status").equals("false"))){
                        return json.getString("data");
                    }
                    else{
                        return "-1";
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }

            }catch (IOException e) {
                e.printStackTrace();
            }
            return "-1";
        }
        @Override
        protected void onPostExecute(String result) {
            if(!result.equalsIgnoreCase("-1")){
                try {
                    JSONArray jarr = new JSONArray(result);
                    json = jarr.getJSONObject(0);
                    String canteen_id = json.getString("canteen_id");
                    String name = json.getString("name");
                    String hostel_no = json.getString("hostel_no");
                    String opening_hrs = json.getString("opening_hrs");
                    String phone_no = json.getString("phone_no");
                    String open = json.getString("open");

                    TextView canteen_details = (TextView) findViewById(R.id.cntndetails);
                    canteen_details.setText("CANTEEN DETAILS"
                                            + canteen_id + "\n"
                                            + name + "\n"
                                            + hostel_no + "\n"
                                            + opening_hrs + "\n"
                                            + phone_no + "\n"
                                            + open + "\n");
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Canteen Datails not available", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class Logout extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {
            try{

                URL url=new URL(com.example.pooja.foodex.url.base_url + "/Logout");
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                BufferedReader reader= new BufferedReader(new InputStreamReader((conn.getInputStream())));

                StringBuilder reply = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    reply.append(line);
                }
                reader.close();
                s = reply.toString();
                try {
                    json = new JSONObject(s);
                    if(!(json.getString("status").equals("false"))){
                        return "1";
                    }
                    else{
                        return "-1";
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }

            }catch (IOException e) {
                e.printStackTrace();
            }
            return "-1";
        }
        @Override
        protected void onPostExecute(String result) {
            if(result.equalsIgnoreCase("1")){
                Toast.makeText(getApplicationContext(),
                        "Successfully Logged out", Toast.LENGTH_LONG).show();
                Intent j = new Intent(Home_Canteen.this, MainActivity.class);
                startActivity(j);
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Logout is unsuccessful ", Toast.LENGTH_LONG).show();

            }

        }

    }
}
