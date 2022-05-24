package com.hammad.managerya.bottomNavFragments.addRecord.income.incomeDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "income_category")
public class IncomeCategoryEntity implements Serializable {

    //columns name
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "income_cat_id")
    private int incomeCatId;

    @ColumnInfo(name = "income_cat_name")
    private String incomeCatName;

    @ColumnInfo(name = "income_icon_name")
    private int incomeCatIconName;

    public IncomeCategoryEntity() {}

    public IncomeCategoryEntity(int incomeCatId, String incomeCatName, int incomeCatIconName) {
        this.incomeCatId = incomeCatId;
        this.incomeCatName = incomeCatName;
        this.incomeCatIconName = incomeCatIconName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIncomeCatId() {
        return incomeCatId;
    }

    public void setIncomeCatId(int incomeCatId) {
        this.incomeCatId = incomeCatId;
    }

    public String getIncomeCatName() {
        return incomeCatName;
    }

    public void setIncomeCatName(String incomeCatName) {
        this.incomeCatName = incomeCatName;
    }

    public int getIncomeCatIconName() {
        return incomeCatIconName;
    }

    public void setIncomeCatIconName(int incomeCatIconName) {
        this.incomeCatIconName = incomeCatIconName;
    }
}
