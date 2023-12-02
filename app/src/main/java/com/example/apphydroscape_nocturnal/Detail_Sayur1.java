package com.example.apphydroscape_nocturnal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class  Detail_Sayur1 extends AppCompatActivity {
    private TextView quantityTextView, detailHarga, detailberat, detailDayatahan, detailCaramenyimpan, detailDeskripsi, detailNamaProduk;

    private Button btn_addCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_sayur1);
        detailberat = findViewById(R.id.detailBerat);
        detailHarga = findViewById(R.id.detailHargaProduk);
        detailDayatahan = findViewById(R.id.detailLamaSimpan);
        detailCaramenyimpan = findViewById(R.id.detailCaraPenyimpanan);
        detailDeskripsi = findViewById(R.id.detailDeskripsiProduk);
        btn_addCart = findViewById(R.id.btnAddCart);
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String email2 = sharedPreferences.getString("email","");
        getDataByEmail(email2);
        btn_addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String email = sharedPreferences.getString("email", "");
                String nama_Produk = detailNamaProduk.getText().toString();
                Integer quantity = Integer.parseInt(quantityTextView.getText().toString());
                Integer harga_Produk = Integer.parseInt(detailHarga.getText().toString());
                Integer beratProduk = Integer.parseInt(detailberat.getText().toString());

                addToCart(email, nama_Produk,quantity,harga_Produk,beratProduk);
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            String namaProduk = intent.getStringExtra("nama_produk");

            // Gunakan data yang Anda terima untuk menampilkan informasi di layout Detail_Sayur1.xml
            detailNamaProduk = findViewById(R.id.detailNamaProduk);
            detailNamaProduk.setText(namaProduk);
            getDataProdukByNama(namaProduk);
        }

        ImageView backbutton = findViewById(R.id.backbtn);
        ImageView minusBtn = findViewById(R.id.minusbtn);
        ImageView plusBtn = findViewById(R.id.plusbtn);
        quantityTextView = findViewById(R.id.quantity);

        // Menetapkan fungsi klik untuk tombol minus
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementQuantity();
            }
        });

        // Menetapkan fungsi klik untuk tombol plus
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementQuantity();
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Detail_Sayur1.this, com.example.apphydroscape_nocturnal.CatalogSayurActivity.class));
            }
        });
    }

    private void addToCart(String email, String nama_Produk, Integer quantity, Integer harga_Produk, Integer beratProduk) {
        String url = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-aeujc/endpoint/insertCart"; // Replace with your actual endpoint URL

        Log.d("Debug", "Email: " + email);
        Log.d("Debug", "Nama Produk: " + nama_Produk);
        Log.d("Debug", "Quantity: " + quantity);
        Log.d("Debug", "Harga Produk: " + harga_Produk);
        Log.d("Debug", "Berat Produk: " + beratProduk);


        // Create a request
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Registration", "Raw Server Response: " + response);

                        try {

                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("success")) {
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                }

                            } else {
                                Log.e("Registration", "Unexpected response format: " + response);
                                Toast.makeText(Detail_Sayur1.this, "Unexpected response format", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
//                            Log.e("Registration", "Error parsing JSON: " + e.getMessage());
//                            Toast.makeText(SignUp.this, "Registration failed! Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(Detail_Sayur1.this, "Data Berhasil Ditambahkan!", Toast.LENGTH_SHORT).show();
//                        Intent loginIntent = new Intent(Detail_Sayur1.this, LoginPage.class);
//                        startActivity(loginIntent);
//                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        handleVolleyError(error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_produk", nama_Produk);
                params.put("email", email);
                params.put("harga_produk", String.valueOf(harga_Produk * quantity));
                params.put("berat", String.valueOf(beratProduk));
                params.put("quantity", String.valueOf(quantity));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
    }



    private void incrementQuantity() {
        // Mendapatkan nilai saat ini
        int quantity = Integer.parseInt(quantityTextView.getText().toString());

        // Menambah satu ke nilai
        quantity++;

        // Memperbarui nilai di TextView
        quantityTextView.setText(String.valueOf(quantity));
    }

    private void decrementQuantity() {
        // Mendapatkan nilai saat ini
        int quantity = Integer.parseInt(quantityTextView.getText().toString());

        // Memastikan nilai tidak kurang dari 0 sebelum mengurang
        if (quantity > 0) {
            // Mengurangkan satu dari nilai
            quantity--;
        }

        // Memperbarui nilai di TextView
        quantityTextView.setText(String.valueOf(quantity));
    }
    public void getDataProdukByNama(String namaProduk) {
        String urlEndPoints = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-aeujc/endpoint/getProdukByNamaProduk?nama_produk=" + namaProduk;

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
                                if (userJson.has("nama_produk")) {
                                    String namaProduk = userJson.getString("nama_produk");
                                    String hargaProduk = userJson.getString("harga_produk");
                                    String berat = userJson.getString("berat");
                                    String dayaTahan = userJson.getString("daya_tahan");
                                    String caraMenyimpan = userJson.getString("cara_menyimpan");
                                    String deskripsiProduk = userJson.getString("deskripsi_produk");

                                    detailNamaProduk.setText(namaProduk);
                                    detailberat.setText(berat);
                                    detailHarga.setText(hargaProduk);
                                    detailCaramenyimpan.setText(caraMenyimpan);
                                    detailDayatahan.setText(dayaTahan);
                                    detailDeskripsi.setText(deskripsiProduk);

                                } else {
                                    // Log an error if the expected fields are not present in the JSON response
                                    Log.e("Data", "JSON response is missing expected fields");
                                    Toast.makeText(Detail_Sayur1.this, "Login Unsuccessful! Unexpected response format", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("Data", "Empty JSON array");
                                Toast.makeText(Detail_Sayur1.this, "Login Unsuccessful! Empty JSON array", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // Log the exception for debugging
                            Log.e("Data", "Error parsing JSON: " + e.getMessage());
                            Toast.makeText(Detail_Sayur1.this, "Login Unsuccessful! Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            Toast.makeText(Detail_Sayur1.this, "Error: " + statusCode, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Detail_Sayur1.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void getDataByEmail(String email2) {
        String urlEndPoints = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-aeujc/endpoint/getUserEmail?email=" + email2;

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


                                } else {
                                    // Log an error if the expected fields are not present in the JSON response
                                    Log.e("Data", "JSON response is missing expected fields");
                                    Toast.makeText(Detail_Sayur1.this, "Login Unsuccessful! Unexpected response format", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("Data", "Empty JSON array");
                                Toast.makeText(Detail_Sayur1.this, "Login Unsuccessful! Empty JSON array", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // Log the exception for debugging
                            Log.e("Data", "Error parsing JSON: " + e.getMessage());
                            Toast.makeText(Detail_Sayur1.this, "Login Unsuccessful! Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            Toast.makeText(Detail_Sayur1.this, "Error: " + statusCode, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Detail_Sayur1.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}