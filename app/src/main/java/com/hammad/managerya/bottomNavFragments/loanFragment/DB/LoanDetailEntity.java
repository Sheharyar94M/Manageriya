package com.hammad.managerya.bottomNavFragments.loanFragment.DB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "loan_detail")
public class LoanDetailEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int loanId;

    @ColumnInfo(name = "phone_no")
    private String phoneNo;

    @ColumnInfo(name = "amount_lend")
    private int amountLend;

    @ColumnInfo(name = "amount_borrow")
    private int amountBorrow;

    @ColumnInfo(name = "trans_date")
    private String transDate;

    @ColumnInfo(name = "optional_desc")
    private String optionalDesc;

    public LoanDetailEntity() {}

    public LoanDetailEntity(String phoneNo, int amountLend, int amountBorrow, String transDate, String optionalDesc) {
        this.phoneNo = phoneNo;
        this.amountLend = amountLend;
        this.amountBorrow = amountBorrow;
        this.transDate = transDate;
        this.optionalDesc = optionalDesc;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getAmountLend() {
        return amountLend;
    }

    public void setAmountLend(int amountLend) {
        this.amountLend = amountLend;
    }

    public int getAmountBorrow() {
        return amountBorrow;
    }

    public void setAmountBorrow(int amountBorrow) {
        this.amountBorrow = amountBorrow;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getOptionalDesc() {
        return optionalDesc;
    }

    public void setOptionalDesc(String optionalDesc) {
        this.optionalDesc = optionalDesc;
    }
}
