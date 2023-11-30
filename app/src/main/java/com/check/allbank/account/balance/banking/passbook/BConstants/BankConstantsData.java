package com.check.allbank.account.balance.banking.passbook.BConstants;

import static com.android.volley.VolleyLog.e;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.check.allbank.account.balance.banking.passbook.BHelper.BankBalanceHelper;
import com.check.allbank.account.balance.banking.passbook.BModel.SMS;
import com.check.allbank.account.balance.banking.passbook.BModel.SMSModel;
import com.check.allbank.account.balance.banking.passbook.BModel.AdverModel;
import com.check.allbank.account.balance.banking.passbook.LoadAdsData;
import com.check.allbank.account.balance.banking.passbook.R;
import com.karumi.dexter.PermissionToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankConstantsData {
    public static final String ADS_URL = "https://7starinnovation.com/api/bankbalancenew.json";
    public static final String SCHEMES_LINK = "SCHEMES_LINK";
    public static final String SCHEMES_TITLE = "SCHEMES_TITLE";
    public static final String LOAN_TYPE = "LOAN_TYPE";
    public static final String LOAN_WEB = "LOAN_WEB";
    public static final String IFSC_BANK = "IFSC_BANK";
    public static final String IFSC_STATE = "IFSC_STATE";
    public static final String IFSC_DISTRICT = "IFSC_DISTRICT";
    public static final String IFSC_BRANCH = "IFSC_BRANCH";
    public static final String BANK_NAME = "BANK_NAME";
    public static final String TRANSCATION = "TRANSCATION";
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
        int i = 0;
        int i2 = 0;
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, "date desc");
        if (smsHelper.isTableExists("SmsData", true)) {
            smsHelper.DeleteSMS();
        }
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("_id");
            int addressIndex = cursor.getColumnIndex("address");
            int bodyIndex = cursor.getColumnIndex("body");
            int dateIndex = cursor.getColumnIndex("date");
            do {
                String id = cursor.getString(idIndex);
                String string = cursor.getString(addressIndex);
                String string2 = cursor.getString(bodyIndex);
                long date = cursor.getLong(dateIndex);

                try {
                    BankConstantsData.FetchSMSData(cursor, context, smsModels, smsHelper);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            } while (cursor.moveToNext());
            cursor.close();
        }
        return smsModels;
    }

    public static String FetchSMSData(String body, String addbreks) {
        String result = "";

        if (body.contains("IMPS")) {
            int index = body.indexOf("IMPS");
            String substring = body.substring(index);
            result = substring;
        } else if (body.contains("NEFT")) {
            int index = body.indexOf("NEFT");
            String substring = body.substring(index);
            result = substring;
        } else if (body.contains("UPI")) {
            int index = body.indexOf("UPI");
            String substring = body.substring(index);
            result = substring;
        } else if (body.contains("towards")) {
            int index = body.indexOf("towards");
            String substring = body.substring(index);
            result = substring;
        } else if (body.contains(" at")) {
            String[] parts = body.split(" at");
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
        } else if (body.contains("being")) {
            String[] parts = body.split("being");
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
        } else if (body.contains("for")) {
            String[] parts = body.split("for");
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
            addbreks = addbreks.replaceAll("\\(", "");
            addbreks = addbreks.replaceAll("\\)", "");
            String[] parts = body.split(addbreks);
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

    public static boolean GetMsgPattern(String msg) {
        if (msg.toLowerCase().contains("otp")) {
            return false;
        }
        if (Pattern.compile("(?:[Aa]ccount|[Aa]\\/[Cc]|[Aa][Cc][Cc][Tt]|[Cc][Aa][Rr][Dd] |[Cc]redited|[Dd]ebited)").matcher(msg.toLowerCase()).find()) {
            return !getAmountFormat(msg).equals("NA") && !msg.toLowerCase().contains("talktime") && !msg.toLowerCase().contains("recharge") && !getAmountFormat(msg).contains("#");
        } else if (getAmountFormat(msg).equals("NA")) {
            return false;
        } else {
            return (msg.toLowerCase().contains("deposited") || msg.toLowerCase().contains("debited") || msg.toLowerCase().contains("transaction") || msg.toLowerCase().contains("credited") || msg.toLowerCase().contains("balance") || msg.toLowerCase().contains("txn") || msg.toLowerCase().contains("bal")) && !msg.toLowerCase().contains("talktime") && !msg.toLowerCase().contains("recharge") && !getAmountFormat(msg).contains("#");
        }
    }

    public static String getAmountFormat(String str) {
        boolean z;
        String str2 = " " + str;
        Matcher matcher = Pattern.compile("(xx\\d+)|ending\\s*\\d+").matcher(str2);
        Matcher matcher2 = Pattern.compile("[.]{2,}[0-9]{2,}|[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}").matcher(str2);
        if (matcher2.find()) {
            return str2.substring(matcher2.start(), matcher2.end());
        }
        if (matcher.find()) {
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
                        break;
                    } else if (!Character.isDigit(Character.valueOf(charArray[i4]).charValue())) {
                        z = true;
                        break;
                    } else {
                        i4++;
                    }
                }
                if (!z) {
                    int i5 = i3 - 1;
                    if (split[i5].equalsIgnoreCase("account") || split[i5].equalsIgnoreCase("a/c")) {
                        return split[i3];
                    }
                    if (i3 > 1) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(split[i3 - 2]);
                        sb.append("");
                        sb.append(split[i5]);
                        return split[i3];
                    }
                    continue;
                }
            }
            return "NA";
        }
        return "NA";
    }

    static ArrayList<SMSModel> arrayList = new ArrayList();

    public static ArrayList<SMSModel> FetchSMSData(Cursor cursor, Context context, ArrayList<SMSModel> smsModels, BankBalanceHelper smsHelper) {
        boolean bool = false;
        int addressIndex = cursor.getColumnIndex("address");
        int bodyIndex = cursor.getColumnIndex("body");
        int dateIndex = cursor.getColumnIndex("date");
        String address = cursor.getString(addressIndex);
        String body = cursor.getString(bodyIndex);
        SMSModel smsModel = new SMSModel();
        String msg = FetchMsg(body);

        if (GetMsgPattern(body) && !address.contains("paytm")) {
            String amountFormat = getAmountFormat(body);
            if (amountFormat.length() > 4) {
                amountFormat = amountFormat.substring(amountFormat.length() - 4);
            }
            smsModel.setDate(cursor.getLong(dateIndex));
            smsModel.setTrans(IsTrans(body));
            if (!body.toLowerCase().contains("card") || body.toLowerCase().contains("debit card of acct") || body.toLowerCase().contains("debit card of a/c") || body.toLowerCase().contains("debit card of account")) {
                smsModel.setConfirmed(false);
            } else {
                smsModel.setTrans(true);
                smsModel.setConfirmed(true);
            }

            String format = null;
            String amFormat = FetchAmount(" " + body);
            String Breaks = FetchAmount(" " + body);
            if (amFormat != null) {
                format = MsgFormats(body);
                if (amFormat.contains(",")) {
                    amFormat = amFormat.replace(",", "");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    amFormat = getAmountFormat(GetDoubleAMount(amFormat).doubleValue());
                }

                if (msg == null) {
                    msg = FetchSMSData(body, Breaks);
                }
                if (format != null) {
                    if (format.contains(",")) {
                        format = format.replace(",", "");
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        format = getAmountFormat(GetDoubleAMount(format).doubleValue());
                    }
                    smsModel.setBalance(format);
                    smsModel.setAmount(amFormat);
                } else {
                    if (!body.toLowerCase().contains("bal") && !body.toLowerCase().contains("balance") && !body.toLowerCase().contains("net")) {
                        smsModel.setAmount(amFormat);
                    } else if (body.toLowerCase().contains("netbank")) {
                        smsModel.setAmount(amFormat);
                    } else {
                        String balance = GetBalance(body);
                        if (balance != null) {
                            smsModel.setBalance(balance);
                            smsModel.setAmount(amFormat);
                        } else {
                            smsModel.setAmount(amFormat);
                        }
                    }
                }
            }

            for (int k = 0; k < context.getResources().getStringArray(R.array.bank_full).length; k++) {
                if (context.getResources().getStringArray(R.array.bank_full)[k].contains(body)) {
                }
            }

            String[] bankShort = context.getResources().getStringArray(R.array.bank_short);
            for (int i = 0; i < bankShort.length; i++) {
                String BankName = context.getResources().getStringArray(R.array.bank_full)[i].toLowerCase();
                if (address.length() >= 6) {
                    String add = address.substring(0, 6);
                    if (address.contains(bankShort[i]) || body.toLowerCase().contains(BankName)) {
                        smsModel.setBankName(context.getResources().getStringArray(R.array.bank_full)[i]);
                        smsModel.setBodyMsg(body);
                        bool = true;
                        break;
                    } else {
                        bool = false;
                    }
                } else {
                    bool = false;
                }
            }


            if (!bool) {
                if (address.toLowerCase().contains("paytm")) {
                    address = "Paytm Bank";
                }
                smsModel.setBankName(address);
            }
            if (amFormat != null && format != null && !amountFormat.equals("NA") && !amountFormat.equals("") && !amountFormat.contains("*") && !amountFormat.contains("#")) {
                if (smsModel.getBodyMsg() != null && smsModel.getBankName() != null) {
                    smsModel.setAddress(address);
                    if (smsModel.getBodyMsg().toLowerCase().contains("credit")) {
                        smsModel.setTypes("credit");
                    } else {
                        smsModel.setTypes("debit");
                    }
                    smsHelper.InsertSMS(smsModel);
                    smsModels.add(smsModel);
                    return smsModels;
                }
            }
        }
        return smsModels;
    }

    private static String GetBalance(String str) {
        Matcher matcher = Pattern.compile("(?i)(?:(?:.?rs|inr| mrp)\\.?\\s?)(\\'?\\d+(:?\\,\\d+)?(\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)").matcher(str);
        if (matcher.find() && matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    public static String FetchMsg(String msg) {
        if (msg.contains("Info")) {
            String[] splitInfo = msg.split("Info");
            if (splitInfo.length == 2) {
                if (!splitInfo[1].contains(".") || splitInfo[1].length() <= 1) {
                    splitInfo[1] = splitInfo[1].replace(":", "");
                    return splitInfo[1];
                }
                String StrTrim = splitInfo[1].substring(1, splitInfo[1].indexOf(".", 1)).trim();
                return StrTrim + ".";
            }
            return null;
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getAmountFormat(double amount) {
        NumberFormat instance = NumberFormat.getInstance(new Locale("en", "IN"));
        instance.setMinimumFractionDigits(2);
        return instance.format(amount);
    }

    public static Double GetDoubleAMount(String amount) {
        StringBuilder builder = new StringBuilder();
        if (amount != null) {
            for (char val : amount.toCharArray()) {
                Character valueOf = Character.valueOf(val);
                if (Character.isDigit(valueOf.charValue()) || valueOf.toString().equals(".")) {
                    builder.append(valueOf);
                }
            }
            String str = builder.toString().indexOf(".") == 0 ? builder.substring(1) : builder.toString();
            if (str.contains(",")) {
                str.replace(",", "");
            }
            if (getAmountFormat(str, '.') > 1) {
                str = str.substring(0, str.indexOf(46));
            }
            if (!str.equals("")) {
                try {
                    Double valueOf2 = Double.valueOf(Double.parseDouble(str));
                    return valueOf2;
                } catch (NumberFormatException unused) {
                    unused.getMessage();
                }
            }
        }
        return Double.valueOf(0.0d);
    }

    public static int getAmountFormat(String sb, char val) {
        int var = 0;
        for (int i3 = 0; i3 < sb.length(); i3++) {
            if (sb.charAt(i3) == val) {
                var++;
            }
        }
        return var;
    }

    public static String MsgFormats(String str) {
        Matcher matcher = Pattern.compile("(?i)(?:(?:balance|bal)\\.{0,2}\\s?\\:?)(?:\\s?(?:rs|inr|INR)?)\\.?\\s?\\|?(\\'?\\d+(:?\\,\\d+)?(\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)").matcher(str);
        if (matcher.find()) {
            return str.substring(matcher.start(), matcher.end());
        }
        return null;
    }

    public static String FetchAmount(String str) {
        Matcher matcher = Pattern.compile("(?i)(?:(?:.?rs|inr| mrp)\\.?\\s?)(\\'?\\d+(:?\\,\\d+)?(\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)").matcher(str);
        if (matcher.find()) {
            return str.substring(matcher.start(), matcher.end());
        }
        return null;
    }

    private static boolean IsTrans(String amount) {
        boolean boole = false;
        boolean tobool = !amount.toLowerCase().contains("credit") && !amount.toLowerCase().contains("credited") && !amount.toLowerCase().contains("credited with") && (amount.toLowerCase().contains("txn") || amount.toLowerCase().contains("debit") || amount.toLowerCase().contains("debited") || amount.toLowerCase().contains("debited with") || amount.toLowerCase().contains("withdrawn"));
        if (amount.toLowerCase().contains("credited") && amount.toLowerCase().contains("debited")) {
            tobool = amount.toLowerCase().indexOf("credited") > amount.toLowerCase().indexOf("debited");
        }
        if (amount.toLowerCase().contains("from your account")) {
            boole = true;
        } else if (!amount.toLowerCase().contains("to your account")) {
            boole = tobool;
        }
        if (amount.toLowerCase().contains("transaction") || amount.toLowerCase().contains("transferred") || amount.toLowerCase().contains("deducted")) {
            return true;
        }
        return boole;
    }

    public static AdverModel LoadAdsData(Context context, LoadAdsId loadAdsId) {
        AdverModel adverModel = new AdverModel();
        StringRequest request = new StringRequest(Request.Method.GET, ADS_URL,
                response2 -> {
                    try {
                        JSONObject response = new JSONObject(response2);
                        adverModel.setPakage(response.getString("pkg"));
                        adverModel.setBannerType(response.getString("btype"));
                        adverModel.setNtype(response.getString("ntype"));
                        adverModel.setItype(response.getString("itype"));
                        adverModel.setAdslogin(response.getString("login"));
                        adverModel.setQuerkaLink(response.getString("querkalink"));
                        adverModel.setOwnb(response.getString("ownb"));
                        adverModel.setOwnn(response.getString("ownn"));
                        adverModel.setOwnblink(response.getString("ownblink"));
                        adverModel.setOwnnlink(response.getString("ownnlink"));
                        adverModel.setStartAppid(response.getString("startappid"));
                        adverModel.setFbad(response.getString("fbad"));
                        adverModel.setFbIad(response.getString("fiad"));
                        adverModel.setFnad(response.getString("fnad"));
                        adverModel.setFnbad(response.getString("fnbad"));
                        adverModel.setPreloadOr(response.getString("preload"));
                        adverModel.setCloseadopen(response.getString("closeadopen"));
                        adverModel.setBackads(response.getString("backads"));
                        adverModel.setsIad(response.getString("siad"));
                        adverModel.setOad2(response.getString("oad2"));
                        adverModel.setOad(response.getString("oad"));
                        adverModel.setBannerAd(response.getString("bad"));
                        adverModel.setInad(response.getString("iad"));
                        adverModel.setNad2(response.getString("nad2"));
                        adverModel.setNad(response.getString("nad"));
                        adverModel.setLbad(response.getString("lbad"));
                        adverModel.setLnad(response.getString("lnad"));
                        adverModel.setLiad(response.getString("liad"));
                        adverModel.setTelegramlink(response.getString("telegramlink"));
                        adverModel.setAppPrivacyPolicyLink(response.getString("app_privacyPolicyLink"));
                        adverModel.setAppAdsButtonColor(response.getString("appAdsButtonColor"));
                        adverModel.setAppAdsButtonTextColor(response.getString("appAdsButtonTextColor"));
                        adverModel.setBackgroundcolor(response.getString("backgroundcolor"));
                        adverModel.setAdscount(response.getString("adscount"));
                        loadAdsId.getAdsIds(adverModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.getLocalizedMessage();
                }) {
        };
        LoadAdsData.getInstance(context).addToRequestQueue(request);
        return adverModel;
    }

    public interface LoadAdsId {
        void getAdsIds(AdverModel adverModel);
    }

}
