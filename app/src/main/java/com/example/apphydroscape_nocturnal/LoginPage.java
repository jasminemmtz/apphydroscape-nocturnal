package com.example.apphydroscape_nocturnal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginPage extends AppCompatActivity {
    private EditText password, email;
    private TextView signupTextView;
    private MaterialButton buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        signupTextView = findViewById(R.id.SignUpText);
        buttonLogin = findViewById(R.id.buttonlogin);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        // Check if the user is already logged in
        if (isLoggedIn()) {
            // If logged in, open the home activity
            Intent profileIntent = new Intent(LoginPage.this, HomePageActivity.class);
            startActivity(profileIntent);
            finish(); // Finish the login activity so the user can't go back to it
        }

        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signupintent = new Intent(LoginPage.this, SignupPage.class);
                startActivity(signupintent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aksiLogin();
            }
        });
    }
    // Check if the user is already logged in
    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        return sharedPreferences.contains("email") && sharedPreferences.contains("password");
    }

    public void aksiLogin() {
        // Mendapatkan password dari EditText
        String plainPassword = password.getText().toString();

        // Melakukan hashing password menggunakan MD5
        String hashedPassword = md5(plainPassword);

        String urlEndPoints = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-aeujc/endpoint/getUserByEmailPassword?email=" + email.getText().toString() + "&password=" + hashedPassword;

        StringRequest sr = new StringRequest(
                Request.Method.GET,
                urlEndPoints,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Login", "Raw Server Response: " + response);

                        try {
                            JSONObject userJson = new JSONObject(response);

                            if (userJson.has("email") && userJson.has("password")) {
                                String email = userJson.getString("email");
                                String hashedPassword = userJson.getString("password");

                                // Store user data in SharedPreferences
                                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", email);
                                editor.putString("password", hashedPassword);
                                editor.apply();

                                Toast.makeText(LoginPage.this, "Login successful!", Toast.LENGTH_SHORT).show();

                                Intent profileIntent = new Intent(LoginPage.this, HomePageActivity.class);
                                startActivity(profileIntent);
                            } else {
                                // Log an error if the expected fields are not present in the JSON response
                                Log.e("Login", "JSON response is missing expected fields");
                                Toast.makeText(LoginPage.this, "Login Unsuccessful! Unexpected response format", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // Log the exception for debugging
                            Log.e("Login", "Error parsing JSON: " + e.getMessage());
                            Toast.makeText(LoginPage.this, "Login Unsuccessful! Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            Toast.makeText(LoginPage.this, "Error: " + statusCode, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginPage.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
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
