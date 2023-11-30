package com.check.allbank.account.balance.banking.passbook.BUi.BActivities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.check.allbank.account.balance.banking.passbook.AdverClass;
import com.check.allbank.account.balance.banking.passbook.BConstants.BankConstantsData;
import com.check.allbank.account.balance.banking.passbook.BHelper.BankBalanceHelper;
import com.check.allbank.account.balance.banking.passbook.BModel.LoanModel;
import com.check.allbank.account.balance.banking.passbook.BModel.SMSModel;
import com.check.allbank.account.balance.banking.passbook.BUi.BActivities.Calculators.BrokerageCalculatorActivity;
import com.check.allbank.account.balance.banking.passbook.BUi.BActivities.Calculators.EMICalculatorActivity;
import com.check.allbank.account.balance.banking.passbook.BUi.BActivities.Calculators.EPFCalculatorActivity;
import com.check.allbank.account.balance.banking.passbook.BUi.BActivities.Calculators.FDCalculatorActivity;
import com.check.allbank.account.balance.banking.passbook.BUi.BActivities.Calculators.GSTCalculatorActivity;
import com.check.allbank.account.balance.banking.passbook.BUi.BActivities.Calculators.GratuityCalculatorActivity;
import com.check.allbank.account.balance.banking.passbook.BUi.BActivities.Calculators.InflationCalculatorActivity;
import com.check.allbank.account.balance.banking.passbook.BUi.BActivities.Calculators.LoanAmountCalculatorActivity;
import com.check.allbank.account.balance.banking.passbook.BUi.BActivities.Calculators.PPFCalculatorActivity;
import com.check.allbank.account.balance.banking.passbook.BUi.BActivities.Calculators.RdCalculatorActivity;
import com.check.allbank.account.balance.banking.passbook.BUi.BActivities.Calculators.SIPCalculatorActivity;
import com.check.allbank.account.balance.banking.passbook.BUi.BActivities.Calculators.SwapCalculatorActivity;
import com.check.allbank.account.balance.banking.passbook.BUi.BAdapters.CalculatorsAdapter;
import com.check.allbank.account.balance.banking.passbook.BUi.BAdapters.FundsAdapter;
import com.check.allbank.account.balance.banking.passbook.BUtils.ExitDialog;
import com.check.allbank.account.balance.banking.passbook.BUtils.SchemesWebData;
import com.check.allbank.account.balance.banking.passbook.BUtilsClasses.BankPreferences;
import com.check.allbank.account.balance.banking.passbook.BuildConfig;
import com.check.allbank.account.balance.banking.passbook.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private RecyclerView RvCreditLoan, RvMutualFund, RvCalculators, RvSchemes;
    private ImageView IvIFSCCode, IvUSSDCode, IvNetBanking, IvBankATMBox, IvBankHoliday;
    private ChipNavigationBar bottom_menu;
    private ConstraintLayout ConsHome, ConsCalculators, ConsAccountDetail, ConsSetting;
    private ProgressBar ProgressBankAcoount;
    private TextView TxtBankName, TxtBankAccNumber, TxtBankAmount, TxtBankTranscation, TxtVersion;
    private BankBalanceHelper SmsHelper;
    private CardView CardPrivacy, CardShare, CardRate, CardUpdate;
    private boolean IsTransc = false;
    private ImageView IvBankTranscationBox;
    private CardView CardBankTranscationArrow;
    private ImageView IvBankBalance;
    private ImageView IvBankBalance1;
    private ImageView IvBankBalance3;
    private int getBalance = 0;
    private ArrayList<SMSModel> smsModelArrayList = new ArrayList<>();
    private TextView EdtBankName,TxtBankDebit,TxtBankCredit;

    public HomeScreenActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        ConsHome = (ConstraintLayout) findViewById(R.id.ConsHome);
        ConsCalculators = (ConstraintLayout) findViewById(R.id.ConsCalculators);
        ConsSetting = (ConstraintLayout) findViewById(R.id.ConsSetting);
        RvCalculators = (RecyclerView) findViewById(R.id.RvCalculators);
        RvSchemes = (RecyclerView) findViewById(R.id.RvSchemes);
        IvIFSCCode = (ImageView) findViewById(R.id.IvIFSCCode);
        IvUSSDCode = (ImageView) findViewById(R.id.IvUSSDCode);
        IvNetBanking = (ImageView) findViewById(R.id.IvNetBanking);
        IvBankHoliday = (ImageView) findViewById(R.id.IvBankHoliday);
        IvBankATMBox = (ImageView) findViewById(R.id.IvBankATMBox);
        bottom_menu = (ChipNavigationBar) findViewById(R.id.bottom_menu);
        RvCreditLoan = (RecyclerView) findViewById(R.id.RvCreditLoan);
        RvMutualFund = (RecyclerView) findViewById(R.id.RvMutualFund);
        ConsAccountDetail = (ConstraintLayout) findViewById(R.id.ConsAccountDetail);
        ProgressBankAcoount = (ProgressBar) findViewById(R.id.ProgressBankAcoount);
        TxtBankName = (TextView) findViewById(R.id.TxtBankName);
        TxtBankAccNumber = (TextView) findViewById(R.id.TxtBankAccNumber);
        TxtBankAmount = (TextView) findViewById(R.id.TxtBankAmount);
        TxtBankTranscation = (TextView) findViewById(R.id.TxtBankTranscation);
        CardPrivacy = (CardView) findViewById(R.id.CardPrivacy);
        CardShare = (CardView) findViewById(R.id.CardShare);
        CardRate = (CardView) findViewById(R.id.CardRate);
        CardUpdate = (CardView) findViewById(R.id.CardUpdate);
        TxtVersion = (TextView) findViewById(R.id.TxtVersion);
        IvBankTranscationBox = (ImageView) findViewById(R.id.IvBankTranscationBox);
        IvBankBalance = (ImageView) findViewById(R.id.IvBankBalance);
        IvBankBalance1 = (ImageView) findViewById(R.id.IvBankBalance1);
        IvBankBalance3 = (ImageView) findViewById(R.id.IvBankBalance3);
        CardBankTranscationArrow = (CardView) findViewById(R.id.CardBankTranscationArrow);
        EdtBankName = (TextView) findViewById(R.id.EdtBankName);
        TxtBankCredit = (TextView) findViewById(R.id.TxtBankCredit);
        TxtBankDebit = (TextView) findViewById(R.id.TxtBankDebit);
    }

    private void BankInitListeners() {
        IvIFSCCode.setOnClickListener(this);
        IvUSSDCode.setOnClickListener(this);
        IvNetBanking.setOnClickListener(this);
        IvBankHoliday.setOnClickListener(this);
        IvBankATMBox.setOnClickListener(this);
        TxtBankTranscation.setOnClickListener(this);
        CardPrivacy.setOnClickListener(this);
        CardShare.setOnClickListener(this);
        CardRate.setOnClickListener(this);
        CardUpdate.setOnClickListener(this);
        EdtBankName.setOnClickListener(this);
        IvBankBalance.setOnClickListener(this);
        IvBankBalance1.setOnClickListener(this);
        IvBankBalance3.setOnClickListener(this);
        TxtBankCredit.setOnClickListener(this);
        TxtBankDebit.setOnClickListener(this);
        bottom_menu.setOnItemSelectedListener(i -> {
            switch (i) {
                case R.id.Menu_Home:
                    ConsHome.setVisibility(View.VISIBLE);
                    ConsCalculators.setVisibility(View.GONE);
                    ConsSetting.setVisibility(View.GONE);
                    RvSchemes.setVisibility(View.GONE);
                    RvCalculators.setVisibility(View.GONE);
                    break;
                case R.id.Menu_Calculators:
                    ConsHome.setVisibility(View.GONE);
                    ConsSetting.setVisibility(View.GONE);
                    ConsCalculators.setVisibility(View.VISIBLE);
                    RvSchemes.setVisibility(View.GONE);
                    RvCalculators.setVisibility(View.VISIBLE);
                    break;
                case R.id.Menu_Funds:
                    ConsHome.setVisibility(View.GONE);
                    ConsSetting.setVisibility(View.GONE);
                    ConsCalculators.setVisibility(View.VISIBLE);
                    RvSchemes.setVisibility(View.VISIBLE);
                    RvCalculators.setVisibility(View.GONE);
                    break;
                case R.id.Menu_Settings:
                    ConsHome.setVisibility(View.GONE);
                    ConsSetting.setVisibility(View.VISIBLE);
                    ConsCalculators.setVisibility(View.GONE);
                    RvSchemes.setVisibility(View.GONE);
                    RvCalculators.setVisibility(View.GONE);
                    break;
            }
        });
    }

    private void BankInitActions() {
        AdverClass.ShowLayoutBannerAds(context, ((ProgressBar) findViewById(R.id.progressBarAd)), (RelativeLayout) findViewById(R.id.RlAdver));
        GetCreditLoan();
        GetMutualFund();
        GetCalculators();
        GetSchemes();
        TxtVersion.setText(getResources().getString(R.string.version_v_0_01, BuildConfig.VERSION_NAME));
        SmsHelper = new BankBalanceHelper(context);
        bottom_menu.setItemSelected(R.id.Menu_Home, true);
        TxtBankName.setText("-- --");
        TxtBankAccNumber.setText("xxxx");
        NumberFormat numberFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            double number = Double.parseDouble("000000");
            numberFormat = NumberFormat.getNumberInstance(Locale.US);
            TxtBankAmount.setText("INR :- " + numberFormat.format(number));
        }

        ConsAccountDetail.setVisibility(View.VISIBLE);
        CardBankTranscationArrow.setVisibility(View.INVISIBLE);
        TxtBankTranscation.setVisibility(View.INVISIBLE);
        String s = Manifest.permission.READ_SMS;
        Dexter.withActivity(this)
                .withPermissions(s)
                .withListener(new MultiplePermissionsListener() {
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            GoTOAvaliableBalance();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            BankConstantsData.showSettingsDialog(HomeScreenActivity.this);
                        }
                    }

                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken permissionToken) {
                        BankConstantsData.showPermissionDialog(HomeScreenActivity.this, permissionToken);
                    }
                })
                .onSameThread()
                .check();
        ConsHome.setVisibility(View.VISIBLE);
        ConsCalculators.setVisibility(View.GONE);
        ConsSetting.setVisibility(View.GONE);

        RvSchemes.setVisibility(View.GONE);
        RvCalculators.setVisibility(View.GONE);
    }

    private void GoTOAvaliableBalance() {
        smsModelArrayList = new ArrayList<>();
        try {

            new AsyncTask<Void, Void, ArrayList<SMSModel>>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    ProgressBankAcoount.setVisibility(View.VISIBLE);
                    ConsAccountDetail.setVisibility(View.VISIBLE);
                }

                @Override
                protected ArrayList<SMSModel> doInBackground(Void... voids) {
                    return BankConstantsData.GotoSMS(context, SmsHelper);
                }

                @Override
                protected void onPostExecute(ArrayList<SMSModel> unused) {
                    super.onPostExecute(unused);

                    if (unused.size() > 0) {
                        smsModelArrayList = SmsHelper.getAllSMSGroup();
                        for (int i = 0; i < smsModelArrayList.size(); i++) {
                            SMSModel smsMode = SmsHelper.getAllSMSBank(smsModelArrayList.get(i).getBankName());
                            smsModelArrayList.set(i, smsMode);
                        }
                        String amountFormat = BankConstantsData.getAmountFormat(smsModelArrayList.get(getBalance).getBodyMsg());
                        if (amountFormat.length() > 4) {
                            amountFormat = amountFormat.substring(amountFormat.length() - 4);
                        }
                        TxtBankName.setText(smsModelArrayList.get(getBalance).getBankName());
                        TxtBankAccNumber.setText("A/c No:- " + amountFormat);

                        String Currency = "";
                        if (smsModelArrayList.get(getBalance).getBodyMsg().contains("Rs")) {
                            Currency = "Rs.";
                        }
                        if (smsModelArrayList.get(getBalance).getBodyMsg().contains("INR")) {
                            Currency = "INR.";
                        }
                        TxtBankAmount.setText(Currency + " " + smsModelArrayList.get(getBalance).getBalance());
                        TxtBankAmount.setSelected(true);
                        TxtBankAccNumber.setSelected(true);
                        TxtBankName.setSelected(true);
                        if (smsModelArrayList.size()!=1) {
                            CardBankTranscationArrow.setVisibility(View.VISIBLE);
                        }else {
                            CardBankTranscationArrow.setVisibility(View.GONE);
                        }
                        IsTransc = true;
                    }else {
                        TxtBankName.setText("-- --");
                        TxtBankAccNumber.setText("xxxx");
                        NumberFormat numberFormat = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            double number = Double.parseDouble("000000");
                            numberFormat = NumberFormat.getNumberInstance(Locale.US);
                            TxtBankAmount.setText("INR :- " + numberFormat.format(number));
                        }
                    }

                    TxtBankTranscation.setVisibility(View.VISIBLE);
                    ProgressBankAcoount.setVisibility(View.GONE);
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CardBankTranscationArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((smsModelArrayList.size() - 1) == getBalance) {
                    getBalance = 0;
                } else {
                    getBalance++;
                }
                String a2 = BankConstantsData.getAmountFormat(smsModelArrayList.get(getBalance).getBodyMsg());
                if (a2.length() > 4) {
                    a2 = a2.substring(a2.length() - 4);
                }
                TxtBankName.setText(smsModelArrayList.get(getBalance).getBankName());
                TxtBankAccNumber.setText("A/c No:- " + a2);
                String Currency = "";
                if (smsModelArrayList.get(getBalance).getBodyMsg().contains("Rs")) {
                    Currency = "Rs.";
                }
                if (smsModelArrayList.get(getBalance).getBodyMsg().contains("INR")) {
                    Currency = "INR.";
                }
                TxtBankAmount.setText(Currency + smsModelArrayList.get(getBalance).getBalance());
            }
        });
    }

    private void GetCreditLoan() {
        ArrayList<LoanModel> strings = new ArrayList<>();
        LoanModel model = new LoanModel(getString(R.string.car_loan), R.drawable.ic_car_loan);
        strings.add(model);
        model = new LoanModel(getString(R.string.home_loan), R.drawable.ic_home_loan);
        strings.add(model);
        model = new LoanModel(getString(R.string.personal_loan), R.drawable.ic_personal_loan);
        strings.add(model);
        model = new LoanModel(getString(R.string.business_loan), R.drawable.ic_bussiness_loan);
        strings.add(model);
        model = new LoanModel(getString(R.string.education_loan), R.drawable.ic_micro_loan);
        strings.add(model);
        model = new LoanModel(getString(R.string.credit_card), R.drawable.ic_credit_card);
        strings.add(model);
        RvCreditLoan.setLayoutManager(new GridLayoutManager(context, 2));
        RvCreditLoan.setAdapter(new CalculatorsAdapter(context, strings, position -> GotoCreditLoanActivity(position, strings)));
    }

    private void GetMutualFund() {
        ArrayList<LoanModel> strings = new ArrayList<>();
        LoanModel model = new LoanModel(getString(R.string.mutual_funds), R.drawable.ic_mutual_fund);
        strings.add(model);
        model = new LoanModel(getString(R.string.popular_fund), R.drawable.ic_popular_fund);
        strings.add(model);
        model = new LoanModel(getString(R.string.explore_fund), R.drawable.ic_expoler_fund);
        strings.add(model);
        RvMutualFund.setLayoutManager(new GridLayoutManager(context, 1, RecyclerView.VERTICAL, false));
        RvMutualFund.setAdapter(new FundsAdapter(context, strings, position -> GotoMutualFundsActivity(position, strings)));
    }

    private void GotoCreditLoanActivity(int position, ArrayList<LoanModel> strings) {
        AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
            @Override
            public void AdListen() {
                Intent intent = new Intent(context, LoanDetailsActivity.class);
                intent.putExtra(BankConstantsData.LOAN_TYPE, strings.get(position).getName().toString());
                switch (position) {
                    case 0:
                        intent.putExtra(BankConstantsData.LOAN_WEB, "car loan");
                        break;
                    case 1:
                        intent.putExtra(BankConstantsData.LOAN_WEB, "home loan");
                        break;
                    case 2:
                        intent.putExtra(BankConstantsData.LOAN_WEB, "personal loan");
                        break;
                    case 3:
                        intent.putExtra(BankConstantsData.LOAN_WEB, "business loan");
                        break;
                    case 4:
                        intent.putExtra(BankConstantsData.LOAN_WEB, "education loan");
                        break;
                    case 5:
                        intent.putExtra(BankConstantsData.LOAN_WEB, "credit loan");
                        break;
                }
                startActivity(intent);
            }
        });
    }

    private void GotoMutualFundsActivity(int position, ArrayList<LoanModel> strings) {
        switch (position) {
            case 0:
                AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
                    @Override
                    public void AdListen() {
                        Intent intent = new Intent(context, MutualFundActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case 1:
                AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
                    @Override
                    public void AdListen() {
                        Intent in = new Intent(context, FundLoanDetailsActivity.class);
                        in.putExtra(BankConstantsData.LOAN_TYPE, strings.get(position).getName().toString());
                        in.putExtra(BankConstantsData.LOAN_WEB, "https://www.paytmmoney.com/mutual-funds/top-rated-funds");
                        startActivity(in);
                    }
                });
                break;
            case 2:
                AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
                    @Override
                    public void AdListen() {
                        Intent ins = new Intent(context, FundLoanDetailsActivity.class);
                        ins.putExtra(BankConstantsData.LOAN_TYPE, strings.get(position).getName().toString());
                        ins.putExtra(BankConstantsData.LOAN_WEB, "https://groww.in/mutual-funds/category");
                        startActivity(ins);
                    }
                });
                break;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.IvIFSCCode:
                AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
                    @Override
                    public void AdListen() {
                        if (!new BankPreferences(context).getPrefString(BankPreferences.BRANCH_NAME, "").isEmpty()) {
                            String BankStr = new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, "");
                            String StateStr = new BankPreferences(context).getPrefString(BankPreferences.STATE_NAME, "");
                            String DistrictStr = new BankPreferences(context).getPrefString(BankPreferences.DISTRICT_NAME, "");
                            String BranchStr = new BankPreferences(context).getPrefString(BankPreferences.BRANCH_NAME, "");

                            startActivity(new Intent(context, IFSCDetailsActivity.class)
                                    .putExtra(BankConstantsData.IFSC_BANK, BankStr)
                                    .putExtra(BankConstantsData.IFSC_STATE, StateStr)
                                    .putExtra(BankConstantsData.IFSC_DISTRICT, DistrictStr)
                                    .putExtra(BankConstantsData.IFSC_BRANCH, BranchStr));
                        } else {
                            startActivity(new Intent(context, IFSCActivity.class));
                        }
                    }
                });
                break;
            case R.id.IvUSSDCode:
                AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
                    @Override
                    public void AdListen() {
                        startActivity(new Intent(context, USSDBankingActivity.class));
                    }
                });
                break;
            case R.id.IvNetBanking:
                AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
                    @Override
                    public void AdListen() {
                        startActivity(new Intent(context, NetBankinActivity.class));
                    }
                });
                break;
            case R.id.IvBankHoliday:
                AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
                    @Override
                    public void AdListen() {
                        startActivity(new Intent(context, BankHolidayActivity.class));
                    }
                });
                break;
            case R.id.IvBankATMBox:
                AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
                    @Override
                    public void AdListen() {
                        startActivity(new Intent(context, NearByActivity.class));
                    }
                });
                break;
            case R.id.TxtBankTranscation:
                AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
                    @Override
                    public void AdListen() {
                        if (IsTransc) {
                            if (SmsHelper.getAllSMSType(TxtBankName.getText().toString(), "all").size()>0){
                                startActivity(new Intent(context, TransactionActivity.class)
                                        .putExtra(BankConstantsData.BANK_NAME, TxtBankName.getText().toString())
                                        .putExtra(BankConstantsData.TRANSCATION, ""));
                            }else {
                                Toast.makeText(context, "No transactions found.", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(context, "No transactions found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.CardPrivacy:
                AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
                    @Override
                    public void AdListen() {
                        startActivity(new Intent(context, BankPrivacyActivity.class));
                    }
                });
                break;
            case R.id.CardShare:
                GotoShareApp();
                break;
            case R.id.CardRate:
                GotoRateUs();
                break;
            case R.id.EdtBankName:
                AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
                    @Override
                    public void AdListen() {
                        startActivity(new Intent(context, SelectBankActivity.class));
                    }
                });
                break;
            case R.id.IvBankBalance:
            case R.id.IvBankBalance1:
            case R.id.IvBankBalance3:
                AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
                    @Override
                    public void AdListen() {
                        startActivity(new Intent(context, BankBalanceActivity.class));
                    }
                });
                break;
            case R.id.CardUpdate:
                break;
            case R.id.CardVersion:
                break;
            case R.id.TxtBankDebit:
                if (IsTransc) {
                    if (SmsHelper.getAllSMSType(TxtBankName.getText().toString(), "debit").size()>0){
                        startActivity(new Intent(context, TransactionActivity.class)
                                .putExtra(BankConstantsData.BANK_NAME, TxtBankName.getText().toString())
                                .putExtra(BankConstantsData.TRANSCATION, "debit"));
                    }else {
                        Toast.makeText(context, "No debit transaction found.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, "No debit transaction found.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.TxtBankCredit:
                if (IsTransc) {
                    if (SmsHelper.getAllSMSType(TxtBankName.getText().toString(), "credit").size()>0){
                        startActivity(new Intent(context, TransactionActivity.class)
                                .putExtra(BankConstantsData.BANK_NAME, TxtBankName.getText().toString())
                                .putExtra(BankConstantsData.TRANSCATION, "credit"));
                    }else {
                        Toast.makeText(context, "No credit transaction found.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, "No credit transaction found.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void GetCalculators() {
        ArrayList<LoanModel> strings = new ArrayList<>();
        LoanModel model = new LoanModel(getString(R.string.sip_calculator), R.drawable.ic_sip_calculator);
        strings.add(model);
        model = new LoanModel(getString(R.string.emi_calculator), R.drawable.ic_emi_calculator);
        strings.add(model);
        model = new LoanModel(getString(R.string.loan_calculator), R.drawable.ic_loan_calculator);
        strings.add(model);
        model = new LoanModel(getString(R.string.gst_calculator), R.drawable.ic_gst_calculator);
        strings.add(model);
        model = new LoanModel(getString(R.string.fd_calculator), R.drawable.ic_fd_calculator);
        strings.add(model);
        model = new LoanModel(getString(R.string.brokerage_calculator), R.drawable.ic_brokareage_calculator);
        strings.add(model);
        model = new LoanModel(getString(R.string.swp_calculator), R.drawable.ic_swp_calculator);
        strings.add(model);
        model = new LoanModel(getString(R.string.rd_calculator), R.drawable.ic_rd_calculator);
        strings.add(model);
        model = new LoanModel(getString(R.string.ppf_calculator), R.drawable.ic_ppf_calculator);
        strings.add(model);
        model = new LoanModel(getString(R.string.epf_calculator), R.drawable.ic_epf_calculator);
        strings.add(model);
        model = new LoanModel(getString(R.string.inflation_calculator), R.drawable.ic_inflation_calculator);
        strings.add(model);
        model = new LoanModel(getString(R.string.gratuity_calculator), R.drawable.ic_gravity_calculator);
        strings.add(model);
        RvCalculators.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false));
        RvCalculators.setAdapter(new CalculatorsAdapter(context, strings, position -> GotoCalculatorsActivity(position)));
    }

    private void GotoCalculatorsActivity(int position) {
        AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
            @Override
            public void AdListen() {
                switch (position) {
                    case 0:
                        startActivity(new Intent(context, SIPCalculatorActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(context, EMICalculatorActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(context, LoanAmountCalculatorActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(context, GSTCalculatorActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(context, FDCalculatorActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(context, BrokerageCalculatorActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(context, SwapCalculatorActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(context, RdCalculatorActivity.class));
                        break;
                    case 8:
                        startActivity(new Intent(context, PPFCalculatorActivity.class));
                        break;
                    case 9:
                        startActivity(new Intent(context, EPFCalculatorActivity.class));
                        break;
                    case 10:
                        startActivity(new Intent(context, InflationCalculatorActivity.class));
                        break;
                    case 11:
                        startActivity(new Intent(context, GratuityCalculatorActivity.class));
                        break;
                }
            }
        });
    }

    private void GetSchemes() {
        ArrayList<LoanModel> strings = new ArrayList<>();
        LoanModel model = new LoanModel(getString(R.string.public_pf), R.drawable.ic_public_pf);
        strings.add(model);
        model = new LoanModel(getString(R.string.employee_pf), R.drawable.ic_employee_pf);
        strings.add(model);
        model = new LoanModel(getString(R.string.natinal_ps), R.drawable.ic_national_ps);
        strings.add(model);
        model = new LoanModel(getString(R.string.sensor_cs), R.drawable.ic_senior_cs);
        strings.add(model);
        model = new LoanModel(getString(R.string.national_sc), R.drawable.ic_national_sc);
        strings.add(model);
        model = new LoanModel(getString(R.string.post_office_ss), R.drawable.ic_postoffice_ss);
        strings.add(model);
        model = new LoanModel(getString(R.string.pm_vaya), R.drawable.ic_pm_vaya);
        strings.add(model);
        model = new LoanModel(getString(R.string.pm_jan_dhan), R.drawable.ic_pm_jhan_dhan);
        strings.add(model);
        RvSchemes.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false));
        RvSchemes.setAdapter(new CalculatorsAdapter(context, strings, position -> GotoSchemeActivity(position, strings)));
    }

    private void GotoSchemeActivity(int position, ArrayList<LoanModel> strings) {
        AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
            @Override
            public void AdListen() {
                Intent intent = new Intent(context, ViewSchemesDetailsActivity.class);
                switch (position) {
                    case 0:
                        intent.putExtra(BankConstantsData.SCHEMES_LINK, new SchemesWebData().getSCHEME_PUBLIC_PF());
                        break;
                    case 1:
                        intent.putExtra(BankConstantsData.SCHEMES_LINK, new SchemesWebData().getSCHEME_EMPLOYEE_PF());
                        break;
                    case 2:
                        intent.putExtra(BankConstantsData.SCHEMES_LINK, new SchemesWebData().getSCHEME_NATIONAL_PS());
                        break;
                    case 3:
                        intent.putExtra(BankConstantsData.SCHEMES_LINK, new SchemesWebData().getSCHEME_SENIOR_CS());
                        break;
                    case 4:
                        intent.putExtra(BankConstantsData.SCHEMES_LINK, new SchemesWebData().getSCHEME_NATIONAL_SC());
                        break;
                    case 5:
                        intent.putExtra(BankConstantsData.SCHEMES_LINK, new SchemesWebData().getSCHEME_POST_OFFICE_SS());
                        break;
                    case 6:
                        intent.putExtra(BankConstantsData.SCHEMES_LINK, new SchemesWebData().getSCHEME_PM_VAYA());
                        break;
                    case 7:
                        intent.putExtra(BankConstantsData.SCHEMES_LINK, new SchemesWebData().getSCHEME_PM_JAN_DHAN());
                        break;
                }
                intent.putExtra(BankConstantsData.SCHEMES_TITLE, strings.get(position).getName().toString());
                startActivity(intent);
            }
        });
    }

    private void GotoShareApp() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.share_msg) + context.getPackageName());
            Intent createChooser = Intent.createChooser(intent, "Share via");
            createChooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(createChooser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GotoRateUs() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, "").isEmpty()) {
            EdtBankName.setText("");
        } else {
            EdtBankName.setText(new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, ""));
        }
    }

    @Override
    public void onBackPressed() {
        ExitDialog exitDialog = new ExitDialog(HomeScreenActivity.this, context, () -> finishAffinity());
        exitDialog.show();
        exitDialog.setCancelable(false);
        WindowManager.LayoutParams lp = exitDialog.getWindow().getAttributes();
        Window window = exitDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

    }
}