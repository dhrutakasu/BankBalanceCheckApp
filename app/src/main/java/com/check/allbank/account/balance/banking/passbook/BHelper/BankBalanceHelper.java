package com.check.allbank.account.balance.banking.passbook.BHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.check.allbank.account.balance.banking.passbook.BModel.BankBalanceModel;
import com.check.allbank.account.balance.banking.passbook.BModel.SMSModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class BankBalanceHelper extends SQLiteOpenHelper {
    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private static String DATABASE_NAME = "BankBalanceDetails.db";

    private static String BANK_TABLE = "Bank_Infomation";
    private static String BANK_NAME = "Bank_Name";
    private static String BANK_INQUIRY = "Bank_Inquiry";
    private static String BANK_CARE = "Bank_Care";
    private static String BANK_NET_BANKING_URI = "NetBank_Url";
    private static String BANK_MINI_STATEMENT = "Mini_Statement";

    private static String SMS_TABLE = "SmsData";
    private static String SMS_ID = "SmsId";
    private static String SMS_TYPES = "SmsTitle";
    private static String SMS_BANK_NAME = "SmsBankName";
    private static String SMS_MESSAGE = "SmsMessage";
    private static String SMS_DATE = "Smsdate";
    private static String SMS_BALANCE = "Smsbalance";
    private static String SMS_AMOUNT = "Smsamount";

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
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + SMS_TABLE + " (" +
                SMS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SMS_TYPES + " TEXT," +
                SMS_BANK_NAME + " TEXT," +
                SMS_MESSAGE + " TEXT," +
                SMS_DATE + " TEXT," +
                SMS_BALANCE + " TEXT," +
                SMS_AMOUNT + " TEXT)";
        db.execSQL(createTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + SMS_TABLE;
        db.execSQL(dropTableQuery);
        onCreate(db);
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
        BankBalanceModel bankBalanceModels = new BankBalanceModel();
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + BANK_TABLE + " WHERE " + BANK_NAME +" =? ";
//                + " LIKE ? ";
        System.out.println("---- - -- bank qq: " + query + new String[]{"%" + bankName + "%"});
        Cursor cursor = database.rawQuery(query, new String[]{bankName});
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

    //todo SMS insert
    public void InsertSMS(SMSModel smsModel) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SMS_TYPES, smsModel.getTypes());
        values.put(SMS_BANK_NAME, smsModel.getBankName());
        values.put(SMS_MESSAGE, smsModel.getBodyMsg());
        values.put(SMS_DATE, String.valueOf(smsModel.getDate()));
        values.put(SMS_BALANCE, smsModel.getBalance());
        values.put(SMS_AMOUNT, smsModel.getAmount());
        db.insert(SMS_TABLE, null, values);
    }

    //todo SMS delete
    public void DeleteSMS() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SMS_TABLE, null, null);
    }

    //todo SMS body delete
    public void DeleteSMSBody(String bankName) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SMS_TABLE, SMS_BANK_NAME, new String[]{bankName});
    }

    //todo get record count SMS
    public int SMSCount() {
        String countQuery = "SELECT  * FROM " + SMS_TABLE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public boolean isTableExists(String tableName, boolean openDb) {
        if (openDb) {
            if (sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
                sqLiteDatabase = getReadableDatabase();
            }

            if (!sqLiteDatabase.isReadOnly()) {
                sqLiteDatabase.close();
                sqLiteDatabase = getReadableDatabase();
            }
        }

        String query = "select DISTINCT tbl_name  from sqlite_master where tbl_name  = '" + tableName + "'";
        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    return true;
                }
            }
            return false;
        }
    }

    @SuppressLint("Range")
    public ArrayList<SMSModel> getAllSMS() {
        ArrayList<SMSModel> smsModelArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT *FROM " + SMS_TABLE;
        Cursor cursor = db.rawQuery(table_name, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    SMSModel smsModel = new SMSModel();
                    smsModel.setId(cursor.getString(cursor.getColumnIndex(SMS_ID)));
                    smsModel.setTypes(cursor.getString(cursor.getColumnIndex(SMS_TYPES)));
                    smsModel.setBankName(cursor.getString(cursor.getColumnIndex(SMS_BANK_NAME)));
                    smsModel.setBodyMsg(cursor.getString(cursor.getColumnIndex(SMS_MESSAGE)));
                    smsModel.setDate(cursor.getLong(cursor.getColumnIndex(SMS_DATE)));
                    smsModel.setBalance(cursor.getString(cursor.getColumnIndex(SMS_BALANCE)));
                    smsModel.setAmount(cursor.getString(cursor.getColumnIndex(SMS_AMOUNT)));
                    smsModelArrayList.add(smsModel);
                } while (cursor.moveToNext());
            }
        }
        return smsModelArrayList;
    }

    @SuppressLint("Range")
    public ArrayList<SMSModel> getAllSMSGroup() {
        ArrayList<SMSModel> smsModelArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT *FROM " + SMS_TABLE + " GROUP BY " + SMS_BANK_NAME;
        Cursor cursor = db.rawQuery(table_name, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    SMSModel smsModel = new SMSModel();
                    smsModel.setId(cursor.getString(cursor.getColumnIndex(SMS_ID)));
                    smsModel.setTypes(cursor.getString(cursor.getColumnIndex(SMS_TYPES)));
                    smsModel.setBankName(cursor.getString(cursor.getColumnIndex(SMS_BANK_NAME)));
                    smsModel.setBodyMsg(cursor.getString(cursor.getColumnIndex(SMS_MESSAGE)));
                    smsModel.setDate(cursor.getLong(cursor.getColumnIndex(SMS_DATE)));
                    smsModel.setBalance(cursor.getString(cursor.getColumnIndex(SMS_BALANCE)));
                    smsModel.setAmount(cursor.getString(cursor.getColumnIndex(SMS_AMOUNT)));
                    smsModelArrayList.add(smsModel);
                } while (cursor.moveToNext());
            }
        }
        return smsModelArrayList;
    }

    @SuppressLint("Range")
    public SMSModel getAllSMSBank(String bankName) {
        SMSModel smsModelArrayList = new SMSModel();
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT *FROM " + SMS_TABLE + " where " + SMS_BANK_NAME + "=? ";
        Cursor cursor = db.rawQuery(table_name, new String[]{bankName});
        if (cursor.moveToFirst()) {
            SMSModel smsModel = new SMSModel();
            smsModel.setId(cursor.getString(cursor.getColumnIndex(SMS_ID)));
            smsModel.setTypes(cursor.getString(cursor.getColumnIndex(SMS_TYPES)));
            smsModel.setBankName(cursor.getString(cursor.getColumnIndex(SMS_BANK_NAME)));
            smsModel.setBodyMsg(cursor.getString(cursor.getColumnIndex(SMS_MESSAGE)));
            smsModel.setDate(cursor.getLong(cursor.getColumnIndex(SMS_DATE)));
            smsModel.setBalance(cursor.getString(cursor.getColumnIndex(SMS_BALANCE)));
            smsModel.setAmount(cursor.getString(cursor.getColumnIndex(SMS_AMOUNT)));
            smsModelArrayList = smsModel;
        }
        return smsModelArrayList;
    }

    @SuppressLint("Range")
    public ArrayList<SMSModel> getAllSMSRupee(String bankName, String type, String Sdate, String Edate) {
        ArrayList<SMSModel> smsModelArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String table_name;
        if (type.equalsIgnoreCase("all")) {
            table_name = "SELECT *FROM " + SMS_TABLE + " where " + SMS_BANK_NAME + " = '" + bankName
                    + "' AND CAST(REPLACE(" + SMS_AMOUNT + ", ',', '') AS DECIMAL(10, 2)) >= '" +
                    Sdate + "'AND CAST(REPLACE(" + SMS_AMOUNT + ", ',', '') AS DECIMAL(10, 2)) <= '" + Edate + "'";
        } else {
            table_name = "SELECT *FROM " + SMS_TABLE + " where " + SMS_BANK_NAME + " = '" + bankName
                    + "' AND " + SMS_TYPES + " = '" + type
                    + "' AND CAST(REPLACE(" + SMS_AMOUNT + ", ',', '') AS DECIMAL(10, 2)) >= '" +
                    Sdate + "'AND CAST(REPLACE(" + SMS_AMOUNT + ", ',', '') AS DECIMAL(10, 2)) <= '" + Edate + "'";
        }
        Cursor cursor = db.rawQuery(table_name, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    SMSModel smsModel = new SMSModel();
                    smsModel.setId(cursor.getString(cursor.getColumnIndex(SMS_ID)));
                    smsModel.setTypes(cursor.getString(cursor.getColumnIndex(SMS_TYPES)));
                    smsModel.setBankName(cursor.getString(cursor.getColumnIndex(SMS_BANK_NAME)));
                    smsModel.setBodyMsg(cursor.getString(cursor.getColumnIndex(SMS_MESSAGE)));
                    smsModel.setDate(cursor.getLong(cursor.getColumnIndex(SMS_DATE)));
                    smsModel.setBalance(cursor.getString(cursor.getColumnIndex(SMS_BALANCE)));
                    smsModel.setAmount(cursor.getString(cursor.getColumnIndex(SMS_AMOUNT)));
                    smsModelArrayList.add(smsModel);
                } while (cursor.moveToNext());
            }
        }
        return smsModelArrayList;
    }

    @SuppressLint("Range")
    public ArrayList<SMSModel> getAllSMSDateType(String bankName, String type, long Sdate, long Edate) {

        ArrayList<SMSModel> smsModelArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String table_name;
        if (type.equalsIgnoreCase("all")) {
            table_name = "SELECT * FROM " + SMS_TABLE + " where " + SMS_BANK_NAME + " = '" + bankName
                    + "' AND " + SMS_DATE + " BETWEEN '" + Sdate + "' AND '" + Edate + "'";
        } else {
            table_name = "SELECT * FROM " + SMS_TABLE + " where " + SMS_BANK_NAME + " = '" + bankName
                    + "' AND " + SMS_TYPES + " = '" + type + "' AND " + SMS_DATE + " BETWEEN '" + Sdate + "' AND '" + Edate + "'";
        }
        Cursor cursor = db.rawQuery(table_name, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    SMSModel smsModel = new SMSModel();
                    smsModel.setId(cursor.getString(cursor.getColumnIndex(SMS_ID)));
                    smsModel.setTypes(cursor.getString(cursor.getColumnIndex(SMS_TYPES)));
                    smsModel.setBankName(cursor.getString(cursor.getColumnIndex(SMS_BANK_NAME)));
                    smsModel.setBodyMsg(cursor.getString(cursor.getColumnIndex(SMS_MESSAGE)));
                    smsModel.setDate(cursor.getLong(cursor.getColumnIndex(SMS_DATE)));
                    smsModel.setBalance(cursor.getString(cursor.getColumnIndex(SMS_BALANCE)));
                    smsModel.setAmount(cursor.getString(cursor.getColumnIndex(SMS_AMOUNT)));
                    smsModelArrayList.add(smsModel);
                } while (cursor.moveToNext());
            }
        }
        return smsModelArrayList;
    }

    @SuppressLint("Range")
    public ArrayList<SMSModel> getAllSMSAAllData(String bankName, String type, String Sdate, String Edate, String Samount, String Eamount) {
        ArrayList<SMSModel> smsModelArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String table_name;
        if (type.equalsIgnoreCase("all")) {
            table_name = "SELECT *FROM " + SMS_TABLE + " where " + SMS_BANK_NAME + " ='" + bankName
                    + "' AND " + SMS_DATE + " BETWEEN '" + Sdate + "' AND '" + Edate +
                    "' AND CAST(REPLACE(" + SMS_AMOUNT + ", ',', '') AS DECIMAL(10, 2)) >= '" + Samount +
                    "' AND CAST(REPLACE(" + SMS_AMOUNT + ", ',', '') AS DECIMAL(10, 2)) <= '" + Eamount + "'";
        } else {
            table_name = "SELECT *FROM " + SMS_TABLE + " where " + SMS_BANK_NAME + " ='" + bankName + "' AND "
                    + SMS_TYPES + " ='" + type + "' AND " + SMS_DATE + " BETWEEN '" + Sdate + "' AND '" + Edate +
                    "' AND CAST(REPLACE(" + SMS_AMOUNT + ", ',', '') AS DECIMAL(10, 2)) >= '" + Samount +
                    "' AND CAST(REPLACE(" + SMS_AMOUNT + ", ',', '') AS DECIMAL(10, 2)) <= '" + Eamount + "'";
        }
        Cursor cursor = db.rawQuery(table_name, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    SMSModel smsModel = new SMSModel();
                    smsModel.setId(cursor.getString(cursor.getColumnIndex(SMS_ID)));
                    smsModel.setTypes(cursor.getString(cursor.getColumnIndex(SMS_TYPES)));
                    smsModel.setBankName(cursor.getString(cursor.getColumnIndex(SMS_BANK_NAME)));
                    smsModel.setBodyMsg(cursor.getString(cursor.getColumnIndex(SMS_MESSAGE)));
                    smsModel.setDate(cursor.getLong(cursor.getColumnIndex(SMS_DATE)));
                    smsModel.setBalance(cursor.getString(cursor.getColumnIndex(SMS_BALANCE)));
                    smsModel.setAmount(cursor.getString(cursor.getColumnIndex(SMS_AMOUNT)));
                    smsModelArrayList.add(smsModel);
                } while (cursor.moveToNext());
            }
        }
        return smsModelArrayList;
    }

    @SuppressLint("Range")
    public ArrayList<SMSModel> getAllSMSType(String bankName, String type) {
        ArrayList<SMSModel> smsModelArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String table_name;
        if (type.equalsIgnoreCase("all")) {
            table_name = "SELECT *FROM " + SMS_TABLE + " where " + SMS_BANK_NAME + " = '" + bankName + "'";
        } else {
            table_name = "SELECT *FROM " + SMS_TABLE + " where " + SMS_BANK_NAME + " = '" + bankName + "' AND " + SMS_TYPES + " = '" + type + "'";
        }
        Cursor cursor = db.rawQuery(table_name, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    SMSModel smsModel = new SMSModel();
                    smsModel.setId(cursor.getString(cursor.getColumnIndex(SMS_ID)));
                    smsModel.setTypes(cursor.getString(cursor.getColumnIndex(SMS_TYPES)));
                    smsModel.setBankName(cursor.getString(cursor.getColumnIndex(SMS_BANK_NAME)));
                    smsModel.setBodyMsg(cursor.getString(cursor.getColumnIndex(SMS_MESSAGE)));
                    smsModel.setDate(cursor.getLong(cursor.getColumnIndex(SMS_DATE)));
                    smsModel.setBalance(cursor.getString(cursor.getColumnIndex(SMS_BALANCE)));
                    smsModel.setAmount(cursor.getString(cursor.getColumnIndex(SMS_AMOUNT)));
                    smsModelArrayList.add(smsModel);
                } while (cursor.moveToNext());
            }
        }
        return smsModelArrayList;
    }
}
