package com.balance.bankbalancecheck.BUi.BActivities.Calculators;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class SIPCalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle, TxtExpectedAmount, TxtInvestmentAmount, TxtWealthGain, TxtReset, TxtCalculate;
    private EditText EdtMonthlyInvest, EdtInvestmentPeriod, EdtAnnualAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sipcalculator);
        CalInitViews();
        CalInitListeners();
        CalInitActions();
    }

    private void CalInitViews() {
        context = this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        ImgShareApp = (ImageView) findViewById(R.id.ImgShareApp);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        EdtMonthlyInvest = (EditText) findViewById(R.id.EdtMonthlyInvest);
        EdtAnnualAmount = (EditText) findViewById(R.id.EdtAnnualAmount);
        EdtInvestmentPeriod = (EditText) findViewById(R.id.EdtInvestmentPeriod);
        TxtExpectedAmount = (TextView) findViewById(R.id.TxtExpectedAmount);
        TxtInvestmentAmount = (TextView) findViewById(R.id.TxtInvestmentAmount);
        TxtWealthGain = (TextView) findViewById(R.id.TxtWealthGain);
        TxtReset = (TextView) findViewById(R.id.TxtReset);
        TxtCalculate = (TextView) findViewById(R.id.TxtCalculate);
    }

    private void CalInitListeners() {
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
        TxtReset.setOnClickListener(this);
        TxtCalculate.setOnClickListener(this);
    }

    private void CalInitActions() {
        AdverClass.ShowLayoutBannerAds(context, ((ProgressBar) findViewById(R.id.progressBarAd)), (RelativeLayout) findViewById(R.id.RlAdver));
        ImgBack.setVisibility(View.VISIBLE);
        ImgShareApp.setVisibility(View.VISIBLE);
        TxtTitle.setText(getResources().getString(R.string.sip_calculator));
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
        EdtMonthlyInvest.setText("");
        EdtAnnualAmount.setText("");
        EdtInvestmentPeriod.setText("");
        TxtExpectedAmount.setText(getResources().getString(R.string._00_0000));
        TxtWealthGain.setText(getResources().getString(R.string._00_0000));
        TxtInvestmentAmount.setText(getResources().getString(R.string._00_0000));
    }

    private void GotoCalculate() {
        String monthlyInvestmentStr = EdtMonthlyInvest.getText().toString();
        String annualReturnRateStr = EdtMonthlyInvest.getText().toString();
        String investmentPeriodStr = EdtInvestmentPeriod.getText().toString();
        BankConstantsData.hideKeyboard(SIPCalculatorActivity.this);
        if (TextUtils.isEmpty(monthlyInvestmentStr) && TextUtils.isEmpty(annualReturnRateStr) && TextUtils.isEmpty(investmentPeriodStr)) {
            Toast.makeText(context, "Please Enter Value.", Toast.LENGTH_SHORT).show();
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#####0.00");
            SipCalculator sipCalculator = new SipCalculator();
            double amountttt = sipCalculator.getTotalValue();
            StringBuilder sb = new StringBuilder();
            sb.append("₹ ");
            sb.append(decimalFormat.format(amountttt));
            TxtExpectedAmount.setText(sb.toString());

            amountttt = sipCalculator.getEstimatedReturns();
            sb = new StringBuilder();
            sb.append("₹ ");
            sb.append(decimalFormat.format(amountttt));
            TxtWealthGain.setText(sb.toString());

            amountttt = sipCalculator.getTotalInvestedAmount();
            sb = new StringBuilder();
            sb.append("₹ ");
            sb.append(decimalFormat.format(amountttt));
            TxtInvestmentAmount.setText(sb.toString());
        }
    }

    public class SipCalculator {
        private int monthlyInvestmentAmountInt = Integer.parseInt(EdtMonthlyInvest.getText().toString());
        private int expectedReturnRateInt = Integer.parseInt(EdtAnnualAmount.getText().toString());
        private int investmentTimePeriodInt = Integer.parseInt(EdtInvestmentPeriod.getText().toString()) * 12;

        public SipCalculator() {
        }

        // Total investment is  considered here according to monthly investment plans
        public long getTotalInvestedAmount() {
            return (long) (monthlyInvestmentAmountInt * investmentTimePeriodInt);
        }

        // Estimated returns = maturity value - total investment amount
        public double getEstimatedReturns() {
            return getTotalValue() - getTotalInvestedAmount();
        }

        // Calculate the maturity value according to the formula
        public double getTotalValue() {
            double futureValue = 0;
            double annualReturnRate = expectedReturnRateInt / 100.0;
            double monthlyReturnRate = annualReturnRate / 12;
            for (int i = 0; i < investmentTimePeriodInt; i++) {
                futureValue += monthlyInvestmentAmountInt;
                futureValue += futureValue * monthlyReturnRate;
            }
            return futureValue;
        }

    }
}