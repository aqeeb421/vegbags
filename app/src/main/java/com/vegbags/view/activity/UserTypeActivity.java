package com.vegbags.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.vegbags.R;
import com.vegbags.ResponseModels.MessageModel;
import com.vegbags.database.Api;
import com.vegbags.database.RetrofitClientService;
import com.vegbags.databinding.ActivityUserTypeBinding;
import com.vegbags.utils.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserTypeActivity extends AppCompatActivity {

    public static final String MY_USER = "USER_TYPE";
    private ActivityUserTypeBinding mBinding;
    private static final String TAG = "UserTypeActivity";
    private String type = "R";
    private String name = "Default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_type);

        mBinding.typeRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.card_restaurant:
                    type = "R";
                    Log.d(TAG, "onCheckedChanged: " + type);
                    mBinding.rbRestaurant.setChecked(true);
                    mBinding.rbGeneral.setChecked(false);
                    mBinding.rbHouse.setChecked(false);
                    break;
                case R.id.card_general:
                    type = "G";
                    Log.d(TAG, "onCheckedChanged: " + type);
                    mBinding.rbRestaurant.setChecked(false);
                    mBinding.rbGeneral.setChecked(true);
                    mBinding.rbHouse.setChecked(false);
                    break;
                case R.id.card_house:
                    type = "H";
                    Log.d(TAG, "onCheckedChanged: " + type);
                    mBinding.rbRestaurant.setChecked(false);
                    mBinding.rbGeneral.setChecked(false);
                    mBinding.rbHouse.setChecked(true);
                    break;
                default:
                    type = "R";
            }
        });

        mBinding.cardRestaurant.setOnClickListener(view -> {
            type = "R";
            Log.d(TAG, "onCheckedChanged: " + type);
            mBinding.rbRestaurant.setChecked(true);
            mBinding.rbGeneral.setChecked(false);
            mBinding.rbHouse.setChecked(false);
            mBinding.UserName.setHint("Enter Restaurant Name");
            mBinding.UserName.setVisibility(View.VISIBLE);
        });
        mBinding.cardGeneral.setOnClickListener(view -> {
            type = "G";
            Log.d(TAG, "onCheckedChanged: " + type);
            mBinding.rbRestaurant.setChecked(false);
            mBinding.rbGeneral.setChecked(true);
            mBinding.rbHouse.setChecked(false);
            mBinding.UserName.setHint("Enter General Store Name");
            mBinding.UserName.setVisibility(View.VISIBLE);
        });
        mBinding.cardHouse.setOnClickListener(view -> {
            type = "H";
            Log.d(TAG, "onCheckedChanged: " + type);
            mBinding.rbRestaurant.setChecked(false);
            mBinding.rbGeneral.setChecked(false);
            mBinding.rbHouse.setChecked(true);
            mBinding.UserName.setHint("Enter Name");
            mBinding.UserName.setVisibility(View.VISIBLE);
        });

        mBinding.buttonUserType.setOnClickListener(view -> {
            if (!type.equalsIgnoreCase("")) {
                name = mBinding.UserName.getText().toString();
                mBinding.UserName.setText(name);
                PostUserType(type, name);
            } else {
                Toast.makeText(this, "Please Select Any Type", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void PostUserType(String type, String name) {
        String token = CommonUtils.getToken();
        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<MessageModel> modelCall = uploadData.DeliveryType(token, type, name);

        modelCall.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserTypeActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = getSharedPreferences(MY_USER, MODE_PRIVATE).edit();
                    editor.putBoolean("isLaunched", true);
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(UserTypeActivity.this, "Error : Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Toast.makeText(UserTypeActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}