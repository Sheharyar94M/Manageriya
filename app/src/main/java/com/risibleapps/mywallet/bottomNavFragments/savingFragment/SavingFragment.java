package com.risibleapps.mywallet.bottomNavFragments.savingFragment;

import static com.risibleapps.mywallet.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;
import static com.risibleapps.mywallet.mainActivity.HomeScreenActivity.isHomeFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.risibleapps.mywallet.BuildConfig;
import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.RoomDB.RoomDBHelper;
import com.risibleapps.mywallet.bottomNavFragments.savingFragment.DB.SavingEntity;
import com.risibleapps.mywallet.bottomNavFragments.savingFragment.savingGoal.ActivityAddSavingGoal;
import com.risibleapps.mywallet.bottomNavFragments.savingFragment.savingTransactionDetails.ActivitySavingTransactionDetail;

import java.util.ArrayList;
import java.util.List;

public class SavingFragment extends Fragment implements SavingsAdapter.OnSavingClickListener {

    private TextView textViewCurrency1, textViewCurrency2;
    private TextView textViewTotalSaved, textViewTotalSavingGoal, textViewRemainingSavingAmount;
    private LinearProgressIndicator progressBar;
    private TextView createSavingGoal;
    private RecyclerView recyclerViewSavings;

    //list of saving goals
    List<SavingEntity> savingGoalList = new ArrayList<>();

    //database instance
    RoomDBHelper database;

    //this is used to handle the if condition of onResume() when a new saving goal is added
    static int check = 0;

    //variables for saving total saving goal values
    int totalSavingGoalAmount =0, totalSavedAmount =0;

    private InterstitialAd mInterstitialAd;

