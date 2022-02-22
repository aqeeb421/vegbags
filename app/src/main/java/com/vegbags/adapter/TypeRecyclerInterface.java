package com.vegbags.adapter;


import com.vegbags.Models.CartItem;

public interface TypeRecyclerInterface {

    void onAddClick(CartItem cartItem, int ItemId);

    void onPlusClick(CartItem cartItem, int ItemId, int count);

    void onMinusClick(CartItem cartItem, int ItemId, int count);
}
