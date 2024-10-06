package com.risibleapps.mywallet.mainActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.risibleapps.mywallet.BuildConfig;
import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.RoomDB.RoomDBHelper;
import com.risibleapps.mywallet.bottomNavFragments.addRecord.AddRecordActivity;
import com.risibleapps.mywallet.bottomNavFragments.addRecord.expense.expenseDB.ExpenseCategoryEntity;
import com.risibleapps.mywallet.bottomNavFragments.addRecord.income.incomeDB.IncomeCategoryEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    ImageView imageViewAddRecord;
    Activity homeScreenActivity;

    //lists of income & expense category
    List<IncomeCategoryEntity> incomeCatList = new ArrayList<>();
    List<ExpenseCategoryEntity> expenseCatList = new ArrayList<>();

    //Room Database instance
    RoomDBHelper database;

    //shared preference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor preferenceEditor;

    //boolean for checking preference value for income & expense categories
    boolean areCategoriesAdded;

    private InterstitialAd mInterstitialAd;

    private String nativeAdvanceId = "";
    private UnifiedNativeAd nativeAd;

    Dialog dialog;

    /*
        static boolean variable for handling the exit dialog on back pressed
        We want only to show exit dialog when home fragment is visible (jugar)
    */
    public static boolean isHomeFragment = true;
    private AdLoader adLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //initializing mobile ad
        MobileAds.initialize(this);

        //initialize views
        initialViews();

        //loading the ad
        loadAd();

        //loading the app exit ad
        exitAlertDialog();

        //functions for adding category data in income & expense category lists
        incomeCategories();
        expenseCategories();

        //initializing home screen activity instance
        homeScreenActivity = this;

        //initialize preference
        initializePreference();

        //Room DB instance
        database = RoomDBHelper.getInstance(homeScreenActivity);

        //getting shared preference value
        areCategoriesAdded = sharedPreferences.getBoolean(getString(R.string.categories_saved), false);

        /*
            This condition is called only once. When this called, income and expense category will be created in database,
            and preference value will be set to true.
            This condition will be only true again when user clears the app data or application is uninstalled and then installed again
        */
        if (!areCategoriesAdded) {
            addCategories();
        }

        //setting the support action bar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //code for menu button on top left side of toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //setting the onclick on Navigation View
        navigationView.setNavigationItemSelectedListener(this);

        //bottom navigation
        NavController navController = Navigation.findNavController(this, R.id.fragment_container);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        //image view add record click listener
        imageViewAddRecord.setOnClickListener(view -> addRecord());

    }

    private void initialViews() {

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        imageViewAddRecord = findViewById(R.id.image_view_add_record);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (isHomeFragment) {
            //calling the exit alert dialog only when HomeFragment is visible
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

    //navigation view items click listeners
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //setting the selected item to checked state
        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.menu_share_with_friends:
                shareAppLink();
                break;

            case R.id.menu_privacy_policy:
                privacyPolicy();
                break;

            case R.id.menu_clear_record:
                clearAllRecord(item);
                break;

           /* case R.id.menu_4:
                Toast.makeText(this, "Menu 4", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_5:
                Toast.makeText(this, "Menu 5", Toast.LENGTH_SHORT).show();
                break;*/
        }

        return true;
    }

    private void addRecord() {
        showAd();
    }

    private void incomeCategories() {

        incomeCatList.add(new IncomeCategoryEntity(1, "Allowance", R.drawable.allowance));

        incomeCatList.add(new IncomeCategoryEntity(2, "Bonus", R.drawable.bonus));
        incomeCatList.add(new IncomeCategoryEntity(3, "Business \nProfit", R.drawable.business_profit));

        incomeCatList.add(new IncomeCategoryEntity(4, "Commission", R.drawable.commission));

        incomeCatList.add(new IncomeCategoryEntity(5, "Freelance", R.drawable.freelance));

        incomeCatList.add(new IncomeCategoryEntity(6, "Gifts \nReceived", R.drawable.gifts_received));

        incomeCatList.add(new IncomeCategoryEntity(7, "Investment", R.drawable.investment));

        incomeCatList.add(new IncomeCategoryEntity(8, "Loan \nReceived", R.drawable.loan_received));

        incomeCatList.add(new IncomeCategoryEntity(9, "Pension", R.drawable.pension));
        incomeCatList.add(new IncomeCategoryEntity(10, "Pocket \nMoney", R.drawable.pocket_money));

        incomeCatList.add(new IncomeCategoryEntity(11, "Salary", R.drawable.salary));
        incomeCatList.add(new IncomeCategoryEntity(12, "Savings", R.drawable.savings));

        incomeCatList.add(new IncomeCategoryEntity(13, "Tuition", R.drawable.tuition));
    }

    private void expenseCategories() {

        expenseCatList.add(new ExpenseCategoryEntity(1, "Bills & \nUtilities", R.drawable.bills));

        expenseCatList.add(new ExpenseCategoryEntity(2, "Charity", R.drawable.charity));
        expenseCatList.add(new ExpenseCategoryEntity(3, "Committee", R.drawable.committee));

        expenseCatList.add(new ExpenseCategoryEntity(4, "Entertain\nment", R.drawable.entertainment));
        expenseCatList.add(new ExpenseCategoryEntity(5, "Electronics", R.drawable.electronics));
        expenseCatList.add(new ExpenseCategoryEntity(6, "Education", R.drawable.education));

        expenseCatList.add(new ExpenseCategoryEntity(7, "Family", R.drawable.family));
        expenseCatList.add(new ExpenseCategoryEntity(8, "Food", R.drawable.food));
        expenseCatList.add(new ExpenseCategoryEntity(9, "Fuel", R.drawable.fuel));

        expenseCatList.add(new ExpenseCategoryEntity(10, "Grocery", R.drawable.grocery));

        expenseCatList.add(new ExpenseCategoryEntity(11, "Health", R.drawable.health));
        expenseCatList.add(new ExpenseCategoryEntity(12, "Home", R.drawable.home_e));

        expenseCatList.add(new ExpenseCategoryEntity(13, "Installment", R.drawable.installment));
        expenseCatList.add(new ExpenseCategoryEntity(14, "Insurance", R.drawable.insurance));

        expenseCatList.add(new ExpenseCategoryEntity(15, "Loan \nPaid", R.drawable.loan_paid));

        expenseCatList.add(new ExpenseCategoryEntity(16, "Medical", R.drawable.medical));
        expenseCatList.add(new ExpenseCategoryEntity(17, "Mobile", R.drawable.mobile));

        expenseCatList.add(new ExpenseCategoryEntity(18, "Office", R.drawable.office));
        expenseCatList.add(new ExpenseCategoryEntity(19, "Other \nExpenses", R.drawable.other_expenses));

        expenseCatList.add(new ExpenseCategoryEntity(20, "Rent \nPaid", R.drawable.rent_paid));

        expenseCatList.add(new ExpenseCategoryEntity(21, "Shopping", R.drawable.shopping));

        expenseCatList.add(new ExpenseCategoryEntity(22, "Transport", R.drawable.transport));
        expenseCatList.add(new ExpenseCategoryEntity(23, "Travel", R.drawable.travel));

        expenseCatList.add(new ExpenseCategoryEntity(24, "Wedding", R.drawable.wedding));

    }

    private void initializePreference() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferenceEditor = sharedPreferences.edit();
    }

    private void addCategories() {

        database.incomeCategoryDao().addIncomeCat(incomeCatList);
        database.expenseCategoryDao().addExpenseCat(expenseCatList);
        //database.close();

        //setting the preference value to true to indicate that categories tables are added
        preferenceEditor.putBoolean(getString(R.string.categories_saved), true);
        preferenceEditor.apply();
    }

    private void shareAppLink() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String subSectionLink = "Download\n Managerya: Daily-Expense-Manager \nfrom:\n\n\t\"https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
        intent.putExtra(Intent.EXTRA_TEXT, subSectionLink);
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    private void privacyPolicy() {
        Uri uri = Uri.parse("https://risibleapps.blogspot.com/2022/02/privacy-policy-at-risibleapps-we.html");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void clearAllRecord(MenuItem item) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Delete All Record")
                .setMessage("Want to delete all record?")
                .setPositiveButton("DELETE", (dialogInterface, i) -> {

                    //deleting all record
                    database.clearAllTables();

                    //setting the preference value to false to indicate that categories tables are empty
                    preferenceEditor.putBoolean(getString(R.string.categories_saved), false);
                    preferenceEditor.apply();

                    //setting the checked item to false (removes highlighted item)
                    item.setChecked(false);

                    Toast.makeText(homeScreenActivity, "Record cleared Successfully!", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(() -> {
                        //showing the ad
                        showAdClearHistory();
                    }, 1000);

                })
                .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss());

        alertDialog.create().show();
    }

    public void loadAd() {

        //checking whether app is running on release/debug version
        String interstitialAdId = "";
        if (BuildConfig.DEBUG) {
            interstitialAdId = getString(R.string.interstitial_ad_debug);
            Log.i("HOME_ACT_AD", "HOME_ACT debug version: " + interstitialAdId);
        } else {
            interstitialAdId = getString(R.string.interstitial_ad_release);
            Log.i("HOME_ACT_AD", "HOME_ACT release version: " + interstitialAdId);
        }

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, interstitialAdId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                Log.i("HOME_ACT_AD", "HOME_ACT onAdLoaded: ");
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.i("HOME_ACT_AD", "HOME_ACT failed ad: " + loadAdError.getCode() + "\n" + loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });
    }

    public void showAd() {
        //checking if ad is loaded or not
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    mInterstitialAd = null;

                    //moving to AddRecordActivity
                    startActivity(new Intent(HomeScreenActivity.this, AddRecordActivity.class));
                }

                //this function make sure no ad is loaded for second time
                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();

                    mInterstitialAd = null;
                }
            });
        } else {
            //if there is no internet connection, then no ad will be loaded and app will move onto next screen
            Intent intent = new Intent(this, AddRecordActivity.class);
            startActivity(intent);
        }
    }

    public void showAdClearHistory() {
        //checking if ad is loaded or not
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    mInterstitialAd = null;

                    //recreating the activity
                    startActivity(new Intent(HomeScreenActivity.this, HomeScreenActivity.class));
                    finish();
                }

                //this function make sure no ad is loaded for second time
                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();

                    mInterstitialAd = null;
                }
            });
        } else {
            //if there is no internet connection, then no ad will be loaded
            startActivity(new Intent(HomeScreenActivity.this, HomeScreenActivity.class));
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mInterstitialAd = null;
    }

    @Override
    protected void onDestroy() {
        mInterstitialAd = null;

        if (nativeAd != null) {
            nativeAd.destroy();
        }

        super.onDestroy();
    }

    private void exitAlertDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.exit_dialog);
        dialog.setCancelable(false);

        // Get the screen dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Calculate the dialog's dimensions (percentage of the screen)
        int dialogWidth = (int) (screenWidth * 1.0);
        int dialogHeight = (int) (screenHeight * 0.5);

        // Set the dialog's dimensions
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = dialogWidth;
        layoutParams.height = dialogHeight;
        Objects.requireNonNull(dialog.getWindow()).setAttributes(layoutParams);

        //setting the transparent background
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //loading the ad
        LoadAd(dialog);

        AppCompatButton buttonExit = dialog.findViewById(R.id.btn_exit);
        AppCompatButton buttonCancel = dialog.findViewById(R.id.btn_cancel);

        //click listener buttons
        buttonExit.setOnClickListener(view -> super.onBackPressed());

        buttonCancel.setOnClickListener(view -> dialog.dismiss());

    }

    private void LoadAd(Dialog dialog) {

        //checking whether the app is running in release or debug version
        if (BuildConfig.DEBUG) {
            nativeAdvanceId = getString(R.string.native_advance_debug);
            Log.i("NATIVE_ADVANCE_AD: ", "debug version: " + nativeAdvanceId);
        } else {
            nativeAdvanceId = getString(R.string.native_advance_release);
            Log.i("NATIVE_ADVANCE_AD: ", "release version: " + nativeAdvanceId);
        }

        //setting Ad
        TemplateView adTemplate = dialog.findViewById(R.id.ad_template);
        adLoader = new AdLoader.Builder(this, nativeAdvanceId).forNativeAd(nativeAd -> {

            adTemplate.setStyles(new NativeTemplateStyle.Builder().build());
            adTemplate.setNativeAd(nativeAd);

        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }
}