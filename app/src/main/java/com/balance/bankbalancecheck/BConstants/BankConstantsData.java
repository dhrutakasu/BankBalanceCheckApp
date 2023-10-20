package com.balance.bankbalancecheck.BConstants;

import static com.android.volley.VolleyLog.e;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.balance.bankbalancecheck.BHelper.BankBalanceHelper;
import com.balance.bankbalancecheck.BModel.SMS;
import com.balance.bankbalancecheck.BModel.SMSModel;
import com.balance.bankbalancecheck.R;
import com.karumi.dexter.PermissionToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.jvm.internal.Ref;
import kotlin.text.Regex;

public class BankConstantsData {
    public static final String SCHEMES_LINK = "SCHEMES_LINK";
    public static final String SCHEMES_TITLE = "SCHEMES_TITLE";
    public static final String LOAN_TYPE = "LOAN_TYPE";
    public static final String LOAN_WEB = "LOAN_WEB";
    public static final String IFSC_BANK = "IFSC_BANK";
    public static final String IFSC_STATE = "IFSC_STATE";
    public static final String IFSC_DISTRICT = "IFSC_DISTRICT";
    public static final String IFSC_BRANCH = "IFSC_BRANCH";
    public static String EMI_Pos = "EMI_Pos";
    public static ArrayList<SMSModel> TranscationsResult = new ArrayList<SMSModel>();


