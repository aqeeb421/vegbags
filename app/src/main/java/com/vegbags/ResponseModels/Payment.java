package com.vegbags.ResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payment {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("transactionType")
    @Expose
    private String transactionType;
    @SerializedName("refId")
    @Expose
    private String refId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("formattedTransactionDateTime")
    @Expose
    private String transactionDateTime;
    @SerializedName("user")
    @Expose
    private Integer user;

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getRefId() {
        return refId;
    }

    public String getAmount() {
        return amount;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public Integer getUser() {
        return user;
    }
}
