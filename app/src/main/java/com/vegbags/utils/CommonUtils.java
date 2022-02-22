package com.vegbags.utils;

public class CommonUtils {

    static int user = 0;
    static int productId = 0;
    static int productTypeId = 0;
    static String token = "";
    static String phone = "";
    static String cartCount = "";
    static Integer billingAddress = 0;

    public static Integer getBillingAddress() {
        return billingAddress;
    }

    public static void setBillingAddress(Integer billingAddress) {
        CommonUtils.billingAddress = billingAddress;
    }

    public static String getCartCount() {
        return cartCount;
    }

    public static void setCartCount(String cartCount) {
        CommonUtils.cartCount = cartCount;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        CommonUtils.phone = phone;
    }

    public int getUser() {
        return user;
    }

    public static void setUser(int user) {
        user = user;
    }

    public static int getProductId() {
        return productId;
    }

    public static void setProductId(int productId) {
        productId = productId;
    }

    public static int getProductTypeId() {
        return productTypeId;
    }

    public static void setProductTypeId(int productTypeId) {
        productTypeId = productTypeId;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        CommonUtils.token = "Token " + token;
    }
}
