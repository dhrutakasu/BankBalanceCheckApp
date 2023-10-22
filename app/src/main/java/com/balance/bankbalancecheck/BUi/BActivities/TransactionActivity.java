package com.balance.bankbalancecheck.BUi.BActivities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BHelper.BankBalanceHelper;
import com.balance.bankbalancecheck.BUi.BAdapters.TranscationAdapter;
import com.balance.bankbalancecheck.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private RelativeLayout RlTopBar;
    private ImageView ImgBack, ImgFilter;
    private TextView TxtTitle, TxtActiveBalanceAmount;
    private RecyclerView RvTranscations;
    private BankBalanceHelper helper;
    private String BankName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcation);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        ImgFilter = (ImageView) findViewById(R.id.ImgFilter);
        RlTopBar = (RelativeLayout) findViewById(R.id.RlTopBar);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        TxtActiveBalanceAmount = (TextView) findViewById(R.id.TxtActiveBalanceAmount);
        RvTranscations = (RecyclerView) findViewById(R.id.RvTranscations);
    }

    private void BankInitListeners() {
        ImgBack.setOnClickListener(this);
        ImgFilter.setOnClickListener(this);
    }

    private void BankInitActions() {
        BankName = getIntent().getStringExtra(BankConstantsData.BANK_NAME);
        helper = new BankBalanceHelper(context);
        RlTopBar.setBackgroundColor(getResources().getColor(R.color.white));
        ImgBack.setVisibility(View.VISIBLE);
        ImgFilter.setVisibility(View.VISIBLE);
        ImgBack.setColorFilter(getResources().getColor(R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        TxtTitle.setText(R.string.bank_transactions);
        TxtTitle.setTextColor(getResources().getColor(R.color.black));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String Currency = "";
            if (helper.getAllSMS(BankName).get(0).getBodyMsg().contains("Rs")) {
                Currency = "Rs.";
            }
            if (helper.getAllSMS(BankName).get(0).getBodyMsg().contains("INR")) {
                Currency = "INR.";
            }
            TxtActiveBalanceAmount.setText(Currency + " " + helper.getAllSMS(BankName).get(0).getBalance());
        }
        RvTranscations.setLayoutManager(new LinearLayoutManager(context));
        TranscationAdapter transcationAdapter = new TranscationAdapter(context, helper.getAllSMS(BankName), (pos, strings) -> {
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_transcation);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            Window window = dialog.getWindow();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            window.setAttributes(lp);
            TextView TxtBankMsg = dialog.findViewById(R.id.TxtBankMsg);
            TextView TxtBankName = dialog.findViewById(R.id.TxtBankName);
            ImageView IvBankClose = dialog.findViewById(R.id.IvBankClose);

            TxtBankName.setText(strings.get(pos).getBankName());
            TxtBankMsg.setText(strings.get(pos).getBodyMsg());
            IvBankClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        });
        RvTranscations.setAdapter(transcationAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ImgBack:
                finish();
                break;
            case R.id.ImgFilter:
                GotoFilter();
                break;
        }
    }

    private void GotoFilter() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        dialog.show();
    }
}