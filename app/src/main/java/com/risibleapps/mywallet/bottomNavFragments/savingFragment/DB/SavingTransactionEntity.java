package com.risibleapps.mywallet.bottomNavFragments.savingFragment.DB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "saving_transaction")
public class SavingTransactionEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "saving_goal_id")
    private int savingGoalId;

    @ColumnInfo(name = "saving_title")
    private String savingTitle;

    @ColumnInfo(name = "saving_trans_amount")
    private int savingTransAmount;

    @ColumnInfo(name = "saving_icon")
    private int savingIcon;

    @ColumnInfo(name = "saving_trans_date")
    private String savingTransDate;

    public SavingTransactionEntity(int savingGoalId, String savingTitle, int savingTransAmount, int savingIcon, String savingTransDate) {
        this.savingGoalId = savingGoalId;
        this.savingTitle = savingTitle;
        this.savingTransAmount = savingTransAmount;
        this.savingIcon = savingIcon;
        this.savingTransDate = savingTransDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSavingGoalId() {
        return savingGoalId;
    }

    public void setSavingGoalId(int savingGoalId) {
        this.savingGoalId = savingGoalId;
    }

    public String getSavingTitle() {
        return savingTitle;
    }

    public void setSavingTitle(String savingTitle) {
        this.savingTitle = savingTitle;
    }

    public int getSavingTransAmount() {
        return savingTransAmount;
    }

    public void setSavingTransAmount(int savingTransAmount) {
        this.savingTransAmount = savingTransAmount;
    }

    public int getSavingIcon() {
        return savingIcon;
    }

    public void setSavingIcon(int savingIcon) {
        this.savingIcon = savingIcon;
    }

    public String getSavingTransDate() {
        return savingTransDate;
    }

    public void setSavingTransDate(String savingTransDate) {
        this.savingTransDate = savingTransDate;
    }
}
