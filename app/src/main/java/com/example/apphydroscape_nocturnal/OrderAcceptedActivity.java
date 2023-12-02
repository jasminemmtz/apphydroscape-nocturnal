package com.example.apphydroscape_nocturnal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderAcceptedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_accepted);
        Button trackorderbtn = findViewById(R.id.trackorderbtn);
        TextView backhome = findViewById(R.id.backhome);

        trackorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderAcceptedActivity.this, TrackorderActivity.class));
            }
        });

        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderAcceptedActivity.this, HomePageActivity.class));
            }
        });
    }
}