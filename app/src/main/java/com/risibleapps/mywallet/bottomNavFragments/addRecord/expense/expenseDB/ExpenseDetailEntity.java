package com.risibleapps.mywallet.bottomNavFragments.addRecord.expense.expenseDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "expense_detail")
public class ExpenseDetailEntity implements Serializable {

    //columns
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "expense_det_cat_id")
    private int expenseDetailCatId;

    @ColumnInfo(name = "expense_amount")
    private int expenseAmount;

    @ColumnInfo(name = "expense_date")
    private String expenseDate;

    @ColumnInfo(name = "expense_desc")
    private String expenseDescription;

    @ColumnInfo(name = "expense_tag")
    private String expenseTag;

    @ColumnInfo(name = "expense_location")
    private String expenseLocation;

    @ColumnInfo(name = "expense_img_path")
    private String expenseImgPath;

    @Ignore
    public ExpenseDetailEntity() {}

    @Ignore
    public ExpenseDetailEntity(int expenseDetailCatId, int expenseAmount, String expenseDate) {
        this.expenseDetailCatId = expenseDetailCatId;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;
    }

    public ExpenseDetailEntity(int expenseDetailCatId, int expenseAmount, String expenseDate, String expenseDescription, String expenseTag, String expenseLocation, String expenseImgPath) {
        this.expenseDetailCatId = expenseDetailCatId;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;
        this.expenseDescription = expenseDescription;
        this.expenseTag = expenseTag;
        this.expenseLocation = expenseLocation;
        this.expenseImgPath = expenseImgPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExpenseDetailCatId() {
        return expenseDetailCatId;
    }

    public void setExpenseDetailCatId(int expenseDetailCatId) {
        this.expenseDetailCatId = expenseDetailCatId;
    }

    public int getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(int expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }

    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription = expenseDescription;
    }

    public String getExpenseTag() {
        return expenseTag;
    }

    public void setExpenseTag(String expenseTag) {
        this.expenseTag = expenseTag;
    }

    public String getExpenseLocation() {
        return expenseLocation;
    }

    public void setExpenseLocation(String expenseLocation) {
        this.expenseLocation = expenseLocation;
    }

    public String getExpenseImgPath() {
        return expenseImgPath;
    }

    public void setExpenseImgPath(String expenseImgPath) {
        this.expenseImgPath = expenseImgPath;
    }
}
