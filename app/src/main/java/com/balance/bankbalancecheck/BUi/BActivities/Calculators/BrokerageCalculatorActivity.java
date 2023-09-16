package com.balance.bankbalancecheck.BUi.BActivities.Calculators;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.balance.bankbalancecheck.BuildConfig;
import com.balance.bankbalancecheck.R;

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
        ImgBack.setVisibility(View.VISIBLE);
        ImgShareApp.setVisibility(View.VISIBLE);
        TxtTitle.setText(getResources().getString(R.string.gst_calculator));
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

    }
}