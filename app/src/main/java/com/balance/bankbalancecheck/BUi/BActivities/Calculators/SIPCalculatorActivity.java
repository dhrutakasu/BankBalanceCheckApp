package com.balance.bankbalancecheck.BUi.BActivities.Calculators;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.balance.bankbalancecheck.R;
import com.karumi.dexter.BuildConfig;

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
                GotoCalCulate();
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

    private void GotoCalCulate() {

    }
}