package com.hammad.managerya.bottomNavFragments.addRecord.expense.expenseDB;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface ExpenseDetailDao {

    @Insert(onConflict = REPLACE)
    void addExpenseDetail(ExpenseDetailEntity expenseDetailEntity);
}
