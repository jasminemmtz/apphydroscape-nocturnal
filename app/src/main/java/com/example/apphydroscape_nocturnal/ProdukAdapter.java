package com.example.apphydroscape_nocturnal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ViewHolder> {
    private Context context;
    private List<ProdukModels> productList; // Change to List from ArrayList
    private SharedPreferences sharedPreferences;

    // Updated constructor to initialize with data
    public ProdukAdapter(List<ProdukModels> productList, Context context, SharedPreferences sharedPreferences) {
        this.productList = productList;
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your list_product.xml layout here and return a ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProdukModels product = productList.get(position);
        holder.nama_produk.setText(product.getNama_produk());
        holder.harga_produk.setText("Rp." + product.getHarga_produk());
        holder.berat.setText(product.getBerat() + " gram");

        Glide.with(context)
                .load(product.get_pic()) // Menggunakan URL gambar dari basis data MongoDB
                .into(holder.image);

        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProdukModels selectedProduct = productList.get(position);
                Intent intent = new Intent(context, Detail_Sayur1.class);
                intent.putExtra("image", selectedProduct.get_pic());
                intent.putExtra("nama_produk", selectedProduct.getNama_produk());
                intent.putExtra("harga_produk", selectedProduct.getHarga_produk());
                intent.putExtra("berat_produk", selectedProduct.getBerat());
                context.startActivity(intent);
            }
        });
        holder.btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch the selected product
                ProdukModels selectedProduct = productList.get(position);
                sendCartRequest(selectedProduct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare your views from list_product.xml here
        TextView nama_produk, berat, harga_produk;
        ImageView image;
        Button btn_detail, btn_cart;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your views here using findViewById
            // Example: titleTxt = itemView.findViewById(R.id.titleTxt);
            nama_produk = itemView.findViewById(R.id.titleTxt);
            image = itemView.findViewById(R.id.pic);
            berat = itemView.findViewById(R.id.gramTxt);
            harga_produk = itemView.findViewById(R.id.hargaTxt);
            btn_detail = itemView.findViewById(R.id.buttondetail);
            btn_cart = itemView.findViewById(R.id.buttoncart);
        }
    }
    // Method to handle the Volley POST request
    private void sendCartRequest(ProdukModels selectedProduct) {
        String url = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-aeujc/endpoint/insertCart"; // Replace with your endpoint URL
        String userEmail = sharedPreferences.getString("email", ""); // Replace with the user's email

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server (if needed)
                        // For example, show a success message
                        Toast.makeText(context, "Added to cart successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors that occur in the request
                        // For example, show an error message
                        Toast.makeText(context, "Error adding to cart", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Pass parameters to your endpoint
                Map<String, String> params = new HashMap<>();
                params.put("nama_produk", selectedProduct.getNama_produk());
                params.put("image", selectedProduct.get_pic());
                params.put("harga_produk", String.valueOf(selectedProduct.getHarga_produk()));
                params.put("berat", String.valueOf(selectedProduct.getBerat()));
                params.put("email", userEmail);
                params.put("quantity", "1"); // Default quantity

                return params;
            }
        };

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
}
