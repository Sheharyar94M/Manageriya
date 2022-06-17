package com.risibleapps.mywallet.bottomNavFragments.walletFragment.budget.RoomDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.io.Serializable;

@Entity(tableName = "budget",primaryKeys = {"category_id","budget_date"})
public class BudgetEntity implements Serializable {

    @ColumnInfo(name = "category_id")
    private int categoryId;

    @ColumnInfo(name = "category_name")
    private String categoryName;

    @ColumnInfo(name = "category_icon")
    private int categoryIcon;

    @ColumnInfo(name = "budget_limit")
    private int budgetLimit;

    @NonNull
    @ColumnInfo(name = "budget_date")
    private String budgetDate;

    public BudgetEntity(int categoryId, String categoryName, int categoryIcon, int budgetLimit,String budgetDate) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
        this.budgetLimit = budgetLimit;
        this.budgetDate = budgetDate;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(int categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public int getBudgetLimit() {
        return budgetLimit;
    }

    public void setBudgetLimit(int budgetLimit) {
        this.budgetLimit = budgetLimit;
    }

    public String getBudgetDate() {
        return budgetDate;
    }

    public void setBudgetDate(String budgetDate) {
        this.budgetDate = budgetDate;
    }
}
