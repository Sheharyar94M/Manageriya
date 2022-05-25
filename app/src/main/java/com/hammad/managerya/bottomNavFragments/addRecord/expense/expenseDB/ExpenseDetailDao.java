package com.hammad.managerya.bottomNavFragments.addRecord.expense.expenseDB;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hammad.managerya.bottomNavFragments.homeFragment.homeDB.ExpenseCatDetailModel;

import java.util.List;

@Dao
public interface ExpenseDetailDao {

    @Insert(onConflict = REPLACE)
    void addExpenseDetail(ExpenseDetailEntity expenseDetailEntity);

    //sum of total expense
    @Query("Select sum(expense_amount) from expense_detail")
    int getTotalExpenseSum();

    //category wise sum of expense categories
    @Query("SELECT expense_category.expense_cat_id AS expCatId,expense_category.expense_cat_name AS expCatName,sum(expense_amount) AS expCatAmount from expense_detail INNER JOIN expense_category ON expense_category.expense_cat_id = expense_detail.expense_det_cat_id group by expense_category.expense_cat_id")
    List<ExpenseCatDetailModel> getExpenseCategoriesSum();
}
