package com.balance.bankbalancecheck.BUi.BActivities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BModel.SMSModel;
import com.balance.bankbalancecheck.BUtilsClasses.BankPreferences;
import com.balance.bankbalancecheck.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

import kotlin.jvm.internal.Ref;
import kotlin.text.Regex;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Context context;
    private Button BtnBankDetail, BtnCal, BtnScheme, BtnCreditLoan, BtnMutualFund;
    private AutoCompleteTextView AutoBankSearch;
    private ImageView IvBankCancel, IvBankSearch;
    private final ArrayList<String> BankList = new ArrayList<>();
    private List<String> assetFileNames = new ArrayList<>();
    private ProgressBar ProgressBankBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;

        BtnBankDetail = findViewById(R.id.BtnBankDetail);
        BtnCal = findViewById(R.id.BtnCal);
        BtnScheme = findViewById(R.id.BtnScheme);
        BtnCreditLoan = findViewById(R.id.BtnCreditLoan);
        BtnMutualFund = findViewById(R.id.BtnMutualFund);
        AutoBankSearch = findViewById(R.id.AutoBankSearch);
        IvBankCancel = findViewById(R.id.IvBankCancel);
        IvBankSearch = findViewById(R.id.IvBankSearch);
        ProgressBankBalance = findViewById(R.id.ProgressBankBalance);
    }

    private void BankInitListeners() {
        BtnCal.setOnClickListener(this);
        BtnBankDetail.setOnClickListener(this);
        BtnScheme.setOnClickListener(this);
        BtnCreditLoan.setOnClickListener(this);
        BtnMutualFund.setOnClickListener(this);
        IvBankCancel.setOnClickListener(this);
//        IvBankSearch.setOnClickListener(this);
        AutoBankSearch.setOnItemClickListener(this);
    }

    private void BankInitActions() {
        BankList.clear();
        AutoBankSearch.setText(new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, ""));
        try {
            int i = 0;
            String[] list = getAssets().list("IFSC Code");
            if (list == null) {
                return;
            }
            if (list.length > 0) {
                int length = list.length;
                while (i < length) {
                    String str = list[i];
                    if (str.contains(".txt")) {
                        BankList.add(str.replace(".txt", ""));
                    } else {
                        BankList.add(str);
                    }
                    i++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("--- -- bank init : " + BankList.size());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, BankList);
        AutoBankSearch.setThreshold(1);
        AutoBankSearch.setAdapter(adapter);
        try {
            assetFileNames = Arrays.asList(getResources().getAssets().list("IFSC Code"));
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
        ProgressBankBalance.bringToFront();
        String s = Manifest.permission.READ_SMS;
        Dexter.withActivity(this)
                .withPermissions(s)
                .withListener(new MultiplePermissionsListener() {
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            new AsyncTask<Void, Void, ArrayList<SMSModel>>() {
                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    ProgressBankBalance.setVisibility(View.VISIBLE);
                                }

                                @Override
                                protected ArrayList<SMSModel> doInBackground(Void... voids) {
                                    return GotoSMS();
                                }

                                @Override
                                protected void onPostExecute(ArrayList<SMSModel> unused) {
                                    super.onPostExecute(unused);
                                    ProgressBankBalance.setVisibility(View.GONE);
                                    ArrayList<SMSModel> result = new ArrayList<SMSModel>();
                                    for (int i = 0; i < unused.size(); i++) {
                                        System.out.println("+++++ 11 BALANCE : " + getAvailableBalance(unused.get(i)).getBalance());
                                        if (getAvailableBalance(unused.get(i)).getBalance() != null) {
                                            if (!getAvailableBalance(unused.get(i)).getBalance().equals("N/A") && !getAvailableBalance(unused.get(i)).getAmount().equals("null")) {
                                                result.add(getAvailableBalance(unused.get(i)));
                                                System.out.println("+++++ 22 BALANCE : " + getAvailableBalance(unused.get(i)).getBalance());
                                                System.out.println("+++++ 22 AMOUNt : " + getAvailableBalance(unused.get(i)).getAmount());
                                            }
                                        }
                                    }
                                    Collections.reverse(result);
                                    if (unused.size() > 0) {
                                        List<String> strings = Arrays.asList(result.get(result.size() - 1).getBody().split("UPI/", 12));
                                        Log.d("TAG", "getAvailableBalance: newBody --1-- --> " + strings);
                                        String ANumber = strings.get(1).substring(0, strings.get(1).indexOf("."));
                                        String last4Digits = ANumber.substring(ANumber.length() - 4);
                                        System.out.println("+++++ strings.get(1) : " + last4Digits);

                                        ((TextView) findViewById(R.id.TxtBankName)).setText(result.get(result.size() - 1).getBody().substring(result.get(result.size() - 1).getBody().lastIndexOf(" - ") + 1).replace("- ", ""));
                                        ((TextView) findViewById(R.id.TxtBankAccNumber)).setText("A/c No:- " + last4Digits);
                                        NumberFormat numberFormat = null;
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                            double number = Double.parseDouble(result.get(result.size() - 1).getBalance());
                                            numberFormat = NumberFormat.getNumberInstance(Locale.US);
                                            ((TextView) findViewById(R.id.TxtBankAmount)).setText("INR :- " + numberFormat.format(number));
                                        }
                                        ((TextView) findViewById(R.id.TxtBankAmount)).setSelected(true);
                                        ((TextView) findViewById(R.id.TxtBankAccNumber)).setSelected(true);
                                        ((TextView) findViewById(R.id.TxtBankName)).setSelected(true);

                                        System.out.println("+++++ getBody : " + result.get(result.size() - 1).getBody());
                                        System.out.println("+++++ BALANCE : " + result.get(result.size() - 1).getBalance());
                                        System.out.println("+++++ AMOUNt : " + result.get(result.size() - 1).getAmount());
                                    }
                                }
                            }.execute();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            BankConstantsData.showSettingsDialog(MainActivity.this);
                        }
                    }

                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken permissionToken) {
                        BankConstantsData.showPermissionDialog(MainActivity.this, permissionToken);
                    }
                })
                .onSameThread()
                .check();
    }

    private SMSModel getAvailableBalance(SMSModel model) {
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

    public final String getTrasferedAmount(SMSModel sm) {
        boolean bool;
        String amount = sm.getAmount();
        StringBuilder builder = new StringBuilder();
        int length = amount.length();
        int i = 0;
        while (true) {
            boolean b = true;
            if (i >= length) {
                break;
            }
            char at = amount.charAt(i);
            if (!Character.isDigit(at) && at != '.') {
                b = false;
            }
            if (b) {
                builder.append(at);
            }
            i++;
        }
        String string = builder.toString();
        bool = string.length() > 0;
        if (bool && string.charAt(0) == '.') {
            return string.replace(string, "").toString();
        }
        return string;
    }

    public final String getTrasfferedAvlBalance(SMSModel sm) {
        String builder;
        boolean bool;
        boolean bool1;
        String balance = sm.getBalance();
        Locale locale = Locale.ROOT;
        String lowerCase = balance.toLowerCase(locale);
        String aCase = "N/A".toLowerCase(locale);
        boolean b = true;
        if (lowerCase.equals(aCase)) {
            builder = sm.getBalance();
        } else {
            String smBalance = sm.getBalance();
            StringBuilder stringBuilder = new StringBuilder();
            int length = smBalance.length();
            for (int i2 = 0; i2 < length; i2++) {
                char charAt = smBalance.charAt(i2);
                bool = Character.isDigit(charAt) || charAt == '.';
                if (bool) {
                    stringBuilder.append(charAt);
                }
            }
            builder = stringBuilder.toString();
        }
        bool1 = builder.length() > 0;
        if (bool1 && builder.charAt(0) == '.') {
            builder = builder.replace(builder, "").toString();
        }
        if (builder.length() != 0) {
            b = false;
        }
        if (b) {
            return "N/A";
        }
        return builder;
    }

    private ArrayList<SMSModel> GotoSMS() {
        ArrayList<SMSModel> smsModels = new ArrayList<>();
        Uri uri = Uri.parse("content://sms/inbox");
        String[] projection = {"_id", "address", "body", "date"};

        Cursor cursor = getContentResolver().query(uri, projection, null, null, "date desc");

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BtnBankDetail:
                GotoBanking();
                break;
            case R.id.BtnCal:
                GotoCalculators();
                break;
            case R.id.BtnScheme:
                GotoSavingScheme();
                break;
            case R.id.BtnCreditLoan:
                GotoCreditLoan();
                break;
            case R.id.BtnMutualFund:
                GotoMutualFund();
                break;
            case R.id.IvBankCancel:
                AutoBankSearch.setText("");
                break;
        }
    }

    private void GotoBanking() {
        startActivity(new Intent(context, BankingActivity.class));
    }

    private void GotoCalculators() {
        startActivity(new Intent(context, CalculatorsActivity.class));
    }

    private void GotoSavingScheme() {
        startActivity(new Intent(context, SavingSchemeActivity.class));
    }

    private void GotoCreditLoan() {
        startActivity(new Intent(context, CreditLoanActivity.class));
    }

    private void GotoMutualFund() {
        startActivity(new Intent(context, MutualFundActivity.class));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        new BankPreferences(context).putPrefString(BankPreferences.BANK_NAME, AutoBankSearch.getText().toString());
        new BankPreferences(context).putPrefString(BankPreferences.STATE_NAME, "");
        new BankPreferences(context).putPrefString(BankPreferences.DISTRICT_NAME, "");
        new BankPreferences(context).putPrefString(BankPreferences.BRANCH_NAME, "");
    }

    @Override
    protected void onResume() {
        if (AutoBankSearch != null) {
            AutoBankSearch.setText(new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, ""));
        }
        super.onResume();
    }
}