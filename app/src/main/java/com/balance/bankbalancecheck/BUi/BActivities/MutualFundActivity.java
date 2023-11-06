package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BUi.BAdapters.MutualFundAdapter;
import com.balance.bankbalancecheck.R;

public class MutualFundActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView ImgBack;
    private TextView TxtTitle;
    private RecyclerView RvMutualFundCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutual_fund);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        RvMutualFundCat = (RecyclerView) findViewById(R.id.RvMutualFundCat);
    }

    private void BankInitListeners() {
        ImgBack.setOnClickListener(this);
    }

    private void BankInitActions() {
        ImgBack.setVisibility(View.VISIBLE);
        TxtTitle.setText(getString(R.string.mutual_fund));

        String[] strings;
        strings = getResources().getStringArray(R.array.Mutual_fund_type);
        RvMutualFundCat.setLayoutManager(new GridLayoutManager(context, 1));
        RvMutualFundCat.setAdapter(new MutualFundAdapter(context, strings, (strings1, position) -> GotoMutualFundActivity(strings1,position)));
    }

    private void GotoMutualFundActivity(String[] strings, int position) {
        Intent intent = new Intent(context, FundLoanDetailsActivity.class);
        intent.putExtra(BankConstantsData.LOAN_TYPE, strings[position].toString());
        switch (position) {
            case 0:
                intent.putExtra(BankConstantsData.LOAN_WEB, "https://www.paytmmoney.com/mutual-funds/balanced-funds");
                break;
            case 1:
                intent.putExtra(BankConstantsData.LOAN_WEB, "https://www.paytmmoney.com/mutual-funds/investment-ideas/better-than-fixed-deposit/26916795-d233-47c4-b806-eef82bf500b5");
                break;
            case 2:
                intent.putExtra(BankConstantsData.LOAN_WEB, "https://www.paytmmoney.com/mutual-funds/best-return-funds");
                break;
            case 3:
                intent.putExtra(BankConstantsData.LOAN_WEB, "https://www.paytmmoney.com/mutual-funds/investment-ideas/index-funds/3b47e748-ee39-430c-9df1-d8fdb4077719");
                break;
            case 4:
                intent.putExtra(BankConstantsData.LOAN_WEB, "https://www.paytmmoney.com/mutual-funds/top-rated-funds");
                break;
            case 5:
                intent.putExtra(BankConstantsData.LOAN_WEB, "https://www.paytmmoney.com/mutual-funds/investment-ideas/invest-to-save-tax/f769eed4-fba6-40f6-a2c3-1096ecd53df8");
                break;
            case 6:
                intent.putExtra(BankConstantsData.LOAN_WEB, "https://www.paytmmoney.com/mutual-funds/investment-ideas/invest-in-large-companies/dbd8a28f-1bef-4b9c-b9d0-8eaf9c28521d");
                break;
            case 7:
                intent.putExtra(BankConstantsData.LOAN_WEB, "https://groww.in/mutual-funds/amc");
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ImgBack:
                finish();
                break;
        }
    }
}