package com.hammad.managerya.bottomNavFragments.homeFragment.homeDB;

public class HomeRecentTransModel {
    public int catId;
    public String catName;
    public int catIcon;
    public int transAmount;
    public String transDate;
    public String transDesc;
    public String transTag;
    public String transLocation;
    public String transImagePath;
    public String transType;

    public HomeRecentTransModel() {}

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getCatIcon() {
        return catIcon;
    }

    public void setCatIcon(int catIcon) {
        this.catIcon = catIcon;
    }

    public int getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(int transAmount) {
        this.transAmount = transAmount;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransDesc() {
        return transDesc;
    }

    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc;
    }

    public String getTransTag() {
        return transTag;
    }

    public void setTransTag(String transTag) {
        this.transTag = transTag;
    }

    public String getTransLocation() {
        return transLocation;
    }

    public void setTransLocation(String transLocation) {
        this.transLocation = transLocation;
    }

    public String getTransImagePath() {
        return transImagePath;
    }

    public void setTransImagePath(String transImagePath) {
        this.transImagePath = transImagePath;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }
}
