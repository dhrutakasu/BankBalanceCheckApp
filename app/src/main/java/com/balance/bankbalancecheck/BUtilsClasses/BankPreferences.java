package com.balance.bankbalancecheck.BUtilsClasses;

import android.content.Context;
import android.content.SharedPreferences;

public class BankPreferences {
    public static final String BANK_NAME = "BANK_NAME";
    public static final String STATE_NAME = "STATE_NAME";
    public static final String DISTRICT_NAME = "DISTRICT_NAME";
    public static final String BRANCH_NAME = "BRANCH_NAME";
    private SharedPreferences sharedPreferences;
    static final String BankPreferences = "BankPreferences";

    public BankPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(BankPreferences, Context.MODE_PRIVATE);
    }

    public int getPrefInt(String str, int i) {
        return sharedPreferences.getInt(str, i);
    }

    public void putPrefInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public String getPrefString(String str, String s) {
        return sharedPreferences.getString(str, s);
    }

    public void putPrefString(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    public void putPrefBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public boolean getPrefBoolean(String str, boolean i) {
        return sharedPreferences.getBoolean(str, i);
    }

    public void putPrefLong(String key, long value) {
        sharedPreferences.edit().putLong(key, value).commit();
    }

    public long getPrefLong(String key, long i) {
        return sharedPreferences.getLong(key, i);
    }
}
