package com.example.apphydroscape_nocturnal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SignupPage extends AppCompatActivity {
    private MaterialButton singupBtn;
    private EditText username, password, email, no_hp;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        // Initialize views
        username = findViewById(R.id.username);
        password = findViewById(R.id.password2);
        email = findViewById(R.id.email2);
        no_hp = findViewById(R.id.no_hp);
        singupBtn = findViewById(R.id.btn_save);
        login = findViewById(R.id.logInText);

        singupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aksiSignup();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupPage.this, LoginPage.class);
                startActivity(i);
            }
        });
    }

    public void aksiSignup() {
        // Mendapatkan password dari EditText
        String plainPassword = password.getText().toString();

        // Melakukan hashing password menggunakan MD5
        String hashedPassword = md5(plainPassword);

        String urlEndPoints = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-aeujc/endpoint/insertUser";

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
                                    saveUserData();

                                }

                            } else {
                                Log.e("Registration", "Unexpected response format: " + response);
                                Toast.makeText(SignupPage.this, "Unexpected response format", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
//                            Log.e("Registration", "Error parsing JSON: " + e.getMessage());
//                            Toast.makeText(SignUp.this, "Registration failed! Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(SignupPage.this, "Akun Berhasil Terdaftar!", Toast.LENGTH_SHORT).show();
                        Intent loginIntent = new Intent(SignupPage.this, LoginPage.class);
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
                params.put("username", username.getText().toString());
                params.put("email", email.getText().toString());
                params.put("telepon", no_hp.getText().toString());
                params.put("password", hashedPassword);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
    }

    private boolean isJSONObjectValid(String json) {
        try {
            new JSONObject(json);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    private void saveUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData2", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username.getText().toString());
        editor.putString("email", email.getText().toString());
        editor.putString("telepon", no_hp.getText().toString());
        editor.apply();

    }

//    private void handleRegistrationFailure(JSONObject jsonResponse) {
//        String errorMessage = jsonResponse.optString("message", "Registration failed!");
//        Log.e("Registration", "Registration failed: " + errorMessage);
//        Toast.makeText(SignUp.this, errorMessage, Toast.LENGTH_SHORT).show();
//    }

    private void handleVolleyError(VolleyError error) {
        if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            String errorMessage = new String(error.networkResponse.data);
            Log.e("Registration", "Error: " + statusCode + ", Response: " + errorMessage);
            Toast.makeText(SignupPage.this, "Error: " + statusCode, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SignupPage.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
        }
    }
    private String md5(String input) {
        try {
            // Membuat instance dari MessageDigest untuk MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Mengubah string menjadi array byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Membuat string dalam format hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
