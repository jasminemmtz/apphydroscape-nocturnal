package com.example.apphydroscape_nocturnal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TrackorderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackorder);
        FloatingActionButton floatingActionButton = findViewById(R.id.cart_btn);
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout profileLayout = findViewById(R.id.profilelayout);
        LinearLayout shopLayout = findViewById(R.id.shoplayout);
        LinearLayout trackorderLayout = findViewById(R.id.trackorderlayout);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrackorderActivity.this, CartActivity.class));
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrackorderActivity.this, HomePageActivity.class));
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrackorderActivity.this, ProfileActivity.class));
            }
        });

        shopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrackorderActivity.this, CatalogSayurActivity.class));
            }
        });

        trackorderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrackorderActivity.this, TrackorderActivity.class));
            }
        });
    }
}