package com.example.tappyspaceship01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class WinScreenActivity extends AppCompatActivity {
    Button b1;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_screen);
        img  = findViewById(R.id.imageView4);
        b1  = findViewById(R.id.winbtn);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),
                        MainActivity.class);
                startActivity(intent);
            }
        });



    }
}
