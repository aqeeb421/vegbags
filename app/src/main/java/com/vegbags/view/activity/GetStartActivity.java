package com.vegbags.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.vegbags.R;
import com.vegbags.database.Api;
import com.vegbags.database.RetrofitClientService;
import com.vegbags.databinding.ActivityGetStartBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetStartActivity extends AppCompatActivity {

    public static final String MY_PREFS = "Picode";
    ActivityGetStartBinding mBinding;
    String pinCode;
    View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_get_start);
        parentLayout = findViewById(android.R.id.content);

        mBinding.buttonVerifyPinCode.setOnClickListener(view -> {
            pinCode = mBinding.pinCodeView.getText().toString().trim();
            if (!pinCode.isEmpty()) {
                if (pinCode.length() == 6) {
                    //GetLogin(phoneNumber, otp);
                    GetServiceAvailble(pinCode);

                } else {
                    Snackbar.make(parentLayout, "Please enter 6 digit PinCode", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.RED)
                            .show();
                }
            } else {
                Snackbar.make(parentLayout, "Please enter PinCode to continue", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(Color.RED)
                        .show();
            }
        });
    }

    private void GetServiceAvailble(String pinCode) {
        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<Void> modelCall = uploadData.getServiceAvailable(pinCode);

        modelCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    mBinding.tvVerified.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
                    editor.putBoolean("isLaunched", true);
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    mBinding.tvVerified.setText("Sorry!!, Our Service is Currently Not Available in Your City.");
                    mBinding.tvVerified.setTextColor(getResources().getColor(R.color.color_red));
                    mBinding.tvVerified.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(GetStartActivity.this, "Error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}