package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BuildConfig;
import com.balance.bankbalancecheck.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class IFSCDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ImageView ImgBack,ImgShareApp;
    private TextView TxtTitle;
    private String BankStr, StateStr, DistrictStr, BranchStr;
    private RecyclerView RvIFSCDetail;
    private String[] IFSCDetailsArray;
    private TextView TxtBank, TxtState, TxtDistrict, TxtBranch, TxtIFSCCode, TxtAddress, TxtMICRCode, TxtPhNo, TxtBranchCode;
    private TextView IvCopyIFSC,IvCopyAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ifscdetails);
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
        TxtBank = (TextView) findViewById(R.id.TxtBank);
        TxtState = (TextView) findViewById(R.id.TxtState);
        TxtDistrict = (TextView) findViewById(R.id.TxtDistrict);
        TxtBranch = (TextView) findViewById(R.id.TxtBranch);
        TxtIFSCCode = (TextView) findViewById(R.id.TxtIFSCCode);
        TxtAddress = (TextView) findViewById(R.id.TxtAddress);
        TxtMICRCode = (TextView) findViewById(R.id.TxtMICRCode);
        TxtPhNo = (TextView) findViewById(R.id.TxtPhNo);
        TxtBranchCode = (TextView) findViewById(R.id.TxtBranchCode);
        IvCopyIFSC = (TextView) findViewById(R.id.IvCopyIFSC);
        IvCopyAll = (TextView) findViewById(R.id.IvCopyAll);
    }

    private void BankIntents() {
        BankStr = getIntent().getStringExtra(BankConstantsData.IFSC_BANK);
        StateStr = getIntent().getStringExtra(BankConstantsData.IFSC_STATE);
        DistrictStr = getIntent().getStringExtra(BankConstantsData.IFSC_DISTRICT);
        BranchStr = getIntent().getStringExtra(BankConstantsData.IFSC_BRANCH);
    }

    private void BankInitListeners() {
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
        IvCopyIFSC.setOnClickListener(this);
        IvCopyAll.setOnClickListener(this);
    }

    private void BankInitActions() {
        ImgBack.setVisibility(View.VISIBLE);
        ImgShareApp.setVisibility(View.VISIBLE);
        TxtTitle.setText(BranchStr);
        try {
            AssetManager manager = getAssets();
            InputStreamReader reader = new InputStreamReader(manager.open("IFSC Code/" + BankStr + ".txt"), "UTF-8");
            BufferedReader bufferedreader = new BufferedReader(reader);
            bufferedreader.readLine();
            bufferedreader.readLine();
            bufferedreader.readLine();
            String[] strings = bufferedreader.readLine().split("\\*\\*");
            if (strings.length > 0) {
                int length = strings.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    String string = strings[i];
                    System.out.println("--- - -- contain : " + string.toString());
                    if (string.contains(StateStr + "->" + DistrictStr + "->" + BranchStr)) {
                        System.out.println("--- - -- inn contain : " + string.toString());
                        IFSCDetailsArray = string.split("->")[3].split("\\*");
                        break;
                    }
                    i++;
                }
                bufferedreader.close();
                reader.close();
                TxtBank.setText(BankStr);
                TxtState.setText(StateStr);
                TxtDistrict.setText(DistrictStr);
                TxtBranch.setText(BranchStr);
                TxtIFSCCode.setText(IFSCDetailsArray[2].toString());
                TxtAddress.setText(IFSCDetailsArray[0].toString());
                TxtBranchCode.setText(IFSCDetailsArray[2].toString().substring(IFSCDetailsArray[2].toString().length() - 6));
                TxtMICRCode.setText(IFSCDetailsArray[3].toString());
                TxtPhNo.setText(IFSCDetailsArray[1].toString());
                TxtAddress.setSelected(true);
                TxtBank.setSelected(true);
            }
        } catch (Exception unused) {
            unused.getMessage();
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
            case R.id.IvCopyIFSC:
                CopyIFSC();
                break;
            case R.id.IvCopyAll:
                CopyAll();
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
        }
    }

    private void CopyIFSC() {
        ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE))
                .setPrimaryClip(ClipData.newPlainText("IFSC Code", TxtIFSCCode.getText().toString()));
        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
    }

    private void CopyAll() {
        ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE))
                .setPrimaryClip(ClipData.newPlainText("", IFSCDetailsArray.toString()));
        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
    }
}