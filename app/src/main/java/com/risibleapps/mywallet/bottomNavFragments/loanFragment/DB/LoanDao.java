package com.risibleapps.mywallet.bottomNavFragments.loanFragment.DB;

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
    @Query("select * from loan_detail where phone_no= :phoneNo ")
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

    //query for getting the latest transaction date and amount
    @Query("select * from loan_detail where phone_no= :phoneNo order by trans_date desc")
    List<LoanDetailEntity> getLoanTransByDate(String phoneNo);

    //deleting an added contact
    @Query("DELETE from loan where phoneNo= :phoneNumber")
    void deleteAddedContact(String phoneNumber);

    //delete all loan transaction of an added contact
    @Query("DELETE from loan_detail where phone_no= :phoneNumber")
    void deleteAllLoanTransaction(String phoneNumber);

    //delete a particular loan transaction
    @Query("DELETE from loan_detail where loanId= :id AND phone_no= :phoneNo")
    void deleteLoanTransaction(int id,String phoneNo);


}
