package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.balance.bankbalancecheck.BUi.BAdapters.CalculatorsAdapter;
import com.balance.bankbalancecheck.R;

import java.util.ArrayList;

public class CreditLoanActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView RvCreditLoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_loan);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        RvCreditLoan = (RecyclerView) findViewById(R.id.RvCreditLoan);
    }

    private void BankInitListeners() {

    }

    private void BankInitActions() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(getString(R.string.car_loan));
        strings.add(getString(R.string.home_loan));
        strings.add(getString(R.string.personal_loan));
        strings.add(getString(R.string.business_loan));
        strings.add(getString(R.string.micro_loan));
        strings.add(getString(R.string.credit_card));
        RvCreditLoan.setLayoutManager(new GridLayoutManager(context, 2));
        RvCreditLoan.setAdapter(new CalculatorsAdapter(context, strings, position -> GotoCreditLoanActivity(position)));
    }

    private void GotoCreditLoanActivity(int position) {
     switch (position){
         case 0:
             break;
         case 1:
             break;
         case 2:
             break;
         case 3:
             break;
         case 4:
             break;
         case 5:
             break;
     }
    }
}