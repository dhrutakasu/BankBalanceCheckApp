package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BUtils.SchemesWebData;
import com.balance.bankbalancecheck.BuildConfig;
import com.balance.bankbalancecheck.R;

public class ViewSchemesDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle;
    private WebView WebSchemes;
    private String SchemeLink, SchemeTitle;
    private ProgressBar ProgressScheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schemes_details);
        BankInitViews();
        BankIntents();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        ImgShareApp = (ImageView) findViewById(R.id.ImgShareApp);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        WebSchemes = (WebView) findViewById(R.id.WebSchemes);
        ProgressScheme = (ProgressBar) findViewById(R.id.ProgressScheme);
    }

    private void BankIntents() {
        SchemeLink = getIntent().getStringExtra(BankConstantsData.SCHEMES_LINK);
        SchemeTitle = getIntent().getStringExtra(BankConstantsData.SCHEMES_TITLE);
        System.out.println("=====SchemeLink : "+SchemeLink);
    }

    private void BankInitListeners() {
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
    }

    private void BankInitActions() {
        ImgBack.setVisibility(View.VISIBLE);
        ImgShareApp.setVisibility(View.VISIBLE);
        TxtTitle.setText(SchemeTitle);
        ProgressScheme.setVisibility(View.VISIBLE);
        WebSchemes.getSettings().setJavaScriptEnabled(true);
        WebSchemes.setScrollContainer(true);
        WebSchemes.loadData(SchemeLink, "text/html", "UTF-8");
        WebSchemes.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ProgressScheme.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ImgBack:
                finish();
                break;
            case R.id.ImgShareApp:
                GotoShare();
                break;
        }
    }

    private void GotoShare() {
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
}