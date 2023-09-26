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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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
        ImgShareApp.setVisibility(View.VISIBLE);
        TxtTitle.setText(R.string.ussd_code);
        ussdCodeModels=new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(loadAssetData());
            JSONArray jsonArray = jsonObject.getJSONArray("USSD");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonArrayJSONObject = jsonArray.getJSONObject(i);
                String bankname = jsonArrayJSONObject.getString("Bank Name");
                String shortname = jsonArrayJSONObject.getString("Short Name");
                String balance = jsonArrayJSONObject.getString("Balance check Ussd Code");
                String IFSC_Code = jsonArrayJSONObject.getString("IFSC Code");
                ussdCodeModels.add(new USSDCodeModel(bankname,balance,shortname,IFSC_Code));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    public String loadAssetData() {
        String json;
        try {
            InputStream inputStream = context.getAssets().open("USSD_Data.json");
            int available = inputStream.available();
            byte[] bytes = new byte[available];
            inputStream.read(bytes);
            inputStream.close();
            json = new String(bytes, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}