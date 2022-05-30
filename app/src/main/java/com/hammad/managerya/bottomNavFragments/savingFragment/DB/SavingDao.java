package com.hammad.managerya.bottomNavFragments.savingFragment.DB;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface SavingDao {

    //insert data
    @Insert(onConflict = REPLACE)
    void addSavingGoal(SavingEntity savingEntity);


}
