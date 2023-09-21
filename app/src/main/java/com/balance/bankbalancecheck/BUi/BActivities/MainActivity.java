package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.balance.bankbalancecheck.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private Button BtnBankDetail,BtnCal, BtnScheme, BtnCreditLoan,BtnMutualFund;

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
    }

    private void BankInitListeners() {
        BtnCal.setOnClickListener(this);
        BtnScheme.setOnClickListener(this);
        BtnCreditLoan.setOnClickListener(this);
        BtnMutualFund.setOnClickListener(this);
    }

    private void BankInitActions() {

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
}