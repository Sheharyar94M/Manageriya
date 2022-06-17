package com.risibleapps.mywallet.bottomNavFragments.walletFragment.budget.RoomDB;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BudgetDao {

    @Insert(onConflict = REPLACE)
    void addBudget(BudgetEntity budgetEntity);

    //where strftime('%Y-%m', budgetDate) = :date

    //get list of created budgets
    @Query("select category_id as budgetCatId, category_name as budgetCatName, category_icon as budgetIcon, budget_limit as budgetLimit, budget_date as budgetDate from budget where budgetDate= :date")
    List<BudgetDetailsModel> getBudgetList(String date);

    //query for searching specific budget by category id
    @Query("select category_id as budgetCatId, category_name as budgetCatName, category_icon as budgetIcon, budget_limit as budgetLimit from budget where category_id= :catId")
    List<BudgetDetailsModel> getBudgetById(int catId);

   /* //query for retrieving all the created budgets
    @Query("select b.category_id as budgetCatId,b.category_name as budgetCatName,b.budget_limit as budgetLimit, (select sum(expense_detail.expense_amount) from expense_detail where category_id= b.category_id ) as budget_spend from budget b")
    List<BudgetDetailsModel> getBudgetDetails();*/

    //update budget values
    @Query("update budget set budget_limit= :budgetAmount where category_id= :catId")
    void updateBudgetLimit(int budgetAmount,int catId);

    //delete budget by id
    @Query("DELETE from budget where category_id= :catId")
    void deleteBudget(int catId);
}
