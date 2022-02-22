package com.vegbags.ResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("total_Qty")
    @Expose
    private Integer totalQty;
    @SerializedName("types")
    @Expose
    private List<ResponseProductTypeModel> responseProductTypeModels = null;
    @SerializedName("media")
    @Expose
    private List<Medium> media = null;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Integer totalQty) {
        this.totalQty = totalQty;
    }

    public List<ResponseProductTypeModel> getResponseProductTypeModels() {
        return responseProductTypeModels;
    }

    public void setResponseProductTypeModels(List<ResponseProductTypeModel> responseProductTypeModels) {
        this.responseProductTypeModels = responseProductTypeModels;
    }

    public List<Medium> getMedia() {
        return media;
    }

    public void setMedia(List<Medium> media) {
        this.media = media;
    }

}