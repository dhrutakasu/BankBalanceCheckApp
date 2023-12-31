package com.check.allbank.account.balance.banking.passbook.BUi.BActivities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.check.allbank.account.balance.banking.passbook.AdverClass;
import com.check.allbank.account.balance.banking.passbook.BHelper.BankBalanceHelper;
import com.check.allbank.account.balance.banking.passbook.BModel.BankBalanceModel;
import com.check.allbank.account.balance.banking.passbook.BUi.BAdapters.BankAdapter;
import com.check.allbank.account.balance.banking.passbook.BUtilsClasses.BankPreferences;
import com.check.allbank.account.balance.banking.passbook.BuildConfig;
import com.check.allbank.account.balance.banking.passbook.R;

import java.io.IOException;
import java.util.ArrayList;

public class BankBalanceActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle, TxtBankBalanceNumber, TxtMiniStatementNumber, TxtCustomerCardNumber, TxtMiniStatementMsg;
    private ImageView IvBankBalanceNumber, IvMiniStatementNumber, IvCustomerCardNumber;
    private BankBalanceHelper helper;
    private ProgressBar ProgressBankBalance;
    private ArrayList<String> BankList = new ArrayList<>();
    private EditText EdtBankSearch;
    private BankAdapter bankAdapter;
    private String BankStr;
    private ConstraintLayout ConsEdtBankBalance, ConsBankBalance;
    private RecyclerView RvBankBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_balance);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        ImgShareApp = (ImageView) findViewById(R.id.ImgShareApp);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        RvBankBalance = (RecyclerView) findViewById(R.id.RvBankBalance);
        ConsEdtBankBalance = (ConstraintLayout) findViewById(R.id.ConsEdtBankBalance);
        ConsBankBalance = (ConstraintLayout) findViewById(R.id.ConsBankBalance);
        EdtBankSearch = (EditText) findViewById(R.id.EdtBankSearch);
        TxtBankBalanceNumber = (TextView) findViewById(R.id.TxtBankBalanceNumber);
        TxtMiniStatementNumber = (TextView) findViewById(R.id.TxtMiniStatementNumber);
        TxtCustomerCardNumber = (TextView) findViewById(R.id.TxtCustomerCardNumber);
        IvBankBalanceNumber = (ImageView) findViewById(R.id.IvBankBalanceNumber);
        IvMiniStatementNumber = (ImageView) findViewById(R.id.IvMiniStatementNumber);
        IvCustomerCardNumber = (ImageView) findViewById(R.id.IvCustomerCardNumber);
        TxtMiniStatementMsg = (TextView) findViewById(R.id.TxtMiniStatementMsg);
        ProgressBankBalance = (ProgressBar) findViewById(R.id.ProgressBankBalance);
    }

    private void BankInitListeners() {
        AdverClass.ShowLayoutBannerAds(context, ((ProgressBar) findViewById(R.id.progressBarAd)), (RelativeLayout) findViewById(R.id.RlAdver));
        ImgBack.setVisibility(View.VISIBLE);
        ImgShareApp.setVisibility(View.VISIBLE);
        TxtTitle.setText(new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, ""));
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
        IvCustomerCardNumber.setOnClickListener(this);
        IvBankBalanceNumber.setOnClickListener(this);
        IvMiniStatementNumber.setOnClickListener(this);
        EdtBankSearch.addTextChangedListener(this);
    }

    private void BankInitActions() {
        RvBankBalance.setLayoutManager(new LinearLayoutManager(context));
        BankList = new ArrayList<>();
        if (new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, "").isEmpty()) {
            ConsEdtBankBalance.setVisibility(View.VISIBLE);
            ConsBankBalance.setVisibility(View.GONE);
            BankList.clear();
            TxtTitle.setText("Select Bank");
            int i = 0;
            try {
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
            bankAdapter = new BankAdapter(context, BankList, new BankAdapter.BankListener() {
                @Override
                public void BankClick(int pos, ArrayList<String> strings) {
                    BankStr = strings.get(pos).toString();
                    new BankPreferences(context).putPrefString(BankPreferences.BANK_NAME, BankStr);
                    BankInitActions();
                }
            });
            RvBankBalance.setAdapter(bankAdapter);
            EdtBankSearch.setText("");
            EdtBankSearch.setHint("Select Bank");
        } else {
            ConsEdtBankBalance.setVisibility(View.GONE);
            ConsBankBalance.setVisibility(View.VISIBLE);
            helper = new BankBalanceHelper(context);
            TxtTitle.setText(new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, ""));
            new AsyncTask<Void, Void, BankBalanceModel>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    ProgressBankBalance.setVisibility(View.VISIBLE);
                }

                @Override
                protected BankBalanceModel doInBackground(Void... voids) {
                    helper = new BankBalanceHelper(context);
                    BankBalanceModel bankBalanceModel = helper.getSearchBankBalance(new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, ""));
                    return bankBalanceModel;
                }

                @Override
                protected void onPostExecute(BankBalanceModel result) {
                    if (result.getMiniStatementMsg() == null) {
                        TxtMiniStatementNumber.setText("NA");
                        TxtMiniStatementMsg.setVisibility(View.GONE);
                        IvMiniStatementNumber.setImageResource(R.drawable.ic_sms_call_now);
                    } else {
                        TxtMiniStatementNumber.setText(result.getMiniStatement());
                        TxtMiniStatementMsg.setText("Send SMS " + result.getMiniStatementMsg());
                        if (result.getMiniStatementMsg().contains("#")) {
                            TxtMiniStatementMsg.setVisibility(View.VISIBLE);
                            IvMiniStatementNumber.setImageResource(R.drawable.ic_sms_now);
                        } else {
                            TxtMiniStatementMsg.setVisibility(View.GONE);
                            IvMiniStatementNumber.setImageResource(R.drawable.ic_sms_call_now);
                        }
                    }
                    if (result.getBankBalance() == null) {
                        TxtBankBalanceNumber.setText("NA");
                    } else {
                        TxtBankBalanceNumber.setText(result.getBankBalance());
                    }
                    if (result.getCustomerCard() == null) {
                        TxtCustomerCardNumber.setText("NA");
                    } else {
                        TxtCustomerCardNumber.setText(result.getCustomerCard());
                    }
                    ProgressBankBalance.setVisibility(View.GONE);
                    OpenRegisteredDialog();
                    super.onPostExecute(result);
                }
            }.execute();
        }
    }

    private void OpenRegisteredDialog() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_msg);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
        ImageView IvBankClose = dialog.findViewById(R.id.IvBankClose);
        TextView TxtBankName = dialog.findViewById(R.id.TxtBankName);
        TxtBankName.setText(new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, ""));
        IvBankClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ImgBack:
                finish();
                break;
            case R.id.ImgShareApp:
                GotoShareApp();
                break;
            case R.id.IvBankBalanceNumber:
                if (!TxtBankBalanceNumber.getText().toString().equalsIgnoreCase("NA")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + TxtBankBalanceNumber.getText().toString()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(context, "No number found.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.IvCustomerCardNumber:
                if (!TxtCustomerCardNumber.getText().toString().equalsIgnoreCase("NA")) {
                    Intent intentCustomer = new Intent(Intent.ACTION_DIAL);
                    intentCustomer.setData(Uri.parse("tel:" + TxtCustomerCardNumber.getText().toString()));
                    intentCustomer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentCustomer);
                }else {
                    Toast.makeText(context, "No number found.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.IvMiniStatementNumber:
                if (!TxtMiniStatementNumber.getText().toString().equalsIgnoreCase("NA")) {
                    if (TxtMiniStatementMsg.getVisibility()==View.VISIBLE) {
                        Intent intentMini = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + TxtMiniStatementNumber.getText().toString().trim()));
                        intentMini.putExtra("sms_body", TxtMiniStatementMsg.getText().toString().replace("Send SMS ", ""));
                        intentMini.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentMini);
                    }else {
                        Intent intentCustomer = new Intent(Intent.ACTION_DIAL);
                        intentCustomer.setData(Uri.parse("tel:" + TxtMiniStatementNumber.getText().toString()));
                        intentCustomer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentCustomer);
                    }
                }else {
                    Toast.makeText(context, "No number found.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void GotoShareApp() {
        try {
            String shareMessage = "download.\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share link:"));
        } catch (Exception e) {
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        bankAdapter.getFilter().filter(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}