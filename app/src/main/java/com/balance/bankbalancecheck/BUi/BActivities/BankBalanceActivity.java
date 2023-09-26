package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.balance.bankbalancecheck.BHelper.BankBalanceHelper;
import com.balance.bankbalancecheck.BModel.BankBalanceModel;
import com.balance.bankbalancecheck.BUi.BAdapters.BankAdapter;
import com.balance.bankbalancecheck.BUtilsClasses.BankPreferences;
import com.balance.bankbalancecheck.BuildConfig;
import com.balance.bankbalancecheck.R;

import java.io.IOException;
import java.util.ArrayList;

public class BankBalanceActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle, TxtBankBalanceNumber, TxtMiniStatementNumber, TxtCustomerCardNumber, TxtMiniStatementMsg, TxtCustomerCardMsg;
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
        TxtMiniStatementMsg = (TextView) findViewById(R.id.TxtMiniStatementMsg);
        TxtCustomerCardMsg = (TextView) findViewById(R.id.TxtCustomerCardMsg);
        ProgressBankBalance = (ProgressBar) findViewById(R.id.ProgressBankBalance);
    }

    private void BankInitListeners() {
        ImgBack.setVisibility(View.VISIBLE);
        ImgShareApp.setVisibility(View.VISIBLE);
        TxtTitle.setText(new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, ""));
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
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
                    System.out.println("------ -- : " + result.toString());

                    TxtBankBalanceNumber.setText(result.getBankBalance());
                    TxtCustomerCardNumber.setText(result.getCustomerCard());
                    TxtMiniStatementNumber.setText(result.getMiniStatement());
                    if (result.getMiniStatementMsg().contains("#")) {
                        TxtMiniStatementMsg.setVisibility(View.VISIBLE);
                    } else {
                        TxtMiniStatementMsg.setVisibility(View.GONE);
                    }
                    TxtMiniStatementMsg.setText("Send SMS " + result.getMiniStatementMsg());
                    ProgressBankBalance.setVisibility(View.GONE);
                    super.onPostExecute(result);
                }
            }.execute();
        }
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
            case R.id.TxtBankBalanceNumber:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(TxtBankBalanceNumber.getText().toString()));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.TxtCustomerCardNumber:
                if (TxtMiniStatementMsg.getVisibility()==View.VISIBLE) {
                    Intent intentCustomer = new Intent(android.content.Intent.ACTION_SEND);
                    intentCustomer.setType("text/plain");
                    intentCustomer.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                    intentCustomer.putExtra(android.content.Intent.EXTRA_TEXT, TxtCustomerCardMsg.getText().toString());
                    startActivity(Intent.createChooser(intentCustomer, "Share"));
                }else {
                    Intent intentMini = new Intent(Intent.ACTION_DIAL);
                    intentMini.setData(Uri.parse(TxtCustomerCardNumber.getText().toString()));
                    intentMini.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentMini);
                }
                break;
            case R.id.TxtMiniStatementNumber:
                Intent intentMini = new Intent(Intent.ACTION_DIAL);
                intentMini.setData(Uri.parse(TxtMiniStatementNumber.getText().toString()));
                intentMini.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentMini);
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
            //e.toString();
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