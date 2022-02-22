package com.vegbags.view.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.vegbags.R;
import com.vegbags.ResponseModels.Due;
import com.vegbags.ResponseModels.Payment;
import com.vegbags.ResponseModels.PaymentResp;
import com.vegbags.ResponseModels.TransactionModel;
import com.vegbags.adapter.DueAdapter;
import com.vegbags.adapter.PaymentAdapter;
import com.vegbags.database.Api;
import com.vegbags.database.RetrofitClientService;
import com.vegbags.databinding.ActivityCreditLedgerBinding;
import com.vegbags.utils.CommonUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreditLedgerActivity extends AppCompatActivity implements PaymentResultListener {

    private ActivityCreditLedgerBinding ledgerBinding;
    private ProgressDialog progressBar;
    private PaymentAdapter paymentAdapter;
    private DueAdapter dueAdapter;
    private List<Due> dueList = new ArrayList<>();
    private List<Payment> paymentList = new ArrayList<>();

    private String Amount = "";
    private String Desc = "";
    private String PaymentType = "";

    private static final String TAG = "CreditLedgerActivity";
    private Checkout checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ledgerBinding = DataBindingUtil.setContentView(this, R.layout.activity_credit_ledger);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Please Wait...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100
        geTransactionData();

        Checkout.preload(getApplicationContext());

        ledgerBinding.imgBack.setOnClickListener(view -> {
            finish();
        });

        ledgerBinding.payNow.setOnClickListener(view -> {
            showDialog();
        });

        ledgerBinding.radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.rbDues:
                    ledgerBinding.rbDues.setTextColor(getResources().getColor(R.color.white));
                    ledgerBinding.rbPayment.setTextColor(getResources().getColor(R.color.primary));
                    setDueAdapter(dueList);
                    break;

                case R.id.rbPayment:
                    ledgerBinding.rbDues.setTextColor(getResources().getColor(R.color.primary));
                    ledgerBinding.rbPayment.setTextColor(getResources().getColor(R.color.white));
                    setPaymentAdapter(paymentList);
                    break;

            }
        });

    }

    public void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.payment_layout);

        Button submit_payment = dialog.findViewById(R.id.submit_payment);
        ImageView ic_close = dialog.findViewById(R.id.ic_close);
        EditText editTextAmount = dialog.findViewById(R.id.editTextAmount);
        EditText editTextDesc = dialog.findViewById(R.id.editTextDesc);
        RadioGroup group = dialog.findViewById(R.id.pGroup);
       /* group.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.rbDue:
                    PaymentType = "D";
                    break;

                case R.id.rbPayment:
                    PaymentType = "P";
                    break;

                default:
                    Toast.makeText(this, "Please Select Payment Type", Toast.LENGTH_SHORT).show();
            }
        });*/


        submit_payment.setOnClickListener(view -> {
            if (!editTextAmount.getText().toString().isEmpty()) {
                if (!editTextDesc.getText().toString().isEmpty()) {
                    Amount = editTextAmount.getText().toString().trim();
                    Desc = editTextDesc.getText().toString().trim();
                    startPayment();
                    dialog.dismiss();
                    // CreateNeWPayment(editTextAmount.getText().toString(), editTextRefId.getText().toString(), editTextDesc.getText().toString());
                } else
                    editTextDesc.setError("Enter Description");
            } else
                editTextAmount.setError("Enter Amount");
        });

        ic_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        int s = Integer.parseInt(Amount);
        double finalAmount = s * 100;

        String phone = CommonUtils.getPhone();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", finalAmount);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", phone);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    private void CreateNeWPayment(String amount, String refID, String desc) {
        String token = CommonUtils.getToken();
        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<PaymentResp> modelCall = uploadData.NewPayment(token, desc, refID, "P", amount);

        modelCall.enqueue(new Callback<PaymentResp>() {
            @Override
            public void onResponse(Call<PaymentResp> call, Response<PaymentResp> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreditLedgerActivity.this, "payment is successfull", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                } else {
                    Toast.makeText(CreditLedgerActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentResp> call, Throwable t) {
                Toast.makeText(CreditLedgerActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setPaymentAdapter(List<Payment> paymentList) {

        if (paymentList.size() > 0) {
            RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
            ledgerBinding.rvTransactionList.setLayoutManager(manager);
            paymentAdapter = new PaymentAdapter(paymentList, this);
            ledgerBinding.rvTransactionList.setHasFixedSize(true);
            ledgerBinding.rvTransactionList.setLayoutManager(new LinearLayoutManager(this));
            ledgerBinding.rvTransactionList.setAdapter(paymentAdapter);
        } else {
            ledgerBinding.noDara.setVisibility(View.VISIBLE);
            ledgerBinding.rvTransactionList.setVisibility(View.GONE);
        }


    }

    private void setDueAdapter(List<Due> dueList) {

        if (dueList.size() > 0) {
            RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
            ledgerBinding.rvTransactionList.setLayoutManager(manager);
            dueAdapter = new DueAdapter(dueList, this);
            ledgerBinding.rvTransactionList.setHasFixedSize(true);
            ledgerBinding.rvTransactionList.setLayoutManager(new LinearLayoutManager(this));
            ledgerBinding.rvTransactionList.setAdapter(dueAdapter);
        } else {
            ledgerBinding.noDara.setVisibility(View.VISIBLE);
            ledgerBinding.rvTransactionList.setVisibility(View.GONE);
        }


    }

    private void geTransactionData() {
        progressBar.show();
        String token = CommonUtils.getToken();
        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<TransactionModel> modelCall = uploadData.getTransaction(token);

        modelCall.enqueue(new Callback<TransactionModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<TransactionModel> call, Response<TransactionModel> response) {
                progressBar.dismiss();
                if (response.isSuccessful()) {
                    ledgerBinding.tvBalance.setText("₹ " + response.body().getBalance());
                    dueList = response.body().getDues();
                    paymentList = response.body().getPayments();
                    setDueAdapter(dueList);
                } else {
                    ledgerBinding.noDara.setVisibility(View.VISIBLE);
                    ledgerBinding.rvTransactionList.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<TransactionModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(CreditLedgerActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            CreateNeWPayment(Amount, razorpayPaymentID, Desc);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }
}