package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.balance.bankbalancecheck.BHelper.BankBalanceHelper;
import com.balance.bankbalancecheck.BUi.BAdapters.TranscationAdapter;
import com.balance.bankbalancecheck.R;

import java.util.Collections;
import java.util.Locale;

public class TransactionActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ImageView ImgBack;
    private TextView TxtTitle, TxtActiveBalanceAmount;
    private RecyclerView RvTranscations;
    private BankBalanceHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcation);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        TxtActiveBalanceAmount = (TextView) findViewById(R.id.TxtActiveBalanceAmount);
        RvTranscations = (RecyclerView) findViewById(R.id.RvTranscations);
    }

    private void BankInitListeners() {
        ImgBack.setOnClickListener(this);
    }

    private void BankInitActions() {
        helper = new BankBalanceHelper(context);
        ImgBack.setVisibility(View.VISIBLE);
        TxtTitle.setText(R.string.bank_transactions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String Currency = helper.getAllSMS().get(0).getBodyMsg().substring(0, helper.getAllSMS().get(0).getBodyMsg().indexOf(".") + 1);
            double number = Double.parseDouble(helper.getAllSMS().get(0).getBalance());
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
            TxtActiveBalanceAmount.setText(Currency + " " + numberFormat.format(number));
        }
        RvTranscations.setLayoutManager(new LinearLayoutManager(context));
        TranscationAdapter transcationAdapter = new TranscationAdapter(context, helper.getAllSMS(), (pos, strings) -> {

        });
        RvTranscations.setAdapter(transcationAdapter);
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