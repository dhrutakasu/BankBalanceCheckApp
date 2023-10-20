package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balance.bankbalancecheck.R;

public class BankPrivacyActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView IvBack;
    private TextView TvTitle, TvNotFOund;
    private WebView WebViewPolicy;
    private ProgressBar ProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_privacy);
        GuideViews();
        GuideListerns();
        GuideActions();
    }

    private void GuideViews() {
        context = this;
        IvBack = (ImageView) findViewById(R.id.ImgBack);
        TvTitle = (TextView) findViewById(R.id.TxtTitle);
        TvNotFOund = (TextView) findViewById(R.id.TvNotFOund);
        WebViewPolicy = (WebView) findViewById(R.id.WebViewPolicy);
        ProgressDialog = (ProgressBar) findViewById(R.id.ProgressDialog);
    }

    private void GuideListerns() {
        IvBack.setOnClickListener(this);
    }

    private void GuideActions() {
//        LoanAdsClass.ShowActivityBannerAds(context,((ProgressBar) findViewById(R.id.progressBar)),(RelativeLayout) findViewById(R.id.RlBannerAd));
        IvBack.setVisibility(View.VISIBLE);
        ProgressDialog.setVisibility(View.VISIBLE);
        WebViewPolicy.setVisibility(View.GONE);
        TvNotFOund.setVisibility(View.GONE);
        TvTitle.setText(getString(R.string.privacy_policy));
//        if (LoanAdsClass.isInternetOn(this)) {
//            LoanConst.LoadAdsData(BankPrivacyActivity.this, new LoanConst.LoadAdsId() {
//                @Override
//                public void getAdsIds(LoanAdsModel loanAdsModel) {
//                    if (!loanAdsModel.getAppPrivacyPolicyLink().equalsIgnoreCase("")) {
//                        WebViewPolicy.setInitialScale(100);
//                        WebSettings webPrivacySettings = WebViewPolicy.getSettings();
//                        webPrivacySettings.setJavaScriptEnabled(true);
//                        webPrivacySettings.setTextZoom(webPrivacySettings.getTextZoom() + 70);
//                        WebViewPolicy.loadUrl("https://sites.google.com/view/sleeping-sound/home");
//                        WebViewPolicy.setWebViewClient(new AppWebViewClients(ProgressDialog));
//                    } else {
//                        ProgressDialog.setVisibility(View.GONE);
//                        TvNotFOund.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ImgBack:
                onBackPressed();
                break;
        }
    }

    private class AppWebViewClients extends WebViewClient {

        private final View progressBar;

        public AppWebViewClients(View progressBar) {
            this.progressBar = progressBar;
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            WebViewPolicy.setVisibility(View.VISIBLE);
        }
    }
}