package com.balance.bankbalancecheck.BUi.BActivities.Calculators;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BuildConfig;
import com.balance.bankbalancecheck.R;

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
            //e.toString();
        }
    }

    private void GotoReset() {
        EdtMonthlyInvestement.setText("");
        EdtRateOfInterest.setText("");
        EdtYears.setText("");
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
            double fdAmount = Double.parseDouble(EdtMonthlyInvestement.getText().toString());
            double interestRate = Double.parseDouble(EdtRateOfInterest.getText().toString());
            int years = Integer.parseInt(EdtYears.getText().toString());

            double interestAmount = (fdAmount * interestRate * years) / 100;
            double totalPayment = fdAmount + interestAmount;

            DecimalFormat decimalFormat = new DecimalFormat("#####0.00");

            StringBuilder sb = new StringBuilder();
            sb.append("₹ ");
            String monthStr = decimalFormat.format(fdAmount);
            sb.append(monthStr);
            TxtRDAmountFirst.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String totalInterestStr = decimalFormat.format(interestAmount);
            sb.append(totalInterestStr);
            TxtRDAmountSecond.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String totalPaymentStr = decimalFormat.format(totalPayment);
            sb.append(totalPaymentStr);
            TxtRDAmountThird.setText(sb.toString());
        }
    }
}