package com.hammad.managerya.bottomNavFragments.savingFragment.DB;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SavingDao {

    //insert data
    @Insert(onConflict = REPLACE)
    void addSavingGoal(SavingEntity savingEntity);

    //insert saving goal transaction
    @Insert(onConflict = REPLACE)
    void addGoalTransaction(SavingTransactionEntity savingTransactionEntity);

    //get list of saving goals
    @Query("select * from saving order by id DESC")
    List<SavingEntity> getSavingGoalList();

    //sum of saving goal transaction searched by id
    @Query("select sum(saving_trans_amount) from saving_transaction where saving_goal_id= :savingId")
    int getSavingTransSumById(int savingId);

}
