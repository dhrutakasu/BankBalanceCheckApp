package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.balance.bankbalancecheck.BUi.BActivities.Calculators.BrokerageCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.EMICalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.EPFCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.FDCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.GSTCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.GratuityCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.InflationCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.LoanAmountCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.PPFCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.RdCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.SIPCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.SwapCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BAdapters.BankingAdapter;
import com.balance.bankbalancecheck.BUi.BAdapters.CalculatorsAdapter;
import com.balance.bankbalancecheck.R;

import java.util.ArrayList;

public class CalculatorsActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView RvCalculators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculators);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        RvCalculators = (RecyclerView) findViewById(R.id.RvCalculators);
    }

    private void BankInitListeners() {

    }

    private void BankInitActions() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(getString(R.string.sip_calculator));
        strings.add(getString(R.string.emi_calculator));
        strings.add(getString(R.string.loan_calculator));
        strings.add(getString(R.string.gst_calculator));
        strings.add(getString(R.string.fd_calculator));
        strings.add(getString(R.string.brokerage_calculator));
        strings.add(getString(R.string.swp_calculator));
        strings.add(getString(R.string.rd_calculator));
        strings.add(getString(R.string.ppf_calculator));
        strings.add(getString(R.string.epf_calculator));
        strings.add(getString(R.string.inflation_calculator));
        strings.add(getString(R.string.gratuity_calculator));
        RvCalculators.setLayoutManager(new GridLayoutManager(context, 2));
        RvCalculators.setAdapter(new BankingAdapter(context, strings, new BankingAdapter.setClickListener() {
            @Override
            public void CalculatorsClickListener(int position) {
                GotoCalculatorActivity(position);
            }
        }));
    }

    private void GotoCalculatorActivity(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(context, SIPCalculatorActivity.class));
                break;
            case 1:
                startActivity(new Intent(context, EMICalculatorActivity.class));
                break;
            case 2:
                startActivity(new Intent(context, LoanAmountCalculatorActivity.class));
                break;
            case 3:
                startActivity(new Intent(context, GSTCalculatorActivity.class));
                break;
            case 4:
                startActivity(new Intent(context, FDCalculatorActivity.class));
                break;
            case 5:
                startActivity(new Intent(context, BrokerageCalculatorActivity.class));
                break;
            case 6:
                startActivity(new Intent(context, SwapCalculatorActivity.class));
                break;
            case 7:
                startActivity(new Intent(context, RdCalculatorActivity.class));
                break;
            case 8:
                startActivity(new Intent(context, PPFCalculatorActivity.class));
                break;
            case 9:
                startActivity(new Intent(context, EPFCalculatorActivity.class));
                break;
            case 10:
                startActivity(new Intent(context, InflationCalculatorActivity.class));
                break;
            case 11:
                startActivity(new Intent(context, GratuityCalculatorActivity.class));
                break;
        }
    }
}