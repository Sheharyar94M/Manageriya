package com.hammad.managerya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    ImageView imageViewAddRecord;
    Activity homeScreenActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //initialize views
        initialViews();

        //initializing home screen activity instance
        homeScreenActivity=this;

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
            case R.id.menu_1:
                Toast.makeText(this, "Menu 1", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_2:
                Toast.makeText(this, "Menu 2", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_3:
                Toast.makeText(this, "Menu 3", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_4:
                Toast.makeText(this, "Menu 4", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_5:
                Toast.makeText(this, "Menu 5", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    private void addRecord()
    {
        Toast.makeText(homeScreenActivity, "Clicked", Toast.LENGTH_SHORT).show();
    }
}