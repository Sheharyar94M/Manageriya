package com.hammad.managerya.bottomNavFragments.walletFragment.budget;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.hammad.managerya.R;
import com.hammad.managerya.RoomDB.RoomDBHelper;
import com.hammad.managerya.bottomNavFragments.addRecord.expense.AddExpenseAdapter;
import com.hammad.managerya.bottomNavFragments.addRecord.expense.expenseDB.ExpenseCategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCreateBudget extends AppCompatActivity implements AddExpenseAdapter.ExpenseInterfaceListener {

    private Toolbar toolbar;
    private EditText editTextAmount;
    private RecyclerView recyclerView;
    private AppCompatButton buttonCreateBudget;

    private String categoryName="";

    //Database instance
    private RoomDBHelper database;

    //this income category list contain first 8 elements
    private List<ExpenseCategoryEntity> expenseCategoryList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);

        //initialize views
        initializeView();

        //initialize database helper class
        database=RoomDBHelper.getInstance(this);

        //getting the income categories list
        expenseCategoryList = database.expenseCategoryDao().getExpenseCategoryList();

        //setting the recyclerview
        setRecyclerView();

        buttonCreateBudget.setOnClickListener(view -> createBudget());
    }

    private void initializeView()
    {
        toolbar=findViewById(R.id.toolbar_create_budget);
        editTextAmount=findViewById(R.id.edit_text_create_budget);
        recyclerView=findViewById(R.id.recycler_create_budget);
        buttonCreateBudget=findViewById(R.id.button_create_budget);

        //setting edittext hint
        editTextAmount.setHint(CURRENCY_ + " 0");

        //setting toolbar as support action bar
        setSupportActionBar(toolbar);
    }

    //onCreateOptionsMenu & onOptionsItemSelected for handling the cancel button on toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_saving_goal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.finish_saving_goal:
                //finish the activity
                finish();
                break;
        }

        return true;
    }

    private void setRecyclerView()
    {
        GridLayoutManager layoutManager=new GridLayoutManager(this,2,RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        AddExpenseAdapter expenseAdapter=new AddExpenseAdapter(this, expenseCategoryList,this);
        recyclerView.setAdapter(expenseAdapter);
    }

    //adapter click listener
    @Override
    public void onExpenseItemClicked(int position, String catName) {
        categoryName = catName;
    }

    private void createBudget()
    {
        if(editTextAmount.getText().toString().isEmpty())
        {
            Snackbar snackbar=Snackbar.make(recyclerView,"Enter Amount", BaseTransientBottomBar.LENGTH_SHORT);
            snackbar.show();
        }
        else if(categoryName.isEmpty())
        {
            Snackbar snackbar=Snackbar.make(recyclerView,"Select Category", BaseTransientBottomBar.LENGTH_SHORT);
            snackbar.show();
        }
        else
        {
            Toast.makeText(this, "Budget created\n"+"Category: "+categoryName, Toast.LENGTH_LONG).show();
        }
    }
}