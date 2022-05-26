package com.hammad.managerya.bottomNavFragments.walletFragment.budget.RoomDB;

public class BudgetDetailsModel {

    public String budgetCatId;
    public String budgetCatName;
    public String budgetLimit;

    public BudgetDetailsModel() {}

    public String getBudgetCatId() {
        return budgetCatId;
    }

    public void setBudgetCatId(String budgetCatId) {
        this.budgetCatId = budgetCatId;
    }

    public String getBudgetCatName() {
        return budgetCatName;
    }

    public void setBudgetCatName(String budgetCatName) {
        this.budgetCatName = budgetCatName;
    }

    public String getBudgetLimit() {
        return budgetLimit;
    }

    public void setBudgetLimit(String budgetLimit) {
        this.budgetLimit = budgetLimit;
    }
}
