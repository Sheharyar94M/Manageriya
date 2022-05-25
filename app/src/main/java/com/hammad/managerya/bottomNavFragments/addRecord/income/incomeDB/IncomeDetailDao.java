package com.hammad.managerya.bottomNavFragments.addRecord.income.incomeDB;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface IncomeDetailDao {

    @Insert(onConflict = REPLACE)
    void addIncomeDetail(IncomeDetailEntity incomeDetailEntity);

    //sum of total income
    @Query("Select sum(income_amount) from income_detail")
    int getTotalIncomeSum();
}
