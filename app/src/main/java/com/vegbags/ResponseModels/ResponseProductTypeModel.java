package com.vegbags.ResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseProductTypeModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("AvailableQty")
    @Expose
    private String availableQty;
    @SerializedName("HouseHoldPrice")
    @Expose
    private String houseHoldPrice;
    @SerializedName("GeneralStorePrice")
    @Expose
    private String generalStorePrice;
    @SerializedName("RestrauntPrice")
    @Expose
    private String restrauntPrice;
    @SerializedName("rPrice")
    @Expose
    private String rPrice;
    @SerializedName("gPrice")
    @Expose
    private String gPrice;
    @SerializedName("hPrice")
    @Expose
    private String hPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(String availableQty) {
        this.availableQty = availableQty;
    }

    public String getHouseHoldPrice() {
        return houseHoldPrice;
    }

    public void setHouseHoldPrice(String houseHoldPrice) {
        this.houseHoldPrice = houseHoldPrice;
    }

    public String getGeneralStorePrice() {
        return generalStorePrice;
    }

    public void setGeneralStorePrice(String generalStorePrice) {
        this.generalStorePrice = generalStorePrice;
    }

    public String getRestrauntPrice() {
        return restrauntPrice;
    }

    public void setRestrauntPrice(String restrauntPrice) {
        this.restrauntPrice = restrauntPrice;
    }

    public String getrPrice() {
        return rPrice;
    }

    public void setrPrice(String rPrice) {
        this.rPrice = rPrice;
    }

    public String getgPrice() {
        return gPrice;
    }

    public void setgPrice(String gPrice) {
        this.gPrice = gPrice;
    }

    public String gethPrice() {
        return hPrice;
    }

    public void sethPrice(String hPrice) {
        this.hPrice = hPrice;
    }

}

