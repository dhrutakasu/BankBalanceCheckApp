package com.check.allbank.account.balance.banking.passbook.BUi.BActivities.Calculators;

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

import com.check.allbank.account.balance.banking.passbook.AdverClass;
import com.check.allbank.account.balance.banking.passbook.BConstants.BankConstantsData;
import com.check.allbank.account.balance.banking.passbook.BuildConfig;
import com.check.allbank.account.balance.banking.passbook.R;

import java.text.DecimalFormat;

public class GratuityCalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle, TxtReset, TxtCalculate;
    private EditText EdtMonthlySalary, EdtYearService;
    private TextView TxtGratuityAmountFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gratuity_calculator);
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
        EdtMonthlySalary = (EditText) findViewById(R.id.EdtMonthlySalary);
        EdtYearService = (EditText) findViewById(R.id.EdtYearService);
        TxtGratuityAmountFirst = (TextView) findViewById(R.id.TxtGratuityAmountFirst);
    }

    private void CalInitListeners() {
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
        TxtReset.setOnClickListener(this);
        TxtCalculate.setOnClickListener(this);
    }

    private void CalInitActions() {
        AdverClass.ShowLayoutNativeAds(context, ((ProgressBar) findViewById(R.id.progressBarAd)), (RelativeLayout) findViewById(R.id.RlAdver));
        ImgBack.setVisibility(View.VISIBLE);
        ImgShareApp.setVisibility(View.VISIBLE);
        TxtTitle.setText(getResources().getString(R.string.gratuity_calculator));
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
        EdtMonthlySalary.setText("");
        EdtYearService.setText("");
        TxtGratuityAmountFirst.setText(getResources().getString(R.string._00_0000));
    }

    private void GotoCalculate() {
        BankConstantsData.hideKeyboard(GratuityCalculatorActivity.this);

        if (EdtMonthlySalary.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter Monthly salary.", Toast.LENGTH_SHORT).show();
        } else if (EdtYearService.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter Year Of Service.", Toast.LENGTH_SHORT).show();
        } else {
            double monthlySalary = Double.parseDouble(EdtMonthlySalary.getText().toString());
            int yearsOfService = Integer.parseInt(EdtYearService.getText().toString());
            if (yearsOfService >= 100) {
                Toast.makeText(context, "Please enter valid years.", Toast.LENGTH_SHORT).show();
                return;
            }
            double gratuity = (monthlySalary * yearsOfService * 15) / 26;
            DecimalFormat decimalFormat = new DecimalFormat("#####0.00");

            StringBuilder sb = new StringBuilder();
            sb.append("₹ ");
            String monthStr = decimalFormat.format(gratuity);
            sb.append(monthStr);
            TxtGratuityAmountFirst.setText(sb.toString());
        }
    }
}