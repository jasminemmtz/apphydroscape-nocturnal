package com.example.apphydroscape_nocturnal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.net.Uri;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

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

import java.net.URLEncoder;

public class ProfileActivity extends AppCompatActivity {
    private TextView usernamep, emailp,no_hpp, btn_logoutt, idUser, btn_customerserv;
    private ImageView keEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        FloatingActionButton floatingActionButton = findViewById(R.id.cart_btn);
        ImageView backbutton = findViewById(R.id.backbtn);
        LinearLayout shopLayout = findViewById(R.id.shoplayout);
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout profileLayout = findViewById(R.id.profilelayout);
        LinearLayout trackorderLayout = findViewById(R.id.trackorderlayout);
        usernamep = findViewById(R.id.usernameProfile);
        btn_customerserv = findViewById(R.id.textView23);
        emailp = findViewById(R.id.emailProfile);
        no_hpp = findViewById(R.id.no_hpProfile);
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String email = sharedPreferences.getString("email","");
        getDataUserbyEmail(email);
        keEdit = findViewById(R.id.keEditPf);
        btn_logoutt = findViewById(R.id.btn_logout);
        idUser = findViewById(R.id.idEtt2);
        idUser.setVisibility(View.INVISIBLE);

        btn_logoutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent i = new Intent(ProfileActivity.this, LoginPage.class);
                startActivity(i);
            }
        });
        btn_customerserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "6281314550254"; // Nomor WhatsApp yang akan dikirimkan pesan konfirmasi
                String message = "Halo Admin! Saya perlu bantuan";

                // Check if WhatsApp is installed
                if (isWhatsAppInstalled()) {
                    openWhatsApp(phoneNumber, message);
                } else {
                    // If WhatsApp is not installed, display a message
                    Toast.makeText(ProfileActivity.this, "WhatsApp tidak terinstall pada perangkat ini", Toast.LENGTH_SHORT).show();
                }
            }
        });



        keEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keEdit = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(keEdit);
            }
        });

//        ImageView editprofilebtn = findViewById(R.id.editprofilebtn);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, CartActivity.class));
            }
        });

//        editprofilebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
//            }
//        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, HomePageActivity.class));
            }
        });

        shopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, CatalogSayurActivity.class));
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
            }
        });

        trackorderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, TrackorderActivity.class));
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, HomePageActivity.class));
            }
        });
    }
    public void getDataUserbyEmail(String email) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String urlEndPoints = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-aeujc/endpoint/getUserByEmail?email=" + email;

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
                                String idust = jsonObject.getString("_id");
                                String nama = jsonObject.getString("username");
                                String emailBaru = jsonObject.getString("email");
                                String noHp = jsonObject.getString("telepon");

                                SharedPreferences sharedPreferences = getSharedPreferences("UserData2", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("_id", idust);
                                editor.putString("username", nama);
                                editor.putString("email", emailBaru);
                                editor.putString("telepon", noHp);
                                editor.apply();
                                usernamep.setText(nama);
                                emailp.setText(emailBaru);
                                no_hpp.setText(noHp);
                                idUser.setText(idust);
                            }
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

    // Method to open WhatsApp with the specified phone number
    private void openWhatsApp(String phoneNumber, String message) {
        try {
            PackageManager packageManager = getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);

            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + URLEncoder.encode(message, "UTF-8");
            i.setPackage("com.whatsapp");

            // Jika WhatsApp terinstall, buka melalui WhatsApp
            if (isWhatsAppInstalled()) {
                i.setData(Uri.parse(url));
                startActivity(i);
            } else {
                // Jika tidak terinstall, buka melalui browser
                Toast.makeText(ProfileActivity.this, "WhatsApp tidak terinstall. Membuka melalui browser.", Toast.LENGTH_SHORT).show();
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }


    private boolean isWhatsAppInstalled() {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}