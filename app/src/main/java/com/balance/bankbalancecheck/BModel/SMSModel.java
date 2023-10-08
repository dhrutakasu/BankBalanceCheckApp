package com.balance.bankbalancecheck.BModel;

public class SMSModel {

    String id,address,body,Title,Balance,Amount,Types;
    long date;

    public SMSModel(String address, String body, String title, String balance, String amount, long date) {
        this.address = address;
        this.body = body;
        Title = title;
        Balance = balance;
        Amount = amount;
        this.date = date;
    }

    public SMSModel(String id, String address, String body, String title, long date) {
        this.id = id;
        this.address = address;
        this.body = body;
        Title = title;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getTypes() {
        return Types;
    }

    public void setTypes(String types) {
        Types = types;
    }

    @Override
    public String toString() {
        return "SMSModel{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", body='" + body + '\'' +
                ", Title='" + Title + '\'' +
                ", Balance='" + Balance + '\'' +
                ", Amount='" + Amount + '\'' +
                ", Types='" + Types + '\'' +
                ", date=" + date +
                '}';
    }
}
