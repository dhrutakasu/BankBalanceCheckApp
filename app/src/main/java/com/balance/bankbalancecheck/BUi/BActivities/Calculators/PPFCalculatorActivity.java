package com.balance.bankbalancecheck.BUi.BActivities.Calculators;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balance.bankbalancecheck.AdverClass;
import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BuildConfig;
import com.balance.bankbalancecheck.R;

import java.text.DecimalFormat;

public class PPFCalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle, TxtReset, TxtCalculate;
    private EditText EdtYearlyInvestment, EdtRateOfInterest, EdtYears;
    private TextView TxtPPFAmountFirst, TxtPPFAmountSecond, TxtPPFAmountThird;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppf_calculator);
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
        EdtYearlyInvestment = (EditText) findViewById(R.id.EdtYearlyInvestment);
        EdtRateOfInterest = (EditText) findViewById(R.id.EdtRateOfInterest);
        EdtYears = (EditText) findViewById(R.id.EdtYears);
        TxtPPFAmountFirst = (TextView) findViewById(R.id.TxtPPFAmountFirst);
        TxtPPFAmountSecond = (TextView) findViewById(R.id.TxtPPFAmountSecond);
        TxtPPFAmountThird = (TextView) findViewById(R.id.TxtPPFAmountThird);
    }

    private void CalInitListeners() {
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
        TxtReset.setOnClickListener(this);
        TxtCalculate.setOnClickListener(this);
    }

    private void CalInitActions() {
        AdverClass.ShowLayoutNativeBannerAds(context, ((ProgressBar) findViewById(R.id.progressBarAd)), (RelativeLayout) findViewById(R.id.RlAdver));
        ImgBack.setVisibility(View.VISIBLE);
        ImgShareApp.setVisibility(View.VISIBLE);
        TxtTitle.setText(getResources().getString(R.string.ppf_calculator));
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
        EdtYearlyInvestment.setText("");
        EdtRateOfInterest.setText("");
        EdtYears.setText("");
        TxtPPFAmountFirst.setText(getResources().getString(R.string._00_0000));
        TxtPPFAmountSecond.setText(getResources().getString(R.string._00_0000));
        TxtPPFAmountThird.setText(getResources().getString(R.string._00_0000));
    }

    private void GotoCalculate() {
        BankConstantsData.hideKeyboard(PPFCalculatorActivity.this);

        if (EdtYearlyInvestment.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter yearly investment.", Toast.LENGTH_SHORT).show();
        } else if (EdtRateOfInterest.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter rate of interest.", Toast.LENGTH_SHORT).show();
        } else if (EdtYears.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter years.", Toast.LENGTH_SHORT).show();
        } else {
            double yearlyInvestment = Double.parseDouble(EdtYearlyInvestment.getText().toString());
            double annualInterestRate = Double.parseDouble(EdtRateOfInterest.getText().toString())/100;
            int years = Integer.parseInt(EdtYears.getText().toString());

            double totalInvestment = yearlyInvestment * years;
            double totalInterest = 0;
            for (int i = 0; i < years; i++) {
                totalInterest += (totalInvestment + totalInterest) * annualInterestRate;
            }
            double maturityValue = totalInvestment + totalInterest;

            DecimalFormat decimalFormat = new DecimalFormat("#####0.00");
            StringBuilder sb = new StringBuilder();
            sb.append("₹ ");
            String monthStr = decimalFormat.format(totalInvestment);
            sb.append(monthStr);
            TxtPPFAmountFirst.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String totalInterestStr = decimalFormat.format(totalInterest);
            sb.append(totalInterestStr);
            TxtPPFAmountSecond.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String totalPaymentStr = decimalFormat.format(maturityValue);
            sb.append(totalPaymentStr);
            TxtPPFAmountThird.setText(sb.toString());
        }
    }
}