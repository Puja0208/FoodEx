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

public class MenuForStudent extends AppCompatActivity {
    JSONArray ordered_items = new JSONArray();
    String canteen_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        canteen_id = bundle.getString("canteen_id");
        setContentView(R.layout.activity_menu_for_student);
        new MenuForStudent.Menu().execute(canteen_id);
    }

    private class Menu extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String[] params) {
            try{
                String  parameters=
                        URLEncoder.encode("canteen_id", "UTF-8")
                                + "="
                                + URLEncoder.encode(params[0], "UTF-8");

                URL url=new URL(com.example.pooja.foodex.url.base_url+"/GetMenuS");
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
                Log.i("menu",s);
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
                    final LinearLayout ll = (LinearLayout)findViewById(R.id.menu_lists);
                    ll.removeAllViews();
                    for (int i = 0; i < data.length(); i++) {
                        String text = "";
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

                        Button order_dish = new Button(getApplicationContext());
                        order_dish.setText("Order");
                        order_dish.setId(Integer.parseInt(dish_id));

                        // If an item is selected to be ordered, open confirm box to get quantity.
                        order_dish.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Get price for the dish.
                                final AlertDialog.Builder builder = new AlertDialog.Builder(MenuForStudent.this);
                                LayoutInflater inflater = MenuForStudent.this.getLayoutInflater();
                                final View inf = inflater.inflate(R.layout.ask_quantity, null);
                                builder.setView(inf)
                                        .setTitle("Quantity")
                                        .setPositiveButton(R.string.hint_add, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                String quantity = ((EditText)inf.findViewById(R.id.quantity)).getText().toString();
                                                Log.i("quantity",quantity);

                                                // Add to cart.
                                                JSONObject item = new JSONObject();
                                                try {
                                                    item.put("dish_id", dish_id);
                                                    item.put("quantity", quantity);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                ordered_items.put(item);
                                                /*dish.setVisibility(View.GONE);
                                                order_dish.setVisibility(View.GONE);*/

                                                dialog.dismiss();
                                                //Intent j = new Intent(MenuForStudent.this, MenuForStudent.class);
                                                //startActivity(j);
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
                        ll.addView(order_dish);

                    }
                    Button place_order_btn = new Button(getApplicationContext());
                    place_order_btn.setText("Place Order");
                    place_order_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("ORDERED_ITEMS", ordered_items.toString());
                            // Confirm dishes before placing order.
                            final AlertDialog.Builder builder = new AlertDialog.Builder(MenuForStudent.this);
                            LayoutInflater inflater = MenuForStudent.this.getLayoutInflater();
                            final View inf = inflater.inflate(R.layout.ordered_items, null);
                            builder.setView(inf)
                                    .setTitle("Place order for these items")
                                    .setPositiveButton(R.string.hint_place_order, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            String time_collect = ((EditText)inf.findViewById(R.id.collect_time)).getText().toString();
                                            Log.i("COLLECT_TIME", time_collect);
                                            new PlaceOrder().execute(ordered_items.toString(), time_collect);
                                            ordered_items = new JSONArray();
                                            dialog.dismiss();
                                        }
                                    })
                                    .setNegativeButton(R.string.hint_cancel, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    }).setMessage(ordered_items.toString());
                            builder.show();
                        }
                    });
                    ll.addView(place_order_btn);

                    Button place_order_back_btn = new Button(getApplicationContext());
                    place_order_back_btn.setText("BACK");
                    place_order_back_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent j = new Intent(MenuForStudent.this, Canteen_Page.class);
                            startActivity(j);
                        }
                    });
                    ll.addView(place_order_back_btn);


                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Unable to place order", Toast.LENGTH_LONG).show();
            }
        }

        private class PlaceOrder extends AsyncTask<String, String, String> {
            @Override
            protected String doInBackground(String[] params) {
                try{
                    String  parameters=
                            URLEncoder.encode("ordered_items", "UTF-8")
                                    + "="
                                    + URLEncoder.encode(params[0], "UTF-8")
                                    + "&"
                                    + URLEncoder.encode("time_of_collecting", "UTF-8")
                                    + "="
                                    + URLEncoder.encode(params[1], "UTF-8")
                                    + "&"
                                    + URLEncoder.encode("canteen_id", "UTF-8")
                                    + "="
                                    + URLEncoder.encode(canteen_id, "UTF-8");

                    URL url=new URL(com.example.pooja.foodex.url.base_url+"/TakeOrder");
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
                if(!result.equalsIgnoreCase("-1")) {
                    Toast.makeText(getApplicationContext(),
                            "Order is placed", Toast.LENGTH_LONG).show();
                }

                else{
                    Toast.makeText(getApplicationContext(),
                            "Order is not placed", Toast.LENGTH_LONG).show();
                }
            }

        }
    }
}
