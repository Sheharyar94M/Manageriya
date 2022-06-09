package com.hammad.managerya.bottomNavFragments.addRecord.expense.expenseDB;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hammad.managerya.bottomNavFragments.homeFragment.homeDB.ExpenseCatDetailModel;
import com.hammad.managerya.bottomNavFragments.homeFragment.homeDB.HomeRecentTransModel;

import java.util.List;

@Dao
public interface ExpenseDetailDao {

    @Insert(onConflict = REPLACE)
    void addExpenseDetail(ExpenseDetailEntity expenseDetailEntity);

    //sum of total expense
    @Query("Select sum(expense_amount) from expense_detail where strftime('%Y-%m', expense_date)= :date")
    int getTotalExpenseSum(String date);

    //category wise sum of expense categories
    @Query("SELECT expense_category.expense_cat_id AS expCatId,expense_category.expense_cat_name AS expCatName,sum(expense_amount) AS expCatAmount from expense_detail INNER JOIN expense_category ON expense_category.expense_cat_id = expense_detail.expense_det_cat_id where strftime('%Y-%m', expense_date)= :date group by expense_category.expense_cat_id")
    List<ExpenseCatDetailModel> getExpenseCategoriesSum(String date);

    //search expense detail by category id
    @Query("Select sum(expense_amount) from expense_detail where expense_det_cat_id= :catId AND expense_date= :date")
    int getExpenseCategorySum(int catId,String date);

    //query for retrieving Expense details transaction searched by category id
    @Query("select expense_detail.id as recordId,expense_detail.expense_det_cat_id as catId,expense_category.expense_cat_name as catName,expense_category.expense_icon_name as catIcon,expense_detail.expense_amount as transAmount, expense_detail.expense_date as transDate,expense_detail.expense_desc as transDesc,expense_detail.expense_tag as transTag,expense_detail.expense_location as transLocation,expense_detail.expense_img_path as transImagePath, \"Expense\" as transType from expense_category inner join expense_detail on expense_category.expense_cat_id=expense_detail.expense_det_cat_id where expense_det_cat_id= :catId AND strftime('%Y-%m', transDate) = :date ORDER by transDate DESC")
    List<HomeRecentTransModel> getExpenseDetailById(int catId,String date);

    //delete expense detail record searched by category id and id(auto-incremented) primary key
    @Query("DELETE from expense_detail WHERE expense_detail.expense_det_cat_id = :catId AND expense_detail.id = :recordId")
    void deleteExpenseDetail(int catId,int recordId);
}
