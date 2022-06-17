package com.risibleapps.mywallet.bottomNavFragments.addRecord.income.incomeDB;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IncomeCategoryDao {

    @Insert(onConflict = REPLACE)
    void addIncomeCat(List<IncomeCategoryEntity> incomeCategoryList);

    //get income category list
    @Query("SELECT * FROM income_category ORDER BY id ASC")
    List<IncomeCategoryEntity> getIncomeCategoryList();
}
