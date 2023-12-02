package com.example.apphydroscape_nocturnal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPopular, recyclerViewPopular2;
    private ProdukAdapter produkAdapter;
    private List<ProdukModels> produkList;

    private ResepAdapter resepAdapter;
    private List<ResepModels> resepList;
    private TextView sapaan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        // Find RecyclerView by its id
        recyclerViewPopular = findViewById(R.id.view1);
        recyclerViewPopular2 = findViewById(R.id.view2);

        // Set layout manager for RecyclerView (You can use LinearLayoutManager or GridLayoutManager)
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPopular2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize and set the adapter for RecyclerView
        // Assuming you have a list of ProdukModels named productList]
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        produkList = new ArrayList<>();
        produkAdapter = new ProdukAdapter(produkList, this, sharedPreferences);
        resepList = new ArrayList<>();
        resepAdapter = new ResepAdapter(resepList, this);
        recyclerViewPopular2.setAdapter(resepAdapter);
        recyclerViewPopular.setAdapter(produkAdapter);
        String email = sharedPreferences.getString("email","");
        getDataByEmail(email);
        sapaan = findViewById(R.id.namaSapaan);

        fetchDataFromEndpoint();
        fetchDataFromEndpoint2();
        bottom_navigation();

    }

    public void getDataByEmail(String email) {
        String urlEndPoints = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-aeujc/endpoint/getUserEmail?email=" + email;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                urlEndPoints,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Akun", "Raw Server Response: " + response);

                        try {
                            JSONArray userArray = new JSONArray(response);

                            if (userArray.length() > 0) {
                                JSONObject userJson = userArray.getJSONObject(0); // Get the first object in the array

                                // Check if the JSON response contains expected fields
                                if (userJson.has("email") && userJson.has("username")) {
                                    String id = userJson.getString("_id");
                                    String username = userJson.getString("username");
                                    sapaan.setText(username);


                                } else {
                                    // Log an error if the expected fields are not present in the JSON response
                                    Log.e("Data", "JSON response is missing expected fields");
                                    Toast.makeText(HomePageActivity.this, "Login Unsuccessful! Unexpected response format", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("Data", "Empty JSON array");
                                Toast.makeText(HomePageActivity.this, "Login Unsuccessful! Empty JSON array", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // Log the exception for debugging
                            Log.e("Data", "Error parsing JSON: " + e.getMessage());
                            Toast.makeText(HomePageActivity.this, "Login Unsuccessful! Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            Toast.makeText(HomePageActivity.this, "Error: " + statusCode, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomePageActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void bottom_navigation() {
        FloatingActionButton floatingActionButton = findViewById(R.id.cart_btn);
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        AppCompatButton buttonlihatproduk = findViewById(R.id.buttonlihatproduk);
        AppCompatButton buttonlihatresep = findViewById(R.id.buttonlihatresep);
        AppCompatButton buttonlihatvideo = findViewById(R.id.buttonlihatvideo);
        LinearLayout shopLayout = findViewById(R.id.shoplayout);
        LinearLayout profileLayout = findViewById(R.id.profilelayout);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, CartActivity.class));
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, HomePageActivity.class));//harusnya ke home
            }
        });

        shopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, CatalogSayurActivity.class));
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, ProfileActivity.class));
            }
        });

        buttonlihatproduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, CatalogSayurActivity.class));
            }
        });

        buttonlihatresep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, CatalogRecipeActivity.class));
            }
        });

        buttonlihatvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, VideoTourActivity.class));
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

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void fetchDataFromEndpoint2() {
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
                                resep.setResep(jsonObject.getString("resep"));
//                                resep.set_pic(jsonObject.getString("image"));
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