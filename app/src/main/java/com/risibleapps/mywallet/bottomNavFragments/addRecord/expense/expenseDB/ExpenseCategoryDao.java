package com.risibleapps.mywallet.bottomNavFragments.addRecord.expense.expenseDB;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExpenseCategoryDao {

    @Insert(onConflict = REPLACE)
    void addExpenseCat(List<ExpenseCategoryEntity> expenseCategoryList);

    //get expense category list
    @Query("SELECT * FROM expense_category ORDER BY id ASC")
    List<ExpenseCategoryEntity> getExpenseCategoryList();
}
