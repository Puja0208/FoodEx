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

public class StudentAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_account);
        new SAcntDetails().execute();
    }

    private class SAcntDetails extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String[] params) {
            try{
                URL url=new URL(com.example.pooja.foodex.url.base_url+"/StudentAccount");
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
                Log.i("SACNT DETAILS", s);
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
                    final LinearLayout ll = (LinearLayout)findViewById(R.id.student_bill_list);
                    JSONArray jarr = new JSONArray(result);
                    JSONObject json = jarr.getJSONObject(0);
                    String id = json.getString("student_id");
                    String balance = json.getString("balance");
                    String dues = json.getString("dues");
                    TextView student_details = new TextView(getApplicationContext());
                    student_details.setText("STUDENT ACCOUNT DETAILS" + "\n"
                            + "STUDENT ID  " + id + "\n"
                            + "BALANCE  " + balance + "\n"
                            + "DUES  " + dues + "\n");
                    student_details.setTextColor(0XFFF06D2F);
                    ll.addView(student_details);
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

                        if(billObj.getString("payment_status").equals("UNPAID")){
                            Button pay_btn = new Button(getApplicationContext());
                            pay_btn.setText("PAY");
                            pay_btn.setOnClickListener(new View.OnClickListener()

                            {
                                @Override

                                public void onClick(View v) {
                                    new Pay_bill().execute(bill_id);
                                }
                            });
                            ll.addView(pay_btn);
                        }
                    }

                    Button student_acnt_back_btn = new Button(getApplicationContext());
                    student_acnt_back_btn.setText("BACK");
                    student_acnt_back_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent j = new Intent(StudentAccount.this, Home.class);
                            startActivity(j);
                        }
                    });
                    ll.addView(student_acnt_back_btn);
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Student Account Datails not available", Toast.LENGTH_LONG).show();
            }

        }

    }

    private class Pay_bill extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {
            try{

                String  parameters=
                        URLEncoder.encode("bill_id", "UTF-8")
                                + "="
                                + URLEncoder.encode(params[0], "UTF-8");

                URL url=new URL(com.example.pooja.foodex.url.base_url+"/PayBill");
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
                Log.i("PAY BILL", s);
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
                Toast.makeText(getApplicationContext(),
                        "Bill Paid", Toast.LENGTH_LONG).show();
                Intent j = new Intent(StudentAccount.this, StudentAccount.class);
                startActivity(j);
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Could Not Pay Bill", Toast.LENGTH_LONG).show();
            }
        }
    }
}
