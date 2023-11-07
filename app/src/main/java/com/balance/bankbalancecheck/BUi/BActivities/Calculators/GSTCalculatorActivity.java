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

public class GSTCalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle, TxtReset, TxtCalculate;
    private EditText EdtInitialAmount, EdtGstRate;
    private TextView TxtGstAmountZero, TxtGstAmountFirst, TxtGstAmountSecond, TxtGstAmountThird, TxtGstAmountFourth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gstcalculator);
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
        EdtInitialAmount = (EditText) findViewById(R.id.EdtInitialAmount);
        EdtGstRate = (EditText) findViewById(R.id.EdtGstRate);
        TxtGstAmountZero = (TextView) findViewById(R.id.TxtGstAmountZero);
        TxtGstAmountFirst = (TextView) findViewById(R.id.TxtGstAmountFirst);
        TxtGstAmountSecond = (TextView) findViewById(R.id.TxtGstAmountSecond);
        TxtGstAmountThird = (TextView) findViewById(R.id.TxtGstAmountThird);
        TxtGstAmountFourth = (TextView) findViewById(R.id.TxtGstAmountFourth);
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
        TxtTitle.setText(getResources().getString(R.string.gst_calculator));
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
        EdtInitialAmount.setText("");
        EdtGstRate.setText("");
        TxtGstAmountZero.setText(getResources().getString(R.string._00_0000));
        TxtGstAmountFirst.setText(getResources().getString(R.string._00_0000));
        TxtGstAmountSecond.setText(getResources().getString(R.string._00_0000));
        TxtGstAmountThird.setText(getResources().getString(R.string._00_0000));
        TxtGstAmountFourth.setText(getResources().getString(R.string._00_0000));
    }

    private void GotoCalculate() {
        BankConstantsData.hideKeyboard(GSTCalculatorActivity.this);
        if (EdtInitialAmount.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter initial amount.", Toast.LENGTH_SHORT).show();
        }  else if (EdtGstRate.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter gst rate.", Toast.LENGTH_SHORT).show();
        } else {
            double initialAmount = Double.parseDouble(EdtInitialAmount.getText().toString());
            double gstRate = Double.parseDouble(EdtGstRate.getText().toString());

            DecimalFormat decimalFormat = new DecimalFormat("#####0.00");
            double gstAmount = (initialAmount * gstRate) / 100;
            double cgst = gstAmount / 2;
            double sgst = gstAmount / 2;
            double totalPayment = initialAmount + gstAmount;

            StringBuilder sb = new StringBuilder();
            sb.append("₹ ");
            String AmountStr = decimalFormat.format(initialAmount);
            sb.append(AmountStr);
            TxtGstAmountZero.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String GSTStr = decimalFormat.format(gstAmount);
            sb.append(GSTStr);
            TxtGstAmountFirst.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String CGSTStr = decimalFormat.format(cgst);
            sb.append(CGSTStr);
            TxtGstAmountSecond.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String SGSTStr = decimalFormat.format(sgst);
            sb.append(SGSTStr);
            TxtGstAmountThird.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String totalPaymentStr = decimalFormat.format(totalPayment);
            sb.append(totalPaymentStr);
            TxtGstAmountFourth.setText(sb.toString());
        }
    }
}