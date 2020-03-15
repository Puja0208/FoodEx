package com.example.pooja.foodex;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Canteen_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen__page);
        new Canteen_Page.CanteenList().execute();
    }

    private class CanteenList extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String[] params) {
            try{
                URL url=new URL(com.example.pooja.foodex.url.base_url+"/CanteenList");
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
                String s = reply.toString();
                Log.i("s",s);
                try {
                    JSONObject json = new JSONObject(s);
                    if(!(json.getString("status").equals("false"))){
                        return json.getString("data");
                    }
                    else{
                        return json.getString("data");
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
                    if(jarr.length()==0){
                        Toast.makeText(getApplicationContext(),
                                "No Items", Toast.LENGTH_LONG).show();
                    }
                    final LinearLayout ll = (LinearLayout)findViewById(R.id.canteen_list);
                    ll.removeAllViews();
                    JSONObject json;
                    for(int i=0; i<jarr.length(); i++) {
                        json = jarr.getJSONObject(i);
                        final String canteen_id = json.getString("canteen_id");
                        String name = json.getString("name");
                        String hostel_no = json.getString("hostel_no");
                        String opening_hrs = json.getString("opening_hrs");
                        String phone_no = json.getString("phone_no");
                        String open = json.getString("open");

                        Button canteen_details = new Button(getApplicationContext());
                        canteen_details.setText(canteen_id + "\t" + name + "\t" + hostel_no + "\n"
                                                + opening_hrs + "\t" + open + "\n"
                                                + phone_no + "\n");
                        canteen_details.setId(Integer.parseInt(canteen_id));
                        canteen_details.setOnClickListener(new View.OnClickListener()

                        {
                            @Override

                            public void onClick(View v) {
                                Intent j = new Intent(Canteen_Page.this, MenuForStudent.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("canteen_id",canteen_id);
                                j.putExtras(bundle);
                                startActivity(j);

                            }
                        });
                        ll.addView(canteen_details);
                    }
                    Button canteen_page_back_btn = new Button(getApplicationContext());
                    canteen_page_back_btn.setText("BACK");
                    canteen_page_back_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent j = new Intent(Canteen_Page.this, Home.class);
                            startActivity(j);
                        }
                    });
                    ll.addView(canteen_page_back_btn);
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
}
