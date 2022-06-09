package com.hammad.managerya.bottomNavFragments.walletFragment.budget;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.hammad.managerya.bottomNavFragments.walletFragment.budget.RoomDB.BudgetEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityCreateBudget extends AppCompatActivity implements AddExpenseAdapter.ExpenseInterfaceListener {

    private Toolbar toolbar;
    private EditText editTextAmount;
    private RecyclerView recyclerView;
    private AppCompatButton buttonCreateBudget;

    //for saving budget category details
    private String categoryName="";
    private int categoryId = -1;
    private int categoryIcon = -1;

    //Database instance
    private RoomDBHelper database;

    //this income category list contain first 8 elements
    private List<ExpenseCategoryEntity> expenseCategoryList=new ArrayList<>();

    //for saving date
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);

        //initialize views
        initializeView();

        //getting the current date
        getCurrentDate();

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

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDate = dateFormat.format(calendar.getTime());
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
                startActivity(new Intent(ActivityCreateBudget.this,BudgetActivity.class));
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
        categoryId = expenseCategoryList.get(position).getExpenseCatId();
        categoryIcon = expenseCategoryList.get(position).getExpenseCatIconName();
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
            //saving the budget
            database.budgetDao().addBudget(new BudgetEntity(categoryId,categoryName,categoryIcon,Integer.valueOf(editTextAmount.getText().toString()),currentDate));
            Toast.makeText(this, "Budget Created Successfully", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(ActivityCreateBudget.this,BudgetActivity.class));
                    finish();
                }
            },1200);
        }
    }
}