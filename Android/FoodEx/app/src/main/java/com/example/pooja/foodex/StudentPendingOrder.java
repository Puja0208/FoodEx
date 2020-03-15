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
import java.net.HttpURLConnection;
import java.net.URL;

public class StudentPendingOrder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_pending_order);
        new StudentPendingOrder.myPendingOrders().execute();
    }

    private class myPendingOrders extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String[] params) {
            try{
                Log.i("URL", com.example.pooja.foodex.url.base_url);
                URL url=new URL(com.example.pooja.foodex.url.base_url+"/StudentOrderStatus");
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
                Log.i("MY_PENDING_ORDERS", s);
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
                    final LinearLayout ll = (LinearLayout)findViewById(R.id.my_pending_order_list);
                    JSONArray jarr = new JSONArray(result);
                    for(int i=0; i<jarr.length(); i++) {
                        JSONObject json = jarr.getJSONObject(i);
                        final String order_id = json.getString("order_id");
                        String time_of_order = json.getString("time_of_order");
                        String time_of_collecting = json.getString("time_of_collecting");
                        String status_of_order = json.getString("status_of_order");
                        JSONArray items_json = new JSONArray(json.getString("items"));
                        String items_string = "";
                        for (int j = 0; j < items_json.length(); j++) {
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

                    }

                    Button my_order_back_btn = new Button(getApplicationContext());
                    my_order_back_btn.setText("BACK");
                    my_order_back_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent j = new Intent(StudentPendingOrder.this, Home.class);
                            startActivity(j);
                        }
                    });
                    ll.addView(my_order_back_btn);

                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Your Pending Order Datails not available", Toast.LENGTH_LONG).show();
            }
        }
    }
}
