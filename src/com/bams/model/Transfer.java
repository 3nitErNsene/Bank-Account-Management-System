package com.bams.model;

import java.util.Date;

public class Transfer {
    private String transferId;
    private long fromAc;
    private long toAc;
    private int amount;
    private Date dateTime;

    public Transfer(String transferId, long fromAc, long toAc, int amount, Date dateTime) {
        this.transferId = transferId;
        this.fromAc = fromAc;
        this.toAc = toAc;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    public String getTransferId() {
        return transferId;
    }

    public long getFromAc() {
        return fromAc;
    }

    public long getToAc() {
        return toAc;
    }

    public int getAmount() {
        return amount;
    }

    public Date getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "Transfer{" + "transferId=" + transferId + ", fromAc=" + fromAc + ", toAc=" + toAc + ", amount=" + amount + ", dateTime=" + dateTime + '}';
    }
    
}
