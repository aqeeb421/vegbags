package com.vegbags.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.vegbags.R;
import com.vegbags.ResponseModels.LoginModel;
import com.vegbags.database.Api;
import com.vegbags.database.RetrofitClientService;
import com.vegbags.utils.CommonUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vegbags.view.activity.UserTypeActivity.MY_USER;


public class VerificationActivity extends AppCompatActivity {

    private String TAG = "";
    public static final String MY_PREFS_NAME = "LoginToken";
    String phoneNumber = "", otp, verificationId;
    View parentLayout;
    TextView Text;
    private boolean isLaunched;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    PinView pinView;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        parentLayout = findViewById(android.R.id.content);
        pinView = findViewById(R.id.pinView);
        Text = findViewById(R.id.subText);
        phoneNumber = getIntent().getStringExtra("phone");
        verificationId = getIntent().getStringExtra("verificationId");
        String subText = "Please type the verification code\n sent to +91 " + phoneNumber;
        Text.setText(subText.trim());

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences prefs1 = getSharedPreferences(MY_USER, MODE_PRIVATE);
        isLaunched = prefs1.getBoolean("isLaunched", false);

        Log.d(TAG, "onCreate: " + isLaunched);


        CommonUtils.setPhone(phoneNumber);
        sendVerificationCode(phoneNumber);

        findViewById(R.id.buttonVerify).setOnClickListener(view1 -> {
            otp = Objects.requireNonNull(pinView.getText()).toString().trim();
            if (!otp.isEmpty()) {
                if (otp.length() == 6) {
                    // GetLogin(phoneNumber, otp);

                    verifyPhoneNumberWithCode(verificationId, otp.trim());
                } else {
                    Snackbar.make(parentLayout, "Please enter 4 digit OTP", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.RED)
                            .show();
                }
            } else {
                Snackbar.make(parentLayout, "Please enter OTP", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(Color.RED)
                        .show();
            }
        });
    }

    private void GetLogin(String phone, String otp) {
        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<LoginModel> modelCall = uploadData.getLogin(phone, otp);
        modelCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    assert response.body() != null;
                    editor.putString("token", response.body().getToken());
                    editor.putString("phone", phone);
                    editor.apply();
                    CommonUtils.setToken(response.body().getToken());
                    Intent intent;
                    if (isLaunched) {
                        intent = new Intent(VerificationActivity.this, MainActivity.class);
                    } else {
                        intent = new Intent(VerificationActivity.this, UserTypeActivity.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    Snackbar.make(parentLayout, "Please enter Valid OTP", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.RED)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
        // [END verify_with_code]
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            GetLogin(phoneNumber, otp);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Log.e(TAG, "onComplete: ");
                            }

                           // Toast.makeText(VerificationActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // [END sign_in_with_phone]


    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                pinView.setText(code);
                verifyPhoneNumberWithCode(verificationId, otp.trim());
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}