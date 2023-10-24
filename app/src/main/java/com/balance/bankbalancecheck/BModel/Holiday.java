package com.balance.bankbalancecheck.BModel;

public class Holiday {
    private String state;
    private String day;
    private String date;
    private String holiday;

    public Holiday(String state, String day, String date, String holiday) {
        this.state = state;
        this.day = day;
        this.date = date;
        this.holiday = holiday;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }
}
