package com.vegbags.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.vegbags.R;
import com.vegbags.ResponseModels.OrderResponseModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderResponseModel> ordersModels;
    OrderClickInterface orderClickInterface;

    public OrderAdapter(List<OrderResponseModel> ordersModels,OrderClickInterface orderClickInterface) {
        this.ordersModels = ordersModels;
        this.orderClickInterface = orderClickInterface;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.order_item, parent, false);
        OrderAdapter.OrderViewHolder OrderViewHolder = new OrderAdapter.OrderViewHolder(listItem);
        return OrderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.itemCount.setText(String.valueOf(ordersModels.get(position).getItems().size()));
        holder.orderId.setText(ordersModels.get(position).getOrderId());
        holder.orderStatus.setText(ordersModels.get(position).getStatus());

        holder.itemView.setOnClickListener(view -> {
            orderClickInterface.onItemClick(ordersModels.get(position));
        });
    }

    @Override
    public int getItemCount() {
        if (ordersModels.size() > 0)
            return ordersModels.size();
        else
            return 0;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderStatus, orderId, itemCount;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            itemCount = itemView.findViewById(R.id.itemCount);
        }
    }
}
