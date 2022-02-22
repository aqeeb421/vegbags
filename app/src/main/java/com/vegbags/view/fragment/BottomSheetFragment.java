package com.vegbags.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vegbags.ResponseModels.OrderResponseModel;
import com.vegbags.ResponseModels.ResponseCartItemModel;
import com.vegbags.adapter.OrderItemAdapter;
import com.vegbags.database.Api;
import com.vegbags.database.RetrofitClientService;
import com.vegbags.databinding.FragmentBottomSheetBinding;
import com.vegbags.utils.CommonUtils;
import com.vegbags.view.activity.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BottomSheetFragment extends BottomSheetDialogFragment {

    OrderResponseModel ordersModel;
    OrderItemAdapter orderItemAdapter;

    public BottomSheetFragment(OrderResponseModel ordersModel) {
        // Required empty public constructor
        this.ordersModel = ordersModel;
    }

    private static final String TAG = "BottomSheetFragment";
    private FragmentBottomSheetBinding sheetBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sheetBinding = FragmentBottomSheetBinding.inflate(inflater, container, false);
        Log.d(TAG, "onCreateView: " + ordersModel);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM | KK:mm aa");
        Date date = null;
        try {
            date = inputFormat.parse(ordersModel.getUpdatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            String formattedDate = outputFormat.format(date);
            System.out.println(formattedDate);
            sheetBinding.orderDate.setText(formattedDate);
        } else {
            sheetBinding.orderDate.setText("01 JAN, 10:00 AM");
        }


        sheetBinding.orderId.setText(String.valueOf(ordersModel.getOrderId()));

        sheetBinding.orderStatus.setText(ordersModel.getStatus());

        List<ResponseCartItemModel> cartItemList = new ArrayList<>(ordersModel.getItems());

        setAdapter(cartItemList);

        sheetBinding.btnCancelOrder.setOnClickListener(view -> {
            DeletingOrder(String.valueOf(ordersModel.getId()));
        });

        return sheetBinding.getRoot();
    }

    private void DeletingOrder(String orderId) {
        String token = CommonUtils.getToken();
        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<Void> modelCall = uploadData.DeleteOrder(token, orderId);

        modelCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("order", true);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setAdapter(List<ResponseCartItemModel> cartItemList) {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        sheetBinding.itemRecyclerView.setLayoutManager(manager);
        orderItemAdapter = new OrderItemAdapter(cartItemList);
        sheetBinding.itemRecyclerView.setHasFixedSize(true);
        sheetBinding.itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sheetBinding.itemRecyclerView.setAdapter(orderItemAdapter);
    }
}