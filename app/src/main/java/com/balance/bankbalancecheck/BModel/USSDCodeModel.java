package com.balance.bankbalancecheck.BModel;

public class USSDCodeModel {
    String BankName,CallNumber,ShortyName,IFSC;

    public USSDCodeModel(String bankName, String callNumber, String shortyName, String IFSC) {
        BankName = bankName;
        CallNumber = callNumber;
        ShortyName = shortyName;
        this.IFSC = IFSC;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getCallNumber() {
        return CallNumber;
    }

    public void setCallNumber(String callNumber) {
        CallNumber = callNumber;
    }

    public String getShortyName() {
        return ShortyName;
    }

    public void setShortyName(String shortyName) {
        ShortyName = shortyName;
    }

    public String getIFSC() {
        return IFSC;
    }

    public void setIFSC(String IFSC) {
        this.IFSC = IFSC;
    }

    @Override
    public String toString() {
        return "USSDCodeModel{" +
                "BankName='" + BankName + '\'' +
                ", CallNumber='" + CallNumber + '\'' +
                ", ShortyName='" + ShortyName + '\'' +
                ", IFSC='" + IFSC + '\'' +
                '}';
    }
}
