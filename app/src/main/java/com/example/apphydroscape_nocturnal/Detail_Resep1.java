package com.example.apphydroscape_nocturnal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Detail_Resep1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_resep1);
        ImageView backbutton = findViewById(R.id.backbtn);
        TextView jdul = findViewById(R.id.textView10);
        TextView desk = findViewById(R.id.textView17);

        // Mengambil data yang dikirim dari ResepAdapter
        Intent intent = getIntent();
        if (intent != null) {
            String resep = intent.getStringExtra("resep");
            String deskripsi = intent.getStringExtra("deskripsi");

            // Lakukan sesuatu dengan data resep dan deskripsi yang diterima
            jdul.setText(resep);
            desk.setText(deskripsi);
        }

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Detail_Resep1.this, com.example.apphydroscape_nocturnal.CatalogRecipeActivity.class));
            }
        });
    }
}
