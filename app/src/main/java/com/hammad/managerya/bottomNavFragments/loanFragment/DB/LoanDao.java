package com.hammad.managerya.bottomNavFragments.loanFragment.DB;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface LoanDao {

    @Insert(onConflict = REPLACE)
    void addContact(LoanEntity loanEntity);
}
