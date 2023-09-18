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

public class EPFCalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle, TxtReset, TxtCalculate;
    private EditText EdtCurrentEPF, EdtBasicSalary, EdtEmployerEPF,EdtContribution,EdtIncreaseSalary,EdtAgeRetire,EdtAge,EdtCurrentInterest;
    private TextView TxtEPFAmountFirst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epf_calculator);
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
        EdtCurrentEPF = (EditText) findViewById(R.id.EdtCurrentEPF);
        EdtBasicSalary = (EditText) findViewById(R.id.EdtBasicSalary);
        EdtEmployerEPF = (EditText) findViewById(R.id.EdtEmployerEPF);
        EdtContribution = (EditText) findViewById(R.id.EdtContribution);
        EdtIncreaseSalary = (EditText) findViewById(R.id.EdtIncreaseSalary);
        EdtAgeRetire = (EditText) findViewById(R.id.EdtAgeRetire);
        EdtAge = (EditText) findViewById(R.id.EdtAge);
        EdtCurrentInterest = (EditText) findViewById(R.id.EdtCurrentInterest);
        TxtEPFAmountFirst = (TextView) findViewById(R.id.TxtEPFAmountFirst);
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
        TxtTitle.setText(getResources().getString(R.string.epf_calculator));
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
        EdtCurrentEPF.setText("");
        EdtBasicSalary.setText("");
        EdtEmployerEPF.setText("");
        EdtAge.setText("");
        EdtContribution.setText("");
        EdtAgeRetire.setText("");
        EdtCurrentInterest.setText("");
        EdtIncreaseSalary.setText("");
        TxtEPFAmountFirst.setText(getResources().getString(R.string._00_0000));
    }

    private void GotoCalculate() {
        BankConstantsData.hideKeyboard(EPFCalculatorActivity.this);

        if (EdtCurrentEPF.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter current pf.", Toast.LENGTH_SHORT).show();
        } else if (EdtBasicSalary.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter basic pf.", Toast.LENGTH_SHORT).show();
        } else if (EdtEmployerEPF.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter employee contribution.", Toast.LENGTH_SHORT).show();
        } else if (EdtCurrentInterest.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter years.", Toast.LENGTH_SHORT).show();
        } else if (EdtAgeRetire.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter retire age.", Toast.LENGTH_SHORT).show();
        } else if (EdtAge.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter your age.", Toast.LENGTH_SHORT).show();
        } else if (EdtContribution.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter your contribution.", Toast.LENGTH_SHORT).show();
        } else if (EdtIncreaseSalary.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter increase salary.", Toast.LENGTH_SHORT).show();
        } else {
            double fdAmount = Double.parseDouble(EdtCurrentEPF.getText().toString());
            double interestRate = Double.parseDouble(EdtBasicSalary.getText().toString());
            int years = Integer.parseInt(EdtEmployerEPF.getText().toString());

            double interestAmount = (fdAmount * interestRate * years) / 100;
            double totalPayment = fdAmount + interestAmount;

            DecimalFormat decimalFormat = new DecimalFormat("#####0.00");

            StringBuilder sb = new StringBuilder();
            sb.append("₹ ");
            String monthStr = decimalFormat.format(fdAmount);
            sb.append(monthStr);
            TxtEPFAmountFirst.setText(sb.toString());
        }
    }
}