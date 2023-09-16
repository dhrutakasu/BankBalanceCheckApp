package com.balance.bankbalancecheck.BUi.BActivities.Calculators;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.balance.bankbalancecheck.BuildConfig;
import com.balance.bankbalancecheck.R;

public class LoanAmountCalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle;
    private TextView TxtMonthlyEMI, TxtLoanAnsFirst, TxtLoanAnsSecond, TxtLoanAnsThird, TxtLoanAmountFirst, TxtLoanAmountSecond, TxtLoanAmountThird, TxtReset, TxtCalculate;
    private EditText EdtMonthlyEMI, EdtLoanYear, EdtLoanMonth, EdtLoanRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_amountcalculator);
        CalInitViews();
        CalInitListeners();
        CalInitActions();
    }

    private void CalInitViews() {
        context = this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        ImgShareApp = (ImageView) findViewById(R.id.ImgShareApp);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        TxtMonthlyEMI = (TextView) findViewById(R.id.TxtMonthlyEMI);
        TxtLoanAnsFirst = (TextView) findViewById(R.id.TxtLoanAnsFirst);
        TxtLoanAnsSecond = (TextView) findViewById(R.id.TxtLoanAnsSecond);
        TxtLoanAnsThird = (TextView) findViewById(R.id.TxtLoanAnsThird);
        TxtLoanAmountFirst = (TextView) findViewById(R.id.TxtLoanAmountFirst);
        TxtLoanAmountSecond = (TextView) findViewById(R.id.TxtLoanAmountSecond);
        TxtLoanAmountThird = (TextView) findViewById(R.id.TxtLoanAmountThird);
        TxtReset = (TextView) findViewById(R.id.TxtReset);
        TxtCalculate = (TextView) findViewById(R.id.TxtCalculate);
        EdtMonthlyEMI = (EditText) findViewById(R.id.EdtMonthlyEMI);
        EdtLoanYear = (EditText) findViewById(R.id.EdtLoanYear);
        EdtLoanMonth = (EditText) findViewById(R.id.EdtLoanMonth);
        EdtLoanRate = (EditText) findViewById(R.id.EdtLoanRate);
    }

    private void CalInitListeners() {
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
        TxtReset.setOnClickListener(this);
        TxtCalculate.setOnClickListener(this);
    }

    private void CalInitActions() {
        ImgBack.setVisibility(View.VISIBLE);
        ImgShareApp.setVisibility(View.VISIBLE);
        TxtTitle.setText(getResources().getString(R.string.loan_calculator));
        GotoReset();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ImgBack:
                finish();
                break;
            case R.id.ImgShareApp:
                GotoShare();
                break;
            case R.id.TxtReset:
                GotoReset();
                break;
            case R.id.TxtCalculate:
                GotoCalculate();
                break;
        }
    }

    private void GotoShare() {
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

    private void GotoReset() {
        EdtMonthlyEMI.setText("");
        EdtLoanYear.setText("");
        EdtLoanMonth.setText("");
        EdtLoanRate.setText("");
        TxtMonthlyEMI.setText(context.getString(R.string.loan_amount_title));
        TxtLoanAnsFirst.setText(context.getString(R.string.monthly_emi_title));
        TxtLoanAnsSecond.setText(context.getString(R.string.total_interest_title));
        TxtLoanAnsThird.setText(context.getString(R.string.total_payment_title));
        TxtLoanAmountFirst.setText(context.getString(R.string._00_0000));
        TxtLoanAmountSecond.setText(context.getString(R.string._00_0000));
        TxtLoanAmountThird.setText(context.getString(R.string._00_0000));
        EdtMonthlyEMI.setHint(context.getString(R.string.enter_amount_rs));
        EdtLoanYear.setHint(context.getString(R.string.years));
        EdtLoanMonth.setHint(context.getString(R.string.months));
        EdtLoanRate.setHint(context.getString(R.string.rate_per));
    }

    private void GotoCalculate() {
    }
}