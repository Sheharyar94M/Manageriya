package com.risibleapps.mywallet.bottomNavFragments.loanFragment.DB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "loan")
public class LoanEntity implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String phoneNo;

    @ColumnInfo(name = "contact_name")
    private String contactName;

    @ColumnInfo(name = "contact_letters")
    private String contactLetter;

    @ColumnInfo(name = "contact_id")
    private long contactId;

    public LoanEntity(String phoneNo, String contactName, String contactLetter, long contactId) {
        this.phoneNo = phoneNo;
        this.contactName = contactName;
        this.contactLetter = contactLetter;
        this.contactId = contactId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactLetter() {
        return contactLetter;
    }

    public void setContactLetter(String contactLetter) {
        this.contactLetter = contactLetter;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }
}
