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
        AdverClass.ShowLayoutBannerAds(context, ((ProgressBar) findViewById(R.id.progressBarAd)), (RelativeLayout) findViewById(R.id.RlAdver));
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
            double currentEPFBalance = Double.parseDouble(EdtCurrentEPF.getText().toString()); // Rs
            double basicSalaryMonthly = Double.parseDouble(EdtBasicSalary.getText().toString()); // Rs
            double employerContributionEPF = Double.parseDouble(EdtEmployerEPF.getText().toString()); // %
            double yourContributionEPF = Double.parseDouble(EdtContribution.getText().toString()); // %
            double annualIncreaseInSalary = Double.parseDouble(EdtIncreaseSalary.getText().toString()); // %
            int ageWhenYouIntendToRetire = Integer.parseInt(EdtAgeRetire.getText().toString()); // years
            int yourAge = Integer.parseInt(EdtAge.getText().toString()); // years
            double currentEPFInterestRate = Double.parseDouble(EdtCurrentInterest.getText().toString()); // %
            double totalPFBalances=calculateTotalPFContribution(currentEPFBalance,basicSalaryMonthly,employerContributionEPF,yourContributionEPF,annualIncreaseInSalary,ageWhenYouIntendToRetire,yourAge,currentEPFInterestRate);

            DecimalFormat decimalFormat = new DecimalFormat("#####0.00");

            StringBuilder sb = new StringBuilder();
            sb.append("₹ ");
            String monthStr = decimalFormat.format(totalPFBalances);
            sb.append(monthStr);
            TxtEPFAmountFirst.setText(sb.toString());
        }
    }
    public static double calculateTotalPFContribution(double currentEPFBalance, double basicSalaryMonthly, double employerContributionPercentage, double employeeContributionPercentage, double annualIncreaseInSalary, int retireAge, int currentAge, double currentEPFInterestRate) {
        double totalPFContribution = 0;
        int yearsUntilRetirement = retireAge - currentAge;
        for (int i = 0; i < yearsUntilRetirement; i++) {
            double annualContribution = (basicSalaryMonthly * employeeContributionPercentage / 100) + (basicSalaryMonthly * employerContributionPercentage / 100);
            currentEPFBalance += annualContribution;
            double interestEarned = currentEPFBalance * (currentEPFInterestRate / 100);
            currentEPFBalance += interestEarned;
            basicSalaryMonthly += (basicSalaryMonthly * annualIncreaseInSalary / 100);
        }
        totalPFContribution = currentEPFBalance;
        return totalPFContribution;
    }
}