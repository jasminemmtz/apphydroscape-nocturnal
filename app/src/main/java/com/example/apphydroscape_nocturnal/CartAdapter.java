package com.example.apphydroscape_nocturnal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private List<CartModels> cartList; // Change to List from ArrayList
    private SharedPreferences sharedPreferences;

    // Updated constructor to initialize with data
    public CartAdapter(List<CartModels> cartList, Context context, SharedPreferences sharedPreferences) {
        this.cartList = cartList;
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }


    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your list_product.xml layout here and return a ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cart, parent, false);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CartModels cart = cartList.get(position);
        holder.nama_produk.setText(cart.getNama_produk());
        holder.harga_produk.setText(String.valueOf(cart.getHarga_produk()));
        holder.berat.setText(cart.getBerat() + " gram");
//        Glide.with(context)
//                .load(cart.get_pic()) // Menggunakan URL gambar dari basis data MongoDB
//                .into(holder.image);
        holder.quantity.setText(String.valueOf(cart.getQuantity()));

        // Implement onClickListeners for +/- buttons to update quantity and price
        holder.plusCardBtn.setOnClickListener(v -> {
            int currentQuantity = cart.getQuantity();
            cart.setQuantity(currentQuantity + 1);

            // Update harga produk dengan kelipatan harga awal terdekat
            cart.setHarga_produk(calculateNewPrice(cart.getHarga_produk(), cart.getQuantity()));
            notifyItemChanged(position);
            ((com.example.apphydroscape_nocturnal.CartActivity) context).updateTotalPrice(0); // Update price after changing quantity
        });

        holder.minusCardBtn.setOnClickListener(v -> {
            int currentQuantity = cart.getQuantity();
            if (currentQuantity > 1) {
                cart.setQuantity(currentQuantity - 1);

                // Update harga produk saat jumlah produk dikurangi
                cart.setHarga_produk(calculateDividedPrice(cart.getHarga_produk(), cart.getQuantity()));
                notifyItemChanged(position);
                ((com.example.apphydroscape_nocturnal.CartActivity) context).updateTotalPrice(0); // Update price after changing quantity
            } else {
                // Optionally, handle the case where the quantity reaches 1 or lower
                Toast.makeText(context, "Minimum quantity reached", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btn_deleteProduk.setOnClickListener(v -> {
            // Aksi untuk menghapus produk dari RecyclerView
            String id = cart.get_id();
            deleteProductFromEndpoint(id); // Panggil metode untuk menghapus produk dari endpoint

            // Hapus item dari list
            cartList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartList.size());

            // Perbarui total harga
            ((com.example.apphydroscape_nocturnal.CartActivity) context).updateTotalPrice(0);
        });
    }


    public void deleteProductFromEndpoint(String id) {
        Log.d("DATA", "Deleting product with ID: " + id);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String urlEndPoints = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-aeujc/endpoint/deleteCart?id="+id;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, urlEndPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DATA", "response: " + response);
                        // Handle successful deletion, if needed
                        // No need to navigate to CartActivity upon successful deletion, if it's not required
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("VolleyError", "Error deleting item: " + error.getMessage());

                        if (error.networkResponse != null) {
                            Log.e("VolleyError", "Status Code: " + error.networkResponse.statusCode);

                            // Handle different error codes, provide meaningful messages
                            if (error.networkResponse.statusCode == 400) {
                                Toast.makeText(context, "Bad request: Unable to delete product", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Failed to delete product", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        // Adding request to request queue
        requestQueue.add(stringRequest);
    }

    private int calculateNewPrice(int initialPrice, int quantity) {
        // Misalnya, mengembalikan harga baru berdasarkan harga awal dan jumlah produk
        int multiplier = initialPrice/quantity; // Mendapatkan kelipatan dari harga awal
        return initialPrice + multiplier;
    }
    private int calculateDividedPrice(int initialPrice, int quantity) {
        // Mengembalikan harga baru dengan membagi harga awal dengan jumlah produk
        if (quantity > 1) {
            int multiplier = initialPrice/quantity; // Mendapatkan kelipatan dari harga awal
            return initialPrice - multiplier;
        } else {
            // Jika jumlah produk adalah 1, kembalikan harga awal
            return initialPrice;
        }
    }




    // Method to update price based on quantity
    private void updateTotalPriceInActivity() {
        if (context instanceof com.example.apphydroscape_nocturnal.CartActivity) {
            ((com.example.apphydroscape_nocturnal.CartActivity) context).updateTotalPrice(0);
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare your views from list_product.xml here
        TextView nama_produk, berat, harga_produk, quantity;

        ImageView plusCardBtn, minusCardBtn, btn_deleteProduk;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your views here using findViewById
            // Example: titleTxt = itemView.findViewById(R.id.titleTxt);
            nama_produk = itemView.findViewById(R.id.namaProduklist);
            berat = itemView.findViewById(R.id.beratProduklist);
//            image = itemView.findViewById(R.id.pic2);
            harga_produk = itemView.findViewById(R.id.hargaProduklist);
            quantity = itemView.findViewById(R.id.quantityList);
            minusCardBtn = itemView.findViewById(R.id.minusCardBtnlist);
            plusCardBtn = itemView.findViewById(R.id.plusCardBtnlist);
            btn_deleteProduk = itemView.findViewById(R.id.deleteIconList);
        }
    }
}
