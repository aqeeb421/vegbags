package com.vegbags.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.vegbags.R;
import com.vegbags.database.Api;
import com.vegbags.database.RetrofitClientService;
import com.vegbags.databinding.ActivityOrderConfirmBinding;
import com.vegbags.utils.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderConfirmActivity extends AppCompatActivity {

    ActivityOrderConfirmBinding confirmBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        confirmBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_confirm);

        confirmBinding.btnCashYes.setOnClickListener(view -> {
            confirmBinding.layout1.setVisibility(View.GONE);
            confirmBinding.layout2.setVisibility(View.VISIBLE);
        });

        confirmBinding.btnCashNo.setOnClickListener(view -> {
            confirmBinding.layout1.setVisibility(View.GONE);
            confirmBinding.layout2.setVisibility(View.VISIBLE);
        });

        confirmBinding.btnConfirmOrder.setOnClickListener(view -> {

            DeleteCart();

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            OrderConfirmActivity.this.finish();
        });


    }

    private static final String TAG = "OrderConfirmActivity";

    private void DeleteCart() {
        String token = CommonUtils.getToken();
        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<Void> modelCall = uploadData.DeleteCart(token);

        modelCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: cart item deleted after ordering...");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Toast.makeText(this, "Please confirm this order.", Toast.LENGTH_SHORT).show();
//    }
}