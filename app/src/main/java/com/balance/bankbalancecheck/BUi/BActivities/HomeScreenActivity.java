package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BModel.LoanModel;
import com.balance.bankbalancecheck.BModel.SMSModel;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.BrokerageCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.EMICalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.EPFCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.FDCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.GSTCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.GratuityCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.InflationCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.LoanAmountCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.PPFCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.RdCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.SIPCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.SwapCalculatorActivity;
import com.balance.bankbalancecheck.BUi.BAdapters.CalculatorsAdapter;
import com.balance.bankbalancecheck.BUi.BAdapters.FundsAdapter;
import com.balance.bankbalancecheck.BUtilsClasses.BankPreferences;
import com.balance.bankbalancecheck.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private RecyclerView RvCreditLoan, RvMutualFund, RvCalculators;
    private ImageView IvIFSCCode, IvUSSDCode, IvNetBanking, IvBankATMBox, IvBankHoliday;
    private ChipNavigationBar bottom_menu;
    private ConstraintLayout ConsHome, ConsCalculators, ConsAccountDetail;
    private ProgressBar ProgressBankAcoount;
    private TextView TxtBankName, TxtBankAccNumber, TxtBankAmount, TxtBankTranscation;

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
        RvCalculators = (RecyclerView) findViewById(R.id.RvCalculators);
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
    }

    private void BankInitListeners() {
        IvIFSCCode.setOnClickListener(this);
        IvUSSDCode.setOnClickListener(this);
        IvNetBanking.setOnClickListener(this);
        IvBankHoliday.setOnClickListener(this);
        IvBankATMBox.setOnClickListener(this);
        TxtBankTranscation.setOnClickListener(this);
        bottom_menu.setOnItemSelectedListener(i -> {
            switch (i) {
                case R.id.Menu_Home:
                    ConsHome.setVisibility(View.VISIBLE);
                    ConsCalculators.setVisibility(View.GONE);
                    break;
                case R.id.Menu_Calculators:
                    ConsHome.setVisibility(View.GONE);
                    ConsCalculators.setVisibility(View.VISIBLE);
                    break;
                case R.id.Menu_Funds:
                    ConsHome.setVisibility(View.GONE);
                    ConsCalculators.setVisibility(View.VISIBLE);
                    break;
                case R.id.Menu_Settings:
                    ConsHome.setVisibility(View.GONE);
                    ConsCalculators.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }

    private void BankInitActions() {

        GetCreditLoan();
        GetMutualFund();
        GetCalculators();
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
        String s = Manifest.permission.READ_SMS;
        Dexter.withActivity(this)
                .withPermissions(s)
                .withListener(new MultiplePermissionsListener() {
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            new AsyncTask<Void, Void, ArrayList<SMSModel>>() {
                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    ProgressBankAcoount.setVisibility(View.VISIBLE);
                                    ConsAccountDetail.setVisibility(View.VISIBLE);
                                }

                                @Override
                                protected ArrayList<SMSModel> doInBackground(Void... voids) {
                                    return BankConstantsData.GotoSMS(context);
                                }

                                @Override
                                protected void onPostExecute(ArrayList<SMSModel> unused) {
                                    super.onPostExecute(unused);
                                    ArrayList<SMSModel> result = new ArrayList<SMSModel>();
                                    for (int i = 0; i < unused.size(); i++) {
                                        System.out.println("+++++ 11 BALANCE : " + BankConstantsData.getAvailableBalance(unused.get(i)).getBalance());
                                        if (BankConstantsData.getAvailableBalance(unused.get(i)).getBalance() != null) {
                                            if (!BankConstantsData.getAvailableBalance(unused.get(i)).getBalance().equals("N/A") && !BankConstantsData.getAvailableBalance(unused.get(i)).getAmount().equals("null")) {
                                                result.add(BankConstantsData.getAvailableBalance(unused.get(i)));
                                                System.out.println("+++++ 22 BALANCE : " + BankConstantsData.getAvailableBalance(unused.get(i)).getBalance());
                                                System.out.println("+++++ 22 AMOUNt : " + BankConstantsData.getAvailableBalance(unused.get(i)).getAmount());
                                            }
                                        }
                                    }
                                    Collections.reverse(result);
                                    if (unused.size() > 0) {
                                        ;
                                        List<String> strings = Arrays.asList(result.get(result.size() - 1).getBody().split("UPI/", 12));
                                        Log.d("TAG", "getAvailableBalance: newBody --1-- --> " + strings);
                                        String ANumber = strings.get(1).substring(0, strings.get(1).indexOf("."));
                                        String last4Digits = ANumber.substring(ANumber.length() - 4);
                                        System.out.println("+++++ strings.get(1) : " + last4Digits);

                                        TxtBankName.setText(result.get(result.size() - 1).getBody().substring(result.get(result.size() - 1).getBody().lastIndexOf(" - ") + 1).replace("- ", ""));
                                        TxtBankAccNumber.setText("A/c No:- " + last4Digits);
                                        NumberFormat numberFormat = null;
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                            double number = Double.parseDouble(result.get(result.size() - 1).getBalance());
                                            numberFormat = NumberFormat.getNumberInstance(Locale.US);
                                            TxtBankAmount.setText("INR :- " + numberFormat.format(number));
                                        }
                                        TxtBankAmount.setSelected(true);
                                        TxtBankAccNumber.setSelected(true);
                                        TxtBankName.setSelected(true);
                                        ConsAccountDetail.setVisibility(View.VISIBLE);
                                        ProgressBankAcoount.setVisibility(View.GONE);


                                        System.out.println("+++++ getBody : " + result.get(result.size() - 1).getBody());
                                        System.out.println("+++++ BALANCE : " + result.get(result.size() - 1).getBalance());
                                        System.out.println("+++++ AMOUNt : " + result.get(result.size() - 1).getAmount());
                                    }
                                    BankConstantsData.TranscationsResult.addAll(result);
                                }
                            }.execute();
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

    private void GotoMutualFundsActivity(int position, ArrayList<LoanModel> strings) {
        Intent intent = new Intent(context, LoanDetailsActivity.class);
        intent.putExtra(BankConstantsData.LOAN_TYPE, strings.get(position).getName().toString());
        switch (position) {
            case 0:
                intent.putExtra(BankConstantsData.LOAN_WEB, "mutual funds");
                break;
            case 1:
                intent.putExtra(BankConstantsData.LOAN_WEB, "popular funds");
                break;
            case 2:
                intent.putExtra(BankConstantsData.LOAN_WEB, "explore funds");
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.IvIFSCCode:
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
                break;
            case R.id.IvUSSDCode:
                startActivity(new Intent(context, USSDBankingActivity.class));
                break;
            case R.id.IvNetBanking:
                startActivity(new Intent(context, NetBankinActivity.class));
                break;
            case R.id.IvBankHoliday:
                break;
            case R.id.IvBankATMBox:
                startActivity(new Intent(context, NearByActivity.class));
                break;
            case R.id.TxtBankTranscation:
                startActivity(new Intent(context, TranscationActivity.class));
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
}