package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BUi.BAdapters.NearByAdapter;
import com.balance.bankbalancecheck.R;

import java.util.ArrayList;

public class LoanDetailsActivity extends AppCompatActivity implements View.OnClickListener {
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
//        webPrivacySettings.setTextZoom(webPrivacySettings.getTextZoom() + 70);
        WebLoanDetails.loadUrl("https://groww.in/mutual-funds/category");
//        WebLoanDetails.loadUrl("file:///android_asset/" + getIntent().getStringExtra(BankConstantsData.LOAN_WEB) + ".html");
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