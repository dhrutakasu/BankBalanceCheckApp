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

public class ReturnCalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle, TxtReset, TxtCalculate;
    private EditText EdtTotalInvestment, EdtRateOfInterest, EdtYears;
    private TextView TxtReturnAmountFirst, TxtReturnAmountSecond, TxtReturnAmountThird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_calculator);
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
        EdtRateOfInterest = (EditText) findViewById(R.id.EdtRateOfInterest);
        EdtYears = (EditText) findViewById(R.id.EdtYears);
        TxtReturnAmountFirst = (TextView) findViewById(R.id.TxtReturnAmountFirst);
        TxtReturnAmountSecond = (TextView) findViewById(R.id.TxtReturnAmountSecond);
        TxtReturnAmountThird = (TextView) findViewById(R.id.TxtReturnAmountThird);
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
        TxtTitle.setText(getResources().getString(R.string.Return_calculator));
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
        EdtRateOfInterest.setText("");
        EdtYears.setText("");
        TxtReturnAmountFirst.setText(getResources().getString(R.string._00_0000));
        TxtReturnAmountSecond.setText(getResources().getString(R.string._00_0000));
        TxtReturnAmountThird.setText(getResources().getString(R.string._00_0000));
    }

    private void GotoCalculate() {
        BankConstantsData.hideKeyboard(ReturnCalculatorActivity.this);

        if (EdtTotalInvestment.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter total investment.", Toast.LENGTH_SHORT).show();
        } else if (EdtRateOfInterest.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter expected rate.", Toast.LENGTH_SHORT).show();
        } else if (EdtYears.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter years.", Toast.LENGTH_SHORT).show();
        } else {
            double parseDouble = Double.parseDouble(EdtTotalInvestment.getText().toString());
            double pow = Math.pow((Double.parseDouble(EdtRateOfInterest.getText().toString()) / 100.0d) + 1.0d, Double.parseDouble(EdtYears.getText().toString())) * parseDouble;
            double d9 = pow - parseDouble;
            ;
            DecimalFormat decimalFormat = new DecimalFormat("#####0.00");
            StringBuilder sb = new StringBuilder();
            sb.append("₹ ");
            String monthStr = decimalFormat.format(parseDouble);
            sb.append(monthStr);
            TxtReturnAmountFirst.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String totalInterestStr = decimalFormat.format(d9);
            sb.append(totalInterestStr);
            TxtReturnAmountSecond.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String totalPaymentStr = decimalFormat.format(pow);
            sb.append(totalPaymentStr);
            TxtReturnAmountThird.setText(sb.toString());
        }
    }
}