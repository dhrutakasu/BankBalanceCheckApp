package com.check.allbank.account.balance.banking.passbook.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.check.allbank.account.balance.banking.passbook.AdverClass;
import com.check.allbank.account.balance.banking.passbook.BConstants.BankConstantsData;
import com.check.allbank.account.balance.banking.passbook.BModel.AdverModel;
import com.check.allbank.account.balance.banking.passbook.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }
    @Override
    protected void onResume() {
        super.onResume();
        CallInitViews();
    }

    private void CallInitViews() {
        if (AdverClass.IsInternetOn(this)) {

            BankConstantsData.LoadAdsData(SplashActivity.this, new BankConstantsData.LoadAdsId() {
                @Override
                public void getAdsIds(AdverModel adverModel) {
                    if (!adverModel.getOad().equalsIgnoreCase("")) {
                        fetchAd(adverModel);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AdverClass.ShowLayoutInterstitialAd(SplashActivity.this, new AdverClass.setAdListerner() {
                                    @Override
                                    public void AdListen() {
                                        GotoActivity();
                                    }
                                });
                            }
                        }, 2000L);
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please Turn Your Internet..", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void fetchAd(AdverModel adverModel) {
        AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            private AppOpenAd openAd;

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                openAd = null;
                GotoActivity();
            }

            @Override
            public void onAdLoaded(AppOpenAd ad) {
                openAd = ad;
                if (openAd != null) {
                    openAd.show(SplashActivity.this);
                    openAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            openAd = null;
                            GotoActivity();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError error) {
                            openAd = null;
                            GotoActivity();
                        }

                        @Override
                        public void onAdImpression() {
                            super.onAdImpression();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent();
                        }
                    });
                }
            }
        };
        AdRequest build = new AdRequest.Builder().build();
        AppOpenAd.load(this, adverModel.getOad(), build, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    private void GotoActivity() {
        startActivity(new Intent(SplashActivity.this, WalkThroughActivity.class));
        finish();
    }
}