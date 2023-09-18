package com.balance.bankbalancecheck.BUi.BFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.EMICalculatorActivity;
import com.balance.bankbalancecheck.R;

import java.text.DecimalFormat;

public class EMIFragment extends Fragment implements View.OnClickListener {
    private int EMI_POS = EMICalculatorActivity.PosLevel;
    private View EMIView;
    private Context context;
    private TextView TxtMonthlyEMI, TxtLoanAnsFirst, TxtLoanAnsSecond, TxtLoanAnsThird, TxtLoanAmountFirst, TxtLoanAmountSecond, TxtLoanAmountThird, TxtReset, TxtCalculate;
    private EditText EdtMonthlyEMI, EdtLoanYear, EdtLoanMonth, EdtLoanRate;
    private int YearsInt, MonthsInt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EMIView = inflater.inflate(R.layout.fragment_emi, container, false);
        CalInitViews();
        CalInitListeners();
        CalInitActions();
        return EMIView;
    }

    private void CalInitViews() {
        context = getContext();
        TxtMonthlyEMI = (TextView) EMIView.findViewById(R.id.TxtMonthlyEMI);
        TxtLoanAnsFirst = (TextView) EMIView.findViewById(R.id.TxtLoanAnsFirst);
        TxtLoanAnsSecond = (TextView) EMIView.findViewById(R.id.TxtLoanAnsSecond);
        TxtLoanAnsThird = (TextView) EMIView.findViewById(R.id.TxtLoanAnsThird);
        TxtLoanAmountFirst = (TextView) EMIView.findViewById(R.id.TxtLoanAmountFirst);
        TxtLoanAmountSecond = (TextView) EMIView.findViewById(R.id.TxtLoanAmountSecond);
        TxtLoanAmountThird = (TextView) EMIView.findViewById(R.id.TxtLoanAmountThird);
        TxtReset = (TextView) EMIView.findViewById(R.id.TxtReset);
        TxtCalculate = (TextView) EMIView.findViewById(R.id.TxtCalculate);
        EdtMonthlyEMI = (EditText) EMIView.findViewById(R.id.EdtMonthlyEMI);
        EdtLoanYear = (EditText) EMIView.findViewById(R.id.EdtLoanYear);
        EdtLoanMonth = (EditText) EMIView.findViewById(R.id.EdtLoanMonth);
        EdtLoanRate = (EditText) EMIView.findViewById(R.id.EdtLoanRate);
    }

    private void CalInitListeners() {
        TxtReset.setOnClickListener(this);
        TxtCalculate.setOnClickListener(this);
    }

    private void CalInitActions() {
        GotoReset();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TxtReset:
                GotoReset();
                break;
            case R.id.TxtCalculate:
                GotoCalculate();
                break;
        }
    }

    private void GotoReset() {
        System.out.println("-- - - -  EMI_POSReset " + EMI_POS);
        if (EMI_POS == 0) {
            TxtMonthlyEMI.setText(context.getString(R.string.monthly_emi_title));
            TxtLoanAnsFirst.setText(context.getString(R.string.loan_amount_title));
            TxtLoanAnsSecond.setText(context.getString(R.string.total_interest_title));
            TxtLoanAnsThird.setText(context.getString(R.string.total_payment_title));
            TxtLoanAmountFirst.setText(context.getString(R.string._00_0000));
            TxtLoanAmountSecond.setText(context.getString(R.string._00_0000));
            TxtLoanAmountThird.setText(context.getString(R.string._00_0000));
            EdtMonthlyEMI.setHint(context.getString(R.string.emi));
            EdtLoanYear.setHint(context.getString(R.string.years));
            EdtLoanMonth.setHint(context.getString(R.string.months));
            EdtLoanRate.setHint(context.getString(R.string.rate_per));
        } else if (EMI_POS == 1 || EMI_POS == 2 || EMI_POS == 3) {
            TxtMonthlyEMI.setText(context.getString(R.string.loan_amount_title));
            TxtLoanAnsFirst.setText(context.getString(R.string.monthly_emi_title));
            TxtLoanAnsSecond.setText(context.getString(R.string.total_interest_title));
            TxtLoanAnsThird.setText(context.getString(R.string.total_payment_title));
            TxtLoanAmountFirst.setText(context.getString(R.string._00_0000));
            TxtLoanAmountSecond.setText(context.getString(R.string._00_0000));
            TxtLoanAmountThird.setText(context.getString(R.string._00_0000));
            EdtMonthlyEMI.setHint(context.getString(R.string.enter_amount_rs));
            EdtLoanYear.setHint(context.getString(R.string.years));
            EdtLoanMonth.setHint(context.getString(R.string.months));
            EdtLoanRate.setHint(context.getString(R.string.rate_per));
        }
        EdtMonthlyEMI.setText("");
        EdtLoanYear.setText("");
        EdtLoanMonth.setText("");
        EdtLoanRate.setText("");
    }

    private void GotoCalculate() {
        System.out.println("-- - - -  EMI_POS " + EMI_POS);
        BankConstantsData.hideKeyboard(getActivity());
        DecimalFormat decimalFormat = new DecimalFormat("#####0.00");
        if (EMI_POS == 0) {
            if (EdtMonthlyEMI.getText().toString().isEmpty()) {
                Toast.makeText(context, "Please enter monthly EMI.", Toast.LENGTH_SHORT).show();
            } else if (EdtLoanYear.getText().toString().isEmpty() && EdtLoanMonth.getText().toString().isEmpty()) {
                Toast.makeText(context, "Please enter period.", Toast.LENGTH_SHORT).show();
            } else if (EdtLoanRate.getText().toString().isEmpty()) {
                Toast.makeText(context, "Please enter rate.", Toast.LENGTH_SHORT).show();
            } else {
                if (EdtLoanYear.getText().toString().isEmpty()) {
                    YearsInt = 0;
                } else {
                    YearsInt = Integer.parseInt(EdtLoanYear.getText().toString());
                }
                if (EdtLoanMonth.getText().toString().isEmpty()) {
                    MonthsInt = 0;
                } else {
                    MonthsInt = Integer.parseInt(EdtLoanMonth.getText().toString());
                }
                double monthlyEMI = Double.parseDouble(EdtMonthlyEMI.getText().toString());
                double annualInterestRate = Double.parseDouble(EdtLoanRate.getText().toString());

                int totalMonths = YearsInt * 12 + MonthsInt;

                double monthlyInterestRate = (annualInterestRate / 12) / 100;
                double loanAmount = calculateLoanAmount(monthlyEMI, monthlyInterestRate, totalMonths);
                double totalInterest = (monthlyEMI * totalMonths) - loanAmount;
                double totalPayment = monthlyEMI * totalMonths;
                StringBuilder sb = new StringBuilder();
                sb.append("₹ ");
                String monthStr = decimalFormat.format(loanAmount);
                sb.append(monthStr);
                TxtLoanAmountFirst.setText(sb.toString());
                sb = new StringBuilder();
                sb.append("₹ ");
                String totalInterestStr = decimalFormat.format(totalInterest);
                sb.append(totalInterestStr);
                TxtLoanAmountSecond.setText(sb.toString());
                sb = new StringBuilder();
                sb.append("₹ ");
                String totalPaymentStr = decimalFormat.format(totalPayment);
                sb.append(totalPaymentStr);
                TxtLoanAmountThird.setText(sb.toString());
            }
        } else if (EMI_POS == 1 || EMI_POS == 2 || EMI_POS == 3) {
            if (EdtMonthlyEMI.getText().toString().isEmpty()) {
                Toast.makeText(context, "Please enter loan amount.", Toast.LENGTH_SHORT).show();
            } else if (EdtLoanYear.getText().toString().isEmpty() && EdtLoanMonth.getText().toString().isEmpty()) {
                Toast.makeText(context, "Please enter period.", Toast.LENGTH_SHORT).show();
            } else if (EdtLoanRate.getText().toString().isEmpty()) {
                Toast.makeText(context, "Please enter rate.", Toast.LENGTH_SHORT).show();
            } else {
                if (EdtLoanYear.getText().toString().isEmpty()) {
                    YearsInt = 0;
                } else {
                    YearsInt = Integer.parseInt(EdtLoanYear.getText().toString());
                }
                if (EdtLoanMonth.getText().toString().isEmpty()) {
                    MonthsInt = 0;
                } else {
                    MonthsInt = Integer.parseInt(EdtLoanMonth.getText().toString());
                }
                double loanAmount = Double.parseDouble(EdtMonthlyEMI.getText().toString());
                double interestRate = Double.parseDouble(EdtLoanRate.getText().toString());

                double monthlyInterestRate = interestRate / 12 / 100;
                int totalMonths = YearsInt * 12 + MonthsInt;
                double monthlyEMI = calculateMonthlyEMI(loanAmount, monthlyInterestRate, totalMonths);
                double totalInterest = monthlyEMI * totalMonths - loanAmount;
                double totalPayment = monthlyEMI * totalMonths;
                StringBuilder sb = new StringBuilder();
                sb.append("₹ ");
                String monthStr = decimalFormat.format(monthlyEMI);
                sb.append(monthStr);
                TxtLoanAmountFirst.setText(sb.toString());
                sb = new StringBuilder();
                sb.append("₹ ");
                String totalInterestStr = decimalFormat.format(totalInterest);
                sb.append(totalInterestStr);
                TxtLoanAmountSecond.setText(sb.toString());
                sb = new StringBuilder();
                sb.append("₹ ");
                String totalPaymentStr = decimalFormat.format(totalPayment);
                sb.append(totalPaymentStr);
                TxtLoanAmountThird.setText(sb.toString());
            }
        }
    }

    public static double calculateLoanAmount(double monthlyEMI, double monthlyInterestRate, int totalMonths) {
        double loanAmount;
        loanAmount = (monthlyEMI * (Math.pow(1 + monthlyInterestRate, totalMonths) - 1)) / (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, totalMonths));
        return loanAmount;
    }

    public static double calculateMonthlyEMI(double loanAmount, double monthlyInterestRate, int totalMonths) {
        double emi = (loanAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, totalMonths)) / (Math.pow(1 + monthlyInterestRate, totalMonths) - 1);
        return emi;
    }
}
