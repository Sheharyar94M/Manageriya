package com.hammad.managerya.bottomNavFragments.loanFragment.DB;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LoanDao {

    //add contacts into database
    @Insert(onConflict = REPLACE)
    void addContact(LoanEntity loanEntity);

    //add loan details
    @Insert(onConflict = REPLACE)
    void addLoanTransaction(LoanDetailEntity loanDetailEntity);

    //list of added contact list
    @Query("select * from loan")
    List<LoanEntity> getAddedContactList();

    //loan transaction list searched by phone no
    @Query("select * from loan_detail where phone_no= :phoneNo")
    List<LoanDetailEntity> getLoanTransList(String phoneNo);

    //sum of total amount lend
    @Query("select sum(amount_lend) from loan_detail")
    int getTotalAmountLend();

    //sum of total amount borrowed
    @Query("select sum(amount_borrow) from loan_detail")
    int getTotalAmountBorrowed();

    //sum of total amount lend search by phone number (for individual user record)
    @Query("select sum(amount_lend) from loan_detail where phone_no= :phoneNo")
    int getLendedAmountSum(String phoneNo);

    //sum of total amount borrowed search by phone number (for individual user record)
    @Query("select sum(amount_borrow) from loan_detail where phone_no= :phoneNo")
    int getBorrowedAmountSum(String phoneNo);


}
