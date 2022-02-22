package com.vegbags.adapter;


import com.vegbags.ResponseModels.AddressModel;

public interface AddressRecyclerInterface {
    void onDeleteClick(int aID);

    void onUpdateClick(AddressModel addressModel);
}
