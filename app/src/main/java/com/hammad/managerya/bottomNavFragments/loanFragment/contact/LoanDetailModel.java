package com.hammad.managerya.bottomNavFragments.loanFragment.contact;

public class LoanDetailModel {
    private String loanContactId;
    private String loanAmount;
    private String transactionType;
    private String loanTransDate;
    private String optionalLoanDetail;

    public LoanDetailModel() {}

    public LoanDetailModel(String loanContactId, String loanAmount, String transactionType, String loanTransDate, String optionalLoanDetail) {
        this.loanContactId = loanContactId;
        this.loanAmount = loanAmount;
        this.transactionType = transactionType;
        this.loanTransDate = loanTransDate;
        this.optionalLoanDetail = optionalLoanDetail;
    }

    public String getLoanContactId() {
        return loanContactId;
    }

    public void setLoanContactId(String loanContactId) {
        this.loanContactId = loanContactId;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getLoanTransDate() {
        return loanTransDate;
    }

    public void setLoanTransDate(String loanTransDate) {
        this.loanTransDate = loanTransDate;
    }

    public String getOptionalLoanDetail() {
        return optionalLoanDetail;
    }

    public void setOptionalLoanDetail(String optionalLoanDetail) {
        this.optionalLoanDetail = optionalLoanDetail;
    }
}
