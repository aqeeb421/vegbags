package com.vegbags.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.vegbags.Models.CartItem;
import com.vegbags.R;
import com.vegbags.ResponseModels.ResponseProductTypeModel;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    CartItem cartItem;
    private List<ResponseProductTypeModel> responseProductTypeModelList;
    private TypeRecyclerInterface typeRecyclerInterface;

    public ItemAdapter(List<ResponseProductTypeModel> responseProductTypeModelList, TypeRecyclerInterface typeRecyclerInterface) {
        this.responseProductTypeModelList = responseProductTypeModelList;
        this.typeRecyclerInterface = typeRecyclerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.type_name.setText(responseProductTypeModelList.get(position).getName());
        holder.type_price.setText("₹ "+responseProductTypeModelList.get(position).getRestrauntPrice());
        // holder.type_name.setText(typeList.get(position).getName());



        int itemId = responseProductTypeModelList.get(position).getId();
        AtomicInteger counter = new AtomicInteger();


        holder.btnAddToCart.setOnClickListener(view -> {
            holder.llCounter.setVisibility(View.VISIBLE);
            holder.type_unit.setVisibility(View.VISIBLE);
            holder.btnAddToCart.setVisibility(View.GONE);

            cartItem = new CartItem(responseProductTypeModelList.get(position).getId().toString(), responseProductTypeModelList.get(position).getId().toString(), "1", "Bags");
            typeRecyclerInterface.onAddClick(cartItem, responseProductTypeModelList.get(position).getId());

        });

        holder.icon_plus.setOnClickListener(view -> {
            counter.set(counter.get() + 1);
            Log.d("TAG", "onCreate: " + counter);
            holder.tvQty.setText(String.valueOf(counter.get()));

            cartItem = new CartItem(responseProductTypeModelList.get(position).getId().toString(), responseProductTypeModelList.get(position).getId().toString(), String.valueOf(counter.get()), "Bags");
            int count = counter.get();
            typeRecyclerInterface.onPlusClick(cartItem, responseProductTypeModelList.get(position).getId(), count);

        });

        holder.icon_minus.setOnClickListener(view -> {
            if (counter.get() > 1) {
                counter.set(counter.get() - 1);
                Log.d("TAG", "onCreate: " + counter);
                holder.tvQty.setText(String.valueOf(counter.get()));

                int count = counter.get();
                cartItem = new CartItem(responseProductTypeModelList.get(position).getId().toString(), responseProductTypeModelList.get(position).getId().toString(), String.valueOf(counter.get()), "Bags");
                typeRecyclerInterface.onMinusClick(cartItem, responseProductTypeModelList.get(position).getId(), count);
            } else {
                holder.llCounter.setVisibility(View.GONE);
                holder.type_unit.setVisibility(View.GONE);
                holder.btnAddToCart.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (responseProductTypeModelList.size() == 0)
            return 0;
        else return responseProductTypeModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayoutCompat btnAddToCart, llCounter;
        TextView tvQty, type_unit, type_name, type_price;
        ImageView icon_minus, icon_plus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            llCounter = itemView.findViewById(R.id.llCounter);
            tvQty = itemView.findViewById(R.id.tvQty);
            type_unit = itemView.findViewById(R.id.type_unit);
            icon_minus = itemView.findViewById(R.id.icon_minus);
            icon_plus = itemView.findViewById(R.id.icon_plus);
            type_name = itemView.findViewById(R.id.type_name);
            type_price = itemView.findViewById(R.id.type_price);
        }
    }
}
