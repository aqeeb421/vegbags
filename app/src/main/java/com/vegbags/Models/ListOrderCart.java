package com.vegbags.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListOrderCart {
    @SerializedName("items")
    @Expose
    private List<CartItem> items = null;
    @SerializedName("shippingAddress")
    @Expose
    private String shippingAddress;
    @SerializedName("billingAddress")
    @Expose
    private String billingAddress;

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }
}
