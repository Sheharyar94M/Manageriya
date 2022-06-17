package com.risibleapps.mywallet.bottomNavFragments.homeFragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.risibleapps.mywallet.R;

public class ViewTransDetailsActivity extends AppCompatActivity {

    private TextView textViewCategoryName,textViewAmount,textViewDate;
    private TextView textViewDescription,textViewTag,textViewLocation,textViewPreviewReceipt,textViewReceipt;

    private String transType,catName,amount,transDate,description,tag,locationAddress,imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trans_details);

        //get the bundle data
        getBundleData();

        //initial views
        initializeViews();

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
        Intent intent=getIntent();

        transType = intent.getStringExtra("type");
        catName = intent.getStringExtra("catName");
        amount = intent.getStringExtra("amount");
        transDate = intent.getStringExtra("date");
        description = intent.getStringExtra("desc");
        tag = intent.getStringExtra("tag");
        locationAddress = intent.getStringExtra("loc");
        imagePath = intent.getStringExtra("img");
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
        textViewReceipt=findViewById(R.id.txt_receipt);

        //setting the values into text views
        textViewCategoryName.setText(catName);
        textViewDate.setText(transDate);
        textViewAmount.setText(amount);

        //setting the amount color based on transaction type
        if(transType.equals("Income"))
        {
            textViewAmount.append(" +");
            textViewAmount.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        else if(transType.equals("Expense"))
        {
            textViewAmount.append(" -");
            textViewAmount.setTextColor(getResources().getColor(R.color.colorRed));
        }

        //checking whether description length is zero or not
        if(description.length() == 0 || description == null)
        {
            textViewDescription.setText("No Added Description");
        }
        else if(description.length() > 0)
        {
            textViewDescription.setText(description);
        }

        //checking whether tag length is zero or not
        if(tag.length() == 0 || tag == null)
        {
            textViewTag.setText("No Added Tag");
        }
        else if(tag.length() > 0)
        {
            textViewTag.setText(tag);
        }

        //checking whether description length is zero or not
        if(locationAddress.length() == 0 || locationAddress == null)
        {
            textViewLocation.setText("No Added Location");
        }
        else if(locationAddress.length() > 0)
        {
            textViewLocation.setText(locationAddress);
        }

        //checking whether details contain image or not
        if(imagePath.length() == 0 || imagePath == null)
        {
            textViewPreviewReceipt.setVisibility(View.GONE);
            textViewReceipt.setText("No Attached Receipt");
        }
        else if(imagePath.length() > 0)
        {
            textViewPreviewReceipt.setVisibility(View.VISIBLE);
        }

    }

    private void openImage()
    {
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(imagePath),"image/*");
        this.startActivity(intent);
    }

}