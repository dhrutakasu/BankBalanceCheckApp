package com.balance.bankbalancecheck.BUi.BActivities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
                                    for (SMSModel model : unused) {

/*                                      for (String assetFileName : assetFileNames) {
                                            Log.d("TAG", "assetFileNames: " + s1.getBody().contains(assetFileName.replace(".txt", "")));
                                            if (s1.getBody().contains(assetFileName.replace(".txt", ""))) {
                                                System.out.println("--- -- -%%% : " + s1.getBody().contains("debited"));
                                                if (s1.getBody().contains("Credited")
//                                                        &&s1.getBody().contains("transferred")&&s1.getBody().contains("withdrawn")
                                                        || s1.getBody().contains("debited")) {
                                                    Log.d("TAG", "assetFileNames Matched: " + DateFormat.format("dd/MM/yyyy", new Date(s1.getDate())).toString() + " -- " + s1.getBody());
                                                }
                                            }
                                        }*/

                                        getAvailableBalance(model);
                                    }
                                    Collections.reverse(unused);
                                    System.out.println("+++++ BALANCE : " + unused.get(unused.size() - 1).getBalance());
                                    System.out.println("+++++ AMOUNt : " + unused.get(unused.size() - 1).getAmount());
//                                        BankConstantsData.BankBalance=unused.get(unused.size()).getBalance()
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
        SMSModel sm2 = null;
        String str = "";
        SMSModel sm3 = null;
        String str2 = "";
        SMSModel sm4 = null;
        String str3 = "";
        SMSModel sm5 = null;
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
                        sm5 = model;
                        str4 = "debited";
                    } else {
                        str4 = "debited";
                        if (model.getBody().contains((CharSequence) str4)
                                || model.getBody().contains("spent")
                                || model.getBody().contains("paying")
                                || model.getBody().contains("deducted")
                                || model.getBody().contains("dr")
                                || model.getBody().contains("txn")
                                || model.getBody().contains("transfer")) {

                            sm5 = model;
                        } else {
                            model.setTypes("balance");
                            model.setBalance("N/A");
                            model.setAmount(replace);
                        }
                    }
//                    sm5.setTypes(str4);
//                    sm5.setBalance(replace14);
                } else {
                    model.setTypes("credited");
                    model.setBalance(replace);
                }
                Log.e("BALANCE", "getAvailableBalance: " + replace);
            }
        } else if (model.getBody().contains("Avlbl Amt:")) {
            String lowerCase = model.getBody().toLowerCase();
            List asList = Arrays.asList(lowerCase.split("avlbl amt:", 6));
            Log.d("TAG", "getAvailableBalance: newBody --15-- --> " + asList);
            Matcher matcher = pattern.matcher(asList.get(1).toString().trim());
            if (matcher.find()) {
                String group = matcher.group(0);
                String replace = new Regex(",").replace(new Regex(" ").replace(new Regex("inr").replace(new Regex("rs").replace(new Regex("inr").replace(group, ""), ""), ""), ""), "");
                Log.e("TAG", "getAvailableBalance: amount::-> " + replace);
                if (!model.getBody().contains("credited") &&
                        !model.getBody().contains("cash deposit") &&
                        !model.getBody().contains("credit") &&
                        !model.getBody().contains("deposited") &&
                        !model.getBody().contains("deposit") &&
                        !model.getBody().contains("received")) {
                    if (model.getBody().contains("withdrawn")) {
                        sm4 = model;
                        str3 = "debited";
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
                            sm4 = model;
                        } else {
                            model.setTypes("balance");
                            model.setBalance("N/A");
                            model.setAmount(replace);
                        }
                    }
//                    sm4.setTypes(str3);
//                    sm4.setBalance(replace15);
                } else {
                    model.setTypes("credited");
                    model.setBalance(replace);
                }
                Log.e("BALANCE", "getAvailableBalance: " + replace);
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
                        sm3 = model;
                        str2 = "debited";
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
                            sm3 = model;
                        } else {
                            model.setTypes("balance");
                            model.setBalance("N/A");
                            model.setAmount(replace);
                        }
                    }
//                    sm3.setTypes(str2);
//                    sm3.setBalance(replace16);
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
                        sm2 = model;
                        str = "debited";
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
                            sm2 = model;
                        } else {
                            model.setTypes("balance");
                            model.setBalance("N/A");
                            model.setAmount(replace);
                        }
                    }
//                    sm2.setTypes(str);
//                    sm2.setBalance(replace17);
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
        }
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