package com.balance.bankbalancecheck.BUi.BActivities.Calculators;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balance.bankbalancecheck.AdverClass;
import com.balance.bankbalancecheck.BUi.BAdapters.EMIAdapter;
import com.balance.bankbalancecheck.BuildConfig;
import com.balance.bankbalancecheck.R;

public class EMICalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle;
    private TextView TxtLoanAmountBtn, TxtMonthlyEMIBtn, TxtTotalInterestBtn, TxtTotalPaymentBtn;
    private ViewPager PagerEMI;
    private EMIAdapter emiAdapter;
    public static int PosLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emicalculator);
        CalInitViews();
        CalInitListeners();
        CalInitActions();
    }

    private void CalInitViews() {
        context = this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        ImgShareApp = (ImageView) findViewById(R.id.ImgShareApp);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        TxtLoanAmountBtn = (TextView) findViewById(R.id.TxtLoanAmountBtn);
        TxtMonthlyEMIBtn = (TextView) findViewById(R.id.TxtMonthlyEMIBtn);
        TxtTotalInterestBtn = (TextView) findViewById(R.id.TxtTotalInterestBtn);
        TxtTotalPaymentBtn = (TextView) findViewById(R.id.TxtTotalPaymentBtn);
        PagerEMI = (ViewPager) findViewById(R.id.PagerEMI);
    }

    private void CalInitListeners() {
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
        TxtLoanAmountBtn.setOnClickListener(this);
        TxtMonthlyEMIBtn.setOnClickListener(this);
        TxtTotalInterestBtn.setOnClickListener(this);
        TxtTotalPaymentBtn.setOnClickListener(this);
        PagerEMI.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    GotoButtonsCheck(TxtLoanAmountBtn, position);
                } else if (position == 1) {
                    GotoButtonsCheck(TxtMonthlyEMIBtn, position);
                } else if (position == 2) {
                    GotoButtonsCheck(TxtTotalInterestBtn, position);
                } else if (position == 3) {
                    GotoButtonsCheck(TxtTotalPaymentBtn, position);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void GotoButtonsCheck(TextView view, int position) {
        TxtLoanAmountBtn.setBackgroundResource(R.drawable.ic_btn_boarder);
        TxtLoanAmountBtn.setTextColor(getResources().getColor(R.color.main_color));
        TxtMonthlyEMIBtn.setBackgroundResource(R.drawable.ic_btn_boarder);
        TxtMonthlyEMIBtn.setTextColor(getResources().getColor(R.color.main_color));
        TxtTotalInterestBtn.setBackgroundResource(R.drawable.ic_btn_boarder);
        TxtTotalInterestBtn.setTextColor(getResources().getColor(R.color.main_color));
        TxtTotalPaymentBtn.setBackgroundResource(R.drawable.ic_btn_boarder);
        TxtTotalPaymentBtn.setTextColor(getResources().getColor(R.color.main_color));
        view.setBackgroundResource(R.drawable.ic_emi);
        view.setTextColor(getResources().getColor(R.color.white));
        emiAdapter.getItem(position);
    }

    private void CalInitActions() {
        AdverClass.ShowLayoutBannerAds(context, ((ProgressBar) findViewById(R.id.progressBarAd)), (RelativeLayout) findViewById(R.id.RlAdver));
        ImgBack.setVisibility(View.VISIBLE);
        ImgShareApp.setVisibility(View.VISIBLE);
        TxtTitle.setText(getResources().getString(R.string.emi_calculator));
        emiAdapter = new EMIAdapter(getSupportFragmentManager(), 4);
        PagerEMI.setAdapter(emiAdapter);
        GotoButtonsCheck(TxtLoanAmountBtn, 0);
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
            case R.id.TxtLoanAmountBtn:
                PagerEMI.setCurrentItem(0);
                GotoButtonsCheck(TxtLoanAmountBtn, PagerEMI.getCurrentItem());
                break;
            case R.id.TxtMonthlyEMIBtn:
                PagerEMI.setCurrentItem(1);
                GotoButtonsCheck(TxtMonthlyEMIBtn, PagerEMI.getCurrentItem());
                break;
            case R.id.TxtTotalInterestBtn:
                PagerEMI.setCurrentItem(2);
                GotoButtonsCheck(TxtTotalInterestBtn, PagerEMI.getCurrentItem());
                break;
            case R.id.TxtTotalPaymentBtn:
                PagerEMI.setCurrentItem(3);
                GotoButtonsCheck(TxtTotalPaymentBtn, PagerEMI.getCurrentItem());
                break;
        }
    }

    private void GotoShare() {
        try {
            String shareMessage = "download.\n\n";
            shareMessage = shareMessage + "https: play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share link:"));
        } catch (Exception e) {
            e.toString();
        }
    }
}