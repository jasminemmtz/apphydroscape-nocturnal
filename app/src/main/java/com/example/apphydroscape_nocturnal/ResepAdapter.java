package com.example.apphydroscape_nocturnal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ResepAdapter extends RecyclerView.Adapter<ResepAdapter.ViewHolder> {
    private Context context;
    private List<ResepModels> resepList; // Change to List from ArrayList

    // Updated constructor to initialize with data
    public ResepAdapter(List<ResepModels> resepList, Context context) {
        this.resepList = resepList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your list_product.xml layout here and return a ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Bind data to your views in list_product.xml here using holder
        ResepModels resep = resepList.get(position);
        // Set your views based on the data from product
        // Example: holder.titleTxt.setText(product.getNama_produk());
        holder.resep.setText(resep.getResep());
        holder.deskripsi.setText(resep.getDeskripsi());

//        Glide.with(context)
//                .load(resep.get_pic()) // Menggunakan URL gambar dari basis data MongoDB
//                .into(holder.image);

        holder.btn_ReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResepModels resep = resepList.get(position);
                String resepString = resep.getResep();
//                String imageString = resep.get_pic();
                String deskripsiString = resep.getDeskripsi();

                // Membuat Intent untuk pindah ke Detail_Resep1 dan membawa data
                Intent intent;
                intent = new Intent(context, Detail_Resep1.class);
                intent.putExtra("resep", resepString);
//                intent.putExtra("image", imageString);
                intent.putExtra("deskripsi", deskripsiString);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resepList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare your views from list_product.xml here
        TextView resep, deskripsi;
        //        ImageView image;
        Button btn_ReadMore;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your views here using findViewById
            // Example: titleTxt = itemView.findViewById(R.id.titleTxt);
            resep = itemView.findViewById(R.id.titleresepTxt);
//            image = itemView.findViewById(R.id.pic2);
            deskripsi = itemView.findViewById(R.id.deskripsiTxt);
            btn_ReadMore = itemView.findViewById(R.id.buttonread);
        }
    }
}
