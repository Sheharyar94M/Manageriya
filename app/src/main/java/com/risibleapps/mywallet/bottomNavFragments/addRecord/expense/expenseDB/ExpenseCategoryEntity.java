package com.risibleapps.mywallet.bottomNavFragments.addRecord.expense.expenseDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "expense_category")
public class ExpenseCategoryEntity implements Serializable {

    //columns
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "expense_cat_id")
    private int expenseCatId;

    @ColumnInfo(name = "expense_cat_name")
    private String expenseCatName;

    @ColumnInfo(name = "expense_icon_name")
    private int expenseCatIconName;

    public ExpenseCategoryEntity() {}

    public ExpenseCategoryEntity(int expenseCatId, String expenseCatName, int expenseCatIconName) {
        this.expenseCatId = expenseCatId;
        this.expenseCatName = expenseCatName;
        this.expenseCatIconName = expenseCatIconName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExpenseCatId() {
        return expenseCatId;
    }

    public void setExpenseCatId(int expenseCatId) {
        this.expenseCatId = expenseCatId;
    }

    public String getExpenseCatName() {
        return expenseCatName;
    }

    public void setExpenseCatName(String expenseCatName) {
        this.expenseCatName = expenseCatName;
    }

    public int getExpenseCatIconName() {
        return expenseCatIconName;
    }

    public void setExpenseCatIconName(int expenseCatIconName) {
        this.expenseCatIconName = expenseCatIconName;
    }
}
