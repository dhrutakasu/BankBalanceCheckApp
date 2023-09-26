package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BUtilsClasses.BankPreferences;
import com.balance.bankbalancecheck.R;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Context context;
    private Button BtnBankDetail, BtnCal, BtnScheme, BtnCreditLoan, BtnMutualFund;
    private AutoCompleteTextView AutoBankSearch;
    private ImageView IvBankCancel, IvBankSearch;
    private ArrayList<String> BankList = new ArrayList<>();

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

        BtnBankDetail = (Button) findViewById(R.id.BtnBankDetail);
        BtnCal = (Button) findViewById(R.id.BtnCal);
        BtnScheme = (Button) findViewById(R.id.BtnScheme);
        BtnCreditLoan = (Button) findViewById(R.id.BtnCreditLoan);
        BtnMutualFund = (Button) findViewById(R.id.BtnMutualFund);
        AutoBankSearch = (AutoCompleteTextView) findViewById(R.id.AutoBankSearch);
        IvBankCancel = (ImageView) findViewById(R.id.IvBankCancel);
        IvBankSearch = (ImageView) findViewById(R.id.IvBankSearch);
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