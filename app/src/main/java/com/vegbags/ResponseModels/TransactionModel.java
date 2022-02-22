package com.vegbags.ResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionModel {
    @SerializedName("balance")
    @Expose
    private Integer balance;
    @SerializedName("dues")
    @Expose
    private List<Due> dues = null;
    @SerializedName("payments")
    @Expose
    private List<Payment> payments = null;

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public List<Due> getDues() {
        return dues;
    }

    public void setDues(List<Due> dues) {
        this.dues = dues;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

}
