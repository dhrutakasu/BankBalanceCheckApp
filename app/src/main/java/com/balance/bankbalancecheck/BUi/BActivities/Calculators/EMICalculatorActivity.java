package com.balance.bankbalancecheck.BUi.BActivities.Calculators;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.balance.bankbalancecheck.BUi.BAdapters.EMIAdapter;
import com.balance.bankbalancecheck.BuildConfig;
import com.balance.bankbalancecheck.R;

public class EMICalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle;
    private TextView TxtLoanAmount, TxtMonthlyEMI, TxtTotalInterest, TxtTotalPayment;
    private ViewPager PagerEMI;
    private EMIAdapter emiAdapter;

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
        TxtLoanAmount = (TextView) findViewById(R.id.TxtLoanAmount);
        TxtMonthlyEMI = (TextView) findViewById(R.id.TxtMonthlyEMI);
        TxtTotalInterest = (TextView) findViewById(R.id.TxtTotalInterest);
        TxtTotalPayment = (TextView) findViewById(R.id.TxtTotalPayment);
        PagerEMI = (ViewPager) findViewById(R.id.PagerEMI);
    }

    private void CalInitListeners() {
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
        TxtLoanAmount.setOnClickListener(this);
        TxtMonthlyEMI.setOnClickListener(this);
        TxtTotalInterest.setOnClickListener(this);
        TxtTotalPayment.setOnClickListener(this);
        PagerEMI.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    GotoButtonsCheck(TxtLoanAmount);
                } else if (position == 1) {
                    GotoButtonsCheck(TxtMonthlyEMI);
                } else if (position == 2) {
                    GotoButtonsCheck(TxtTotalInterest);
                } else if (position == 3) {
                    GotoButtonsCheck(TxtTotalPayment);
                }
                emiAdapter.getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void GotoButtonsCheck(TextView view) {
        TxtLoanAmount.setBackgroundColor(getResources().getColor(R.color.gray_light));
        TxtLoanAmount.setTextColor(getResources().getColor(R.color.black));
        TxtMonthlyEMI.setBackgroundColor(getResources().getColor(R.color.gray_light));
        TxtMonthlyEMI.setTextColor(getResources().getColor(R.color.black));
        TxtTotalInterest.setBackgroundColor(getResources().getColor(R.color.gray_light));
        TxtTotalInterest.setTextColor(getResources().getColor(R.color.black));
        TxtTotalPayment.setBackgroundColor(getResources().getColor(R.color.gray_light));
        TxtTotalPayment.setTextColor(getResources().getColor(R.color.black));
        view.setBackgroundColor(getResources().getColor(R.color.black));
        view.setTextColor(getResources().getColor(R.color.white));
    }

    private void CalInitActions() {
        ImgBack.setVisibility(View.VISIBLE);
        ImgShareApp.setVisibility(View.VISIBLE);
        TxtTitle.setText(getResources().getString(R.string.emi_calculator));
        emiAdapter = new EMIAdapter(getSupportFragmentManager(),4);
        PagerEMI.setAdapter(emiAdapter);
        GotoButtonsCheck(TxtLoanAmount);
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