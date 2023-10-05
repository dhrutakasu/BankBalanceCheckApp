package com.balance.bankbalancecheck.BModel;

public class SMSModel {

    String address,body,Title;
    long date;

    public SMSModel(String address, String body, String title, long date) {
        this.address = address;
        this.body = body;
        Title = title;
        this.date = date;
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

    @Override
    public String toString() {
        return "SMSModel{" +
                "address='" + address + '\'' +
                ", body='" + body + '\'' +
                ", Title='" + Title + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
