package com.vegbags.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;
import com.vegbags.R;
import com.vegbags.ResponseModels.ResponseCartItemModel;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
    List<ResponseCartItemModel> cartItemList;

    public OrderItemAdapter(List<ResponseCartItemModel> cartItemList) {
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_type_item, parent, false);
        return new OrderItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        double qty = Double.parseDouble(cartItemList.get(position).getQty());
        int Qty = (int) qty;
        holder.txtQty.setText(String.valueOf(Qty));
        holder.cart_unit.setText(cartItemList.get(position).getUnit());
        holder.cart_name.setText(cartItemList.get(position).getProduct().getName());
        holder.cart_type.setText(cartItemList.get(position).getProductType().getName());
        holder.cart_price.setText("₹ "+cartItemList.get(position).getProductType().getrPrice());

        Picasso.get().load(cartItemList.get(position).getProduct().getImage()).into(holder.cart_img);
    }

    @Override
    public int getItemCount() {
        if (cartItemList.size() > 0) return cartItemList.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cart_img;
        TextView cart_name, cart_type, cart_price, cart_unit, txtQty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cart_img = itemView.findViewById(R.id.cart_img);
            cart_name = itemView.findViewById(R.id.cart_name);
            cart_type = itemView.findViewById(R.id.cart_type);
            cart_price = itemView.findViewById(R.id.cart_price);
            cart_unit = itemView.findViewById(R.id.cart_unit);
            txtQty = itemView.findViewById(R.id.txtQty);
        }
    }
}
