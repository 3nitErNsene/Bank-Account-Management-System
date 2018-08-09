package com.bams.model;

import java.util.Date;

public class Account {

    private long accountNo;
    private int balance;
    private String status;
    private Date activeDate;
    private String blockBy;

    public Account(long accountNo, int balance, String status, Date activeDate, String blockBy) {
        this.accountNo = accountNo;
        this.balance = balance;
        this.status = status;
        this.activeDate = activeDate;
        this.blockBy = blockBy;
    }

    public long getAccountNo() {
        return accountNo;
    }

    public int getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }

    public Date getActiveDate() {
        return activeDate;
    }

    public String getBlockBy() {
        return blockBy;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setBlockBy(String blockBy) {
        this.blockBy = blockBy;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Account{" + "accountNo=" + accountNo + ", balance=" + balance + ", status=" + status + ", activeDate=" + activeDate + ", blockBy=" + blockBy + '}';
    }

}
