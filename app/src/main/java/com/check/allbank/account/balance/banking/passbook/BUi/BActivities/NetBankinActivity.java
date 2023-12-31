package com.check.allbank.account.balance.banking.passbook.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.check.allbank.account.balance.banking.passbook.AdverClass;
import com.check.allbank.account.balance.banking.passbook.BHelper.BankBalanceHelper;
import com.check.allbank.account.balance.banking.passbook.BUi.BAdapters.BankAdapter;
import com.check.allbank.account.balance.banking.passbook.BUtilsClasses.BankPreferences;
import com.check.allbank.account.balance.banking.passbook.BuildConfig;
import com.check.allbank.account.balance.banking.passbook.R;

import java.io.IOException;
import java.util.ArrayList;

public class NetBankinActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle;
    private BankBalanceHelper helper;
    private ProgressBar ProgressNetBank;
    private ArrayList<String> BankList = new ArrayList<>();
    private EditText EdtBankSearch;
    private BankAdapter bankAdapter;
    private String BankStr;
    private ConstraintLayout ConsEdtNetBank, ConsNetBank;
    private RecyclerView RvNetBank;
    private WebView WebNetBanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_bankin);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        ImgShareApp = (ImageView) findViewById(R.id.ImgShareApp);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        RvNetBank = (RecyclerView) findViewById(R.id.RvNetBank);
        ConsEdtNetBank = (ConstraintLayout) findViewById(R.id.ConsEdtNetBank);
        ConsNetBank = (ConstraintLayout) findViewById(R.id.ConsNetBank);
        EdtBankSearch = (EditText) findViewById(R.id.EdtBankSearch);
        WebNetBanking = (WebView) findViewById(R.id.WebNetBanking);
        ProgressNetBank = (ProgressBar) findViewById(R.id.ProgressNetBank);
    }

    private void BankInitListeners() {
        ImgBack.setVisibility(View.VISIBLE);
        ImgShareApp.setVisibility(View.VISIBLE);
        TxtTitle.setText(new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, ""));
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
        EdtBankSearch.addTextChangedListener(this);
    }

    private void BankInitActions() {
        AdverClass.ShowLayoutBannerAds(context, ((ProgressBar) findViewById(R.id.progressBarAd)), (RelativeLayout) findViewById(R.id.RlAdver));
        RvNetBank.setLayoutManager(new LinearLayoutManager(context));
        BankList = new ArrayList<>();
        if (new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, "").isEmpty()) {
            ConsEdtNetBank.setVisibility(View.VISIBLE);
            ConsNetBank.setVisibility(View.GONE);
            BankList.clear();
            TxtTitle.setText("Select Bank");
            int i = 0;
            try {
                String[] list = getAssets().list("IFSC Code");
                if (list == null) {
                    return;
                }
                if (list.length > 0) {
                    int length = list.length;
                    while (i < length) {
                        String str = list[i];
                        if (str.contains(".txt")) {
                            BankList.add(str.replace(".txt", ""));
                        } else {
                            BankList.add(str);
                        }
                        i++;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            bankAdapter = new BankAdapter(context, BankList, new BankAdapter.BankListener() {
                @Override
                public void BankClick(int pos, ArrayList<String> strings) {
                    BankStr = strings.get(pos).toString();
                    new BankPreferences(context).putPrefString(BankPreferences.BANK_NAME, BankStr);
                    BankInitActions();
                }
            });
            RvNetBank.setAdapter(bankAdapter);
            EdtBankSearch.setText("");
            EdtBankSearch.setHint("Select Bank");
        } else {
            ConsEdtNetBank.setVisibility(View.GONE);
            ConsNetBank.setVisibility(View.VISIBLE);
            helper = new BankBalanceHelper(context);
            TxtTitle.setText(new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, ""));
            new AsyncTask<Void, Void, String>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    ProgressNetBank.setVisibility(View.VISIBLE);
                }

                @Override
                protected String doInBackground(Void... voids) {
                    helper = new BankBalanceHelper(context);
                    String searchNetBanking = helper.getSearchNetBanking(new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, ""));
                    return searchNetBanking;
                }

                @Override
                protected void onPostExecute(String result) {
                    WebSettings webPrivacySettings = WebNetBanking.getSettings();
                    webPrivacySettings.setJavaScriptEnabled(true);
                    WebNetBanking.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        WebNetBanking.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                    }
                    WebNetBanking.loadUrl(result);
                    WebNetBanking.setWebViewClient(new AppWebViewClients(ProgressNetBank));
                    super.onPostExecute(result);
                }
            }.execute();
        }
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
            e.toString();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        bankAdapter.getFilter().filter(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

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
            WebNetBanking.setVisibility(View.VISIBLE);
        }
    }
}