package com.balance.bankbalancecheck.BUi.BFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.R;

public class EMIFragment extends Fragment implements View.OnClickListener {
    private static int EMI_POS;
    private View EMIView;
    private Context context;
    private TextView TxtMonthlyEMI, TxtLoanAnsFirst, TxtLoanAnsSecond, TxtLoanAnsThird, TxtLoanAmountFirst, TxtLoanAmountSecond, TxtLoanAmountThird, TxtReset, TxtCalculate;
    private EditText EdtMonthlyEMI, EdtLoanYear, EdtLoanMonth, EdtLoanRate;

    public static Fragment newInstance(int position) {
        EMIFragment myFragment = new EMIFragment();
        Bundle data = new Bundle();
        System.out.println("-- - - -  EMI_Pos " + position);
        data.putInt(BankConstantsData.EMI_Pos, position);
        myFragment.setArguments(data);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EMIView = inflater.inflate(R.layout.fragment_emi, container, false);
        EMI_POS = getArguments().getInt(BankConstantsData.EMI_Pos);

        System.out.println("-- - - -  EMI_POS " + EMI_POS);
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
        System.out.println("-- - - -  EMI_POS Reset Reset" + EMI_POS);
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
        EdtMonthlyEMI.setText("");
        EdtLoanYear.setText("");
        EdtLoanMonth.setText("");
        EdtLoanRate.setText("");
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
    }

    private void GotoCalculate() {
    }
}
