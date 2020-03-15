package com.example.pooja.foodex;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Add_Dish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__dish);
        new DishList().execute();

        Button create_dish_btn = (Button) findViewById(R.id.create_dish_btn);
        create_dish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Add_Dish.this);
                LayoutInflater inflater = Add_Dish.this.getLayoutInflater();
                final View inf = inflater.inflate(R.layout.create_dish, null);
                builder.setView(inf)
                        .setTitle("Create Dish")
                        .setPositiveButton(R.string.hint_create, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String name = ((EditText)inf.findViewById(R.id.dish_name)).getText().toString();
                                String veg = ((EditText)inf.findViewById(R.id.veg)).getText().toString();
                                String duration = ((EditText)inf.findViewById(R.id.duration)).getText().toString();
                                Log.i("name",name);
                                new Create_Dish().execute(name, veg, duration);

                                dialog.dismiss();
                                Intent j = new Intent(Add_Dish.this, Add_Dish.class);
                                startActivity(j);

                            }
                        })
                        .setNegativeButton(R.string.hint_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                            }
                        });
                builder.show();
            }
        });

        Button add_dish_back_btn = (Button) findViewById(R.id.back_add_dish_btn);
        add_dish_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Add_Dish.this, Menu_Page.class);
                startActivity(j);
            }
        });
    }

    private class DishList extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String[] params) {
            try{
                URL url=new URL(com.example.pooja.foodex.url.base_url+"/Dishes");
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
                    final  LinearLayout ll = (LinearLayout)findViewById(R.id.dishlistLayout);
                    ll.removeAllViews();
                    for (int i = 0; i < data.length(); i++) {
                        String text = "";
                        TextView dish=new TextView(getApplicationContext());
                        final String dish_id = data.getJSONObject(i).getString("dish_id");
                        String name = data.getJSONObject(i).getString("name");
                        String veg  = data.getJSONObject(i).getString("veg");
                        String cooking_drn  = data.getJSONObject(i).getString("cooking_drn");
                        text = dish_id + "\t" + name +"\t"+ veg +"\t"+ cooking_drn+ "\n";
                        dish.setText(text);
                        dish.setTextColor(0XFFF06D2F);
                        ll.addView(dish);

                        Button add_to_menu = new Button(getApplicationContext());
                        add_to_menu.setText("Add to menu");
                        add_to_menu.setId(Integer.parseInt(dish_id));
                        add_to_menu.setOnClickListener(new View.OnClickListener()

                        {
                            @Override

                            public void onClick(View v) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(Add_Dish.this);
                                LayoutInflater inflater = Add_Dish.this.getLayoutInflater();
                                final View inf = inflater.inflate(R.layout.add_dish_tomenu, null);
                                builder.setView(inf)
                                        .setTitle("Add Dish")
                                        .setPositiveButton(R.string.hint_add, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                String price = ((EditText)inf.findViewById(R.id.price)).getText().toString();
                                                Log.i("name",price);
                                                new Add_Dish_toMenu().execute(dish_id,price);

                                                dialog.dismiss();
                                                Intent j = new Intent(Add_Dish.this, Add_Dish.class);
                                                startActivity(j);

                                            }
                                        })
                                        .setNegativeButton(R.string.hint_cancel, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                builder.show();
                            }
                        });
                        ll.addView(add_to_menu);
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


    private class Create_Dish extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String[] params) {
            try{
                String  parameters=
                        URLEncoder.encode("name", "UTF-8")
                                + "="
                                + URLEncoder.encode(params[0], "UTF-8")
                                + "&"
                                + URLEncoder.encode("veg", "UTF-8")
                                + "="
                                + URLEncoder.encode(params[1], "UTF-8")
                                + "&"
                                + URLEncoder.encode("cooking_drn", "UTF-8")
                                + "="
                                + URLEncoder.encode(params[2], "UTF-8");

                URL url=new URL(com.example.pooja.foodex.url.base_url+"/CreateDish");
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

        }

    }
    private class Add_Dish_toMenu extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String[] params) {
            try{
                String  parameters=
                        URLEncoder.encode("dish_id", "UTF-8")
                                + "="
                                + URLEncoder.encode(params[0], "UTF-8")
                                + "&"
                                + URLEncoder.encode("price", "UTF-8")
                                + "="
                                + URLEncoder.encode(params[1], "UTF-8");

                URL url=new URL(com.example.pooja.foodex.url.base_url+"/CreateMenu");
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

        }

    }
}
