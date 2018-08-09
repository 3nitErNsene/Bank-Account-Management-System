package com.bams.model;

import java.util.Date;

public class Transaction {
    private int tranId;
    private long account;
    private String type;
    private int amount;
    private Date dateTime;
    private String remark;

    public Transaction(int tranId, long account, String type, int amount, Date dateTime, String remark) {
        this.tranId = tranId;
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.dateTime = dateTime;
        this.remark = remark;
    }

    public int getTranId() {
        return tranId;
    }

    public long getAccount() {
        return account;
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public String getRemark() {
        return remark;
    }

    @Override
    public String toString() {
        return "Transaction{" + "tranId=" + tranId + ", account=" + account + ", type=" + type + ", amount=" + amount + ", dateTime=" + dateTime + ", remark=" + remark + '}';
    }
    
}
