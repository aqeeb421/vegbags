package com.vegbags.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vegbags.R;
import com.vegbags.ResponseModels.ProductModel;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<ProductModel> productModelList;
    private RecyclerInterface recyclerInterface;

    public ProductAdapter(List<ProductModel> productModelList, RecyclerInterface recyclerInterface) {
        this.productModelList = productModelList;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.product_item, parent, false);
        ProductAdapter.ViewHolder viewHolder = new ProductAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {

        if (position != -1 && productModelList.size() != 0) {
            Picasso.get()
                    .load(productModelList.get(position).getImage())
                    .into(holder.category_img);

            holder.category_name.setText(productModelList.get(position).getName());
            holder.category_qty.setText(productModelList.get(position).getTotalQty().toString());
            holder.category_price.setText("₹ "+productModelList.get(position).getResponseProductTypeModels().get(0).getRestrauntPrice());

            holder.itemView.setOnClickListener(view -> {
                recyclerInterface.onItemClick(productModelList.get(position).getId());
            });

        }

    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView category_img;
        TextView category_name, category_qty, category_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            category_img = itemView.findViewById(R.id.category_img);
            category_name = itemView.findViewById(R.id.category_name);
            category_qty = itemView.findViewById(R.id.category_qty);
            category_price = itemView.findViewById(R.id.category_price);
        }
    }
}
