package com.hammad.managerya.bottomNavFragments.addRecord;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hammad.managerya.R;

public class AddRecordActivity extends AppCompatActivity {

    ImageView imageViewFinishActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        //flag for full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //in case of rounded corners, the white edges are removed by this
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //initializing views
        initializeViews();

        //finish activity button click listener
        imageViewFinishActivity.setOnClickListener(view -> finish());
    }

    private void initializeViews() {

        //finish activity button
        imageViewFinishActivity=findViewById(R.id.img_view_1_add_record);
    }
}