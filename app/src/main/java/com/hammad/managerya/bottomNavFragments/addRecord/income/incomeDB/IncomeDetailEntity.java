package com.hammad.managerya.bottomNavFragments.addRecord.income.incomeDB;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "income_detail")
public class IncomeDetailEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "income_det_cat_id")
    private int incomeDetailCatId;

    @ColumnInfo(name = "income_amount")
    private int incomeAmount;

    @ColumnInfo(name = "income_date")
    private String incomeDate;

    @ColumnInfo(name = "income_desc")
    private String incomeDescription;

    @ColumnInfo(name = "income_tag")
    private String incomeTag;

    @ColumnInfo(name = "income_location")
    private String incomeLocation;

    @ColumnInfo(name = "income_img_path")
    private String incomeImgPath;

    public IncomeDetailEntity() {}

    public IncomeDetailEntity(int incomeDetailCatId, int incomeAmount, String incomeDate) {
        this.incomeDetailCatId = incomeDetailCatId;
        this.incomeAmount = incomeAmount;
        this.incomeDate = incomeDate;
    }

    public IncomeDetailEntity(int incomeDetailCatId, int incomeAmount, String incomeDate, String incomeDescription, String incomeTag, String incomeLocation, String incomeImgPath) {
        this.incomeDetailCatId = incomeDetailCatId;
        this.incomeAmount = incomeAmount;
        this.incomeDate = incomeDate;
        this.incomeDescription = incomeDescription;
        this.incomeTag = incomeTag;
        this.incomeLocation = incomeLocation;
        this.incomeImgPath = incomeImgPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIncomeDetailCatId() {
        return incomeDetailCatId;
    }

    public void setIncomeDetailCatId(int incomeDetailCatId) {
        this.incomeDetailCatId = incomeDetailCatId;
    }

    public int getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(int incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public String getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(String incomeDate) {
        this.incomeDate = incomeDate;
    }

    public String getIncomeDescription() {
        return incomeDescription;
    }

    public void setIncomeDescription(String incomeDescription) {
        this.incomeDescription = incomeDescription;
    }

    public String getIncomeTag() {
        return incomeTag;
    }

    public void setIncomeTag(String incomeTag) {
        this.incomeTag = incomeTag;
    }

    public String getIncomeLocation() {
        return incomeLocation;
    }

    public void setIncomeLocation(String incomeLocation) {
        this.incomeLocation = incomeLocation;
    }

    public String getIncomeImgPath() {
        return incomeImgPath;
    }

    public void setIncomeImgPath(String incomeImgPath) {
        this.incomeImgPath = incomeImgPath;
    }
}
