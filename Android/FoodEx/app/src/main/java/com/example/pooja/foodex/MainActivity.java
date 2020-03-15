package com.example.pooja.foodex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnStudent = (Button) findViewById(R.id.btnStudent);
        btnStudent.setOnClickListener(new View.OnClickListener()

        {
            @Override

            public void onClick(View v) {
                Intent j = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(j);
            }

            //close on listener
        });
        Button btnCanteen = (Button) findViewById(R.id.btnCanteen);
        btnCanteen.setOnClickListener(new View.OnClickListener()

        {
            @Override

            public void onClick(View v) {
                Intent j = new Intent(MainActivity.this, CanteenActivity.class);
                startActivity(j);
            }

            //close on listener
        });
    }
}
