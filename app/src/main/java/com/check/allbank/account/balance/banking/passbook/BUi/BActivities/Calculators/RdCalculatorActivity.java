package com.check.allbank.account.balance.banking.passbook.BUi.BActivities.Calculators;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.check.allbank.account.balance.banking.passbook.AdverClass;
import com.check.allbank.account.balance.banking.passbook.BConstants.BankConstantsData;
import com.check.allbank.account.balance.banking.passbook.BuildConfig;
import com.check.allbank.account.balance.banking.passbook.R;

import java.text.DecimalFormat;

public class RdCalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle, TxtReset, TxtCalculate;
    private EditText EdtMonthlyInvestement, EdtRateOfInterest, EdtYears;
    private Spinner SpinnerTimePeriod;
    private TextView TxtRDAmountFirst, TxtRDAmountSecond, TxtRDAmountThird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rd_calculator);
        CalInitViews();
        CalInitListeners();
        CalInitActions();
    }

    private void CalInitViews() {
        context = this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        ImgShareApp = (ImageView) findViewById(R.id.ImgShareApp);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        TxtReset = (TextView) findViewById(R.id.TxtReset);
        TxtCalculate = (TextView) findViewById(R.id.TxtCalculate);
        EdtMonthlyInvestement = (EditText) findViewById(R.id.EdtMonthlyInvestement);
        EdtRateOfInterest = (EditText) findViewById(R.id.EdtRateOfInterest);
        EdtYears = (EditText) findViewById(R.id.EdtYears);
        SpinnerTimePeriod = (Spinner) findViewById(R.id.SpinnerTimePeriod);
        TxtRDAmountFirst = (TextView) findViewById(R.id.TxtRDAmountFirst);
        TxtRDAmountSecond = (TextView) findViewById(R.id.TxtRDAmountSecond);
        TxtRDAmountThird = (TextView) findViewById(R.id.TxtRDAmountThird);
    }

    private void CalInitListeners() {
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
        TxtReset.setOnClickListener(this);
        TxtCalculate.setOnClickListener(this);
    }

    private void CalInitActions() {
        AdverClass.ShowLayoutBannerAds(context, ((ProgressBar) findViewById(R.id.progressBarAd)), (RelativeLayout) findViewById(R.id.RlAdver));
        String[] timePeriod = {"Yearly", "Half Yearly", "Quarterly", "Monthly"};

        ImgBack.setVisibility(View.VISIBLE);
        ImgShareApp.setVisibility(View.VISIBLE);
        TxtTitle.setText(getResources().getString(R.string.rd_calculator));
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, timePeriod);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerTimePeriod.setAdapter(arrayAdapter);
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
        }
    }

    private void GotoReset() {
        EdtMonthlyInvestement.setText("");
        EdtRateOfInterest.setText("");
        EdtYears.setText("");
        SpinnerTimePeriod.setSelection(0);
        TxtRDAmountFirst.setText(getResources().getString(R.string._00_0000));
        TxtRDAmountSecond.setText(getResources().getString(R.string._00_0000));
        TxtRDAmountThird.setText(getResources().getString(R.string._00_0000));
    }

    private void GotoCalculate() {
        BankConstantsData.hideKeyboard(RdCalculatorActivity.this);

        if (EdtMonthlyInvestement.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter monthly investment.", Toast.LENGTH_SHORT).show();
        } else if (EdtRateOfInterest.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter rate of interest.", Toast.LENGTH_SHORT).show();
        } else if (EdtYears.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter years.", Toast.LENGTH_SHORT).show();
        } else {
            double monthlyInvestment = Double.parseDouble(EdtMonthlyInvestement.getText().toString());
            double annualInterestRate = Double.parseDouble(EdtRateOfInterest.getText().toString()) / 100;
            int years = Integer.parseInt(EdtYears.getText().toString());
            int timePeriodType = SpinnerTimePeriod.getSelectedItemPosition();

            int compoundingFrequency;
            switch (timePeriodType) {
                case 0:
                    compoundingFrequency = 1;
                    break;
                case 1:
                    compoundingFrequency = 2;
                    break;
                case 2:
                    compoundingFrequency = 4;
                    break;
                case 3:
                    compoundingFrequency = 12;
                    break;
                default:
                    return;
            }

            int totalPeriods = years * compoundingFrequency;
            double totalInterest = 0;
            double depositedAmount = 0;
            for (int i = 0; i < totalPeriods; i++) {
                depositedAmount += monthlyInvestment;
                totalInterest += (depositedAmount + totalInterest) * (annualInterestRate / compoundingFrequency);
            }

            double maturityAmount = depositedAmount + totalInterest;
            DecimalFormat decimalFormat = new DecimalFormat("#####0.00");

            StringBuilder sb = new StringBuilder();
            sb.append("₹ ");
            String monthStr = decimalFormat.format(totalInterest);
            sb.append(monthStr);
            TxtRDAmountFirst.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String totalInterestStr = decimalFormat.format(depositedAmount);
            sb.append(totalInterestStr);
            TxtRDAmountSecond.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String totalPaymentStr = decimalFormat.format(maturityAmount);
            sb.append(totalPaymentStr);
            TxtRDAmountThird.setText(sb.toString());
        }
    }
}