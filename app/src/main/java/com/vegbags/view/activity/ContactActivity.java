package com.vegbags.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.vegbags.R;
import com.vegbags.databinding.ActivityContactBinding;

public class ContactActivity extends AppCompatActivity {

    private ActivityContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact);

        binding.contactWhatsApp.setOnClickListener(view -> {
            Uri uri = Uri.parse("smsto:" + "8123470055");
            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
            i.setPackage("com.whatsapp");
            startActivity(i);
        });

        binding.contactGmail.setOnClickListener(view -> {
            Uri uri = Uri.parse("mailto:"+"Mntraders62@gmail.com");
            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
            i.setPackage("com.google.android.gm");
            startActivity(i);
        });

        binding.contactPhone.setOnClickListener(view -> {
            Uri uri = Uri.parse("tel:" + "8123470044");
            Intent i = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(i);
        });

    }
}