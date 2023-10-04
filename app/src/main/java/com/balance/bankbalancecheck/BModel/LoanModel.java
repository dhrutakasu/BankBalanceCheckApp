package com.balance.bankbalancecheck.BModel;

public class LoanModel {
    String name;
    int icons;

    public LoanModel(String name, int icons) {
        this.name = name;
        this.icons = icons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcons() {
        return icons;
    }

    public void setIcons(int icons) {
        this.icons = icons;
    }

    @Override
    public String toString() {
        return "LoanModel{" +
                "name='" + name + '\'' +
                ", icons=" + icons +
                '}';
    }
}
