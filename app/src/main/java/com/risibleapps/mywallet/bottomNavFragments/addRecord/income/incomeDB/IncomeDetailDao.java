package com.risibleapps.mywallet.bottomNavFragments.addRecord.income.incomeDB;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.risibleapps.mywallet.bottomNavFragments.homeFragment.homeDB.ExpenseCatDetailModel;

import java.util.List;

@Dao
public interface IncomeDetailDao {

    @Insert(onConflict = REPLACE)
    void addIncomeDetail(IncomeDetailEntity incomeDetailEntity);

    //sum of total income
    @Query("Select sum(income_amount) from income_detail where strftime('%Y-%m', income_date)= :date")
    int getTotalIncomeSum(String date);

    //category wise sum of income categories
    @Query("SELECT income_category.income_cat_id AS expCatId,income_category.income_cat_name AS expCatName,sum(income_amount) AS expCatAmount from income_detail INNER JOIN income_category ON income_category.income_cat_id = income_detail.income_det_cat_id where strftime('%Y-%m', income_date) = :date group by income_category.income_cat_id")
    List<ExpenseCatDetailModel> getIncomeCategoriesSum(String date);

    //delete income detail record searched by category id and id(auto-incremented) primary key
    @Query("DELETE from income_detail WHERE income_detail.income_det_cat_id = :catId AND income_detail.id = :recordId")
    void deleteIncomeDetail(int catId,int recordId);

}
