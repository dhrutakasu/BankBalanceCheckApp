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

public class FDCalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle,TxtReset,TxtCalculate;
    private EditText EdtFixedDepositAmount,EdtRateInterest,EdtHowSave;
    private TextView TxtFDAmountFirst,TxtFDAmountSecond,TxtFDAmountThird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fdcalculator);
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
        EdtFixedDepositAmount = (EditText) findViewById(R.id.EdtFixedDepositAmount);
        EdtRateInterest = (EditText) findViewById(R.id.EdtRateInterest);
        EdtHowSave = (EditText) findViewById(R.id.EdtHowSave);
        TxtFDAmountFirst = (TextView) findViewById(R.id.TxtFDAmountFirst);
        TxtFDAmountSecond = (TextView) findViewById(R.id.TxtFDAmountSecond);
        TxtFDAmountThird = (TextView) findViewById(R.id.TxtFDAmountThird);
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
        TxtTitle.setText(getResources().getString(R.string.fd_calculator));
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
        EdtFixedDepositAmount.setText("");
        EdtRateInterest.setText("");
        EdtHowSave.setText("");
        TxtFDAmountFirst.setText(getResources().getString(R.string._00_0000));
        TxtFDAmountSecond.setText(getResources().getString(R.string._00_0000));
        TxtFDAmountThird.setText(getResources().getString(R.string._00_0000));
    }

    private void GotoCalculate() {
        BankConstantsData.hideKeyboard(FDCalculatorActivity.this);

        if (EdtFixedDepositAmount.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter fixed deposit amount.", Toast.LENGTH_SHORT).show();
        }  else if (EdtRateInterest.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter rate of interest.", Toast.LENGTH_SHORT).show();
        } else if (EdtHowSave.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter years.", Toast.LENGTH_SHORT).show();
        } else {
            double fdAmount = Double.parseDouble(EdtFixedDepositAmount.getText().toString());
            double interestRate = Double.parseDouble(EdtRateInterest.getText().toString());
            int years =Integer.parseInt(EdtHowSave.getText().toString());

            double interestAmount = (fdAmount * interestRate * years) / 100;
            double totalPayment = fdAmount + interestAmount;
            
            DecimalFormat decimalFormat = new DecimalFormat("#####0.00");
            
            StringBuilder sb = new StringBuilder();
            sb.append("₹ ");
            String monthStr = decimalFormat.format(fdAmount);
            sb.append(monthStr);
            TxtFDAmountFirst.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String totalInterestStr = decimalFormat.format(interestAmount);
            sb.append(totalInterestStr);
            TxtFDAmountSecond.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String totalPaymentStr = decimalFormat.format(totalPayment);
            sb.append(totalPaymentStr);
            TxtFDAmountThird.setText(sb.toString());
        }
    }
}