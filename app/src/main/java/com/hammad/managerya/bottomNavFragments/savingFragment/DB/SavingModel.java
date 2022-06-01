package com.hammad.managerya.bottomNavFragments.savingFragment.DB;

public class SavingModel {

    public int savingId;
    public int savingGoalAmount;
    public String savingTitle;
    public int savingIcon;
    public String savingTargetDate;

    public SavingModel() {}

    public SavingModel(int savingId, int savingGoalAmount, String savingTitle, int savingIcon, String savingTargetDate) {
        this.savingId = savingId;
        this.savingGoalAmount = savingGoalAmount;
        this.savingTitle = savingTitle;
        this.savingIcon = savingIcon;
        this.savingTargetDate = savingTargetDate;
    }

    public int getSavingId() {
        return savingId;
    }

    public void setSavingId(int savingId) {
        this.savingId = savingId;
    }

    public int getSavingGoalAmount() {
        return savingGoalAmount;
    }

    public void setSavingGoalAmount(int savingGoalAmount) {
        this.savingGoalAmount = savingGoalAmount;
    }

    public String getSavingTitle() {
        return savingTitle;
    }

    public void setSavingTitle(String savingTitle) {
        this.savingTitle = savingTitle;
    }

    public int getSavingIcon() {
        return savingIcon;
    }

    public void setSavingIcon(int savingIcon) {
        this.savingIcon = savingIcon;
    }

    public String getSavingTargetDate() {
        return savingTargetDate;
    }

    public void setSavingTargetDate(String savingTargetDate) {
        this.savingTargetDate = savingTargetDate;
    }
}
