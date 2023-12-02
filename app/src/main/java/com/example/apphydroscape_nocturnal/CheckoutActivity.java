package com.example.apphydroscape_nocturnal;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {
    private String subtotal = "Rp0";
    private String biayaKirim = "10000";
    private String total = "Rp0";
    private EditText alamatt,provinsii, kotaa, kodepos;
    private int hargaProduk = 0;
    private TextView subtotalTextView, biayaKirimTextView, totalTextView;
    private List<com.example.apphydroscape_nocturnal.CartModels> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
        TextView bayarbtn = findViewById(R.id.bayarbtn);
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String email = sharedPreferences.getString("email","");
        getDataCartByEmail(email);
        RadioGroup kurirGroup = findViewById(R.id.kurir);
        subtotalTextView = findViewById(R.id.subtotal);
        biayaKirimTextView = findViewById(R.id.biayakirim);
        totalTextView = findViewById(R.id.total);
        alamatt = findViewById(R.id.alamatlengkap);
        provinsii = findViewById(R.id.provinsi);
        kotaa = findViewById(R.id.kabupaten);
        kodepos = findViewById(R.id.kodepos);
        bayarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aksiCheckOut();
            }
        });
        getDataCartByEmail(email);
    }
    public void getDataCartByEmail(String email) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String urlEndPoints = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-aeujc/endpoint/getCartByEmail?email=" + email;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlEndPoints, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("DATA", "Raw Response: " + response);
                        int totalHargaProduk = 0;

                        try {
                            // Parse the JSON response without updating UI components
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String namaProduk = jsonObject.getString("nama_produk");
                                int hargaProdukItem = jsonObject.getInt("harga_produk");
                                totalHargaProduk += hargaProdukItem;
                            }
                            // Calculate values
                            subtotal = "Rp" + totalHargaProduk;
                            biayaKirim = "Rp" + 10000;
                            total = "Rp" + (totalHargaProduk + 10000);
                            // Update UI components
                            String finalSubtotal = subtotal;
                            String finalBiayaKirim = biayaKirim;
                            String finalTotal = total;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    subtotalTextView.setText(finalSubtotal);
                                    biayaKirimTextView.setText(finalBiayaKirim);
                                    totalTextView.setText(finalTotal);
                                }
                            });
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
    public void aksiCheckOut() {

        RadioGroup kurirGroup = findViewById(R.id.kurir);
        RadioGroup metodeBayarGroup = findViewById(R.id.metodebayar);

        int selectedKurirId = kurirGroup.getCheckedRadioButtonId();
        int selectedMetodeBayarId = metodeBayarGroup.getCheckedRadioButtonId();

        String selectedKurir = "";
        String selectedMetodeBayar = "";

        if (selectedKurirId != -1) {
            RadioButton selectedKurirButton = findViewById(selectedKurirId);
            selectedKurir = selectedKurirButton.getText().toString();
        }

        if (selectedMetodeBayarId != -1) {
            RadioButton selectedMetodeBayarButton = findViewById(selectedMetodeBayarId);
            selectedMetodeBayar = selectedMetodeBayarButton.getText().toString();
        }
        String urlEndPoints = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-aeujc/endpoint/insertTransaksi";

        String finalSelectedKurir = selectedKurir;
        String finalSelectedMetodeBayar = selectedMetodeBayar;
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                urlEndPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Registration", "Raw Server Response: " + response);

                        try {

                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("success")) {
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    // Save user data to SharedPreferences

                                }

                            } else {
                                Log.e("Registration", "Unexpected response format: " + response);
                                Toast.makeText(CheckoutActivity.this, "Unexpected response format", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
//                            Log.e("Registration", "Error parsing JSON: " + e.getMessage());
//                            Toast.makeText(SignUp.this, "Registration failed! Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(CheckoutActivity.this, "Pembeli Berhasil!", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences2 = getSharedPreferences("PurchaseStatus", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences2.edit();
                        editor.putBoolean("purchaseCompleted", true);
                        editor.apply();
                        Intent loginIntent = new Intent(CheckoutActivity.this, OrderAcceptedActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleVolleyError(error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String email2 = sharedPreferences.getString("email","");
                params.put("alamat_lengkap", alamatt.getText().toString());
                params.put("provinsi", provinsii.getText().toString());
                params.put("kota", kotaa.getText().toString());
                params.put("kodepos", kodepos.getText().toString());
                params.put("kurir", finalSelectedKurir);
                params.put("metode_pembayaran", finalSelectedMetodeBayar);
                params.put("subtotal", subtotal);
                params.put("ongkos_kirim", biayaKirim);
                params.put("total", total);
                params.put("email", email2);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
    }
    private void handleVolleyError(VolleyError error) {
        if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            String errorMessage = new String(error.networkResponse.data);
            Log.e("Registration", "Error: " + statusCode + ", Response: " + errorMessage);
            Toast.makeText(CheckoutActivity.this, "Error: " + statusCode, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(CheckoutActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
        }
    }

}