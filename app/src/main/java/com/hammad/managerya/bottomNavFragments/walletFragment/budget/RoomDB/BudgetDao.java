package com.hammad.managerya.bottomNavFragments.walletFragment.budget.RoomDB;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BudgetDao {

    @Insert(onConflict = REPLACE)
    void addBudget(BudgetEntity budgetEntity);

    //query for retrieving all the created budgets
    @Query("select b.category_id as budgetCatId,b.category_name as budgetCatName,b.budget_limit as budgetLimit, (select sum(expense_detail.expense_amount) from expense_detail where category_id= b.category_id ) as budget_spend from budget b")
    List<BudgetDetailsModel> getBudgetDetails();

    //update budget values
    @Query("update budget set budget_limit= :budgetAmount where category_id= :catId")
    void updateBudgetLimit(int budgetAmount,int catId);
}
