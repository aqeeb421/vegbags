package com.vegbags.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.vegbags.R;
import com.vegbags.ResponseModels.TransactionModel;
import com.vegbags.database.Api;
import com.vegbags.database.RetrofitClientService;
import com.vegbags.utils.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vegbags.view.activity.GetStartActivity.MY_PREFS;
import static com.vegbags.view.activity.UserTypeActivity.MY_USER;
import static com.vegbags.view.activity.VerificationActivity.MY_PREFS_NAME;


public class SplashActivity extends AppCompatActivity {

    //    vegbags
    //    pwd:vegbags12345
    //    veg
    //    pwd:vegbags12345
    private static final String TAG = "SplashActivity";
    private boolean isLaunched, isLaunched1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences prefs1 = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        isLaunched = prefs1.getBoolean("isLaunched", false);
        SharedPreferences prefs2 = getSharedPreferences(MY_USER, MODE_PRIVATE);
        isLaunched1 = prefs2.getBoolean("isLaunched", false);

        Log.d(TAG, "onCreate: " + isLaunched);


        ImageView imageView = findViewById(R.id.splash_imageView);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        imageView.setAnimation(animation);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String token = prefs.getString("token", "null");
        String phone = prefs.getString("phone", "null");
        CommonUtils.setToken(token);
        CommonUtils.setPhone(phone);


        new Handler().postDelayed(this::GetUser, 3000);

    }

    private void GetUser() {
        String token = CommonUtils.getToken();
        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<TransactionModel> modelCall = uploadData.getGetUser(token);

        modelCall.enqueue(new Callback<TransactionModel>() {
            @Override
            public void onResponse(Call<TransactionModel> call, Response<TransactionModel> response) {
                Intent i;
                if (response.isSuccessful()) {
                    if (isLaunched1)
                        i = new Intent(SplashActivity.this, MainActivity.class);
                    else
                        i = new Intent(SplashActivity.this, UserTypeActivity.class);
                } else {
                    if (isLaunched)
                        i = new Intent(SplashActivity.this, LoginActivity.class);
                    else
                        i = new Intent(SplashActivity.this, GetStartActivity.class);

                }
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<TransactionModel> call, Throwable t) {
                Toast.makeText(SplashActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}