package com.example.pooja.foodex;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PendingOrder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);
        new PendingOrder.showPendingOrders().execute();
    }

    private class showPendingOrders extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {
            try{
                URL url=new URL(com.example.pooja.foodex.url.base_url+"/PendingOrders");
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
                Log.i("PENDING_ORDERS", s);
                try {
                    JSONObject json = new JSONObject(s);
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
                    final LinearLayout ll = (LinearLayout)findViewById(R.id.pending_order_list);
                    JSONArray jarr = new JSONArray(result);
                    for(int i=0; i<jarr.length(); i++) {
                        JSONObject json = jarr.getJSONObject(i);
                        final String order_id = json.getString("order_id");
                        String stu_id = json.getString("student_id");
                        String time_of_order = json.getString("time_of_order");
                        String time_of_collecting = json.getString("time_of_collecting");
                        String status_of_order = json.getString("status_of_order");
                        JSONArray items_json = new JSONArray(json.getString("items"));
                        String items_string = "";
                        for(int j=0; j<items_json.length(); j++) {
                            JSONObject json1 = items_json.getJSONObject(j);
                            items_string += "\t" + json1.getString("name") + "\t"
                                            + json1.getString("quantity") + "\n";
                        }

                        // View details of the order to the canteen manager.
                        TextView order_text = new TextView(getApplicationContext());
                        order_text.setText("ORDER NO " + order_id + "\n"
                                + items_string + "\n"
                                + time_of_order + "\t"
                                + time_of_collecting + "\n"
                                + status_of_order + "\n");
                        order_text.setTextColor(0XFFF06D2F);
                        ll.addView(order_text);

                        if(status_of_order.equals("PENDING")) {
                            // Button to notify student regarding status order.
                            Button confirm_btn = new Button(getApplicationContext());
                            confirm_btn.setText("Confirm");
                            confirm_btn.setOnClickListener(new View.OnClickListener()

                            {
                                @Override

                                public void onClick(View v) {

                                    new Confirm_order().execute(order_id);

                                }
                            });
                            ll.addView(confirm_btn);

                            Button reject_btn = new Button(getApplicationContext());
                            reject_btn.setText("Reject");
                            reject_btn.setOnClickListener(new View.OnClickListener()

                            {
                                @Override

                                public void onClick(View v) {

                                    new Reject_order().execute(order_id);

                                }
                            });
                            ll.addView(reject_btn);
                        }
                        else if(status_of_order.equals("CONFIRMED")){
                            Button ready_btn = new Button(getApplicationContext());
                            ready_btn.setText("READY");
                            ready_btn.setOnClickListener(new View.OnClickListener()

                            {
                                @Override

                                public void onClick(View v) {

                                    new Ready_order().execute(order_id);

                                }
                            });
                            ll.addView(ready_btn);
                        }

                    }

                    Button pending_order_back_btn = new Button(getApplicationContext());
                    pending_order_back_btn.setText("BACK");
                    pending_order_back_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent j = new Intent(PendingOrder.this, Home_Canteen.class);
                            startActivity(j);
                        }
                    });
                    ll.addView(pending_order_back_btn);

                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Pending Order Datails not available", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class Confirm_order extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {
            try{

                String  parameters=
                        URLEncoder.encode("order_id", "UTF-8")
                                + "="
                                + URLEncoder.encode(params[0], "UTF-8")
                                + "&"
                                +URLEncoder.encode("status", "UTF-8")
                                + "="
                                + URLEncoder.encode("CONFIRMED", "UTF-8");
                URL url=new URL(com.example.pooja.foodex.url.base_url+"/UpdateStatus");
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());
                wr.write(parameters);
                wr.flush();

                BufferedReader reader= new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder reply = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    reply.append(line);
                }
                reader.close();
                String s = reply.toString();
                try {
                    JSONObject json = new JSONObject(s);
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
            if(!result.equalsIgnoreCase("-1")){
                Intent j = new Intent(PendingOrder.this, PendingOrder.class);
                startActivity(j);
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Order is not confirmed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class Reject_order extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {
            try{

                String  parameters=
                        URLEncoder.encode("order_id", "UTF-8")
                                + "="
                                + URLEncoder.encode(params[0], "UTF-8")
                                + "&"
                                +URLEncoder.encode("status", "UTF-8")
                                + "="
                                + URLEncoder.encode("REJECTED", "UTF-8");

                URL url=new URL(com.example.pooja.foodex.url.base_url+"/UpdateStatus");
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());
                wr.write(parameters);
                wr.flush();

                BufferedReader reader= new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder reply = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    reply.append(line);
                }
                reader.close();
                String s = reply.toString();
                try {
                    JSONObject json = new JSONObject(s);
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
            if(!result.equalsIgnoreCase("-1")){
                Intent j = new Intent(PendingOrder.this, PendingOrder.class);
                startActivity(j);
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Order is not rejected", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class Ready_order extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {
            try{

                String  parameters=
                        URLEncoder.encode("order_id", "UTF-8")
                                + "="
                                + URLEncoder.encode(params[0], "UTF-8")
                                + "&"
                                +URLEncoder.encode("status", "UTF-8")
                                + "="
                                + URLEncoder.encode("READY", "UTF-8");

                Log.i("URL", com.example.pooja.foodex.url.base_url);
                URL url=new URL(com.example.pooja.foodex.url.base_url+"/UpdateStatus");
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());
                wr.write(parameters);
                wr.flush();

                BufferedReader reader= new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder reply = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    reply.append(line);
                }
                reader.close();
                String s = reply.toString();
                Log.i("S", s);
                try {
                    JSONObject json = new JSONObject(s);
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
            if(!result.equalsIgnoreCase("-1")){
                Intent j = new Intent(PendingOrder.this, PendingOrder.class);
                startActivity(j);
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Order is not delivered", Toast.LENGTH_LONG).show();
            }
        }
    }
}
