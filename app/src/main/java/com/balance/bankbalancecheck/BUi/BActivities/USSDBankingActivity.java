package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balance.bankbalancecheck.AdverClass;
import com.balance.bankbalancecheck.BuildConfig;
import com.balance.bankbalancecheck.R;

public class USSDBankingActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView ImgBack,ImgShareApp;
    private TextView TxtTitle;
    private CardView CardMyProfile,CardRequestMoney,CardPendingRequest,CardTransaction,CardCheckBank,CardFirstTime,CardAllBank,CardSendMoney,CardUPIPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ussdbanking);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context=this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        ImgShareApp = (ImageView) findViewById(R.id.ImgShareApp);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        CardMyProfile = (CardView) findViewById(R.id.CardMyProfile);
        CardRequestMoney = (CardView) findViewById(R.id.CardRequestMoney);
        CardPendingRequest = (CardView) findViewById(R.id.CardPendingRequest);
        CardTransaction = (CardView) findViewById(R.id.CardTransaction);
        CardCheckBank = (CardView) findViewById(R.id.CardCheckBank);
        CardFirstTime = (CardView) findViewById(R.id.CardFirstTime);
        CardAllBank = (CardView) findViewById(R.id.CardAllBank);
        CardSendMoney = (CardView) findViewById(R.id.CardSendMoney);
        CardUPIPin = (CardView) findViewById(R.id.CardUPIPin);
    }

    private void BankInitListeners() {
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
        CardMyProfile.setOnClickListener(this);
        CardRequestMoney.setOnClickListener(this);
        CardPendingRequest.setOnClickListener(this);
        CardTransaction.setOnClickListener(this);
        CardCheckBank.setOnClickListener(this);
        CardFirstTime.setOnClickListener(this);
        CardAllBank.setOnClickListener(this);
        CardSendMoney.setOnClickListener(this);
        CardUPIPin.setOnClickListener(this);
    }

    private void BankInitActions() {
        AdverClass.ShowLayoutBannerAds(context, ((ProgressBar) findViewById(R.id.progressBarAd)), (RelativeLayout) findViewById(R.id.RlAdver));
        ImgBack.setVisibility(View.VISIBLE);
        TxtTitle.setText(R.string.ussd_banking);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ImgBack:
                finish();
                break;
            case R.id.ImgShareApp:
                GotoShareApp();
                break;
            case R.id.CardMyProfile:
                GotoCall("tel:*99*4#");
                break;
            case R.id.CardRequestMoney:
                GotoCall("tel:*99*2#");
                break;
            case R.id.CardPendingRequest:
                GotoCall("tel:*99*5#");
                break;
            case R.id.CardTransaction:
                GotoCall("tel:*99*6#");
                break;
            case R.id.CardCheckBank:
                GotoCall("tel:*99*3#");
                break;
            case R.id.CardFirstTime:
                GotoCall("tel:*99#");
                break;
            case R.id.CardAllBank:
                AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
                    @Override
                    public void AdListen() {
                        startActivity(new Intent(context, AllBankUSSDActivity.class));
                    }
                });
                break;
            case R.id.CardSendMoney:
                GotoCall("tel:*99*1#");
                break;
            case R.id.CardUPIPin:
                GotoCall("tel:*99*7#");
                break;
        }
    }

    private void GotoShareApp() {
        try {
            String shareMessage = "download.\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share link:"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    private void GotoCall(String str) {
        Intent intent = new Intent("android.intent.action.DIAL");
        intent.setData(Uri.parse(str));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}