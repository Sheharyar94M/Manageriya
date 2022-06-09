package com.hammad.managerya.bottomNavFragments.homeFragment.homeDB;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HomeFragmentDao {

    //query for getting all the recent transactions (from income and expense detail tables combined)
    @Query("select income_detail.id as recordId,income_detail.income_det_cat_id as catId,income_category.income_cat_name as catName,income_category.income_icon_name as catIcon ,income_detail.income_amount as transAmount, income_detail.income_date as transDate, income_detail.income_desc as transDesc, income_detail.income_tag as transTag,income_detail.income_location as transLocation,income_detail.income_img_path as transImagePath, \"Income\" as transType from income_category inner join income_detail on income_category.income_cat_id=income_detail.income_det_cat_id UNION select expense_detail.id as recordId,expense_detail.expense_det_cat_id as catId,expense_category.expense_cat_name as catName,expense_category.expense_icon_name as catIcon,expense_detail.expense_amount as transAmount, expense_detail.expense_date as transDate,expense_detail.expense_desc as transDesc,expense_detail.expense_tag as transTag,expense_detail.expense_location as transLocation,expense_detail.expense_img_path as transImagePath, \"Expense\" as transType from expense_category inner join expense_detail on expense_category.expense_cat_id=expense_detail.expense_det_cat_id where strftime('%Y-%m', transDate)= :date ORDER by transDate DESC ")
    List<HomeRecentTransModel> getAllTransactions(String date);


    //query for getting all the recent transactions by ASC order
    @Query("select income_detail.id as recordId,income_detail.income_det_cat_id as catId,income_category.income_cat_name as catName,income_category.income_icon_name as catIcon ,income_detail.income_amount as transAmount, income_detail.income_date as transDate, income_detail.income_desc as transDesc, income_detail.income_tag as transTag,income_detail.income_location as transLocation,income_detail.income_img_path as transImagePath, \"Income\" as transType from income_category inner join income_detail on income_category.income_cat_id=income_detail.income_det_cat_id UNION select expense_detail.id as recordId,expense_detail.expense_det_cat_id as catId,expense_category.expense_cat_name as catName,expense_category.expense_icon_name as catIcon,expense_detail.expense_amount as transAmount, expense_detail.expense_date as transDate,expense_detail.expense_desc as transDesc,expense_detail.expense_tag as transTag,expense_detail.expense_location as transLocation,expense_detail.expense_img_path as transImagePath, \"Expense\" as transType from expense_category inner join expense_detail on expense_category.expense_cat_id=expense_detail.expense_det_cat_id where strftime('%Y-%m', transDate)= :date ORDER by transDate ASC")
    List<HomeRecentTransModel> getAllTransactionsByASC(String date);

}
