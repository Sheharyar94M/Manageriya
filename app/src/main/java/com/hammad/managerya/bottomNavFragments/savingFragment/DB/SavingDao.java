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

    //get the saving transaction list searched by id
    @Query("select id as id, saving_goal_id as savingId, saving_title as savingTitle,saving_trans_amount as savingGoalAmount,saving_icon as savingIcon, saving_trans_date as savingTargetDate  from saving_transaction where saving_goal_id= :savingId ORDER BY saving_trans_date DESC")
    List<SavingModel> getSavingTransDetailsListById(int savingId);

    //sum of all saving goal amounts
    @Query("select sum(saving_goal_amount) from saving")
    int getAllSavingGoalSum();

    //sum of all the saving goals transactions
    @Query("select sum(saving_trans_amount) from saving_transaction")
    int getAllSavingTransactionSum();

    //delete saving goal
    @Query("DELETE from saving where id= :id")
    void deleteSavingGoal(int id);

    //delete all saving goal transaction
    @Query("DELETE from saving_transaction where saving_goal_id= :id")
    void deleteAllSavingTransactions(int id);

    //deleting a specific goal transaction
    @Query("DELETE from saving_transaction where id= :id AND saving_goal_id= :savingId")
    void deleteSavingGoalTransaction(int id, int savingId);

}
