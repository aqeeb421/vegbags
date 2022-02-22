package com.vegbags.view.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.vegbags.R;
import com.vegbags.ResponseModels.AddressModel;
import com.vegbags.adapter.AddressAdapter;
import com.vegbags.adapter.AddressRecyclerInterface;
import com.vegbags.database.Api;
import com.vegbags.database.RetrofitClientService;
import com.vegbags.databinding.ActivityManageAddressBinding;
import com.vegbags.utils.CommonUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageAddressActivity extends AppCompatActivity implements AddressRecyclerInterface {

    ActivityManageAddressBinding addressBinding;
    private Dialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addressBinding = DataBindingUtil.setContentView(this, R.layout.activity_manage_address);


        addressBinding.shimmerFrameLayout.startShimmerAnimation();

        addressBinding.imgBack.setOnClickListener(view -> {
            // startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        addressBinding.btnAddAddress.setOnClickListener(view -> {
            showAddAddressDialog();
        });

        getAddresses();

    }

    private void getAddresses() {
        String token = CommonUtils.getToken();
        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<List<AddressModel>> modelCall = uploadData.getAddresses(token);

        modelCall.enqueue(new Callback<List<AddressModel>>() {
            @Override
            public void onResponse(Call<List<AddressModel>> call, Response<List<AddressModel>> response) {
                addressBinding.shimmerFrameLayout.stopShimmerAnimation();
                addressBinding.shimmerFrameLayout.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body().size() > 0) {
                    List<AddressModel> addressModelList = response.body();

                    RecyclerView.LayoutManager manager = new LinearLayoutManager(ManageAddressActivity.this);
                    addressBinding.addressRecyclerView.setLayoutManager(manager);
                    AddressAdapter addressAdapter = new AddressAdapter(addressModelList, ManageAddressActivity.this);
                    addressBinding.addressRecyclerView.setHasFixedSize(true);
                    addressBinding.addressRecyclerView.setLayoutManager(new LinearLayoutManager(ManageAddressActivity.this));
                    addressBinding.addressRecyclerView.setAdapter(addressAdapter);

                    addressBinding.ll1.setVisibility(View.VISIBLE);
                    addressBinding.noDara.setVisibility(View.GONE);

                } else {
                    addressBinding.ll1.setVisibility(View.GONE);
                    addressBinding.noDara.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<AddressModel>> call, Throwable t) {
                addressBinding.shimmerFrameLayout.stopShimmerAnimation();
                addressBinding.shimmerFrameLayout.setVisibility(View.GONE);
                Toast.makeText(ManageAddressActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showAddAddressDialog() {
        dialog = new Dialog(this);
        //final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.address_layout);

        EditText CName = dialog.findViewById(R.id.CName_dialog);
        EditText CAddress = dialog.findViewById(R.id.CAddress_dialog);
        EditText pinCode = dialog.findViewById(R.id.pinCode_dialog);
        EditText Phone = dialog.findViewById(R.id.Phone_dialog);
        EditText altPhone = dialog.findViewById(R.id.altPhone_dialog);
        ImageView close_dialog = dialog.findViewById(R.id.close_dialog);


        Button SubmitButton = (Button) dialog.findViewById(R.id.btnSubmit_dialog);

        SubmitButton.setOnClickListener(view -> {

            if (!TextUtils.isEmpty(CName.getText())) {
                if (!TextUtils.isEmpty(CAddress.getText())) {

                    if (!TextUtils.isEmpty(pinCode.getText())) {

                        if (!TextUtils.isEmpty(Phone.getText())) {

                            CreateAddress(CName.getText().toString().trim(), CAddress.getText().toString().trim(),
                                    pinCode.getText().toString().trim(),
                                    Phone.getText().toString().trim(),
                                    altPhone.getText().toString().trim());

                        } else Phone.setError("Phone Number is required!");

                    } else pinCode.setError("Pin Code is required!");

                } else CAddress.setError("Address name is required!");

            } else CName.setError("Contact name is required!");

        });

        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }


    private void showAddAddressDialog(AddressModel addressModel) {
        dialog = new Dialog(this);
        //final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.address_layout);

        EditText CName = dialog.findViewById(R.id.CName_dialog);
        EditText CAddress = dialog.findViewById(R.id.CAddress_dialog);
        EditText pinCode = dialog.findViewById(R.id.pinCode_dialog);
        EditText Phone = dialog.findViewById(R.id.Phone_dialog);
        EditText altPhone = dialog.findViewById(R.id.altPhone_dialog);
        ImageView close_dialog = dialog.findViewById(R.id.close_dialog);

        CName.setText(addressModel.getContactName());
        CAddress.setText(addressModel.getAddress());
        pinCode.setText(addressModel.getPincode());
        Phone.setText(addressModel.getPhoneNo());
        altPhone.setText(addressModel.getAltPhoneNo());


        Button SubmitButton = (Button) dialog.findViewById(R.id.btnSubmit_dialog);
        SubmitButton.setText("Update Address");
        SubmitButton.setOnClickListener(view -> {

            if (!TextUtils.isEmpty(CName.getText())) {
                if (!TextUtils.isEmpty(CAddress.getText())) {

                    if (!TextUtils.isEmpty(pinCode.getText())) {

                        if (!TextUtils.isEmpty(Phone.getText())) {
                            UpdateAddress(addressModel.getId(), CName.getText().toString().trim(), CAddress.getText().toString().trim(),
                                    pinCode.getText().toString().trim(),
                                    Phone.getText().toString().trim(),
                                    altPhone.getText().toString().trim());
                        } else Phone.setError("Phone Number is required!");

                    } else pinCode.setError("Pin Code is required!");

                } else CAddress.setError("Address name is required!");

            } else CName.setError("Contact name is required!");


        });

        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    private void UpdateAddress(Integer id, String CName, String CAddress, String pincode, String phone, String altPhone) {
        String token = CommonUtils.getToken();

        AddressModel addressModel = new AddressModel(CName, phone, altPhone, CAddress, pincode);

        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<AddressModel> modelCall = uploadData.UpdateAddresses(token, String.valueOf(id), addressModel);

        modelCall.enqueue(new Callback<AddressModel>() {
            @Override
            public void onResponse(Call<AddressModel> call, Response<AddressModel> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    finish();
                    startActivity(getIntent());
                }
            }

            @Override
            public void onFailure(Call<AddressModel> call, Throwable t) {

            }
        });

    }

    private void CreateAddress(String CName, String CAddress, String pincode, String phone, String altPhone) {

        String token = CommonUtils.getToken();

        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<AddressModel> modelCall = uploadData.CreateAddress(token, CName, phone, altPhone, CAddress, pincode);

        modelCall.enqueue(new Callback<AddressModel>() {
            @Override
            public void onResponse(Call<AddressModel> call, Response<AddressModel> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    finish();
                    startActivity(getIntent());
                }
            }

            @Override
            public void onFailure(Call<AddressModel> call, Throwable t) {
                Toast.makeText(ManageAddressActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDeleteClick(int aID) {
        String token = CommonUtils.getToken();

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                //set icon
                .setIcon(R.drawable.ic_baseline_warning_24)
                //set title
                .setTitle("You want to Delete?")
                //sub title
                .setMessage("It will delete this address permanently.")
                //set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        Api uploadData = RetrofitClientService.getClient().create(Api.class);
                        Call<Void> modelCall = uploadData.DeleteAddresses(token, String.valueOf(aID));

                        modelCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    //dialog.dismiss();
                                    finish();
                                    startActivity(getIntent());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(ManageAddressActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                //set negative button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onUpdateClick(AddressModel addressModel) {
        showAddAddressDialog(addressModel);
    }
}