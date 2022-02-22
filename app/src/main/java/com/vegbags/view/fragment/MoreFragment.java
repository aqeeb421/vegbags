package com.vegbags.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.vegbags.databinding.FragmentMoreBinding;
import com.vegbags.utils.CommonUtils;
import com.vegbags.view.activity.ContactActivity;
import com.vegbags.view.activity.CreditLedgerActivity;
import com.vegbags.view.activity.LoginActivity;
import com.vegbags.view.activity.ManageAddressActivity;

import static android.content.Context.MODE_PRIVATE;
import static com.vegbags.view.activity.VerificationActivity.MY_PREFS_NAME;

public class MoreFragment extends Fragment {

    public MoreFragment() {
        // Required empty public constructor
    }

    FragmentMoreBinding moreBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        moreBinding = FragmentMoreBinding.inflate(inflater, container, false);


        moreBinding.manageAddress.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), ManageAddressActivity.class));
        });
        moreBinding.Transaction.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), CreditLedgerActivity.class));
        });
        moreBinding.contctUs.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), ContactActivity.class));
        });
        moreBinding.privacyPolicy.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Privacy Policy", Toast.LENGTH_SHORT).show();
        });
        moreBinding.termCondition.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Terms and Conditions", Toast.LENGTH_SHORT).show();
        });

        moreBinding.ph.setText(CommonUtils.getPhone());

        moreBinding.LogOut.setOnClickListener(view -> {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    //set icon
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    //set title
                    .setTitle("You want to Logout")
                    //set positive button
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        //set what would happen when positive button is clicked
                        CommonUtils.setToken("");
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.remove("token");
                        editor.apply();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        getActivity().finish();
                    })
                    //set negative button
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
        });

        return moreBinding.getRoot();

    }


}