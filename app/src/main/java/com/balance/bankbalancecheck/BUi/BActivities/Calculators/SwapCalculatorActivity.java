package com.balance.bankbalancecheck.BUi.BActivities.Calculators;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BuildConfig;
import com.balance.bankbalancecheck.R;

import java.text.DecimalFormat;

public class SwapCalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle, TxtReset, TxtCalculate;
    private EditText EdtTotalInvestment, EdtWithdrawalMonth, EdtReturnRate, EdtTimePeriod;
    private TextView TxtSwapAmountFirst, TxtSwapAmountSecond, TxtSwapAmountThird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_calculator);
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
        EdtTotalInvestment = (EditText) findViewById(R.id.EdtTotalInvestment);
        EdtWithdrawalMonth = (EditText) findViewById(R.id.EdtWithdrawalMonth);
        EdtReturnRate = (EditText) findViewById(R.id.EdtReturnRate);
        EdtTimePeriod = (EditText) findViewById(R.id.EdtTimePeriod);
        TxtSwapAmountFirst = (TextView) findViewById(R.id.TxtSwapAmountFirst);
        TxtSwapAmountSecond = (TextView) findViewById(R.id.TxtSwapAmountSecond);
        TxtSwapAmountThird = (TextView) findViewById(R.id.TxtSwapAmountThird);
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
        TxtTitle.setText(getResources().getString(R.string.swp_calculator));
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
        EdtTotalInvestment.setText("");
        EdtWithdrawalMonth.setText("");
        EdtReturnRate.setText("");
        EdtTimePeriod.setText("");
        TxtSwapAmountFirst.setText(getResources().getString(R.string._00_0000));
        TxtSwapAmountSecond.setText(getResources().getString(R.string._00_0000));
        TxtSwapAmountThird.setText(getResources().getString(R.string._00_0000));
    }

    private void GotoCalculate() {
        BankConstantsData.hideKeyboard(SwapCalculatorActivity.this);

        if (EdtTotalInvestment.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter total investment amount.", Toast.LENGTH_SHORT).show();
        } else if (EdtWithdrawalMonth.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter withdrawal per month amount.", Toast.LENGTH_SHORT).show();
        } else if (EdtReturnRate.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter rate.", Toast.LENGTH_SHORT).show();
        } else if (EdtTimePeriod.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter years.", Toast.LENGTH_SHORT).show();
        } else {
            double totalInvestment = Double.parseDouble(EdtTotalInvestment.getText().toString());
            double withdrawalAmount = Double.parseDouble(EdtWithdrawalMonth.getText().toString());
            double annualReturnRatePercentage = Double.parseDouble(EdtReturnRate.getText().toString());
            double annualReturnRate = annualReturnRatePercentage / 100.0;
            int years = Integer.parseInt(EdtTimePeriod.getText().toString());

            double monthlyReturnRate = annualReturnRate / 12;
            int months = years * 12;
            double finalValue = totalInvestment;

            for (int i = 0; i < months; i++) {
                finalValue = finalValue * (1 + monthlyReturnRate) - withdrawalAmount;
            }

            double totalWithdrawal = withdrawalAmount * months;

            DecimalFormat decimalFormat = new DecimalFormat("#####0.00");

            StringBuilder sb = new StringBuilder();
            sb.append("₹ ");
            String monthStr = decimalFormat.format(totalInvestment);
            sb.append(monthStr);
            TxtSwapAmountFirst.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String totalInterestStr = decimalFormat.format(totalWithdrawal);
            sb.append(totalInterestStr);
            TxtSwapAmountSecond.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String totalPaymentStr = decimalFormat.format(finalValue);
            sb.append(totalPaymentStr);
            TxtSwapAmountThird.setText(sb.toString());
        }
    }
}