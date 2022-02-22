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

import com.squareup.picasso.Picasso;
import com.vegbags.Models.CartItem;
import com.vegbags.R;
import com.vegbags.ResponseModels.ResponseCartItemModel;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    CartItem cartItem;
    private List<ResponseCartItemModel> cartModelList;
    private boolean isEditable;
    private static final String TAG = "CartAdapter";
    private TypeRecyclerInterface typeRecyclerInterface;


    public CartAdapter(TypeRecyclerInterface typeRecyclerInterface, List<ResponseCartItemModel> cartModelList, boolean isEditable) {
        this.cartModelList = cartModelList;
        this.typeRecyclerInterface = typeRecyclerInterface;
        this.isEditable = isEditable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.cart_item, parent, false);
        CartAdapter.ViewHolder viewHolder = new CartAdapter.ViewHolder(listItem);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cart_name.setText(cartModelList.get(position).getProduct().getName());
        holder.cart_type.setText(cartModelList.get(position).getProductType().getName());
        holder.cart_price.setText("₹ "+cartModelList.get(position).getProductType().getrPrice());
        holder.cart_unit.setText(cartModelList.get(position).getUnit());
        double qty = Double.parseDouble(cartModelList.get(position).getQty());
        int Qty = (int) qty;
        holder.cart_qty.setText(String.valueOf(Qty));
        holder.txtQty.setText(String.valueOf(Qty));
        Picasso.get().load(cartModelList.get(position).getProduct().getImage()).into(holder.cart_img);

        if (isEditable) {
            holder.txtQty.setVisibility(View.GONE);
            holder.llCounter.setVisibility(View.VISIBLE);
        } else {
            holder.txtQty.setVisibility(View.VISIBLE);
            holder.llCounter.setVisibility(View.GONE);
        }

        AtomicInteger counter = new AtomicInteger();
        counter.set(Integer.parseInt(holder.txtQty.getText().toString()));

        holder.icon_plus.setOnClickListener(view -> {
            counter.set(counter.get() + 1);
            Log.d("TAG", "onCreate: " + counter);
            holder.cart_qty.setText(String.valueOf(counter.get()));

            cartItem = new CartItem(cartModelList.get(position).getProduct().getId().toString(), cartModelList.get(position).getProductType().getId().toString(), String.valueOf(counter.get()), "bags");
            int count = counter.get();
            typeRecyclerInterface.onPlusClick(cartItem, cartModelList.get(position).getId(), count);

        });

        holder.icon_minus.setOnClickListener(view -> {
            if (counter.get() > 1) {
                counter.set(counter.get() - 1);
                Log.d("TAG", "onCreate: " + counter);
                holder.cart_qty.setText(String.valueOf(counter.get()));

                int count = counter.get();
                cartItem = new CartItem(cartModelList.get(position).getProduct().getId().toString(), cartModelList.get(position).getProductType().getId().toString(), String.valueOf(counter.get()), "bags");
                typeRecyclerInterface.onMinusClick(cartItem, cartModelList.get(position).getId(), count);
            } else {
                Log.d(TAG, "onBindViewHolder: No item reduced");
            }
        });


    }

    @Override
    public int getItemCount() {
        if (cartModelList != null)
            return cartModelList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cart_img, icon_minus, icon_plus;
        TextView cart_name, cart_type, cart_price, cart_unit, cart_qty, txtQty;
        LinearLayoutCompat llCounter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cart_img = itemView.findViewById(R.id.cart_img);
            cart_name = itemView.findViewById(R.id.cart_name);
            cart_type = itemView.findViewById(R.id.cart_type);
            cart_price = itemView.findViewById(R.id.cart_price);
            cart_unit = itemView.findViewById(R.id.cart_unit);
            cart_qty = itemView.findViewById(R.id.cart_qty);
            txtQty = itemView.findViewById(R.id.txtQty);
            llCounter = itemView.findViewById(R.id.llCounter);
            icon_minus = itemView.findViewById(R.id.icon_minus);
            icon_plus = itemView.findViewById(R.id.icon_plus);

        }
    }
}
