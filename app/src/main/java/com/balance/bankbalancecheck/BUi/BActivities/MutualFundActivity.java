package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.balance.bankbalancecheck.BUi.BAdapters.BankingAdapter;
import com.balance.bankbalancecheck.BUi.BAdapters.CalculatorsAdapter;
import com.balance.bankbalancecheck.R;

import java.util.ArrayList;

public class MutualFundActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView RvMutualFund;

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
        RvMutualFund = (RecyclerView) findViewById(R.id.RvMutualFund);
    }

    private void BankInitListeners() {

    }

    private void BankInitActions() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(getString(R.string.mutual_fund));
        strings.add(getString(R.string.popular_fund));
        strings.add(getString(R.string.explore_fund));
        RvMutualFund.setLayoutManager(new GridLayoutManager(context, 2));
        RvMutualFund.setAdapter(new BankingAdapter(context, strings, position -> GotoMutualFundActivity(position)));
    }

    private void GotoMutualFundActivity(int position) {
        switch (position){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }
}