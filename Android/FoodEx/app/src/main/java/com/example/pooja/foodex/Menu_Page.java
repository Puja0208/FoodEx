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

public class Menu_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__page);
        new Menu().execute();

        Button menu_btn = (Button) findViewById(R.id.add_dish_btn);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Menu_Page.this, Add_Dish.class);

                startActivity(j);
            }
        });
        Button menu_back_btn = (Button) findViewById(R.id.menu_back_btn);
        menu_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Menu_Page.this, Home_Canteen.class);
                startActivity(j);
            }
        });
    }

    private class Menu extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String[] params) {
            try{
                URL url=new URL(com.example.pooja.foodex.url.base_url+"/GetMenuC");
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
                Log.i("sinmenu", s);
                try {
                    JSONObject json = new JSONObject(s);
                    if(!(json.getString("status").equals("false"))){
                        Log.i("list",json.getString("data"));
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
                    JSONArray data = new JSONArray(result);
                    if(data.length()==0){
                        Toast.makeText(getApplicationContext(),
                                "No Items", Toast.LENGTH_LONG).show();
                    }
                    final LinearLayout ll = (LinearLayout)findViewById(R.id.menu_list);
                    ll.removeAllViews();
                    for (int i = 0; i < data.length(); i++) {
                        String text = "";

                        // TextView for displaying details of dishes on menu.
                        TextView dish=new TextView(getApplicationContext());
                        final String dish_id = data.getJSONObject(i).getString("dish_id");
                        String dish_name = data.getJSONObject(i).getString("name");
                        String veg = data.getJSONObject(i).getString("veg");
                        String cooking_drn = data.getJSONObject(i).getString("cooking_drn");
                        Integer price = data.getJSONObject(i).getInt("price");
                        text = dish_id + "\t"+dish_name+"\t" +veg+ "\t"+ cooking_drn+"\t"+price.toString() + "\n";
                        dish.setText(text);
                        dish.setTextColor(0XFFF06D2F);
                        ll.addView(dish);

                        // Button for each dish on menu to remove it.
                        Button remove_from_menu = new Button(getApplicationContext());
                        remove_from_menu.setText("Remove from Menu");
                        remove_from_menu.setId(Integer.parseInt(dish_id));
                        remove_from_menu.setOnClickListener(new View.OnClickListener()

                        {
                            @Override

                            public void onClick(View v) {
                                new Remove_Dish_fromMenu().execute(dish_id);

                            }
                        });
                        ll.addView(remove_from_menu);
                    }
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

    private class Remove_Dish_fromMenu extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String[] params) {
            try{
                String  parameters=
                        URLEncoder.encode("dish_id", "UTF-8")
                                + "="
                                + URLEncoder.encode(params[0], "UTF-8");

                URL url=new URL(com.example.pooja.foodex.url.base_url+"/DeleteFromMenu");
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
            Toast.makeText(getApplicationContext(),
                    result, Toast.LENGTH_LONG).show();
            Intent j = new Intent(Menu_Page.this, Menu_Page.class);
            startActivity(j);
        }

    }
}
