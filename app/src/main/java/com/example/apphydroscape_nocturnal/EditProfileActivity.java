package com.example.apphydroscape_nocturnal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    private EditText usernameEt, emailEt, no_hpEt, idEtt;
    private Button editButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        Button backbutton = findViewById(R.id.backbtn5);
        usernameEt = findViewById(R.id.editusername2);
        emailEt = findViewById(R.id.editEmail2);
        no_hpEt = findViewById(R.id.edithp2);
        idEtt = findViewById(R.id.idEtt2);
        editButton = findViewById(R.id.editsave2);
        SharedPreferences sharedPreferences = getSharedPreferences("UserData2", MODE_PRIVATE);
        String nama2 = sharedPreferences.getString("username", "");
        String email2 = sharedPreferences.getString("email", "");
        String noHp2 = sharedPreferences.getString("telepon", "");
        String id2 = sharedPreferences.getString("_id", "");

        usernameEt.setText(nama2);
        emailEt.setText(email2);
        no_hpEt.setText(noHp2);
        idEtt.setText(id2);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aksiUpdateData(id2);
            }
        });

    }

    public void aksiUpdateData(String id) {
        Log.d("DATA", "response: " + id);
        String urlEndPoints = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-aeujc/endpoint/updateUserById?id=" + id;

        StringRequest sr = new StringRequest(
                Request.Method.PUT,
                urlEndPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("success")) {

                            } else {
//                                Log.e("Registration", "Unexpected response format: " + response);
//                                Toast.makeText(EditAkun.this, "Unexpected response format", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
//                            Log.e("Registration", "Error parsing JSON: " + e.getMessage());
//                            Toast.makeText(SignUp.this, "Registration failed! Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(EditProfileActivity.this, "Data Berhasil Terupdate!", Toast.LENGTH_SHORT).show();
                        Intent loginIntent = new Intent(EditProfileActivity.this, ProfileActivity.class);
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
                params.put("_id", idEtt.getText().toString());
                params.put("username", usernameEt.getText().toString());
                params.put("email", emailEt.getText().toString());
                params.put("telepon", no_hpEt.getText().toString());
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
            Toast.makeText(EditProfileActivity.this, "Error: " + statusCode, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(EditProfileActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
        }
    }
}