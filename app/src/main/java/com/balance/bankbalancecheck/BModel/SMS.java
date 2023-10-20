package com.balance.bankbalancecheck.BModel;

public class SMS {
    String body,bank;
    long date;

    public SMS(String body, String bank, long date) {
        this.body = body;
        this.bank = bank;
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