    public static void showSettingsDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permissions to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                openSettings(activity);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showPermissionDialog(final Activity activity, final PermissionToken permissionToken) {
        new AlertDialog.Builder(activity
        ).setMessage(R.string.MSG_ASK_PERMISSION).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                permissionToken.cancelPermissionRequest();
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                permissionToken.continuePermissionRequest();
            }
        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                permissionToken.cancelPermissionRequest();
            }
        }).show();
    }

    private static void openSettings(Activity activity) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivityForResult(intent, 101);
    }

    public static void hideKeyboardFromView(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {
        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            hideKeyboardFromView(activity, focusedView);
        }
    }

    public static ArrayList<SMSModel> GotoSMS(Context context, BankBalanceHelper smsHelper) {
        ArrayList<SMSModel> smsModels = new ArrayList<>();
        ArrayList<SMS> sms = new ArrayList<>();
        Uri uri = Uri.parse("content://sms/inbox");
        String[] projection = {"_id", "address", "date", "body"};
//        String[] projection = {"_id", "address", "body", "date"};
        int i = 0;
        int i2 = 0;
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, "date desc");
//        smsHelper.DeleteSMS();
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("_id");
            int addressIndex = cursor.getColumnIndex("address");
            int bodyIndex = cursor.getColumnIndex("body");
            int dateIndex = cursor.getColumnIndex("date");
            System.out.println("------ cursor : " + cursor.getCount());
            do {
                String id = cursor.getString(idIndex);
                String string = cursor.getString(addressIndex);
                String string2 = cursor.getString(bodyIndex);
                long date = cursor.getLong(dateIndex);
                String dateString = DateFormat.format("dd MMM ,yyyy", new Date(date)).toString();
                if (string.contains("BOBTXN")) {
                    System.out.println("------ addre : " + string + " - " + string2);
                    i++;
                }
                if (string2.contains("Bank of Baroda")) {
                    i2++;
                }

//                sms.add(new SMS(string2, string, date));
//                smsModels.add(new SMSModel(string2, string, date));
                try {
                    BankConstantsData.a(cursor, context, smsModels, smsHelper);
                } catch (Exception e) {
                    System.out.println("------ exexex : " + e.getMessage());
                    throw new RuntimeException(e);
                }

//                if (string2.contains("-")) {
//                    smsModels.add(new SMSModel(id, address, string2, string2.substring(string2.lastIndexOf(" - ") + 1).replace("- ", ""), date));
//                } else {
//                    smsModels.add(new SMSModel(id, address, string2, "", date));
//                }
            } while (cursor.moveToNext());
            cursor.close();
            System.out.println("------ unused --1-- --> : " + smsModels.size());
            System.out.println("------ unused --1-- -->iiii : " + i + " - " + i2);
        }
        return smsModels;
    }

    private static String a(String r8, String r9) {
        String result = "";

        if (r8.contains("IMPS")) {
            int index = r8.indexOf("IMPS");
            String substring = r8.substring(index);
            result = substring;
        } else if (r8.contains("NEFT")) {
            int index = r8.indexOf("NEFT");
            String substring = r8.substring(index);
            result = substring;
        } else if (r8.contains("UPI")) {
            int index = r8.indexOf("UPI");
            String substring = r8.substring(index);
            result = substring;
        } else if (r8.contains("towards")) {
            int index = r8.indexOf("towards");
            String substring = r8.substring(index);
            result = substring;
        } else if (r8.contains(" at")) {
            String[] parts = r8.split(" at");
            if (parts.length == 2) {
                String part = parts[1].trim();
                if (part.contains(".")) {
                    int dotIndex = part.indexOf(".");
                    if (dotIndex > 1) {
                        result = part.substring(1, dotIndex);
                    }
                } else {
                    result = part.substring(1);
                }
            }
        } else if (r8.contains("being")) {
            String[] parts = r8.split("being");
            if (parts.length == 2) {
                String part = parts[1].trim();
                if (part.contains(".")) {
                    int dotIndex = part.indexOf(".");
                    if (dotIndex > 1) {
                        result = part.substring(1, dotIndex);
                    }
                } else {
                    result = part.substring(1);
                }
            }
        } else if (r8.contains("for")) {
            String[] parts = r8.split("for");
            if (parts.length == 2) {
                String part = parts[1].trim();
                if (part.contains(".")) {
                    int dotIndex = part.indexOf(".");
                    if (dotIndex > 1) {
                        result = part.substring(0, dotIndex);
                    }
                } else {
                    result = part;
                }
            }
        } else {
            r9 = r9.replaceAll("\\(", "");
            r9 = r9.replaceAll("\\)", "");
            String[] parts = r8.split(r9);
            if (parts.length == 2) {
                String part = parts[1].trim();
                if (part.contains(".")) {
                    int dotIndex = part.indexOf(".");
                    if (dotIndex > 1) {
                        result = part.substring(0, dotIndex);
                    }
                } else {
                    result = part;
                }
            }
        }

        if (result != null && !result.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append(result);
            builder.append(".");
            result = builder.toString();
        }

        return result;
    }

    public static boolean d(String str) {
        if (str.toLowerCase().contains("otp")) {
            return false;
        }
        if (Pattern.compile("(?:[Aa]ccount|[Aa]\\/[Cc]|[Aa][Cc][Cc][Tt]|[Cc][Aa][Rr][Dd] |[Cc]redited|[Dd]ebited)").matcher(str.toLowerCase()).find()) {
            return !a(str).equals("NA") && !str.toLowerCase().contains("talktime") && !str.toLowerCase().contains("recharge") && !a(str).contains("#");
        } else if (a(str).equals("NA")) {
            return false;
        } else {
            return (str.toLowerCase().contains("deposited") || str.toLowerCase().contains("debited") || str.toLowerCase().contains("transaction") || str.toLowerCase().contains("credited") || str.toLowerCase().contains("balance") || str.toLowerCase().contains("txn") || str.toLowerCase().contains("bal")) && !str.toLowerCase().contains("talktime") && !str.toLowerCase().contains("recharge") && !a(str).contains("#");
        }
    }

    public static String a(String str) {
        boolean z;
//        int i2 = 0;
        String str2 = " " + str;
        Matcher matcher = Pattern.compile("(xx\\d+)|ending\\s*\\d+").matcher(str2);
        Matcher matcher2 = Pattern.compile("[.]{2,}[0-9]{2,}|[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}").matcher(str2);
        if (matcher2.find()) {
            System.out.println("------ str2.substring(matcher2.start(), matcher2.end())) break " + str2.substring(matcher2.start(), matcher2.end()));
            return str2.substring(matcher2.start(), matcher2.end());
        }
        if (matcher.find()) {
            System.out.println("------ matcher.group(0) break " + matcher.group(0));
            return matcher.group(0);
        }
        if (str2.toLowerCase().contains("account") || str2.toLowerCase().contains("a/c") || str2.toLowerCase().contains("a/c no") || str2.toLowerCase().contains("account no")) {
            String[] split = str2.split("\\s");
            for (int i3 = 1; i3 < split.length; i3++) {
                char[] charArray = split[i3].toCharArray();
                int length = charArray.length;
                int i4 = 0;
                while (true) {
                    if (i4 >= length) {
                        z = false;
                        System.out.println("------ i4 >= length break " + z);
                        break;
                    } else if (!Character.isDigit(Character.valueOf(charArray[i4]).charValue())) {
                        z = true;
                        System.out.println("------ i4 <= length break " + z);
                        break;
                    } else {
                        i4++;
                    }
                }
                if (!z) {
                    int i5 = i3 - 1;
                    if (split[i5].equalsIgnoreCase("account") || split[i5].equalsIgnoreCase("a/c")) {
                        System.out.println("------ split[i5].toLowerCase().equals(\"account\") || split[i5].toLowerCase().equals(\"a/c\") break " + split[i3]);
                        return split[i3];
                    }
                    if (i3 > 1) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(split[i3 - 2]);
                        sb.append("");
                        sb.append(split[i5]);
                        if (!sb.toString().equalsIgnoreCase("account no")) {
//                            if ((split[i2] + "" + split[i5]).toLowerCase().equals("a/c no")) {
//                            }
                        }
                        System.out.println("------ sb.toString().toLowerCase().equals(\"account no\") break " + split[i3]);
                        return split[i3];
                    }
                    continue;
                }
            }
            System.out.println("------ \"NA\" break");
            return "NA";
        }
        System.out.println("------ \"NA\"222 break");
        return "NA";
    }

    static ArrayList<SMSModel> arrayList = new ArrayList();

    public static ArrayList<SMSModel> a(Cursor cursor, Context context, ArrayList<SMSModel> smsModels, BankBalanceHelper smsHelper) {
        boolean z;
        String str;
        boolean z2;
        int addressIndex = cursor.getColumnIndex("address");
        int bodyIndex = cursor.getColumnIndex("body");
        int dateIndex = cursor.getColumnIndex("date");
        String string = cursor.getString(addressIndex);
        String string2 = cursor.getString(bodyIndex);
//        String string = cursor.getBankName();
//        String string2 = cursor.getBodyMsg();
        System.out.println("------ string2 " + string2);
        SMSModel smsModel = new SMSModel();
        if (d(string2) && !string.contains("paytm")) {
            String a2 = a(string2);
            if (a2.length() > 4) {
                a2 = a2.substring(a2.length() - 4);
            }
            System.out.println("------ setBodyMsg(a2) " + a2);
            Date date = new Date(cursor.getLong(dateIndex));
            smsModel.setDate(cursor.getLong(dateIndex));
            System.out.println("------ setDate(cursor.getLong(2) " + date.getTime());
            smsModel.setaBoolean(e(string2));
            if (!string2.toLowerCase().contains("card") || string2.toLowerCase().contains("debit card of acct") || string2.toLowerCase().contains("debit card of a/c") || string2.toLowerCase().contains("debit card of account")) {
                smsModel.setConfirmed(false);
                System.out.println("------ card ");
            } else {
                System.out.println("------ not card ");
                smsModel.setaBoolean(true);
                smsModel.setConfirmed(true);
            }

            String str4 = null;
            String g2 = g(" " + string2);
            String g3 = g(" " + string2);
            System.out.println("------ g2 = g(\" \" + string2) " + g(" " + string2));
            System.out.println("------ g3 = g(\" \" + string2) " + g(" " + string2));
            String f2 = f(string2);
            if (g2 != null) {
                str4 = c(string2);
                System.out.println("------ c(string2) " + c(string2));
                if (g2.contains(",")) {
                    g2 = g2.replace(",", "");
                    System.out.println("------ g2.replace(\",\", \"\") " + g2);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    g2 = a(b(g2).doubleValue());
                    System.out.println("------ a(b(g2).doubleValue()) " + g2);
                }

                if (f2 == null) {
                    f2 = a(string2, g3);
                }
                System.out.println("------ f(string2) " + f2);
                if (str4 != null) {
                    if (str4.contains(",")) {
                        str4 = str4.replace(",", "");
                        System.out.println("------ 112str4.replace(\",\", \"\") " + str4);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        str4 = a(b(str4).doubleValue());
                        System.out.println("------ 112a(b(str4).doubleValue()) " + str4);
                    }
                    smsModel.setBalance(str4);
                    smsModel.setAmount(g2);
                } else {
                    if (!string2.toLowerCase().contains("bal") && !string2.toLowerCase().contains("balance") && !string2.toLowerCase().contains("net")) {
                        smsModel.setAmount(g2);
                        str = "setDescription:4 ";
                        System.out.println("------ 11Bal " + string2);
                    } else if (string2.toLowerCase().contains("netbank")) {
                        smsModel.setAmount(g2);
                        str = "setDescription:1 ";
                        System.out.println("------ 11netbank " + string2);
                    } else {
                        String h2 = h(string2);
                        System.out.println("------ 11elseBank " + string2);
                        System.out.println("------ 11h(string2) " + h2);
                        if (h2 != null) {
                            smsModel.setAmount(g2);
                            smsModel.setBalance(h2);
                            System.out.println("------ 11h2 != null " + h2 + " ^^^ " + g2);
                        } else {
                            System.out.println("------ 11h2 == null " + g2);
                            smsModel.setAmount(g2);
                        }
                        str = "setDescription:2 ";
                    }
                    System.out.println("------ 11sb.append(str) " + str + " ^^^^ " + f2);
                }
                System.out.println("------ 11aVar2.d(f2) " + string2 + " ^^^^ " + f2);
            }
            String[] strArr = context.getResources().getStringArray(R.array.bank_short);
            for (int i = 0; i < strArr.length; i++) {
                if (i >= strArr.length) {
                    z = false;
                    System.out.println("------ z = false break " + z);
                    break;
                }
                String lowerCase = context.getResources().getStringArray(R.array.bank_full)[i].toLowerCase();
                System.out.println("------ strArr2.valuesss : " + string);
                System.out.println("------ strArr2.value : " + context.getResources().getStringArray(R.array.bank_full)[i]);
                if (string.contains(strArr[i]) || string.toLowerCase().contains(lowerCase) || string2.contains(strArr[i]) || string2.toLowerCase().contains(lowerCase)) {
                    smsModel.setBankName(context.getResources().getStringArray(R.array.bank_full)[i]);
                    smsModel.setBodyMsg(string2);
                    System.out.println("------ string.contains(str2) || string.toLowerCase().contains(lowerCase) break");
                    break;
                }
            }

            z = true;
            if (!z) {
                if (string.toLowerCase().contains("paytm")) {
                    string = "Paytm Bank";
                }
                smsModel.setBankName(string);
                System.out.println("------ -string = \"Paytm Bank\" " + string);
            }
            System.out.println("------ getBBBBBodyMsg " + string2);
            if (g2 != null && str4 != null && !a2.equals("NA") && !a2.equals("") && !a2.contains("*") && !a2.contains("#")) {
                int i3 = 0;
                System.out.println("------ getBg2 != null || str4 != null " + g2 + " ^^^ " + str4);
                System.out.println("------ getBa2.equals(\"NA\") " + f2);
                System.out.println("------ getBodyMsg " + smsModel.getBodyMsg());
                System.out.println("------ getDate " + smsModel.getDate());
                System.out.println("------ getBankName " + smsModel.getBankName());
                System.out.println("------ getBalance " + smsModel.getBalance());
                System.out.println("------ getAmount " + smsModel.getAmount());
                if (smsModel.getBodyMsg() != null && smsModel.getBankName() != null) {
                    smsModel.setAddress(string);
                    if (smsModel.getBodyMsg().toLowerCase().contains("credit")) {
                        smsModel.setTypes("credit");
                    } else {
                        smsModel.setTypes("debit");
                    }
                    while (true) {
                        if (i3 >= smsHelper.getAllSMS().size()) {
                            z2 = true;
                            break;
                        }
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(new Date(smsHelper.getAllSMS().get(i3).getDate()).getTime() - date.getTime());
                        if (seconds < 60 && 0 <= seconds && g2 != null && g3 != null && ((smsHelper.getAllSMS().get(i3).getBodyMsg().contains(a2) || a2.contains(smsHelper.getAllSMS().get(i3).getBodyMsg())) && g2.equals(smsHelper.getAllSMS().get(i3).getBodyMsg()))) {
                            z2 = false;
                            break;
                        }
                        i3++;
                    }
                    if (z2) {
                        smsHelper.InsertSMS(smsModel);
                        smsModels.add(smsModel);
                    }
                    return smsModels;
                }
            }
        }
        System.out.println("------ -string = str3 " + string);
        System.out.println("getAvailableBalance: unused --1-- --> bbb " + smsModels.size());
        return smsModels;
    }

    public static void aCopy(Cursor cursor, Context context) {
        boolean z;
        boolean z2;
        StringBuilder sb;
        String str;
        String sb2;
        new ArrayList();

//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                do {
        int addressIndex = cursor.getColumnIndex("address");
        int bodyIndex = cursor.getColumnIndex("body");
        int dateIndex = cursor.getColumnIndex("date");
        String string = cursor.getString(addressIndex);
        String string2 = cursor.getString(bodyIndex);
//        String string = cursor.getBankName();
//        String string2 = cursor.getBodyMsg();
        System.out.println("------ string2 " + string2);
        SMSModel smsModel = new SMSModel();
        if (d(string2) && !string.contains("paytm")) {
            String a2 = a(string2);
            if (a2.length() > 4) {
                a2 = a2.substring(a2.length() - 4);
            }
            System.out.println("------ setBodyMsg(a2) " + a2);
//                        aVar2.a(a2);
            Date date = new Date(cursor.getLong(dateIndex));
            smsModel.setDate(cursor.getLong(dateIndex));
            System.out.println("------ setDate(cursor.getLong(2) " + date.getTime());
// //                       aVar2.a(date);
            smsModel.setaBoolean(e(string2));
//                        System.out.println("------ setaBoolean(e(string2)) " + e(string2));
//     //                   aVar2.b(e(string2));
            if (!string2.toLowerCase().contains("card") || string2.toLowerCase().contains("debit card of acct") || string2.toLowerCase().contains("debit card of a/c") || string2.toLowerCase().contains("debit card of account")) {
                smsModel.setConfirmed(false);
                System.out.println("------ card ");
////                            aVar2.a(false);
            } else {
                System.out.println("------ not card ");
                smsModel.setaBoolean(true);
                smsModel.setConfirmed(true);
//                            aVar2.b(true);
//                            aVar2.a(true);
            }
//            int i2 = 0;
//            String[] strArr2 = context.getResources().getStringArray(R.array.bank_full);

            String str4 = null;
            String g2 = g(" " + string2);
            String g3 = g(" " + string2);
            System.out.println("------ g2 = g(\" \" + string2) " + g(" " + string2));
            System.out.println("------ g3 = g(\" \" + string2) " + g(" " + string2));
            String f2 = f(string2);
            if (g2 != null) {
                str4 = c(string2);
                System.out.println("------ c(string2) " + c(string2));
                if (g2.contains(",")) {
                    g2 = g2.replace(",", "");
                    System.out.println("------ g2.replace(\",\", \"\") " + g2);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    g2 = a(b(g2).doubleValue());
                    System.out.println("------ a(b(g2).doubleValue()) " + g2);
                }

                if (f2 == null) {
                    f2 = a(string2, g3);
                }
                System.out.println("------ f(string2) " + f2);
                if (str4 != null) {
                    if (str4.contains(",")) {
                        str4 = str4.replace(",", "");
                        System.out.println("------ 112str4.replace(\",\", \"\") " + str4);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        str4 = a(b(str4).doubleValue());
                        System.out.println("------ 112a(b(str4).doubleValue()) " + str4);
                    }
                    smsModel.setBalance(str4);
                    smsModel.setAmount(g2);
//                                aVar2.g(str4);
//                                aVar2.c(g2);
//                                sb2 = "setDescription: " + f2;
                } else {
                    if (!string2.toLowerCase().contains("bal") && !string2.toLowerCase().contains("balance") && !string2.toLowerCase().contains("net")) {
//                                    aVar2.c(g2);
                        smsModel.setAmount(g2);
                        sb = new StringBuilder();
                        str = "setDescription:4 ";
                        System.out.println("------ 11Bal " + string2);
                    } else if (string2.toLowerCase().contains("netbank")) {
//                                    aVar2.c(g2);
                        smsModel.setAmount(g2);
                        sb = new StringBuilder();
                        str = "setDescription:1 ";
                        System.out.println("------ 11netbank " + string2);
                    } else {
                        String h2 = h(string2);
                        System.out.println("------ 11elseBank " + string2);
                        System.out.println("------ 11h(string2) " + h2);
                        if (h2 != null) {
                            smsModel.setAmount(g2);
//                                        aVar2.c(g2);
//                                        aVar2.g(h2);
                            smsModel.setBalance(h2);
                            System.out.println("------ 11h2 != null " + h2 + " ^^^ " + g2);
                        } else {
                            System.out.println("------ 11h2 == null " + g2);
//                                        aVar2.g(g2);
                            smsModel.setAmount(g2);
                        }
//                                    sb = new StringBuilder();
                        str = "setDescription:2 ";
                    }
                    System.out.println("------ 11sb.append(str) " + str + " ^^^^ " + f2);
//                                sb.append(str);
//                                sb.append(f2);
//                                sb2 = sb.toString();
                }
                System.out.println("------ 11aVar2.d(f2) " + string2 + " ^^^^ " + f2);
//                            Log.d("TAG", sb2);
//                            aVar2.d(f2);
//                            aVar2.e(string2);
            }
            String[] strArr = context.getResources().getStringArray(R.array.bank_short);
            for (int i = 0; i < strArr.length; i++) {
                if (i >= strArr.length) {
                    z = false;
                    System.out.println("------ z = false break " + z);
                    break;
                }
                String lowerCase = context.getResources().getStringArray(R.array.bank_full)[i].toLowerCase();
                System.out.println("------ strArr2.valuesss : " + string);
                System.out.println("------ strArr2.value : " + context.getResources().getStringArray(R.array.bank_full)[i]);
                if (string.contains(strArr[i]) || string.toLowerCase().contains(lowerCase) || string2.contains(strArr[i]) || string2.toLowerCase().contains(lowerCase)) {
                    smsModel.setBankName(context.getResources().getStringArray(R.array.bank_full)[i]);
                    smsModel.setBodyMsg(string2);
                    System.out.println("------ string.contains(str2) || string.toLowerCase().contains(lowerCase) break");
                    break;
                }
            }
        /*    while (true) {
                System.out.println("------ strArr.length : " + strArr.length);
                if (i2 >= strArr.length) {
                    z = false;
                    System.out.println("------ z = false break " + z);
                    break;
                }
                String str2 = strArr[i2];
                System.out.println("------ str2 : " + str2);
                String lowerCase = context.getResources().getStringArray(R.array.bank_full)[i2].toLowerCase();
                string = context.getResources().getStringArray(R.array.bank_full)[i2];
                System.out.println("------ lowerCase : " + lowerCase);
                if (string.contains(str2) || string.toLowerCase().contains(lowerCase) || string2.contains(str2) || string2.toLowerCase().contains(lowerCase)) {
                    System.out.println("------ string.contains(str2) || string.toLowerCase().contains(lowerCase) break");
                    break;
                }
                System.out.println("------ strArr2.value : " + context.getResources().getStringArray(R.array.bank_full)[i2]);
                i2++;
                smsModel.setBankName(context.getResources().getStringArray(R.array.bank_full)[i2]);
                System.out.println("------ i2++ : " + i2);
            }*/
//                        if (!R.contains(W[i2])) {
//                            R.add(this.W[i2]);
//                        }
//            System.out.println("------ i2 i2 i2 : " + i2);

//            System.out.println("------ strArr2.length() : " + strArr2.length);
//                        aVar2.b(strArr2[i2]);
//                        System.out.println("------ setBankName(strArr2[i2]) " + str3);

            z = true;
            if (!z) {
                if (string.toLowerCase().contains("paytm")) {
                    string = "Paytm Bank";
                }
                smsModel.setBankName(string);
                System.out.println("------ -string = \"Paytm Bank\" " + string);
//                            aVar2.b(string);
            }
            System.out.println("------ getBBBBBodyMsg " + string2);
            if (g2 != null && str4 != null && !a2.equals("NA") && !a2.equals("") && !a2.contains("*") && !a2.contains("#")) {
                int i3 = 0;
                System.out.println("------ getBg2 != null || str4 != null " + g2 + " ^^^ " + str4);
                System.out.println("------ getBa2.equals(\"NA\") " + f2);
                System.out.println("------ getBodyMsg " + smsModel.getBodyMsg());
                System.out.println("------ getDate " + smsModel.getDate());
                System.out.println("------ getBankName " + smsModel.getBankName());
                System.out.println("------ getBalance " + smsModel.getBalance());
                System.out.println("------ getAmount " + smsModel.getAmount());
                smsModel.setAddress(string);
                if (smsModel.getBodyMsg().toLowerCase().contains("credit")) {
                    smsModel.setTypes("credit");
                } else {
                    smsModel.setTypes("debit");
                }
                arrayList.add(smsModel);


//                            while (true) {
//                                if (i3 >= this.Q.size()) {
//                                    z2 = true;
//                                    break;
//                                }
//                                String a3 = this.Q.get(i3).a();
//                                String c2 = this.Q.get(i3).c();
//                                long seconds = TimeUnit.MILLISECONDS.toSeconds(this.Q.get(i3).e().getTime() - date.getTime());
//                                if (seconds < 60 && 0 <= seconds && g2 != null && g3 != null && ((a3.contains(a2) || a2.contains(a3)) && g2.equals(c2))) {
//                                    z2 = false;
//                                    break;
//                                }
//                                i3++;
//                            }
//                            if (z2) {
//                                this.Q.add(aVar2);
//                                aVar.a(aVar2);
//                                if (!R.contains(aVar2.b())) {
//                                    R.add(aVar2.b());
//                                }
//                            }
            }
        }
        System.out.println("------ -string = str3 " + string);
        System.out.println("getAvailableBalance: unused --1-- --> aa " + arrayList.size());
        System.out.println("getAvailableBalance: unused --1-- --> bbb " + Arrays.toString(arrayList.toArray()));

//                } while (cursor.moveToNext());
//                SharedPreferences.Editor edit = this.Y.edit();
//                edit.putBoolean("isDbLoaded", true);
//                edit.apply();
//            } else {
////                SharedPreferences.Editor edit2 = this.Y.edit();
////                edit2.putBoolean("isDbLoaded", true);
////                edit2.apply();
//            }
//        }
//        C();
    }

    private static String h(String str) {
        Matcher matcher = Pattern.compile("(?i)(?:(?:.?rs|inr| mrp)\\.?\\s?)(\\'?\\d+(:?\\,\\d+)?(\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)").matcher(str);
        if (matcher.find() && matcher.find()) {
            System.out.println("------ String h break " + matcher.group(0));
            return matcher.group(0);
        }
        System.out.println("------ String h break ");
        return null;
    }

    private static String f(String str) {
        if (str.contains("Info")) {
            String[] split = str.split("Info");
            if (split.length == 2) {
                if (!split[1].contains(".") || split[1].length() <= 1) {
                    split[1] = split[1].replace(":", "");
                    System.out.println("------ split[1] break " + split[1]);
                    return split[1];
                }
                String trim = split[1].substring(1, split[1].indexOf(".", 1)).trim();
                System.out.println("------ trim break " + trim);
                return trim + ".";
            }
            System.out.println("------ String f break ");
            return null;
        }
        System.out.println("------ String f null break ");
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String a(double d2) {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("en", "IN"));
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat.format(d2);
    }

    public static Double b(String str) {
        StringBuilder sb = new StringBuilder();
        if (str != null) {
            for (char c2 : str.toCharArray()) {
                Character valueOf = Character.valueOf(c2);
                if (Character.isDigit(valueOf.charValue()) || valueOf.toString().equals(".")) {
                    sb.append(valueOf);
                }
            }
            String substring = sb.toString().indexOf(".") == 0 ? sb.substring(1) : sb.toString();
            if (substring.contains(",")) {
                substring.replace(",", "");
            }
            if (a(substring, '.') > 1) {
                substring = substring.substring(0, substring.indexOf(46));
            }
            Log.d("ASD", str + "  Net balance---" + substring);
            if (!substring.equals("")) {
                try {
                    Double valueOf2 = Double.valueOf(Double.parseDouble(substring));
                    Log.d("ASD", "Net balance Double---" + valueOf2);
                    return valueOf2;
                } catch (NumberFormatException unused) {
//                    com.google.firebase.crashlytics.g.a().a("Number", substring);
                }
            }
        }
        return Double.valueOf(0.0d);
    }

    public static int a(String str, char c2) {
        int i2 = 0;
        for (int i3 = 0; i3 < str.length(); i3++) {
            if (str.charAt(i3) == c2) {
                i2++;
            }
        }
        return i2;
    }

    public static String c(String str) {
        Matcher matcher = Pattern.compile("(?i)(?:(?:balance|bal)\\.{0,2}\\s?\\:?)(?:\\s?(?:rs|inr|INR)?)\\.?\\s?\\|?(\\'?\\d+(:?\\,\\d+)?(\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)").matcher(str);
        if (matcher.find()) {
            return str.substring(matcher.start(), matcher.end());
        }
        return null;
    }

    private static String g(String str) {
        Matcher matcher = Pattern.compile("(?i)(?:(?:.?rs|inr| mrp)\\.?\\s?)(\\'?\\d+(:?\\,\\d+)?(\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)").matcher(str);
        if (matcher.find()) {
            return str.substring(matcher.start(), matcher.end());
        }
        return null;
    }

    private static boolean e(String str) {
        boolean z = false;
        boolean z2 = !str.toLowerCase().contains("credit") && !str.toLowerCase().contains("credited") && !str.toLowerCase().contains("credited with") && (str.toLowerCase().contains("txn") || str.toLowerCase().contains("debit") || str.toLowerCase().contains("debited") || str.toLowerCase().contains("debited with") || str.toLowerCase().contains("withdrawn"));
        if (str.toLowerCase().contains("credited") && str.toLowerCase().contains("debited")) {
            z2 = str.toLowerCase().indexOf("credited") > str.toLowerCase().indexOf("debited");
        }
        if (str.toLowerCase().contains("from your account")) {
            z = true;
        } else if (!str.toLowerCase().contains("to your account")) {
            z = z2;
        }
        if (str.toLowerCase().contains("transaction") || str.toLowerCase().contains("transferred") || str.toLowerCase().contains("deducted")) {
            return true;
        }
        return z;
    }

    public static SMSModel getAvailableBalance(SMSModel model) {
        String str;
        String str2;
        String str3;
        String str4;
        Pattern pattern = Pattern.compile("(?i)(?:RS|INR|MRP)?(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)+");
        if (model.getBodyMsg().contains("curr o/s - ")) {
            List<String> strings = Arrays.asList(model.getBodyMsg().split("o/s - ", 6));
            Log.d("TAG", "getAvailableBalance: newBody --1-- --> " + strings);
            Matcher matcher = pattern.matcher(strings.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("BALANCE", "getAvailableBalance: " + replace);
            }
        } else if (model.getBodyMsg().contains("The Balance is")) {
            List<String> the_balance_is_ = Arrays.asList(model.getBodyMsg().split("The Balance is ", 6));
            Log.d("TAG", "getAvailableBalance: newBody --2-- --> " + the_balance_is_);
            Matcher matcher = pattern.matcher(the_balance_is_.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("BALANCE", "getAvailableBalance: " + replace);
                return model;
            }
        } else if (model.getBodyMsg().contains("The Available Balance is")) {
            List<String> theAvailableBalanceIs = Arrays.asList(model.getBodyMsg().split("The Available Balance is ", 6));
            Log.d("TAG", "getAvailableBalance: newBody --3-- --> " + theAvailableBalanceIs);
            Matcher matcher = pattern.matcher(theAvailableBalanceIs.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("BALANCE", "getAvailableBalance: " + replace);
                return model;
            }
        } else if (model.getBodyMsg().contains("Avbl Lmt:")) {
            List<String> list = Arrays.asList(model.getBodyMsg().split("Avbl Lmt:", 6));
            Log.d("TAG", "getAvailableBalance: newBody --4-- --> " + list);
            Matcher matcher = pattern.matcher(list.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("BALANCE", "getAvailableBalance: " + replace);
                return model;
            }
        } else if (model.getBodyMsg().contains("Avlbal")) {
            List<String> avlbal = Arrays.asList(model.getBodyMsg().split("Avlbal", 6));
            Log.d("TAG", "getAvailableBalance: newBody --5-- --> " + avlbal);
            Matcher matcher = pattern.matcher(avlbal.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("BALANCE", "getAvailableBalance: " + replace);
                return model;
            }
        } else if (model.getBodyMsg().contains("balance is")) {
            String lowerCase = model.getBodyMsg().toLowerCase();
            List<String> balanceIs = Arrays.asList(lowerCase.split("balance is ", 6));
            Matcher matcher = pattern.matcher(balanceIs.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("BALANCE", "getAvailableBalance: " + replace);
                return model;
            }
        } else if (model.getBodyMsg().contains("AvBl Bal:")) {
            String lowerCase = model.getBodyMsg().toLowerCase();
            List<String> stringList = Arrays.asList(lowerCase.split("avbl bal: ", 6));
            Log.d("TAG", "getAvailableBalance: newBody --7-- --> " + stringList);
            Matcher matcher = pattern.matcher(stringList.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("BALANCE", "getAvailableBalance: " + replace);
                return model;
            }
        } else if (model.getBodyMsg().contains("Avl. Bal:")) {
            String lowerCase = model.getBodyMsg().toLowerCase();
            List<String> asList = Arrays.asList(lowerCase.split("avl. bal:", 6));
            Matcher matcher = pattern.matcher(asList.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("BALANCE", "getAvailableBalance: " + replace);
                return model;
            }
        } else if (model.getBodyMsg().contains("AVl BAL")) {
            if (model.getBodyMsg().contains("Avl. Bal:")) {
                String lowerCase = model.getBodyMsg().toLowerCase(Locale.ROOT);
                List<String> list = Arrays.asList(lowerCase.split("avl. bal:", 6));
                Log.d("mTAG30Dec2022", "getAvailableBalance: newBody --9-- --> " + list);
                if (list.isEmpty() || list.size() < 2) {
                    return model;
                }
                Matcher matcher = pattern.matcher(list.get(1).trim());
                if (matcher.find()) {
                    String group = matcher.group(0);
                    String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                    model.setBalance(replace);
                    Log.e("mTAG30Dec2022", "getAvailableBalance: " + replace);
                    return model;
                }
                model.setBalance(list.get(1).split(" ", 6)[0].trim());
                return model;
            }
            String toLowerCase = model.getBodyMsg().toLowerCase(Locale.ROOT);
            List<String> avlBal = Arrays.asList(toLowerCase.split("avl bal", 6));
            Log.d("mTAG30Dec2022", "getAvailableBalance: newBody --10-- --> " + avlBal);
            if (avlBal.isEmpty() || avlBal.size() < 2) {
                return model;
            }
            Log.e("mTAG30Dec2022", "getAvailableBalance: 10-> 1");
            Matcher matcher = pattern.matcher(avlBal.get(1).trim());
            if (matcher.find()) {
                Log.e("mTAG30Dec2022", "getAvailableBalance: 10-> 2");
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("mTAG30Dec2022", "getAvailableBalance: " + replace);
                return model;
            }
            String[] split = avlBal.get(1).trim().split(" ", 6);
            Log.e("mTAG30Dec2022", "getAvailableBalance: 10-> 3 -> " + avlBal.get(1).trim().split(" ", 6));
            Ref.BooleanRef booleanRef = new Ref.BooleanRef();
            for (String str5 : split) {
                if (!booleanRef.element) {
                    StringBuilder sb = new StringBuilder();
                    int length = str5.length();
                    for (int i2 = 0; i2 < length; i2++) {
                        char charAt = str5.charAt(i2);
                        if (Character.isDigit(charAt)) {
                            sb.append(charAt);
                        }
                    }
                    String sb2 = sb.toString();
                    boolean z = sb2.length() > 0;
                    booleanRef.element = z;
                    if (z) {
                        model.setBalance(str5);
                    }
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("getAvailableBalance: 10-> 3 -> ");
                    sb3.append(str5);
                    sb3.append(" -> ");
                    StringBuilder sb4 = new StringBuilder();
                    int length2 = str5.length();
                    for (int i3 = 0; i3 < length2; i3++) {
                        char charAt2 = str5.charAt(i3);
                        if (Character.isDigit(charAt2)) {
                            sb4.append(charAt2);
                        }
                    }
                    String sb5 = sb4.toString();
                    sb3.append(sb5.length() > 0);
                    Log.e("mTAG30Dec2022", sb3.toString());
                }
            }
            if (booleanRef.element) {
                return model;
            }
            String obj = avlBal.get(1).trim().split(" ", 6)[0];
            Log.e("mTAG30Dec2022", "getAvailableBalance: amount::-> " + obj);
            model.setBalance(obj);
            return model;
        } else if (model.getBodyMsg().contains("Avail Bal")) {
            String lowerCase = model.getBodyMsg().toLowerCase();
            List<String> availBal = Arrays.asList(lowerCase.split("avail bal ", 6));
            Log.d("TAG", "getAvailableBalance: newBody --11-- --> " + availBal);
            Matcher matcher = pattern.matcher(new Regex("\\s").replace(availBal.get(1).trim(), ""));
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("BALANCE", "getAvailableBalance: " + replace);
                return model;
            }
        } else if (model.getBodyMsg().contains("The combine BAL is")) {
            String lowerCase = model.getBodyMsg().toLowerCase();
            List balIs = Arrays.asList(lowerCase.split("bal is ", 6));
            Log.d("TAG", "getAvailableBalance: newBody --12-- --> " + balIs);
            Matcher matcher = pattern.matcher(balIs.get(1).toString().trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("BALANCE", "getAvailableBalance: " + replace);
                return model;
            }
        } else if (model.getBodyMsg().contains("The balance in")) {
            String lowerCase = model.getBodyMsg().toLowerCase();
            List<String> balanceIn = Arrays.asList(lowerCase.split("balance in ", 6));
            Log.d("TAG", "getAvailableBalance: newBody --13-- --> " + balanceIn);
            Matcher matcher = pattern.matcher(balanceIn.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance("N/A");
                model.setAmount(replace);
                model.setTypes("balance");
                Log.e("BALANCE", "getAvailableBalance: " + replace);
                return model;
            }
        } else if (model.getBodyMsg().contains("Available balance:")) {
            String lowerCase = model.getBodyMsg().toLowerCase();
            List<String> strings = Arrays.asList(lowerCase.split("available balance:", 6));
            Log.d("TAG", "getAvailableBalance: newBody --14-- --> " + strings);
            Matcher matcher = pattern.matcher(strings.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                if (!model.getBodyMsg().toLowerCase().contains("credited") && !model.getBodyMsg().toLowerCase().contains("cash deposit")
                        && !model.getBodyMsg().toLowerCase().contains("credit")
                        && !model.getBodyMsg().toLowerCase().contains("deposited")
                        && !model.getBodyMsg().toLowerCase().contains("deposit")
                        && !model.getBodyMsg().toLowerCase().contains("received")) {
                    if (model.getBodyMsg().toLowerCase().contains("withdrawn")) {
                        str4 = "debited";
                        model.setTypes(str4);
                        model.setBalance(replace);
                        pattern = Pattern.compile("\\d+");
                        matcher = pattern.matcher(strings.get(0).trim());
                        if (matcher.find()) {
                            String amountString = matcher.group();
                            model.setAmount(amountString);
                        }
                    } else {
                        str4 = "debited";
                        if (model.getBodyMsg().toLowerCase().contains(str4)
                                || model.getBodyMsg().toLowerCase().contains("spent")
                                || model.getBodyMsg().toLowerCase().contains("paying")
                                || model.getBodyMsg().toLowerCase().contains("deducted")
                                || model.getBodyMsg().toLowerCase().contains("dr")
                                || model.getBodyMsg().toLowerCase().contains("txn")
                                || model.getBodyMsg().toLowerCase().contains("transfer")) {
                            model.setTypes(str4);
                            model.setBalance(replace);
                            pattern = Pattern.compile("\\d+");
                            matcher = pattern.matcher(strings.get(0).trim());
                            if (matcher.find()) {
                                String amountString = matcher.group();
                                model.setAmount(amountString);
                            }
                        } else {
                            model.setTypes("balance");
                            model.setBalance("N/A");
                            model.setAmount(replace);
                        }
                    }
                } else {
                    model.setTypes("credited");
                    model.setBalance(replace);
                    pattern = Pattern.compile("\\d+");
                    matcher = pattern.matcher(strings.get(0).trim());
                    if (matcher.find()) {
                        String amountString = matcher.group();
                        model.setAmount(amountString);
                    }
                }
                Log.e("BALANCE", "getAvailableBalance: " + replace);
                return model;
            }
        } else if (model.getBodyMsg().contains("Avlbl Amt:")) {
            String lowerCase = model.getBodyMsg().toLowerCase();
            List asList = Arrays.asList(lowerCase.split("avlbl amt:", 6));
            Log.d("TAG", "getAvailableBalance: newBody --15-- --> " + asList);
            Matcher matcher = pattern.matcher(asList.get(1).toString().trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs.").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                Log.e("TAG", "getAvailableBalance: amount::-> " + model.getBodyMsg().toLowerCase());
                if (!model.getBodyMsg().toLowerCase().contains("credited") &&
                        !model.getBodyMsg().toLowerCase().contains("cash deposit") &&
                        !model.getBodyMsg().toLowerCase().contains("credit") &&
                        !model.getBodyMsg().toLowerCase().contains("deposited") &&
                        !model.getBodyMsg().toLowerCase().contains("deposit") &&
                        !model.getBodyMsg().toLowerCase().contains("received")) {
                    if (model.getBodyMsg().toLowerCase().contains("withdrawn")) {
                        str3 = "debited";
                        model.setTypes(str3);
                        model.setBalance(replace);
                        pattern = Pattern.compile("\\d+");
                        matcher = pattern.matcher(asList.get(0).toString().trim());
                        if (matcher.find()) {
                            String amountString = matcher.group();
                            model.setAmount(amountString);
                        }
                        Log.e("BALANCE", "getAvailableBalance str3: " + model.getBalance());
                        Log.e("BALANCE", "getAvailableBalance str3@@@: " + model.getAmount());

                        return model;
                    } else {
                        str3 = "debited";
                        if (model.getBodyMsg().toLowerCase().contains(str3)
                                || model.getBodyMsg().toLowerCase().contains("spent")
                                || model.getBodyMsg().toLowerCase().contains("paying")
                                || model.getBodyMsg().toLowerCase().contains("payment")
                                || model.getBodyMsg().toLowerCase().contains("deducted")
                                || model.getBodyMsg().toLowerCase().contains("debit")
                                || model.getBodyMsg().toLowerCase().contains("dr")
                                || model.getBodyMsg().toLowerCase().contains("txn")
                                || model.getBodyMsg().toLowerCase().contains("transfer")) {
                            model.setTypes(str3);
                            model.setBalance(replace);
                            pattern = Pattern.compile("\\d+");
                            matcher = pattern.matcher(asList.get(0).toString().trim());
                            if (matcher.find()) {
                                String amountString = matcher.group();
                                model.setAmount(amountString);
                            }
                            Log.e("BALANCE", "getAvailableBalance sm4: " + model.getBalance());
                            Log.e("BALANCE", "getAvailableBalance sm4@@@: " + model.getAmount());
                            return model;
                        } else {
                            model.setTypes("balance");
                            model.setBalance("N/A");
                            model.setAmount(replace);
                            Log.e("BALANCE", "getAvailableBalance modelgetBalance: " + model.getBalance());
                            Log.e("BALANCE", "getAvailableBalance modelamount: " + model.getAmount());
                            return model;
                        }
                    }
                } else {
                    model.setTypes("credited");
                    model.setBalance(replace);
                    pattern = Pattern.compile("\\d+");
                    matcher = pattern.matcher(asList.get(0).toString().trim());
                    if (matcher.find()) {
                        String amountString = matcher.group();
                        model.setAmount(amountString);
                    }
                    Log.e("TAG", "getAvailableBalance:ele ::-> " + model.getBodyMsg().toLowerCase());
                    return model;
                }
            }
        } else if (model.getBodyMsg().contains("Avl Bal")) {
            String lowerCase = model.getBodyMsg().toLowerCase();
            Log.d("TAG", "getAvailableBalance: newBody --lowerCase-- --> " + lowerCase);
            List avlBal = Arrays.asList(lowerCase.split("Avl Bal ", 6));
            Log.d("TAG", "getAvailableBalance: newBody --16-- --> " + avlBal.size());
            Matcher matcher;
            if (avlBal.size() >= 1) {
                matcher = pattern.matcher(avlBal.toString());
            } else {
                matcher = pattern.matcher(avlBal.get(1).toString().trim());
            }
            Log.d("TAG", "getAvailableBalance: myTest Match --> " + matcher);
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                if (!model.getBodyMsg().toLowerCase().contains("credited") &&
                        !model.getBodyMsg().toLowerCase().contains("cash deposit") &&
                        !model.getBodyMsg().toLowerCase().contains("credit") &&
                        !model.getBodyMsg().toLowerCase().contains("deposited") &&
                        !model.getBodyMsg().toLowerCase().contains("deposit") &&
                        !model.getBodyMsg().toLowerCase().contains("received")) {
                    if (model.getBodyMsg().toLowerCase().contains("withdrawn")) {
                        str3 = "debited";
                        model.setTypes(str3);
                        model.setBalance(replace);
                        pattern = Pattern.compile("\\d+");
                        matcher = pattern.matcher(avlBal.get(0).toString().trim());
                        if (matcher.find()) {
                            String amountString = matcher.group();
                            model.setAmount(amountString);
                        }
                        Log.e("BALANCE", "getAvailableBalance str3: " + model.getBalance());
                        Log.e("BALANCE", "getAvailableBalance str3@@@: " + model.getAmount());

                        return model;
                    } else {
                        str3 = "debited";
                        if (model.getBodyMsg().toLowerCase().contains(str3)
                                || model.getBodyMsg().toLowerCase().contains("spent")
                                || model.getBodyMsg().toLowerCase().contains("paying")
                                || model.getBodyMsg().toLowerCase().contains("payment")
                                || model.getBodyMsg().toLowerCase().contains("deducted")
                                || model.getBodyMsg().toLowerCase().contains("debit")
                                || model.getBodyMsg().toLowerCase().contains("dr")
                                || model.getBodyMsg().toLowerCase().contains("txn")
                                || model.getBodyMsg().toLowerCase().contains("transfer")) {
                            model.setTypes(str3);
                            model.setBalance(replace);
                            pattern = Pattern.compile("\\d+");
                            matcher = pattern.matcher(avlBal.get(0).toString().trim());
                            if (matcher.find()) {
                                String amountString = matcher.group();
                                model.setAmount(amountString);
                            }
                            Log.e("BALANCE", "getAvailableBalance sm4: " + model.getBalance());
                            Log.e("BALANCE", "getAvailableBalance sm4@@@: " + model.getAmount());
                            return model;
                        } else {
                            model.setTypes("balance");
                            model.setBalance("N/A");
                            model.setAmount(replace);
                            Log.e("BALANCE", "getAvailableBalance modelgetBalance: " + model.getBalance());
                            Log.e("BALANCE", "getAvailableBalance modelamount: " + model.getAmount());
                            return model;
                        }
                    }
                } else {
                    model.setTypes("credited");
                    model.setBalance(replace);
                    pattern = Pattern.compile("\\d+");
                    matcher = pattern.matcher(avlBal.get(0).toString().trim());
                    if (matcher.find()) {
                        String amountString = matcher.group();
                        model.setAmount(amountString);
                    }
                    Log.e("BALANCE", "getAvailableBalance: BOIIIII " + replace);
                    Log.e("TAG", "getAvailableBalance:ele ::-> " + model.getBodyMsg().toLowerCase());
                    return model;
                }
                /*if (!model.getBodyMsg().toLowerCase().contains("credited")
                        && !model.getBodyMsg().toLowerCase().contains("cash deposit")
                        && !model.getBodyMsg().toLowerCase().contains("credit")
                        && !model.getBodyMsg().toLowerCase().contains("deposited")
                        && !model.getBodyMsg().toLowerCase().contains("deposit")
                        && !model.getBodyMsg().toLowerCase().contains("received")) {
                    if (model.getBodyMsg().toLowerCase().contains("withdrawn")) {
                        str2 = "debited";
                        model.setTypes(str2);
                        model.setBalance(replace);
                        pattern = Pattern.compile("\\d+");
                        matcher = pattern.matcher(avlBal.get(0).toString().trim());
                        if (matcher.find()) {
                            String amountString = matcher.group();
                            model.setAmount(amountString);
                        }
                        return model;
                    } else {
                        str2 = "debited";
                        if (model.getBodyMsg().toLowerCase().contains(str2)
                                || model.getBodyMsg().toLowerCase().contains("spent")
                                || model.getBodyMsg().toLowerCase().contains("paying")
                                || model.getBodyMsg().toLowerCase().contains("payment")
                                || model.getBodyMsg().toLowerCase().contains("debit")
                                || model.getBodyMsg().toLowerCase().contains("dr")
                                || model.getBodyMsg().toLowerCase().contains("txn")
                                || model.getBodyMsg().toLowerCase().contains("transfer")) {
                            model.setTypes(str2);
                            model.setBalance(replace);
                            pattern = Pattern.compile("\\d+");
                            matcher = pattern.matcher(avlBal.get(0).toString().trim());
                            if (matcher.find()) {
                                String amountString = matcher.group();
                                model.setAmount(amountString);
                                return model;
                            }
                        } else {
                            model.setTypes("balance");
                            model.setBalance("N/A");
                            model.setAmount(replace);
                            return model;
                        }
                    }
                } else {
                    model.setTypes("credited");
                    model.setBalance(replace);
                    pattern = Pattern.compile("\\d+");
                    matcher = pattern.matcher(avlBal.get(0).toString().trim());
                    if (matcher.find()) {
                        String amountString = matcher.group();
                        model.setAmount(amountString);
                    }
                    return model;
                }*/
            }
            Log.d("TAG", "getAvailableBalance: myTest --Else--> 2");
        } else if (model.getBodyMsg().contains("Available")) {
            String lowerCase = model.getBodyMsg().toLowerCase(Locale.ROOT);
            List available = Arrays.asList(lowerCase.split("Available", 4));
            Log.d("mTAG30Dec2022", "getAvailableBalance: newBody --17-- --> " + available.get(1).toString().trim());
            Matcher matcher = pattern.matcher(available.get(1).toString().trim());
            Log.d("mTAG30Dec2022", "getAvailableBalance: myTest Match --> " + matcher);
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                if (!model.getBodyMsg().toLowerCase().contains("credited")
                        && !model.getBodyMsg().toLowerCase().contains("cash deposit")
                        && !model.getBodyMsg().toLowerCase().contains("credit")
                        && !model.getBodyMsg().toLowerCase().contains("deposited")
                        && !model.getBodyMsg().toLowerCase().contains("deposit")
                        && !model.getBodyMsg().toLowerCase().contains("received")) {
                    if (model.getBodyMsg().toLowerCase().contains("withdrawn")) {
                        str = "debited";
                        model.setTypes(str);
                        model.setBalance(replace);
                        pattern = Pattern.compile("\\d+");
                        matcher = pattern.matcher(available.get(0).toString().trim());
                        if (matcher.find()) {
                            String amountString = matcher.group();
                            model.setAmount(amountString);
                        }
                    } else {
                        str = "debited";
                        if (model.getBodyMsg().toLowerCase().contains(str)
                                || model.getBodyMsg().toLowerCase().contains("spent")
                                || model.getBodyMsg().toLowerCase().contains("paying")
                                || model.getBodyMsg().toLowerCase().contains("payment")
                                || model.getBodyMsg().toLowerCase().contains("deducted")
                                || model.getBodyMsg().toLowerCase().contains("debit")
                                || model.getBodyMsg().toLowerCase().contains("dr")
                                || model.getBodyMsg().toLowerCase().contains("txn")
                                || model.getBodyMsg().toLowerCase().contains("transfer")) {
                            model.setTypes(str);
                            model.setBalance(replace);
                            pattern = Pattern.compile("\\d+");
                            matcher = pattern.matcher(available.get(0).toString().trim());
                            if (matcher.find()) {
                                String amountString = matcher.group();
                                model.setAmount(amountString);
                            }
                        } else {
                            model.setTypes("balance");
                            model.setBalance("N/A");
                            model.setAmount(replace);
                        }
                    }
                } else {
                    model.setTypes("credited");
                    model.setBalance(replace);
                    pattern = Pattern.compile("\\d+");
                    matcher = pattern.matcher(available.get(0).toString().trim());
                    if (matcher.find()) {
                        String amountString = matcher.group();
                        model.setAmount(amountString);
                    }
                }
                Log.e("mTAG30Dec2022", "getAvailableBalance: BOIIIII " + replace);
                return model;
            }
            Log.d("mTAG30Dec2022", "getAvailableBalance: myTest --Else--> 3");
        } else {
            model.setBalance("N/A");
            return model;
        }
        Log.e("BALANCE", "getAvailableBalance getAmount: " + model.getAmount());
        Log.e("BALANCE", "getAvailableBalance getBalance: " + model.getBalance());
        return model;
    }
}