    private TextView textViewNoSavingGoal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializing mobile ad
        MobileAds.initialize(requireContext(), initializationStatus -> {});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saving, container, false);

        //initialize views
        initializeView(view);

        //loading the ad
        loadAd();

        //database initialization
        database = RoomDBHelper.getInstance(requireContext());

        //getting the saving goal list
        savingGoalList = database.savingDao().getSavingGoalList();

        //create saving transaction click listener
        createSavingGoal.setOnClickListener(v -> {
            /*startActivity(new Intent(requireContext(), ActivityAddSavingGoal.class));*/
            showAd();
        });

        //setting the recyclerview
        setupRecyclerview(savingGoalList);

        //setting the values to textviews
        setValuesToViews();

        return view;
    }

    private void initializeView(View view) {
        textViewCurrency1 = view.findViewById(R.id.txt_currency);
        textViewCurrency2 = view.findViewById(R.id.txt_currency_2);

        //setting the currency to the textviews
        textViewCurrency1.setText(CURRENCY_);
        textViewCurrency2.setText(CURRENCY_);

        textViewTotalSaved = view.findViewById(R.id.txt_total_saved);
        textViewTotalSavingGoal = view.findViewById(R.id.txt_total_saving_goal);

        textViewRemainingSavingAmount = view.findViewById(R.id.txt_remaining_saving_amount);

        //no saving goal
        textViewNoSavingGoal = view.findViewById(R.id.no_recent_trans_saving);

        progressBar = view.findViewById(R.id.progress_savings);

        createSavingGoal = view.findViewById(R.id.txt_create_saving_goal);

        recyclerViewSavings = view.findViewById(R.id.recycler_savings);
    }

    private void setupRecyclerview(List<SavingEntity> list) {
        //this static variable is incremented in recyclerview function to handle the if condition of onResume()
        check++;

        if(list.size() == 0)
        {
            textViewNoSavingGoal.setVisibility(View.VISIBLE);
            recyclerViewSavings.setVisibility(View.INVISIBLE);
        }
        else if(list.size() > 0)
        {
            textViewNoSavingGoal.setVisibility(View.GONE);
            recyclerViewSavings.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewSavings.setLayoutManager(layoutManager);

        SavingsAdapter savingsAdapter = new SavingsAdapter(requireContext(), list, this);
        recyclerViewSavings.setAdapter(savingsAdapter);
    }

    //Savings Adapter click listener
    @Override
    public void onSavingItemClicked(int position) {
        SavingEntity item= savingGoalList.get(position);

        Intent intent=new Intent(requireContext(), ActivitySavingTransactionDetail.class);
        intent.putExtra("id",item.getId());
        intent.putExtra("title",item.getSavingTitle());
        intent.putExtra("amount",item.getSavingGoalAmount());
        intent.putExtra("icon",item.getSavingIcon());
        intent.putExtra("date",item.getSavingTargetDate());
        startActivity(intent);
    }

    //Savings Adapter interface long click listener
    @Override
    public void onSavingItemLongClick() {

        //getting the updated values

        //getting the saving goal list
        savingGoalList = database.savingDao().getSavingGoalList();

        //setting the recyclerview
        setupRecyclerview(savingGoalList);

        //setting the values to zero
        totalSavingGoalAmount = 0;
        totalSavedAmount =0;

        //setting the values to textviews
        setValuesToViews();
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
            when saving fragment is called for first time the value is 1.
            when a new saving goal is added, and ActivityAddSavingGoal is finished,
            the onResume() of this (SavingFragment) is called
            */
        if(check >= 1)
        {
            //getting the saving goal list
            savingGoalList = database.savingDao().getSavingGoalList();

            //setting the latest list to DB
            setupRecyclerview(savingGoalList);

            //for the total savings record
            setValuesToViews();
        }

        //boolean variable for handling the exit dialog in HomeScreenActivity
        isHomeFragment = false;
    }

    private void setValuesToViews(){

        //sum of total saving goals amount
        totalSavingGoalAmount = database.savingDao().getAllSavingGoalSum();

        //sum of all saving goals transactions
        totalSavedAmount = database.savingDao().getAllSavingTransactionSum();

        //setting values to views

        textViewTotalSavingGoal.setText(String.valueOf(totalSavingGoalAmount));

        textViewTotalSaved.setText(String.valueOf(totalSavedAmount));

        textViewRemainingSavingAmount.setText(String.valueOf(totalSavingGoalAmount - totalSavedAmount));

        //setting the progress bar values
        progressBar.setProgress(totalSavedAmount);
        progressBar.setMax(totalSavingGoalAmount);
    }

    public void loadAd() {

        //checking whether app is running on release/debug version
        String interstitialAdId="";
        if(BuildConfig.DEBUG)
        {
            interstitialAdId=getString(R.string.interstitial_ad_debug);
            Log.i("SAVING_AD", "SAVING debug version: "+interstitialAdId);
        }
        else {
            interstitialAdId=getString(R.string.interstitial_ad_release);
            Log.i("SAVING_AD", "SAVING release version: "+interstitialAdId);
        }

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(requireContext(), interstitialAdId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                Log.i("SAVING_AD", "SAVING onAdLoaded: ");
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.i("SAVING_AD", "SAVING failed ad: "+loadAdError.getCode()+"\n"+loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });
    }

    public void showAd() {
        //checking if ad is loaded or not
        if (mInterstitialAd != null) {
            mInterstitialAd.show(requireActivity());

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    mInterstitialAd = null;

                    //moving to ActivityAddSavingGoal
                    startActivity(new Intent(requireContext(), ActivityAddSavingGoal.class));
                }

                //this function make sure no ad is loaded for second time
                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();

                    mInterstitialAd = null;
                }
            });
        }
        else
        {
            //if there is no internet connection, then no ad will be loaded and app will move onto next screen
            Intent intent = new Intent(requireContext(), ActivityAddSavingGoal.class);
            startActivity(intent);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mInterstitialAd = null;
    }

    @Override
    public void onDestroy() {
        mInterstitialAd = null;
        super.onDestroy();
    }
}