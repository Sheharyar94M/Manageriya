package com.hammad.managerya.bottomNavFragments.homeFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hammad.managerya.R;

import java.io.File;
import java.net.URI;

public class ViewTransDetailsActivity extends AppCompatActivity {

    private TextView textViewCategoryName,textViewAmount,textViewDate;
    private TextView textViewDescription,textViewTag,textViewLocation,textViewPreviewReceipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trans_details);

        //get the bundle data
        getBundleData();

        //initial views
        initializeViews();

        textViewPreviewReceipt.setVisibility(View.VISIBLE);

        //text view preview click listener
        textViewPreviewReceipt.setOnClickListener(view -> openImage());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getBundleData()
    {
        Bundle bundle=getIntent().getExtras();

        if(bundle != null)
        {
            Toast.makeText(this, "position: "+bundle.getInt("position"), Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeViews()
    {
        textViewCategoryName=findViewById(R.id.txt_cat);
        textViewAmount=findViewById(R.id.txt_amount);
        textViewDate=findViewById(R.id.txt_date);

        textViewDescription=findViewById(R.id.txt_desc);
        textViewTag=findViewById(R.id.txt_tag);
        textViewLocation=findViewById(R.id.txt_location);
        textViewPreviewReceipt=findViewById(R.id.txt_preview);
    }

    private void openImage()
    {
        String imagePath="/storage/emulated/0/DCIM/Screenshots/IMG_20220417_200351.jpg";
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(imagePath),"image/*");
        this.startActivity(intent);
    }

}