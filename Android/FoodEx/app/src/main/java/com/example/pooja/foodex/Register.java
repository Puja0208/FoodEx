package com.example.pooja.foodex;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Register extends AppCompatActivity {
    EditText student_id, student_name, password;
    Button btnRegister;

    static JSONObject json= null;
    static String s = "";
    String Username = "";
    String Userid = "";
    String pass = "";

    static final int REQUEST_CAMERA=0;
    static final int SELECT_FILE=1;
    static int click_image=0;

    private void cameraIntent(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        ImageView Image = (ImageView)findViewById(R.id.Image);
        Image.setImageBitmap(thumbnail);
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ImageView Image = (ImageView)findViewById(R.id.Image);
        Image.setImageBitmap(bm);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    public String getStringImage(Bitmap bm){
        ByteArrayOutputStream ba=new ByteArrayOutputStream(  );
        bm.compress( Bitmap.CompressFormat.PNG,90,ba );
        byte[] by=ba.toByteArray();
        return Base64.encodeToString( by, Base64.DEFAULT );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //initialise controls
        student_id = (EditText) findViewById(R.id.student_id);
        student_name = (EditText) findViewById(R.id.student_name);
        password = (EditText) findViewById(R.id.password);

        Button btnPhoto =(Button) findViewById(R.id.btnPhoto);

        btnPhoto.setOnClickListener(new View.OnClickListener()

        {
            @Override

            public void onClick(View v) {
                final CharSequence[] items = { "Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo")) {
                            cameraIntent();
                            click_image=1;
                        } else if (items[item].equals("Choose from Library")) {
                            galleryIntent();
                            click_image=1;
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();


            }

            //close on listener
        });


        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener()

        {
            @Override

            public void onClick(View v) {
                Userid = student_id.getText().toString();
                Username = student_name.getText().toString();
                pass = password.getText().toString();
                String image="";
                if(click_image==1) {
                    ImageView Image = (ImageView) findViewById(R.id.Image);

                    image = getStringImage(((BitmapDrawable) Image.getDrawable()).getBitmap());
                }

                new Student_Register().execute(image);
            }

            //close on listener
        });

    }

    private class Student_Register extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {
            try{
                String  parameters=
                        URLEncoder.encode("student_id", "UTF-8")
                                + "="
                                + URLEncoder.encode(Userid, "UTF-8")
                                + "&"
                                + URLEncoder.encode("password", "UTF-8")
                                + "="
                                + URLEncoder.encode(pass, "UTF-8")
                                + "&"
                                + URLEncoder.encode("name", "UTF-8")
                                + "="
                                + URLEncoder.encode(Username, "UTF-8")
                                + "&"
                                + URLEncoder.encode("photo", "UTF-8")
                                + "="
                                + URLEncoder.encode(params[0], "UTF-8");


                URL url=new URL(com.example.pooja.foodex.url.base_url+"/Register");
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
                        "Register Successful", Toast.LENGTH_LONG).show();
                Intent j = new Intent(Register.this, LoginActivity.class);
                startActivity(j);
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Registration Unsuccessful", Toast.LENGTH_LONG).show();

            }

        }

    }


}
