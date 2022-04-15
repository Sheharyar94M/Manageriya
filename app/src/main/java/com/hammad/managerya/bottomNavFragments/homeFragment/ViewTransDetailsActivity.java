package com.hammad.managerya.bottomNavFragments.homeFragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hammad.managerya.R;

public class ViewTransDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trans_details);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}