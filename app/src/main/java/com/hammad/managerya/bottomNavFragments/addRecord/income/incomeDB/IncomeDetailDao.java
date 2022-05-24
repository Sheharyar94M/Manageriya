package com.hammad.managerya.bottomNavFragments.addRecord.income.incomeDB;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface IncomeDetailDao {

    @Insert(onConflict = REPLACE)
    void addIncomeDetail(IncomeDetailEntity incomeDetailEntity);
}
