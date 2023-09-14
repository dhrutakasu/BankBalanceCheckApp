package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.balance.bankbalancecheck.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private Button BtnCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        BtnCal = (Button) findViewById(R.id.BtnCal);
    }

    private void BankInitListeners() {
        BtnCal.setOnClickListener(this);
    }

    private void BankInitActions() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BtnCal:
                GotoCalculators();
                break;
        }
    }

    private void GotoCalculators() {
        startActivity(new Intent(context, CalculatorsActivity.class));
    }
}