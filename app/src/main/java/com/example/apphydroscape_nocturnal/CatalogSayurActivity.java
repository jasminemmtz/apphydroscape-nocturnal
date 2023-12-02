package com.example.apphydroscape_nocturnal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CatalogSayurActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCatalog;
    private ProdukAdapter produkAdapter;
    private List<ProdukModels> produkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_sayur);
        FloatingActionButton floatingActionButton = findViewById(R.id.cart_btn);
        Button backbutton = findViewById(R.id.backbtn);
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout profileLayout = findViewById(R.id.profilelayout);
        LinearLayout trackorderLayout = findViewById(R.id.trackorderlayout);
        // Inisialisasi RecyclerView
        recyclerViewCatalog = findViewById(R.id.recycleViewCatalog);
        recyclerViewCatalog.setLayoutManager(new GridLayoutManager(this, 2)); // Atur layout manager menjadi GridLayoutManager dengan 2 kolom

        // Inisialisasi data produk dan adapter
        produkList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        produkAdapter = new ProdukAdapter(produkList, this, sharedPreferences);

        // Set adapter ke RecyclerView
        recyclerViewCatalog.setAdapter(produkAdapter);

        //Load data produk
        fetchDataFromEndpoint();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CatalogSayurActivity.this, com.example.apphydroscape_nocturnal.CartActivity.class));
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CatalogSayurActivity.this, HomePageActivity.class));
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CatalogSayurActivity.this, HomePageActivity.class));
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CatalogSayurActivity.this, ProfileActivity.class));
            }
        });

        trackorderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CatalogSayurActivity.this, TrackorderActivity.class));
            }
        });
    }
    private void fetchDataFromEndpoint() {
        String url = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-aeujc/endpoint/getAllProducts";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Data", "Raw Server Response: " + response);
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                // Pastikan struktur JSON cocok dengan ProdukModels
                                ProdukModels produk = new ProdukModels();
                                produk.set_id(jsonObject.getString("_id"));
                                produk.set_pic(jsonObject.getString("image"));
                                produk.setNama_produk(jsonObject.getString("nama_produk"));
                                produk.setHarga_produk(jsonObject.getInt("harga_produk"));
                                produk.setBerat(jsonObject.getInt("berat"));

                                produkList.add(produk);
                            }

                            // Notify the adapter that the data has changed
                            produkAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}