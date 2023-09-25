package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.balance.bankbalancecheck.BModel.USSDCodeModel;
import com.balance.bankbalancecheck.BUi.BAdapters.ALlBankUSSDAdapter;
import com.balance.bankbalancecheck.BuildConfig;
import com.balance.bankbalancecheck.R;

import java.util.ArrayList;

public class AllBankUSSDActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView ImgBack,ImgShareApp;
    private TextView TxtTitle;
    private ArrayList<USSDCodeModel> ussdCodeModels=new ArrayList<>();
    private RecyclerView RvAllBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bank_ussdactivity);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        ImgShareApp = (ImageView) findViewById(R.id.ImgShareApp);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        RvAllBank = (RecyclerView) findViewById(R.id.RvAllBank);

    }

    private void BankInitListeners() {
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
    }

    private void BankInitActions() {
        ImgBack.setVisibility(View.VISIBLE);
        TxtTitle.setText(R.string.ussd_banking);
        ussdCodeModels=new ArrayList<>();

        ussdCodeModels.add(new USSDCodeModel("Abhyudaya Co-op. Bank","*99*87#","ACB","ABHY"));
        ussdCodeModels.add(new USSDCodeModel("Allahabad Bank","*99*54#","ALB","ALLA"));
        ussdCodeModels.add(new USSDCodeModel("Andhra Bank","*99*59#","ANB","ANDB"));
        ussdCodeModels.add(new USSDCodeModel("Apna Sahakari Bank","*99*85#","APN","ASBL"));
        ussdCodeModels.add(new USSDCodeModel("Axis Bank","*99*45#","AXB","UTIB"));
        ussdCodeModels.add(new USSDCodeModel("Bank of Baroda","*99*48#","BOB","BARB"));
        ussdCodeModels.add(new USSDCodeModel("Bank of India","*99*47#","BOI","BKID"));
        ussdCodeModels.add(new USSDCodeModel("Bank of Maharastra","*99*61#","BOM","MAHB"));
        ussdCodeModels.add(new USSDCodeModel("Bharatiya Mahila Bank","*99*86#","BMB","MBMBL"));
        ussdCodeModels.add(new USSDCodeModel("Canara Bank","*99*46#","CNB","CNRB"));

        RvAllBank.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        RvAllBank.setAdapter(new ALlBankUSSDAdapter(context,ussdCodeModels));
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
            //e.toString();
        }
    }
}