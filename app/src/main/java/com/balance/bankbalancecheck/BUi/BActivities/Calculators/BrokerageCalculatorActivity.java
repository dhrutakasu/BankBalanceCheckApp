package com.balance.bankbalancecheck.BUi.BActivities.Calculators;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.balance.bankbalancecheck.AdverClass;
import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BuildConfig;
import com.balance.bankbalancecheck.R;

import java.text.DecimalFormat;

public class BrokerageCalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle, TxtReset, TxtCalculate;
    private EditText EdtBuyAmount, EdtSellAmount, EdtQuanity;
    private Spinner SpinnerTrade;
    private TextView TxtBrokerageAmountFirst, TxtBrokerageAmountSecond, TxtBrokerageAmountThird, TxtBrokerageAmountFourth, TxtBrokerageAmountFifth, TxtBrokerageAmountSixth;
    private TextView TxtBrokerageAmountSeventh, TxtBrokerageAmountEighth, TxtBrokerageAmountNinth, TxtBrokerageAmountTenth, TxtBrokerageAmountEleventh, TxtBrokerageAmountTwelveth;
    private TextView TxtBrokerageAmountThirteen, TxtBrokerageAmountFourteen, TxtBrokerageAmountFifteen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brokerage_calculator);
        CalInitViews();
        CalInitListeners();
        CalInitActions();
    }

    private void CalInitViews() {
        context = this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        ImgShareApp = (ImageView) findViewById(R.id.ImgShareApp);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        TxtReset = (TextView) findViewById(R.id.TxtReset);
        TxtCalculate = (TextView) findViewById(R.id.TxtCalculate);
        EdtBuyAmount = (EditText) findViewById(R.id.EdtBuyAmount);
        EdtSellAmount = (EditText) findViewById(R.id.EdtSellAmount);
        EdtQuanity = (EditText) findViewById(R.id.EdtQuanity);
        SpinnerTrade = (Spinner) findViewById(R.id.SpinnerTrade);
        TxtBrokerageAmountFirst = (TextView) findViewById(R.id.TxtBrokerageAmountFirst);
        TxtBrokerageAmountSecond = (TextView) findViewById(R.id.TxtBrokerageAmountSecond);
        TxtBrokerageAmountThird = (TextView) findViewById(R.id.TxtBrokerageAmountThird);
        TxtBrokerageAmountFourth = (TextView) findViewById(R.id.TxtBrokerageAmountFourth);
        TxtBrokerageAmountFifth = (TextView) findViewById(R.id.TxtBrokerageAmountFifth);
        TxtBrokerageAmountSixth = (TextView) findViewById(R.id.TxtBrokerageAmountSixth);
        TxtBrokerageAmountSeventh = (TextView) findViewById(R.id.TxtBrokerageAmountSeventh);
        TxtBrokerageAmountEighth = (TextView) findViewById(R.id.TxtBrokerageAmountEighth);
        TxtBrokerageAmountNinth = (TextView) findViewById(R.id.TxtBrokerageAmountNinth);
        TxtBrokerageAmountTenth = (TextView) findViewById(R.id.TxtBrokerageAmountTenth);
        TxtBrokerageAmountEleventh = (TextView) findViewById(R.id.TxtBrokerageAmountEleventh);
        TxtBrokerageAmountTwelveth = (TextView) findViewById(R.id.TxtBrokerageAmountTwelveth);
        TxtBrokerageAmountThirteen = (TextView) findViewById(R.id.TxtBrokerageAmountThirteen);
        TxtBrokerageAmountFourteen = (TextView) findViewById(R.id.TxtBrokerageAmountFourteen);
        TxtBrokerageAmountFifteen = (TextView) findViewById(R.id.TxtBrokerageAmountFifteen);
    }

    private void CalInitListeners() {
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
        TxtReset.setOnClickListener(this);
        TxtCalculate.setOnClickListener(this);
    }

    private void CalInitActions() {
        AdverClass.ShowScreenBannerAds(context, ((ProgressBar) findViewById(R.id.progressBarAd)), (RelativeLayout) findViewById(R.id.RlAdver));
        String[] trade = {"Delivery Equity", "Intraday Equity", "Futures", "Options"};
        ImgBack.setVisibility(View.VISIBLE);
        ImgShareApp.setVisibility(View.VISIBLE);
        TxtTitle.setText(getResources().getString(R.string.brokerage_calculator));
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, trade);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerTrade.setAdapter(arrayAdapter);
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
            case R.id.TxtReset:
                GotoReset();
                break;
            case R.id.TxtCalculate:
                GotoCalculate();
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

    private void GotoReset() {
        EdtBuyAmount.setText("");
        EdtSellAmount.setText("");
        EdtQuanity.setText("");
        SpinnerTrade.setSelection(0);
        TxtBrokerageAmountFirst.setText(getResources().getString(R.string._00_0000));
        TxtBrokerageAmountSecond.setText(getResources().getString(R.string._00_0000));
        TxtBrokerageAmountThird.setText(getResources().getString(R.string._00_0000));
        TxtBrokerageAmountFourth.setText(getResources().getString(R.string._00_0000));
        TxtBrokerageAmountFifth.setText(getResources().getString(R.string._00_0000));
        TxtBrokerageAmountSixth.setText(getResources().getString(R.string._00_0000));
        TxtBrokerageAmountSeventh.setText(getResources().getString(R.string._00_0000));
        TxtBrokerageAmountEighth.setText(getResources().getString(R.string._00_0000));
        TxtBrokerageAmountNinth.setText(getResources().getString(R.string._00_0000));
        TxtBrokerageAmountTenth.setText(getResources().getString(R.string._00_0000));
        TxtBrokerageAmountEleventh.setText(getResources().getString(R.string._00_0000));
        TxtBrokerageAmountTwelveth.setText(getResources().getString(R.string._00_0000));
        TxtBrokerageAmountThirteen.setText(getResources().getString(R.string._00_0000));
        TxtBrokerageAmountFourteen.setText(getResources().getString(R.string._00_0000));
        TxtBrokerageAmountFifteen.setText(getResources().getString(R.string._00_0000));
    }

    private void GotoCalculate() {
        BankConstantsData.hideKeyboard(BrokerageCalculatorActivity.this);

        if (EdtBuyAmount.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter buy amount.", Toast.LENGTH_SHORT).show();
        } else if (EdtSellAmount.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter sell amount.", Toast.LENGTH_SHORT).show();
        } else if (EdtQuanity.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please enter quantity.", Toast.LENGTH_SHORT).show();
        } else {

            double buyAmount = Double.parseDouble(EdtBuyAmount.getText().toString());
            double sellAmount = Double.parseDouble(EdtSellAmount.getText().toString());
            int quantity = Integer.parseInt(EdtQuanity.getText().toString());
            int tradeType = SpinnerTrade.getSelectedItemPosition();

            double totalBuyOrderValue = buyAmount * quantity;
            double totalSellOrderValue = sellAmount * quantity;
            double buyBrokerage,sellBrokerage;
            if (tradeType==0){
                buyBrokerage=(totalBuyOrderValue * 0.001);
                sellBrokerage=(totalSellOrderValue * 0.001);
            } else if (tradeType==1) {
                buyBrokerage=(totalBuyOrderValue * 0.00025);
                sellBrokerage=(totalSellOrderValue * 0.00025);
            } else if (tradeType == 2) {
                buyBrokerage=(totalBuyOrderValue * 0.005);
                sellBrokerage=(totalSellOrderValue * 0.005);
            }else {
                buyBrokerage=(totalBuyOrderValue * 0.01);
                sellBrokerage=(totalSellOrderValue * 0.01);
            }
//            double buyBrokerage = (tradeType==0) ? (totalBuyOrderValue * 0.001) : (totalBuyOrderValue * 0.00025);
//            double sellBrokerage = (tradeType==0) ? (totalSellOrderValue * 0.001) : (totalSellOrderValue * 0.00025);
            double sttBuy = totalBuyOrderValue * 0.001;
            double sttSell = totalSellOrderValue * 0.001;
            double exchangeBuyTxnCharge = totalBuyOrderValue * 0.0000325;
            double exchangeSellTxnCharge = totalSellOrderValue * 0.0000325;
            double gstBuy = (buyBrokerage + exchangeBuyTxnCharge) * 0.18;
            double gstSell = (sellBrokerage + exchangeSellTxnCharge) * 0.18;
            double sebiBuyCharges = totalBuyOrderValue * 0.000000015;
            double sebiSellCharges = totalSellOrderValue * 0.000000015;
            double stampDuty = totalBuyOrderValue * 0.00015;
            double totalTaxAndCharges = buyBrokerage + sellBrokerage + sttBuy + sttSell +
                    exchangeBuyTxnCharge + exchangeSellTxnCharge + gstBuy + gstSell +
                    sebiBuyCharges + sebiSellCharges + stampDuty;
            double netProfitOrLoss = totalSellOrderValue - totalBuyOrderValue - totalTaxAndCharges;


            DecimalFormat decimalFormat = new DecimalFormat("#####0.00");

            StringBuilder sb = new StringBuilder();
            sb.append("₹ ");
            String totalBuyOrderValueStr = decimalFormat.format(totalBuyOrderValue);
            sb.append(totalBuyOrderValueStr);
            TxtBrokerageAmountFirst.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String totalSellOrderValueStr = decimalFormat.format(totalSellOrderValue);
            sb.append(totalSellOrderValueStr);
            TxtBrokerageAmountSecond.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String buyBrokerageStr = decimalFormat.format(buyBrokerage);
            sb.append(buyBrokerageStr);
            TxtBrokerageAmountThird.setText(sb.toString()); sb = new StringBuilder();
            sb.append("₹ ");
            String sellBrokerageStr = decimalFormat.format(sellBrokerage);
            sb.append(sellBrokerageStr);
            TxtBrokerageAmountFourth.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String sttBuyStr = decimalFormat.format(sttBuy);
            sb.append(sttBuyStr);
            TxtBrokerageAmountFifth.setText(sb.toString()); sb = new StringBuilder();
            sb.append("₹ ");
            String sttSellStr = decimalFormat.format(sttSell);
            sb.append(sttSellStr);
            TxtBrokerageAmountSixth.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String exchangeBuyTxnChargeStr = decimalFormat.format(exchangeBuyTxnCharge);
            sb.append(exchangeBuyTxnChargeStr);
            TxtBrokerageAmountSeventh.setText(sb.toString()); sb = new StringBuilder();
            sb.append("₹ ");
            String exchangeSellTxnChargeStr = decimalFormat.format(exchangeSellTxnCharge);
            sb.append(exchangeSellTxnChargeStr);
            TxtBrokerageAmountEighth.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String gstBuyStr = decimalFormat.format(gstBuy);
            sb.append(gstBuyStr);
            TxtBrokerageAmountNinth.setText(sb.toString()); sb = new StringBuilder();
            sb.append("₹ ");
            String gstSellStr = decimalFormat.format(gstSell);
            sb.append(gstSellStr);
            TxtBrokerageAmountTenth.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String sebiBuyChargesStr = decimalFormat.format(sebiBuyCharges);
            sb.append(sebiBuyChargesStr);
            TxtBrokerageAmountEleventh.setText(sb.toString()); sb = new StringBuilder();
            sb.append("₹ ");
            String sebiSellChargesStr = decimalFormat.format(sebiSellCharges);
            sb.append(sebiSellChargesStr);
            TxtBrokerageAmountTwelveth.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String stampDutyStr = decimalFormat.format(stampDuty);
            sb.append(stampDutyStr);
            TxtBrokerageAmountThirteen.setText(sb.toString()); sb = new StringBuilder();
            sb.append("₹ ");
            String totalTaxAndChargesStr = decimalFormat.format(totalTaxAndCharges);
            sb.append(totalTaxAndChargesStr);
            TxtBrokerageAmountFourteen.setText(sb.toString());
            sb = new StringBuilder();
            sb.append("₹ ");
            String netProfitOrLossStr = decimalFormat.format(netProfitOrLoss);
            sb.append(netProfitOrLossStr);
            TxtBrokerageAmountFifteen.setText(sb.toString());
        }
    }
}