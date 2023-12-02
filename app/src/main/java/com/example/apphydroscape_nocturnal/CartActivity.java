package com.example.apphydroscape_nocturnal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private List<CartModels> cartList;

    private TextView totalharga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        recyclerViewCart = findViewById(R.id.recycleViewCartt);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCart.setLayoutManager(layoutManager);
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(cartList, this, sharedPreferences);
        recyclerViewCart.setAdapter(cartAdapter);
        totalharga = findViewById(R.id.total_harga);
        String email = sharedPreferences.getString("email","");
        SharedPreferences sharedPreferences2 = getSharedPreferences("PurchaseStatus", MODE_PRIVATE);
        boolean purchaseCompleted = sharedPreferences2.getBoolean("purchaseCompleted", false);

        if (purchaseCompleted) {
            // If purchase is completed, hide the products
            // For example, hide the RecyclerView or update the adapter's data list
            totalharga.setVisibility(View.GONE);
            recyclerViewCart.setVisibility(View.GONE);
        }

        getDataCartByEmail(email);

        bottom_navigation();

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences2 = getSharedPreferences("PurchaseStatus", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putBoolean("purchaseCompleted", false);
        editor.apply();
    }


    public void getDataCartByEmail(String email) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String urlEndPoints = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-aeujc/endpoint/getCartByEmail?email=" + email;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlEndPoints, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("DATA", "Raw Response: " + response);
                        try {
                            // Clear the cartList before adding new data
                            cartList.clear();

                            // Parse the JSON response
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                CartModels cart = new CartModels();
                                cart.set_id(jsonObject.optString("_id"));
//                                cart.set_pic(jsonObject.getString("image"));
                                cart.setNama_produk(jsonObject.optString("nama_produk"));
                                cart.setHarga_produk(jsonObject.optInt("harga_produk"));
                                cart.setBerat(jsonObject.optInt("berat"));
                                cart.setQuantity(jsonObject.optInt("quantity"));

                                // Add the ProdukModels object to the list
                                cartList.add(cart);
                            }

                            // Notify the adapter that the data has changed
                            cartAdapter.notifyDataSetChanged();
                            updateTotalPrice(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONError", "Error parsing JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("VolleyError", "Error fetching data: " + error.getMessage());

                        if (error.networkResponse != null) {
                            Log.e("VolleyError", "Status Code: " + error.networkResponse.statusCode);
                        }
                    }
                });

        // Adding request to request queue
        requestQueue.add(jsonArrayRequest);
    }
    public void updateTotalPrice(int i) {
        int totalPrice = 0;

        for (CartModels cart : cartList) {
            totalPrice += cart.getHarga_produk() * cart.getQuantity();
        }

        TextView totalHargaTextView = findViewById(R.id.total_harga);
        totalHargaTextView.setText("Rp" + totalPrice);

        // Perbarui hargaProduklist di setiap item dalam CartAdapter
        cartAdapter.notifyDataSetChanged();
    }

    private void bottom_navigation() {
        FloatingActionButton floatingActionButton = findViewById(R.id.cart_btn);
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout shopLayout = findViewById(R.id.shoplayout);
        LinearLayout profileLayout = findViewById(R.id.profilelayout);
        LinearLayout trackorderLayout = findViewById(R.id.trackorderlayout);
        TextView checkoutbtn = findViewById(R.id.checkout_btn);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, CartActivity.class));
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, HomePageActivity.class));//harusnya ke home
            }
        });

        shopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, CatalogSayurActivity.class));
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, ProfileActivity.class));
            }
        });

        trackorderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, TrackorderActivity.class));
            }
        });

        checkoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
            }
        });
    }
}