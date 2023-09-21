package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.balance.bankbalancecheck.BUi.BAdapters.CalculatorsAdapter;
import com.balance.bankbalancecheck.R;

import java.util.ArrayList;

public class BankingActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView RvBanking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banking);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        RvBanking = (RecyclerView) findViewById(R.id.RvBanking);
    }

    private void BankInitListeners() {

    }

    private void BankInitActions() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(getString(R.string.bank_balance));
        strings.add(getString(R.string.ifsc_code));
        strings.add(getString(R.string.bank_holiday));
        strings.add(getString(R.string.ussd_banking));
        strings.add(getString(R.string.net_banking));
        strings.add(getString(R.string.near_by_atm));
        RvBanking.setLayoutManager(new GridLayoutManager(context, 2));
        RvBanking.setAdapter(new CalculatorsAdapter(context, strings, new CalculatorsAdapter.setClickListener() {
            @Override
            public void CalculatorsClickListener(int position) {
                GotoBankingActivity(position);
            }
        }));
    }

    private void GotoBankingActivity(int position) {
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