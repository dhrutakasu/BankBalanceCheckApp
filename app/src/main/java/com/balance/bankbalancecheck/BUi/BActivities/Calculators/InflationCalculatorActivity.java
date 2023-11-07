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

public class InflationCalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle, TxtReset, TxtCalculate;
    private EditText EdtAmount, EdtInitialYear, EdtFinalYears;
    private TextView TxtAnsResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inflation_calculator);
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
        EdtAmount = (EditText) findViewById(R.id.EdtAmount);
        EdtInitialYear = (EditText) findViewById(R.id.EdtInitialYear);
        EdtFinalYears = (EditText) findViewById(R.id.EdtFinalYears);
        TxtAnsResult = (TextView) findViewById(R.id.TxtAnsResult);
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
        TxtTitle.setText(getResources().getString(R.string.nps_calculator));
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
        EdtAmount.setText("");
        EdtInitialYear.setText("");
        EdtFinalYears.setText("");
        TxtAnsResult.setText(getResources().getString(R.string._00_0000));
    }

    private void GotoCalculate() {
        BankConstantsData.hideKeyboard(InflationCalculatorActivity.this);

        if (EdtAmount.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter amount.", Toast.LENGTH_SHORT).show();
        } else if (EdtInitialYear.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter initial year.", Toast.LENGTH_SHORT).show();
        } else if (EdtFinalYears.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter final year.", Toast.LENGTH_SHORT).show();
        } else {
            double initialAmount = Double.parseDouble(EdtAmount.getText().toString());
            int initialYear = Integer.parseInt(EdtInitialYear.getText().toString());
            int finalYear = Integer.parseInt(EdtFinalYears.getText().toString());

            if (finalYear <= initialYear) {
                System.out.println("Final year must be greater than the initial year.");
                return;
            }
            if (initialYear < 1997 || initialYear > 2016 || finalYear < 1998 || finalYear > 2017) {
                System.out.println("Invalid year range. Please enter years between 1997-2016 for initial year and 1998-2017 for final year.");
                return;
            }
            double[] inflationRates = {
                    0.045, 0.038, 0.055, 0.048, 0.042, 0.036, 0.034, 0.039, 0.047, 0.052,
                    0.048, 0.043, 0.037, 0.035, 0.042, 0.049, 0.056, 0.051, 0.044, 0.041
            };

            double adjustedAmount = calculateInflationAdjustedAmount(initialAmount, initialYear, finalYear, inflationRates);
            DecimalFormat decimalFormat = new DecimalFormat("#####0.00");

            StringBuilder sb = new StringBuilder();
            sb.append("₹ ");
            String monthStr = decimalFormat.format(adjustedAmount);
            sb.append(monthStr);
            TxtAnsResult.setText(sb.toString());
        }
    }
    public static double calculateInflationAdjustedAmount(double initialAmount, int initialYear, int finalYear, double[] inflationRates) {
        double adjustedAmount = initialAmount;
        for (int year = initialYear; year <= finalYear; year++) {
            if (year - initialYear < inflationRates.length) {
                adjustedAmount *= (1 + inflationRates[year - initialYear]);
            }
        }
        return adjustedAmount;
    }
}