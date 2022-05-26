package com.hammad.managerya.RoomDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hammad.managerya.bottomNavFragments.walletFragment.budget.RoomDB.BudgetDao;
import com.hammad.managerya.bottomNavFragments.walletFragment.budget.RoomDB.BudgetEntity;
import com.hammad.managerya.bottomNavFragments.addRecord.expense.expenseDB.ExpenseCategoryDao;
import com.hammad.managerya.bottomNavFragments.addRecord.expense.expenseDB.ExpenseCategoryEntity;
import com.hammad.managerya.bottomNavFragments.addRecord.expense.expenseDB.ExpenseDetailDao;
import com.hammad.managerya.bottomNavFragments.addRecord.expense.expenseDB.ExpenseDetailEntity;
import com.hammad.managerya.bottomNavFragments.addRecord.income.incomeDB.IncomeCategoryDao;
import com.hammad.managerya.bottomNavFragments.addRecord.income.incomeDB.IncomeCategoryEntity;
import com.hammad.managerya.bottomNavFragments.addRecord.income.incomeDB.IncomeDetailDao;
import com.hammad.managerya.bottomNavFragments.addRecord.income.incomeDB.IncomeDetailEntity;
import com.hammad.managerya.bottomNavFragments.homeFragment.homeDB.HomeFragmentDao;

@Database(entities = {IncomeCategoryEntity.class, IncomeDetailEntity.class, ExpenseCategoryEntity.class, ExpenseDetailEntity.class, BudgetEntity.class}, version = 1, exportSchema = false)
public abstract class RoomDBHelper extends RoomDatabase {

    //create database instance
    public static RoomDBHelper instance;

    //database name
    public static String DATABASE_NAME = "managerya_db";

    public static synchronized RoomDBHelper getInstance(Context context) {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context, RoomDBHelper.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

    //DAOs
    public abstract IncomeCategoryDao incomeCategoryDao();

    public abstract IncomeDetailDao incomeDetailDao();

    public abstract ExpenseCategoryDao expenseCategoryDao();

    public abstract ExpenseDetailDao expenseDetailDao();

    public abstract HomeFragmentDao homeFragmentDao();

    public abstract BudgetDao budgetDao();

}
