package com.hammad.managerya.mainActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.hammad.managerya.BuildConfig;
import com.hammad.managerya.R;
import com.hammad.managerya.RoomDB.RoomDBHelper;
import com.hammad.managerya.bottomNavFragments.addRecord.AddRecordActivity;
import com.hammad.managerya.bottomNavFragments.addRecord.expense.expenseDB.ExpenseCategoryEntity;
import com.hammad.managerya.bottomNavFragments.addRecord.income.incomeDB.IncomeCategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    ImageView imageViewAddRecord;
    Activity homeScreenActivity;

    //lists of income & expense category
    List<IncomeCategoryEntity> incomeCatList=new ArrayList<>();
    List<ExpenseCategoryEntity> expenseCatList=new ArrayList<>();

    //Room Database instance
    RoomDBHelper database;

    //shared preference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor preferenceEditor;

    //boolean for checking preference value for income & expense categories
    boolean areCategoriesAdded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //initialize views
        initialViews();

        //functions for adding category data in income & expense category lists
        incomeCategories();
        expenseCategories();

        //initializing home screen activity instance
        homeScreenActivity=this;

        //initialize preference
        initializePreference();

        //Room DB instance
        database=RoomDBHelper.getInstance(homeScreenActivity);

        //getting shared preference value
        areCategoriesAdded=sharedPreferences.getBoolean(getString(R.string.categories_saved),false);

        /*
            This condition is called only once. When this called, income and expense category will be created in database,
            and preference value will be set to true.
            This condition will be only true again when user clears the app data or application is uninstalled and then installed again
        */
        if(!areCategoriesAdded)
        {
            addCategories();
        }

        //setting the support action bar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //code for menu button on top left side of toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //setting the onclick on Navigation View
        navigationView.setNavigationItemSelectedListener(this);

        //bottom navigation
        NavController navController= Navigation.findNavController(this,R.id.fragment_container);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

        //image view add record click listener
        imageViewAddRecord.setOnClickListener(view -> addRecord());

    }

    private void initialViews() {

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navigation_view);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        imageViewAddRecord=findViewById(R.id.image_view_add_record);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //navigation view items click listeners
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //setting the selected item to checked state
        item.setChecked(true);

        switch (item.getItemId())
        {
            case R.id.menu_share_with_friends:
                shareAppLink();
                break;

            case R.id.menu_privacy_policy:
                privacyPolicy();
                break;

            case R.id.menu_clear_record:
                clearAllRecord();
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

    private void addRecord()
    {
        startActivity(new Intent(this, AddRecordActivity.class));
    }

    private void incomeCategories() {

        incomeCatList.add(new IncomeCategoryEntity(1,"Allowance",R.drawable.allowance));

        incomeCatList.add(new IncomeCategoryEntity(2,"Bonus",R.drawable.bonus));
        incomeCatList.add(new IncomeCategoryEntity(3,"Business \nProfit",R.drawable.business_profit));

        incomeCatList.add(new IncomeCategoryEntity(4,"Commission",R.drawable.commission));

        incomeCatList.add(new IncomeCategoryEntity(5,"Freelance",R.drawable.freelance));

        incomeCatList.add(new IncomeCategoryEntity(6,"Gifts \nReceived",R.drawable.gifts_received));

        incomeCatList.add(new IncomeCategoryEntity(7,"Investment",R.drawable.investment));

        incomeCatList.add(new IncomeCategoryEntity(8,"Loan \nReceived",R.drawable.loan_received));

        incomeCatList.add(new IncomeCategoryEntity(9,"Pension",R.drawable.pension));
        incomeCatList.add(new IncomeCategoryEntity(10,"Pocket \nMoney",R.drawable.pocket_money));

        incomeCatList.add(new IncomeCategoryEntity(11,"Salary",R.drawable.salary));
        incomeCatList.add(new IncomeCategoryEntity(12,"Savings",R.drawable.savings));

        incomeCatList.add(new IncomeCategoryEntity(13,"Tuition",R.drawable.tuition));
    }

    private void expenseCategories() {

        expenseCatList.add(new ExpenseCategoryEntity(1,"Bills & \nUtilities",R.drawable.bills));

        expenseCatList.add(new ExpenseCategoryEntity(2,"Charity", R.drawable.charity));
        expenseCatList.add(new ExpenseCategoryEntity(3,"Committee",R.drawable.committee));

        expenseCatList.add(new ExpenseCategoryEntity(4,"Entertain\nment", R.drawable.entertainment));
        expenseCatList.add(new ExpenseCategoryEntity(5,"Electronics",R.drawable.electronics));
        expenseCatList.add(new ExpenseCategoryEntity(6,"Education",R.drawable.education));

        expenseCatList.add(new ExpenseCategoryEntity(7,"Family",R.drawable.family));
        expenseCatList.add(new ExpenseCategoryEntity(8,"Food",R.drawable.food));
        expenseCatList.add(new ExpenseCategoryEntity(9,"Fuel",R.drawable.fuel));

        expenseCatList.add(new ExpenseCategoryEntity(10,"Grocery",R.drawable.grocery));

        expenseCatList.add(new ExpenseCategoryEntity(11,"Health",R.drawable.health));
        expenseCatList.add(new ExpenseCategoryEntity(12,"Home",R.drawable.home_e));

        expenseCatList.add(new ExpenseCategoryEntity(13,"Installment",R.drawable.installment));
        expenseCatList.add(new ExpenseCategoryEntity(14,"Insurance",R.drawable.insurance));

        expenseCatList.add(new ExpenseCategoryEntity(15,"Loan \nPaid",R.drawable.loan_paid));

        expenseCatList.add(new ExpenseCategoryEntity(16,"Medical",R.drawable.medical));
        expenseCatList.add(new ExpenseCategoryEntity(17,"Mobile",R.drawable.mobile));

        expenseCatList.add(new ExpenseCategoryEntity(18,"Office",R.drawable.office));
        expenseCatList.add(new ExpenseCategoryEntity(19,"Other \nExpenses",R.drawable.other_expenses));

        expenseCatList.add(new ExpenseCategoryEntity(20,"Rent \nPaid",R.drawable.rent_paid));

        expenseCatList.add(new ExpenseCategoryEntity(21,"Shopping",R.drawable.shopping));

        expenseCatList.add(new ExpenseCategoryEntity(22,"Transport",R.drawable.transport));
        expenseCatList.add(new ExpenseCategoryEntity(23,"Travel",R.drawable.travel));

        expenseCatList.add(new ExpenseCategoryEntity(24,"Wedding",R.drawable.wedding));

    }

    private void initializePreference() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferenceEditor = sharedPreferences.edit();
    }

    private void addCategories(){

        database.incomeCategoryDao().addIncomeCat(incomeCatList);
        database.expenseCategoryDao().addExpenseCat(expenseCatList);
        database.close();

        //setting the preference value to true to indicate that categories tables are added
        preferenceEditor.putBoolean(getString(R.string.categories_saved),true);
        preferenceEditor.apply();
    }

    private void shareAppLink(){
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

    private void clearAllRecord(){

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);

        alertDialog.setTitle("Delete All Record")
                .setMessage("Want to delete all record?")
                .setPositiveButton("DELETE", (dialogInterface, i) -> {

                    //deleting all record
                    database.clearAllTables();

                    //setting the preference value to false to indicate that categories tables are empty
                    preferenceEditor.putBoolean(getString(R.string.categories_saved),false);
                    preferenceEditor.apply();

                    //recreating the activity
                    recreate();
                })
                .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss());

        alertDialog.create().show();
    }
}