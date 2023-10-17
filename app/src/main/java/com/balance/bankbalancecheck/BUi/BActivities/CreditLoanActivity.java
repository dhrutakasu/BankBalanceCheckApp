package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BModel.LoanModel;
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
        ArrayList<LoanModel> strings = new ArrayList<>();
        LoanModel model = new LoanModel(getString(R.string.car_loan), R.drawable.ic_car_loan);
        strings.add(model);
        model = new LoanModel(getString(R.string.home_loan), R.drawable.ic_home_loan);
        strings.add(model);
        model = new LoanModel(getString(R.string.personal_loan), R.drawable.ic_personal_loan);
        strings.add(model);
        model = new LoanModel(getString(R.string.business_loan), R.drawable.ic_bussiness_loan);
        strings.add(model);
        model = new LoanModel(getString(R.string.education_loan), R.drawable.ic_micro_loan);
        strings.add(model);
        model = new LoanModel(getString(R.string.credit_card), R.drawable.ic_credit_card);
        strings.add(model);
        RvCreditLoan.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        RvCreditLoan.setAdapter(new CalculatorsAdapter(context, strings, position -> GotoCreditLoanActivity(position, strings)));
    }

    private void GotoCreditLoanActivity(int position, ArrayList<LoanModel> strings) {
        Intent intent = new Intent(context, LoanDetailsActivity.class);
        intent.putExtra(BankConstantsData.LOAN_TYPE, strings.get(position).getName().toString());
        switch (position) {
            case 0:
                intent.putExtra(BankConstantsData.LOAN_WEB, "car loan");
                break;
            case 1:
                intent.putExtra(BankConstantsData.LOAN_WEB, "home loan");
                break;
            case 2:
                intent.putExtra(BankConstantsData.LOAN_WEB, "personal loan");
                break;
            case 3:
                intent.putExtra(BankConstantsData.LOAN_WEB, "business loan");
                break;
            case 4:
                intent.putExtra(BankConstantsData.LOAN_WEB, "education loan");
                break;
            case 5:
                intent.putExtra(BankConstantsData.LOAN_WEB, "credit loan");
                break;
        }
        startActivity(intent);
    }
}