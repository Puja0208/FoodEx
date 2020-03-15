package com.example.pooja.foodex;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class CanteenAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_account);
        new CanteenAccount.CAcntDetails().execute();
    }

    private class CAcntDetails extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String[] params) {
            try{
                URL url=new URL(com.example.pooja.foodex.url.base_url+"/CanteenAccount");
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
                Log.i("CACNT DETAILS", s);
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
                    final LinearLayout ll = (LinearLayout)findViewById(R.id.canteen_bill_list);
                    JSONArray jarr = new JSONArray(result);
                    JSONObject json = jarr.getJSONObject(0);
                    String id = json.getString("canteen_id");
                    String balance = json.getString("balance");
                    String dues = json.getString("dues");
                    TextView can_details = new TextView(getApplicationContext());
                    can_details.setText("CANTEEN ACCOUNT DETAILS" + "\n"
                            + "CANTEEN ID  " + id + "\n"
                            + "BALANCE  " + balance + "\n"
                            + "DUES  " + dues + "\n");
                    can_details.setTextColor(0XFFF06D2F);
                    ll.addView(can_details);
                    JSONArray bill_list = new JSONArray(json.getString("bill_data"));

                    for(int k=0; k<bill_list.length(); k++) {
                        JSONObject billObj = bill_list.getJSONObject(k);
                        TextView bill_text = new TextView(getApplicationContext());
                        final String bill_id = billObj.getString("bill_id");
                        bill_text.setText("BILL NO " + bill_id + "\n"
                                + "AMOUNT " + billObj.getString("amount") + "\n"
                                + "PAYMENT STATUS " + billObj.getString("payment_status") + "\n"
                                + "ORDER ID " + billObj.getString("order_id") + "\n"
                                + "DETAILS " + billObj.getString("details") + "\n");
                        bill_text.setTextColor(0XFFF06D2F);
                        ll.addView(bill_text);
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Canteen Account Datails not available", Toast.LENGTH_LONG).show();
            }

        }

    }
}
