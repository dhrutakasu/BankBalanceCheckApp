package com.check.allbank.account.balance.banking.passbook.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.check.allbank.account.balance.banking.passbook.AdverClass;
import com.check.allbank.account.balance.banking.passbook.BUi.BAdapters.WalkThorughAdapter;
import com.check.allbank.account.balance.banking.passbook.R;

public class WalkThroughActivity extends AppCompatActivity {

    private Context context;
    private ViewPager PagerWalk;
    private ImageView TvWalkThorughContinue;

    private Integer[] images = {R.drawable.ic_exit_vector, R.drawable.ic_walk2, R.drawable.ic_walk3};
    private String[] Text = {"Bank Balance Check", "View Statements", "Finance Calculators"};
    private String[] SubText = {"Instant Bank Balance Check your bank account or internet banking one click All bank balance enquiry.", "The bank's official toll-free number for mini-statement just give a missed-call or send a SMS to get statement", "Statistics show Principal Amount, Interest rate, and remaining balance per month."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        setContentView(R.layout.activity_walk_through);

        initViews();
        initListeners();
        initActions();
    }

    private void initViews() {
        context = this;
        PagerWalk = (ViewPager) findViewById(R.id.PagerWalk);
        TvWalkThorughContinue = (ImageView) findViewById(R.id.TvWalkThorughContinue);
    }

    private void initListeners() {
        TvWalkThorughContinue.setOnClickListener(view -> {
            if (PagerWalk.getCurrentItem() == 0) {
                PagerWalk.setCurrentItem(1);
            } else if (PagerWalk.getCurrentItem() == 1) {
                PagerWalk.setCurrentItem(2);
            } else {
                AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
                    @Override
                    public void AdListen() {
                        startActivity(new Intent(context, HomeScreenActivity.class));
                        finish();
                    }
                });
            }
        });
    }

    private void initActions() {
        AdverClass.ShowLayoutBannerAds90(context, ((ProgressBar) findViewById(R.id.progressBarAd)), (RelativeLayout) findViewById(R.id.RlAdver));
        WalkThorughAdapter adapter = new WalkThorughAdapter(this, images, Text, SubText);
        PagerWalk.setAdapter(adapter);

        PagerWalk.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}