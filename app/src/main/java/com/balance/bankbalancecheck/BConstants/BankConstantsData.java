package com.balance.bankbalancecheck.BConstants;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;

import com.balance.bankbalancecheck.BModel.SMSModel;
import com.balance.bankbalancecheck.R;
import com.karumi.dexter.PermissionToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
        builder.setTitle((CharSequence) "Need Permissions");
        builder.setMessage((CharSequence) "This app needs permissions to use this feature. You can grant them in app settings.");
        builder.setPositiveButton((CharSequence) "GOTO SETTINGS", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                openSettings(activity);
            }
        });
        builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showPermissionDialog(final Activity activity, final PermissionToken permissionToken) {
        new AlertDialog.Builder(activity
        ).setMessage((int) R.string.MSG_ASK_PERMISSION).setNegativeButton("Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                permissionToken.cancelPermissionRequest();
            }
        }).setPositiveButton("Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
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
        intent.setData(Uri.fromParts("package", activity.getPackageName(), (String) null));
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

    public static ArrayList<SMSModel> GotoSMS(Context context) {
        ArrayList<SMSModel> smsModels = new ArrayList<>();
        Uri uri = Uri.parse("content://sms/inbox");
        String[] projection = {"_id", "address", "body", "date"};

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, "date desc");

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("_id");
            int addressIndex = cursor.getColumnIndex("address");
            int bodyIndex = cursor.getColumnIndex("body");
            int dateIndex = cursor.getColumnIndex("date");

            do {
                String id = cursor.getString(idIndex);
                String address = cursor.getString(addressIndex);
                String body = cursor.getString(bodyIndex);
                long date = cursor.getLong(dateIndex);
                smsModels.add(new SMSModel(id, address, body, "", date));
                // Do something with the SMS message data
             /*   Log.d("TAG", "ID: " + id);
                Log.d("TAG", "Address: " + address);
                Log.d("TAG", "Body: " + body);
                Log.d("TAG", "Date: " + new Date(date));*/

            } while (cursor.moveToNext());
            cursor.close();
        }
        return smsModels;
    }

    public static SMSModel getAvailableBalance(SMSModel model) {
        String str = "";
        String str2 = "";
        String str3 = "";
        String str4 = "";
        Pattern pattern = Pattern.compile("(?i)(?:RS|INR|MRP)?(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)+");
        if (model.getBody().contains("curr o/s - ")) {
            List<String> strings = Arrays.asList(model.getBody().split("o/s - ", 6));
            Log.d("TAG", "getAvailableBalance: newBody --1-- --> " + strings);
            Matcher matcher = pattern.matcher(strings.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("BALANCE", "getAvailableBalance: " + replace);
            }
        } else if (model.getBody().contains("The Balance is")) {
            List<String> the_balance_is_ = Arrays.asList(model.getBody().split("The Balance is ", 6));
            Log.d("TAG", "getAvailableBalance: newBody --2-- --> " + the_balance_is_);
            Matcher matcher = pattern.matcher(the_balance_is_.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("BALANCE", "getAvailableBalance: " + replace);
                return model;
            }
        } else if (model.getBody().contains("The Available Balance is")) {
            List<String> theAvailableBalanceIs = Arrays.asList(model.getBody().split("The Available Balance is ", 6));
            Log.d("TAG", "getAvailableBalance: newBody --3-- --> " + theAvailableBalanceIs);
            Matcher matcher = pattern.matcher(theAvailableBalanceIs.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("BALANCE", "getAvailableBalance: " + replace);
                return model;
            }
        } else if (model.getBody().contains("Avbl Lmt:")) {
            List<String> list = Arrays.asList(model.getBody().split("Avbl Lmt:", 6));
            Log.d("TAG", "getAvailableBalance: newBody --4-- --> " + list);
            Matcher matcher = pattern.matcher(list.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("BALANCE", "getAvailableBalance: " + replace);
                return model;
            }
        } else if (model.getBody().contains("Avlbal")) {
            List<String> avlbal = Arrays.asList(model.getBody().split("Avlbal", 6));
            Log.d("TAG", "getAvailableBalance: newBody --5-- --> " + avlbal);
            Matcher matcher = pattern.matcher(avlbal.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("BALANCE", "getAvailableBalance: " + replace);
                return model;
            }
        } else if (model.getBody().contains("balance is")) {
            String lowerCase = model.getBody().toLowerCase();
            List<String> balanceIs = Arrays.asList(lowerCase.split("balance is ", 6));
            Matcher matcher = pattern.matcher(balanceIs.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("BALANCE", "getAvailableBalance: " + replace);

                return model;
            }
        } else if (model.getBody().contains("AvBl Bal:")) {
            String lowerCase = model.getBody().toLowerCase();
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
        } else if (model.getBody().contains("Avl. Bal:")) {
            String lowerCase = model.getBody().toLowerCase();
            List<String> asList = Arrays.asList(lowerCase.split("avl. bal:", 6));
            Matcher matcher = pattern.matcher(asList.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                model.setBalance(replace);
                Log.e("BALANCE", "getAvailableBalance: " + replace);

                return model;
            }
        } else if (model.getBody().contains("AVl BAL")) {
            if (model.getBody().contains("Avl. Bal:")) {
                String lowerCase = model.getBody().toLowerCase(Locale.ROOT);
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
            String toLowerCase = model.getBody().toLowerCase(Locale.ROOT);
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
        } else if (model.getBody().contains("Avail Bal")) {
            String lowerCase = model.getBody().toLowerCase();
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
        } else if (model.getBody().contains("The combine BAL is")) {
            String lowerCase = model.getBody().toLowerCase();
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
        } else if (model.getBody().contains("The balance in")) {
            String lowerCase = model.getBody().toLowerCase();
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
        } else if (model.getBody().contains("Available balance:")) {
            String lowerCase = model.getBody().toLowerCase();
            List<String> strings = Arrays.asList(lowerCase.split("available balance:", 6));
            Log.d("TAG", "getAvailableBalance: newBody --14-- --> " + strings);
            Matcher matcher = pattern.matcher(strings.get(1).trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                if (!model.getBody().contains("credited") && !model.getBody().contains("cash deposit")
                        && !model.getBody().contains("credit")
                        && !model.getBody().contains("deposited")
                        && !model.getBody().contains("deposit")
                        && !model.getBody().contains("received")) {
                    if (model.getBody().contains("withdrawn")) {

                        str4 = "debited";
                        model.setTypes(str4);
                        model.setBalance(replace);
                        pattern = Pattern.compile("\\d+");
                        matcher = pattern.matcher(strings.get(0).toString().trim());
                        if (matcher.find()) {
                            String amountString = matcher.group(); // Get the matched number as a string
                            int amount = Integer.parseInt(amountString);
                            model.setAmount(amountString);
                        }
                    } else {
                        str4 = "debited";
                        if (model.getBody().contains((CharSequence) str4)
                                || model.getBody().contains("spent")
                                || model.getBody().contains("paying")
                                || model.getBody().contains("deducted")
                                || model.getBody().contains("dr")
                                || model.getBody().contains("txn")
                                || model.getBody().contains("transfer")) {

                            model.setTypes(str4);
                            model.setBalance(replace);
                            pattern = Pattern.compile("\\d+");
                            matcher = pattern.matcher(strings.get(0).toString().trim());
                            if (matcher.find()) {
                                String amountString = matcher.group(); // Get the matched number as a string
                                int amount = Integer.parseInt(amountString);
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
                }
                Log.e("BALANCE", "getAvailableBalance: " + replace);

                return model;
            }
        } else if (model.getBody().contains("Avlbl Amt:")) {
            String lowerCase = model.getBody().toLowerCase();
            List asList = Arrays.asList(lowerCase.split("avlbl amt:", 6));
            Log.d("TAG", "getAvailableBalance: newBody --15-- --> " + asList);
            Matcher matcher = pattern.matcher(asList.get(1).toString().trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs.").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                Log.e("TAG", "getAvailableBalance: amount::-> " + replace);
                if (!model.getBody().contains("credited") &&
                        !model.getBody().contains("cash deposit") &&
                        !model.getBody().contains("credit") &&
                        !model.getBody().contains("deposited") &&
                        !model.getBody().contains("deposit") &&
                        !model.getBody().contains("received")) {
                    if (model.getBody().contains("withdrawn")) {
//                        sm4 = model;
                        str3 = "debited";
                        model.setTypes(str3);
                        model.setBalance(replace);
                        pattern = Pattern.compile("\\d+");
                        matcher = pattern.matcher(asList.get(0).toString().trim());
                        if (matcher.find()) {
                            String amountString = matcher.group(); // Get the matched number as a string
                            int amount = Integer.parseInt(amountString);
                            model.setAmount(amountString);
                        }
                        Log.e("BALANCE", "getAvailableBalance str3: " + model.getBalance());
                        Log.e("BALANCE", "getAvailableBalance str3@@@: " + model.getAmount());

                        return model;
                    } else {
                        str3 = "debited";
                        if (model.getBody().contains(str3)
                                || model.getBody().contains("spent")
                                || model.getBody().contains("paying")
                                || model.getBody().contains("payment")
                                || model.getBody().contains("deducted")
                                || model.getBody().contains("debit")
                                || model.getBody().contains("dr")
                                || model.getBody().contains("txn")
                                || model.getBody().contains("transfer")) {
                            model.setTypes("credited");
                            model.setBalance(replace);
                            pattern = Pattern.compile("\\d+");
                            matcher = pattern.matcher(asList.get(0).toString().trim());
                            if (matcher.find()) {
                                String amountString = matcher.group(); // Get the matched number as a string
                                int amount = Integer.parseInt(amountString);
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
                    Log.e("BALANCE", "getAvailableBalance: " + model.getAmount());

                    return model;
                }
            }
        } else if (model.getBody().contains("Avl Bal")) {
            String lowerCase = model.getBody().toLowerCase();
            List avlBal = Arrays.asList(lowerCase.split("Avl Bal ", 6));
            Log.d("TAG", "getAvailableBalance: newBody --16-- --> " + avlBal);
            Matcher matcher = pattern.matcher(avlBal.get(1).toString().trim().toString());
            Log.d("TAG", "getAvailableBalance: myTest Match --> " + matcher);
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                if (!model.getBody().contains("credited")
                        && !model.getBody().contains("cash deposit")
                        && !model.getBody().contains("credit")
                        && !model.getBody().contains("deposited")
                        && !model.getBody().contains("deposit")
                        && !model.getBody().contains("received")) {
                    if (model.getBody().contains("withdrawn")) {
                        str2 = "debited";
                        model.setTypes(str2);
                        model.setBalance(replace);
                        pattern = Pattern.compile("\\d+");
                        matcher = pattern.matcher(avlBal.get(0).toString().trim());
                        if (matcher.find()) {
                            String amountString = matcher.group(); // Get the matched number as a string
                            int amount = Integer.parseInt(amountString);
                            model.setAmount(amountString);
                        }
                    } else {
                        str2 = "debited";
                        if (model.getBody().contains(str2)
                                || model.getBody().contains("spent")
                                || model.getBody().contains("paying")
                                || model.getBody().contains("payment")
                                || model.getBody().contains("debit")
                                || model.getBody().contains("dr")
                                || model.getBody().contains("txn")
                                || model.getBody().contains("transfer")) {
                            model.setTypes(str2);
                            model.setBalance(replace);
                            pattern = Pattern.compile("\\d+");
                            matcher = pattern.matcher(avlBal.get(0).toString().trim());
                            if (matcher.find()) {
                                String amountString = matcher.group(); // Get the matched number as a string
                                int amount = Integer.parseInt(amountString);
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
                }
                Log.e("BALANCE", "getAvailableBalance: BOIIIII " + replace);

                return model;
            }
            Log.d("TAG", "getAvailableBalance: myTest --Else--> 2");
        } else if (model.getBody().contains("Available")) {
            String lowerCase = model.getBody().toLowerCase(Locale.ROOT);
            List available = Arrays.asList(lowerCase.split("Available", 4));
            Log.d("mTAG30Dec2022", "getAvailableBalance: newBody --17-- --> " + available.get(1).toString().trim());
            Matcher matcher = pattern.matcher(available.get(1).toString().trim().toString());
            Log.d("mTAG30Dec2022", "getAvailableBalance: myTest Match --> " + matcher);
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                if (!model.getBody().contains("credited")
                        && !model.getBody().contains("cash deposit")
                        && !model.getBody().contains("credit")
                        && !model.getBody().contains("deposited")
                        && !model.getBody().contains("deposit")
                        && !model.getBody().contains("received")) {
                    if (model.getBody().contains("withdrawn")) {
                        str = "debited";
                        model.setTypes(str);
                        model.setBalance(replace);
                        pattern = Pattern.compile("\\d+");
                        matcher = pattern.matcher(available.get(0).toString().trim());
                        if (matcher.find()) {
                            String amountString = matcher.group(); // Get the matched number as a string
                            int amount = Integer.parseInt(amountString);
                            model.setAmount(amountString);
                        }
                    } else {
                        str = "debited";
                        if (model.getBody().contains(str)
                                || model.getBody().contains("spent")
                                || model.getBody().contains("paying")
                                || model.getBody().contains("payment")
                                || model.getBody().contains("deducted")
                                || model.getBody().contains("debit")
                                || model.getBody().contains("dr")
                                || model.getBody().contains("txn")
                                || model.getBody().contains("transfer")) {
                            model.setTypes(str);
                            model.setBalance(replace);
                            pattern = Pattern.compile("\\d+");
                            matcher = pattern.matcher(available.get(0).toString().trim());
                            if (matcher.find()) {
                                String amountString = matcher.group(); // Get the matched number as a string
                                int amount = Integer.parseInt(amountString);
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
