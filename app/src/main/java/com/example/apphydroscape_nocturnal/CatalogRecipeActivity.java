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

public class CatalogRecipeActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCatalog;
    private ResepAdapter resepAdapter;
    private List<ResepModels> resepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_recipe);
        FloatingActionButton floatingActionButton = findViewById(R.id.cart_btn);
        Button backbutton = findViewById(R.id.backbtn);
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout profileLayout = findViewById(R.id.profilelayout);
        LinearLayout shopLayout = findViewById(R.id.shoplayout);
        LinearLayout trackorderLayout = findViewById(R.id.trackorderlayout);
        recyclerViewCatalog = findViewById(R.id.recycleViewResep);
        recyclerViewCatalog.setLayoutManager(new GridLayoutManager(this, 1)); // Atur layout manager menjadi GridLayoutManager dengan 2 kolom

        // Inisialisasi data produk dan adapter
        resepList = new ArrayList<>();
        resepAdapter = new ResepAdapter(resepList, this);

        // Set adapter ke RecyclerView
        recyclerViewCatalog.setAdapter(resepAdapter);

        //Load data produk
        fetchDataFromEndpoint();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CatalogRecipeActivity.this, com.example.apphydroscape_nocturnal.CartActivity.class));
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CatalogRecipeActivity.this, HomePageActivity.class));
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CatalogRecipeActivity.this, HomePageActivity.class));
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CatalogRecipeActivity.this, ProfileActivity.class));
            }
        });

        shopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CatalogRecipeActivity.this, CatalogSayurActivity.class));
            }
        });

        trackorderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CatalogRecipeActivity.this, TrackorderActivity.class));
            }
        });
    }
    private void fetchDataFromEndpoint() {
        String url = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-aeujc/endpoint/getAllResep";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Data", "Raw Server Response: " + response);
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                ResepModels resep = new ResepModels();
                                resep.set_id(jsonObject.getString("_id"));
//                                resep.set_pic(jsonObject.getString("image"));
                                resep.setResep(jsonObject.getString("resep"));
                                resep.setDeskripsi(jsonObject.getString("deskripsi"));

                                resepList.add(resep);
                            }

                            // Notify the adapter that the data has changed
                            resepAdapter.notifyDataSetChanged();
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

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}