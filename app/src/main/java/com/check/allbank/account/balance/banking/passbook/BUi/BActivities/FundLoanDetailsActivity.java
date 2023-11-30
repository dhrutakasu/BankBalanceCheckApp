package com.check.allbank.account.balance.banking.passbook.BUi.BActivities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.check.allbank.account.balance.banking.passbook.BConstants.BankConstantsData;
import com.check.allbank.account.balance.banking.passbook.R;

public class FundLoanDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ImageView ImgBack;
    private TextView TxtTitle;
    private WebView WebLoanDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_details);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        WebLoanDetails = (WebView) findViewById(R.id.WebLoanDetails);
    }

    private void BankInitListeners() {
        ImgBack.setOnClickListener(this);
    }

    private void BankInitActions() {
        ImgBack.setVisibility(View.VISIBLE);
        TxtTitle.setText(getIntent().getStringExtra(BankConstantsData.LOAN_TYPE));

        WebSettings webPrivacySettings = WebLoanDetails.getSettings();
        webPrivacySettings.setJavaScriptEnabled(true);
        WebLoanDetails.loadUrl(getIntent().getStringExtra(BankConstantsData.LOAN_WEB));
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