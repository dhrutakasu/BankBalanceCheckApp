package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BModel.LoanModel;
import com.balance.bankbalancecheck.BUi.BAdapters.CalculatorsAdapter;
import com.balance.bankbalancecheck.R;

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private RecyclerView RvCreditLoan, RvMutualFund;
    private ImageView IvIFSCCode, IvUSSDCode, IvNetBanking, IvBankATMBox, IvBankHoliday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        IvIFSCCode = (ImageView) findViewById(R.id.IvIFSCCode);
        IvUSSDCode = (ImageView) findViewById(R.id.IvUSSDCode);
        IvNetBanking = (ImageView) findViewById(R.id.IvNetBanking);
        IvBankHoliday = (ImageView) findViewById(R.id.IvBankHoliday);
        IvBankATMBox = (ImageView) findViewById(R.id.IvBankATMBox);
        RvCreditLoan = (RecyclerView) findViewById(R.id.RvCreditLoan);
        RvCreditLoan = (RecyclerView) findViewById(R.id.RvCreditLoan);
        RvMutualFund = (RecyclerView) findViewById(R.id.RvMutualFund);
    }

    private void BankInitListeners() {
        IvIFSCCode.setOnClickListener(this);
    }

    private void BankInitActions() {

        GetCreditLoan();
        GetMutualFund();

    }

    private void GetCreditLoan() {
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
        RvCreditLoan.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false));
        RvCreditLoan.setAdapter(new CalculatorsAdapter(context, strings, position -> GotoCreditLoanActivity(position, strings)));
    }

    private void GetMutualFund() {
        ArrayList<LoanModel> strings = new ArrayList<>();
        LoanModel model = new LoanModel(getString(R.string.mutual_funds), R.drawable.ic_mutual_fund);
        strings.add(model);
        model = new LoanModel(getString(R.string.popular_fund), R.drawable.ic_popular_fund);
        strings.add(model);
        model = new LoanModel(getString(R.string.explore_fund), R.drawable.ic_expoler_fund);
        strings.add(model);
        RvMutualFund.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false));
        RvMutualFund.setAdapter(new CalculatorsAdapter(context, strings, position -> GotoMutualFundsActivity(position, strings)));
    }

    private void GotoCreditLoanActivity(int position, ArrayList<LoanModel> strings) {
        Intent intent = new Intent(context, LoanDetailsActivity.class);
        intent.putExtra(BankConstantsData.LOAN_TYPE, strings.get(position).toString());
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

    private void GotoMutualFundsActivity(int position, ArrayList<LoanModel> strings) {
        Intent intent = new Intent(context, LoanDetailsActivity.class);
        intent.putExtra(BankConstantsData.LOAN_TYPE, strings.get(position).toString());
        switch (position) {
            case 0:
                intent.putExtra(BankConstantsData.LOAN_WEB, "mutual funds");
                break;
            case 1:
                intent.putExtra(BankConstantsData.LOAN_WEB, "popular funds");
                break;
            case 2:
                intent.putExtra(BankConstantsData.LOAN_WEB, "explore funds");
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.IvIFSCCode:
                startActivity(new Intent(context,IFSCActivity.class));
                break;
            case R.id.IvUSSDCode:
                startActivity(new Intent(context, USSDBankingActivity.class));
                break;
            case R.id.IvNetBanking:
                startActivity(new Intent(context,NetBankinActivity.class));
                break;
            case R.id.IvBankHoliday:
                break;
            case R.id.IvBankATMBox:
                startActivity(new Intent(context,NearByActivity.class));
                break;
        }
    }
}