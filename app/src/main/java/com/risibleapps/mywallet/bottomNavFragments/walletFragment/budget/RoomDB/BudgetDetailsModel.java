package com.risibleapps.mywallet.bottomNavFragments.walletFragment.budget.RoomDB;

public class BudgetDetailsModel {

    public int budgetCatId;
    public String budgetCatName;
    public int budgetIcon;
    public int budgetLimit;
    public String budgetDate;

    public BudgetDetailsModel() {}

    public int getBudgetCatId() {
        return budgetCatId;
    }

    public void setBudgetCatId(int budgetCatId) {
        this.budgetCatId = budgetCatId;
    }

    public String getBudgetCatName() {
        return budgetCatName;
    }

    public void setBudgetCatName(String budgetCatName) {
        this.budgetCatName = budgetCatName;
    }

    public int getBudgetIcon() {
        return budgetIcon;
    }

    public void setBudgetIcon(int budgetIcon) {
        this.budgetIcon = budgetIcon;
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
