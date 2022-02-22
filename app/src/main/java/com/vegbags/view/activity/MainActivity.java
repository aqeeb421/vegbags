package com.vegbags.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vegbags.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    BottomNavigationView navView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Bottom Navigation View.
        navView = findViewById(R.id.bottomNav_view);

        //Initialize NavController.
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
//        NavigationUI.setupActionBarWithNavController(this, navController);
        NavigationUI.setupWithNavController(navView, navController);

        boolean isOrder = getIntent().getBooleanExtra("order", false);
        if (isOrder) {
            navView.setSelectedItemId(R.id.navigation_order);
        }


    }

    @Override
    public void onBackPressed() {
        int selectedItemId = navView.getSelectedItemId();
        if (R.id.navigation_home != selectedItemId) {
            setHomeItem(MainActivity.this);
        } else {
            finishAffinity();
        }
    }

    public void setHomeItem(Activity activity) {
        navView.setSelectedItemId(R.id.navigation_home);
    }
}