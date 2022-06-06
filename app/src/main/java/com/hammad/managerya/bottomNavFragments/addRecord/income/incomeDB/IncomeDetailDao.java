package com.hammad.managerya.bottomNavFragments.addRecord.income.incomeDB;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hammad.managerya.bottomNavFragments.homeFragment.homeDB.ExpenseCatDetailModel;

import java.util.List;

@Dao
public interface IncomeDetailDao {

    @Insert(onConflict = REPLACE)
    void addIncomeDetail(IncomeDetailEntity incomeDetailEntity);

    //sum of total income
    @Query("Select sum(income_amount) from income_detail")
    int getTotalIncomeSum();

    //category wise sum of income categories
    @Query("SELECT income_category.income_cat_id AS expCatId,income_category.income_cat_name AS expCatName,sum(income_amount) AS expCatAmount from income_detail INNER JOIN income_category ON income_category.income_cat_id = income_detail.income_det_cat_id group by income_category.income_cat_id")
    List<ExpenseCatDetailModel> getIncomeCategoriesSum();
}
