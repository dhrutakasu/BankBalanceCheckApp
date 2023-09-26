package com.balance.bankbalancecheck.BHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.balance.bankbalancecheck.BModel.BankBalanceModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BankBalanceHelper extends SQLiteOpenHelper {
    private Context context;
    private static String DATABASE_PATH;
    private SQLiteDatabase sqLiteDatabase;
    private static String DATABASE_NAME = "BankBalanceDetails.db";

    private static String BANK_TABLE = "Bank_Infomation";
    private static String BANK_ID = "Bank_Id";
    private static String BANK_NAME = "Bank_Name";
    private static String BANK_INQUIRY = "Bank_Inquiry";
    private static String BANK_CARE = "Bank_Care";
    private static String BANK_FAVOURITE = "Bank_Favourite";
    private static String BANK_NET_BANKING_URI = "NetBank_Url";
    private static String BANK_MINI_STATEMENT = "Mini_Statement";
    private static String BANK_SHORT = "Bank_Short";

    private static String SMS_TABLE = "SmsInformation";
    private static String SMS_ID = "Sms_Id";
    private static String SMS_TITLE = "Sms_Title";
    private static String SMS_BANK_NAME = "Bank_Name";
    private static String SMS_MESSAGE = "Message";
    private static String SMS_INFO = "Information";
    private static String SMS_PH_No = "Phone_No";

    public BankBalanceHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
        this.context = context;

        if (!isDatabaseExists()) {
            try {
                copyDatabases();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private boolean isDatabaseExists() {
        return context.getDatabasePath(DATABASE_NAME).exists();
    }

    public void copyDatabases() throws IOException {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            dbFile.getParentFile().mkdirs();
            InputStream inputStream = context.getAssets().open("Database/" + DATABASE_NAME);
            OutputStream outputStream = new FileOutputStream(dbFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }
    }

    //todo balanceList
    @SuppressLint("Range")
    public BankBalanceModel getSearchBankBalance(String bankName) {
        System.out.println("---- - -- bank : " + bankName);
        BankBalanceModel bankBalanceModels = new BankBalanceModel();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + BANK_TABLE + " WHERE " + BANK_NAME + "=? ", new String[]{bankName});
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                String miniStatement = null;
                if (cursor.getString(cursor.getColumnIndex(BANK_MINI_STATEMENT)).contains("#")) {
                    miniStatement = cursor.getString(cursor.getColumnIndex(BANK_MINI_STATEMENT)).substring(cursor.getString(cursor.getColumnIndex(BANK_MINI_STATEMENT)).lastIndexOf("#") + 1);
                } else {
                    miniStatement = cursor.getString(cursor.getColumnIndex(BANK_MINI_STATEMENT));
                }
                bankBalanceModels = new BankBalanceModel(cursor.getString(cursor.getColumnIndex(BANK_INQUIRY))
                        , miniStatement
                        , cursor.getString(cursor.getColumnIndex(BANK_MINI_STATEMENT))
                        , cursor.getString(cursor.getColumnIndex(BANK_CARE)));
            } while (cursor.moveToNext());
        }
        return bankBalanceModels;
    }

    //todo balanceList
    @SuppressLint("Range")
    public String getSearchNetBanking(String bankName) {
        System.out.println("---- - -- bank : " + bankName);
        String miniStatement = "";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + BANK_TABLE + " WHERE " + BANK_NAME + "=? ", new String[]{bankName});
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                miniStatement = cursor.getString(cursor.getColumnIndex(BANK_NET_BANKING_URI));

            } while (cursor.moveToNext());
        }
        return miniStatement;
    }

}
