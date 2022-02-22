package com.vegbags.ResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentResp {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("refId")
    @Expose
    private String refId;
    @SerializedName("transactionType")
    @Expose
    private String transactionType;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("formattedTransactionDateTime")
    @Expose
    private String formattedTransactionDateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFormattedTransactionDateTime() {
        return formattedTransactionDateTime;
    }

    public void setFormattedTransactionDateTime(String formattedTransactionDateTime) {
        this.formattedTransactionDateTime = formattedTransactionDateTime;
    }
}
