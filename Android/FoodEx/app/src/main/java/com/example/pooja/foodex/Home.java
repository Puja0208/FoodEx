package com.example.pooja.foodex;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class Home extends AppCompatActivity {
    static JSONObject json= null;
    static String s = "";
    Button place_order, view_acnt, logout, status_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        new StudentDetails().execute();

        place_order = (Button) findViewById(R.id.order_btn);
        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Home.this, Canteen_Page.class);
                startActivity(j);
            }
        });

        view_acnt = (Button) findViewById(R.id.view_acnt_btn);
        view_acnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Home.this, StudentAccount.class);
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

        status_order = (Button) findViewById(R.id.my_order_status_btn);
        status_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Home.this, StudentPendingOrder.class);
                startActivity(j);
            }
        });

    }

    private class StudentDetails extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {
            try{
                Log.i("URL", com.example.pooja.foodex.url.base_url);
                URL url=new URL(com.example.pooja.foodex.url.base_url+"/StudentHome");
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
                    Log.i("RESULT", result);
                    JSONArray jarr = new JSONArray(result);
                    json = jarr.getJSONObject(0);
                    String id = json.getString("student_id");
                    String name = json.getString("name");
                    String image="";
                    if(json.getString("photo")!=null) {
                        image = json.getString("photo");
                    }
                    ImageView Image = (ImageView) findViewById(R.id.image);
                    byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    Image.setImageBitmap(decodedByte);
                    TextView student_details = (TextView) findViewById(R.id.studetails);
                    student_details.setText("STUDENT DETAILS" + "\n"
                            + id + "\n"
                            + name + "\n");
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Student Details not available", Toast.LENGTH_LONG).show();
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
                Intent j = new Intent(Home.this, MainActivity.class);
                startActivity(j);
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Logout is unsuccessful ", Toast.LENGTH_LONG).show();

            }

        }

    }
}
