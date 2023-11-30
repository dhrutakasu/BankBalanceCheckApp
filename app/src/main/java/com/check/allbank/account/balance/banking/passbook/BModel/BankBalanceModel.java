package com.check.allbank.account.balance.banking.passbook.BModel;

public class BankBalanceModel {
    String BankBalance,MiniStatement,MiniStatementMsg,CustomerCard;

    public BankBalanceModel(String bankBalance, String miniStatement, String miniStatementMsg, String customerCard) {
        BankBalance = bankBalance;
        MiniStatement = miniStatement;
        MiniStatementMsg = miniStatementMsg;
        CustomerCard = customerCard;
    }

    public BankBalanceModel() {

    }

    public String getBankBalance() {
        return BankBalance;
    }

    public void setBankBalance(String bankBalance) {
        BankBalance = bankBalance;
    }

    public String getMiniStatement() {
        return MiniStatement;
    }

    public void setMiniStatement(String miniStatement) {
        MiniStatement = miniStatement;
    }

    public String getMiniStatementMsg() {
        return MiniStatementMsg;
    }

    public void setMiniStatementMsg(String miniStatementMsg) {
        MiniStatementMsg = miniStatementMsg;
    }

    public String getCustomerCard() {
        return CustomerCard;
    }

    public void setCustomerCard(String customerCard) {
        CustomerCard = customerCard;
    }

    @Override
    public String toString() {
        return "BankBalanceModel{" +
                "BankBalance='" + BankBalance + '\'' +
                ", MiniStatement='" + MiniStatement + '\'' +
                ", MiniStatementMsg='" + MiniStatementMsg + '\'' +
                ", CustomerCard='" + CustomerCard + '\'' +
                '}';
    }
}
