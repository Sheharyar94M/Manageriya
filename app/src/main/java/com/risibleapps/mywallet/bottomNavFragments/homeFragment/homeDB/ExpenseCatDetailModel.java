package com.risibleapps.mywallet.bottomNavFragments.homeFragment.homeDB;

public class ExpenseCatDetailModel {

    public int expCatId;
    public String expCatName;
    public float expCatAmount;

    public ExpenseCatDetailModel() {}

    public int getExpCatId() {
        return expCatId;
    }

    public void setExpCatId(int expCatId) {
        this.expCatId = expCatId;
    }

    public String getExpCatName() {
        return expCatName;
    }

    public void setExpCatName(String expCatName) {
        this.expCatName = expCatName;
    }

    public float getExpCatAmount() {
        return expCatAmount;
    }

    public void setExpCatAmount(float expCatAmount) {
        this.expCatAmount = expCatAmount;
    }
}
