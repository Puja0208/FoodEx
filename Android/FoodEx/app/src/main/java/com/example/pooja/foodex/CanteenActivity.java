package com.example.pooja.foodex;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class CanteenActivity extends AppCompatActivity {
    EditText canteen_id, password;
    Button btnLoginM;
    static JSONObject json= null;
    static String s = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen);
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        //initialise controls
        canteen_id = (EditText) findViewById(R.id.canteen_id);
        password = (EditText) findViewById(R.id.password);
        btnLoginM = (Button) findViewById(R.id.btnLoginM);
        btnLoginM.setOnClickListener(new View.OnClickListener()

        {
            @Override

            public void onClick(View v) {
                String Username = canteen_id.getText().toString();
                String pass = password.getText().toString();
                new Login().execute(Username, pass);
            }

            //close on listener
        });

    }

    private class Login extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {
            try{
                String  parameters=
                        URLEncoder.encode("canteen_id", "UTF-8")
                                + "="
                                + URLEncoder.encode(params[0], "UTF-8")
                                + "&"
                                + URLEncoder.encode("password", "UTF-8")
                                + "="
                                + URLEncoder.encode(params[1], "UTF-8");

                URL url=new URL(com.example.pooja.foodex.url.base_url+"/LoginM");
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
                        "Valid Credentials", Toast.LENGTH_LONG).show();
                Intent j = new Intent(CanteenActivity.this, Home_Canteen.class);
                startActivity(j);
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Invalid Credentials", Toast.LENGTH_LONG).show();

            }

        }

    }


}
