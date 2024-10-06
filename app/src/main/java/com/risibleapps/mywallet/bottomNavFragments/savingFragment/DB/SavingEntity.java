package com.risibleapps.mywallet.bottomNavFragments.savingFragment.DB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "saving")
public class SavingEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "saving_goal_amount")
    private int savingGoalAmount;

    @ColumnInfo(name = "saving_title")
    private String savingTitle;

    @ColumnInfo(name = "saving_tag")
    private String savingTag;

    @ColumnInfo(name = "saving_icon")
    private int savingIcon;

    @ColumnInfo(name = "saving_target_date")
    private String savingTargetDate;

    public SavingEntity() {}

    public SavingEntity(int savingGoalAmount, String savingTitle, String savingTag, int savingIcon, String savingTargetDate) {
        this.savingGoalAmount = savingGoalAmount;
        this.savingTitle = savingTitle;
        this.savingTag = savingTag;
        this.savingIcon = savingIcon;
        this.savingTargetDate = savingTargetDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSavingTag() {
        return savingTag;
    }

    public void setSavingTag(String savingTag) {
        this.savingTag = savingTag;
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
