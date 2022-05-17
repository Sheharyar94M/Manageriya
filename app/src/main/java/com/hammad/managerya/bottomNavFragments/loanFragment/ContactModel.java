package com.hammad.managerya.bottomNavFragments.loanFragment;

import java.util.Set;

public class ContactModel {

    private long contactId;
    private String contactName;
    private String phoneNo;
    private String contactLetters;



    public ContactModel(long contactId,  String contactName, String phoneNo, String contactLetters) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.phoneNo = phoneNo;
        this.contactLetters = contactLetters;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }


    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }


    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getContactLetters() {
        return contactLetters;
    }

    public void setContactLetters(String contactLetters) {
        this.contactLetters = contactLetters;
    }

}
