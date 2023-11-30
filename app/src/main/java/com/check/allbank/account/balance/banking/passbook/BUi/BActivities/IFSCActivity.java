package com.check.allbank.account.balance.banking.passbook.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.check.allbank.account.balance.banking.passbook.AdverClass;
import com.check.allbank.account.balance.banking.passbook.BConstants.BankConstantsData;
import com.check.allbank.account.balance.banking.passbook.BUi.BAdapters.BankAdapter;
import com.check.allbank.account.balance.banking.passbook.BUi.BAdapters.BranchAdapter;
import com.check.allbank.account.balance.banking.passbook.BUi.BAdapters.DistrictAdapter;
import com.check.allbank.account.balance.banking.passbook.BUi.BAdapters.StateAdapter;
import com.check.allbank.account.balance.banking.passbook.BUtilsClasses.BankPreferences;
import com.check.allbank.account.balance.banking.passbook.BuildConfig;
import com.check.allbank.account.balance.banking.passbook.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class IFSCActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle;
    private RecyclerView RvIFSCBank, RvIFSCState, RvIFSCDistrict, RvIFSCBranch;
    private String IFSCType;
    private ArrayList<String> BankList = new ArrayList<>();
    private ArrayList<String> StateList = new ArrayList<>();
    private ArrayList<String> DistrictList = new ArrayList<>();
    private ArrayList<String> BranchList = new ArrayList<>();
    private String BankStr, StateStr, DistrictStr, BranchStr;
    private EditText EdtIFSCSearch;
    private BankAdapter bankAdapter;
    private DistrictAdapter districtAdapter;
    private StateAdapter stateAdapter;
    private BranchAdapter branchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ifscactivity);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        ImgShareApp = (ImageView) findViewById(R.id.ImgShareApp);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        RvIFSCBank = (RecyclerView) findViewById(R.id.RvIFSCBank);
        RvIFSCState = (RecyclerView) findViewById(R.id.RvIFSCState);
        RvIFSCBranch = (RecyclerView) findViewById(R.id.RvIFSCBranch);
        RvIFSCDistrict = (RecyclerView) findViewById(R.id.RvIFSCDistrict);
        EdtIFSCSearch = (EditText) findViewById(R.id.EdtIFSCSearch);
        if (new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, "").isEmpty()) {
            IFSCType = "BANK";
            RvIFSCBank.setVisibility(View.VISIBLE);
            RvIFSCState.setVisibility(View.GONE);
            RvIFSCDistrict.setVisibility(View.GONE);
            RvIFSCBranch.setVisibility(View.GONE);
        } else if (new BankPreferences(context).getPrefString(BankPreferences.BRANCH_NAME, "").isEmpty()) {
            IFSCType = "STATE";
            BankStr = new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, "");
            RvIFSCBank.setVisibility(View.GONE);
            RvIFSCState.setVisibility(View.VISIBLE);
            RvIFSCBranch.setVisibility(View.GONE);
            RvIFSCDistrict.setVisibility(View.GONE);
        }

        ImgBack.setVisibility(View.VISIBLE);
        ImgShareApp.setVisibility(View.VISIBLE);
        TxtTitle.setText(R.string.ifsc_code);
    }

    private void BankInitListeners() {
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
        EdtIFSCSearch.addTextChangedListener(this);
    }

    private void BankInitActions() {
        AdverClass.ShowLayoutBannerAds(context, ((ProgressBar) findViewById(R.id.progressBarAd)), (RelativeLayout) findViewById(R.id.RlAdver));
        RvIFSCBank.setLayoutManager(new LinearLayoutManager(context));
        RvIFSCState.setLayoutManager(new LinearLayoutManager(context));
        RvIFSCDistrict.setLayoutManager(new LinearLayoutManager(context));
        RvIFSCBranch.setLayoutManager(new LinearLayoutManager(context));
        BankList = new ArrayList<>();
        StateList = new ArrayList<>();
        DistrictList = new ArrayList<>();
        BranchList = new ArrayList<>();
        int i = 0;
        if (IFSCType.equalsIgnoreCase("BANK")) {
            BankList.clear();
            TxtTitle.setText("Select Bank");
            try {
                String[] list = getAssets().list("IFSC Code");
                if (list == null) {
                    return;
                }
                if (list.length > 0) {
                    int length = list.length;
                    while (i < length) {
                        String str = list[i];
                        if (str.contains(".txt")) {
                            BankList.add(str.replace(".txt", ""));
                        } else {
                            BankList.add(str);
                        }
                        i++;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            bankAdapter = new BankAdapter(context, BankList, new BankAdapter.BankListener() {
                @Override
                public void BankClick(int pos, ArrayList<String> strings) {
                    BankStr = strings.get(pos).toString();
                    new BankPreferences(context).putPrefString(BankPreferences.BANK_NAME, BankStr);
                    IFSCType = "STATE";
                    BankInitActions();
                    RvIFSCBank.setVisibility(View.GONE);
                    RvIFSCState.setVisibility(View.VISIBLE);
                    RvIFSCBranch.setVisibility(View.GONE);
                    RvIFSCDistrict.setVisibility(View.GONE);
                }
            });
            RvIFSCBank.setAdapter(bankAdapter);
            EdtIFSCSearch.setText("");
            EdtIFSCSearch.setHint("Select Bank");
        } else if (IFSCType.equalsIgnoreCase("STATE")) {
            TxtTitle.setText("Select State");
            try {
                AssetManager manager = getAssets();
                InputStreamReader streamReader = new InputStreamReader(manager.open("IFSC Code/" + BankStr + ".txt"), "UTF-8");
                BufferedReader reader = new BufferedReader(streamReader);
                StateList.addAll(Arrays.asList(reader.readLine().split("\\*")));
                Log.d("tytyt", "" + StateList.size());
                reader.close();
                streamReader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stateAdapter = new StateAdapter(context, StateList, new StateAdapter.BankListener() {
                @Override
                public void BankClick(int pos, ArrayList<String> strings) {
                    StateStr = strings.get(pos).toString();
                    IFSCType = "DISTRICT";
                    BankInitActions();
                    RvIFSCBank.setVisibility(View.GONE);
                    RvIFSCState.setVisibility(View.GONE);
                    RvIFSCDistrict.setVisibility(View.VISIBLE);
                    RvIFSCBranch.setVisibility(View.GONE);
                }
            });
            RvIFSCState.setAdapter(stateAdapter);
            EdtIFSCSearch.setText("");
            EdtIFSCSearch.setHint("Select State");
        } else if (IFSCType.equalsIgnoreCase("DISTRICT")) {
            TxtTitle.setText("Select District");
            try {
                AssetManager assetManager = getAssets();
                InputStreamReader streamReader = new InputStreamReader(assetManager.open("IFSC Code/" + BankStr + ".txt"), "UTF-8");
                BufferedReader reader = new BufferedReader(streamReader);
                reader.readLine();
                String[] strings = reader.readLine().split("\\*\\*");
                if (strings.length > 0) {
                    int length = strings.length;
                    while (true) {
                        if (i >= length) {
                            break;
                        }
                        String string = strings[i];
                        if (string.contains(StateStr)) {
                            DistrictList.addAll(Arrays.asList(string.split("->")[1].split("\\*")));
                            break;
                        }
                        i++;
                    }
                    reader.close();
                    streamReader.close();
                    districtAdapter = new DistrictAdapter(context, DistrictList, new DistrictAdapter.BankListener() {
                        @Override
                        public void BankClick(int pos, ArrayList<String> strings) {
                            DistrictStr = strings.get(pos).toString();
                            IFSCType = "BRANCH";
                            BankInitActions();
                            RvIFSCBank.setVisibility(View.GONE);
                            RvIFSCState.setVisibility(View.GONE);
                            RvIFSCDistrict.setVisibility(View.GONE);
                            RvIFSCBranch.setVisibility(View.VISIBLE);
                        }
                    });
                    RvIFSCDistrict.setAdapter(districtAdapter);
                    EdtIFSCSearch.setText("");
                    EdtIFSCSearch.setHint("Select District");
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (IFSCType.equalsIgnoreCase("BRANCH")) {
            TxtTitle.setText("Select Branch");
            try {
                AssetManager assetManager = getAssets();
                InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open("IFSC Code/" + BankStr + ".txt"), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                bufferedReader.readLine();
                bufferedReader.readLine();
                String[] split = bufferedReader.readLine().split("\\*\\*");
                if (split.length > 0) {
                    int length = split.length;
                    while (true) {
                        if (i >= length) {
                            break;
                        }
                        String split3 = split[i];
                        if (split3.contains(StateStr + "->" + DistrictStr)) {
                            BranchList.addAll(Arrays.asList(split3.split("->")[2].split("\\*")));
                            break;
                        }
                        i++;
                    }
                    bufferedReader.close();
                    inputStreamReader.close();
                    branchAdapter = new BranchAdapter(context, BranchList, new BranchAdapter.BankListener() {
                        @Override
                        public void BankClick(int pos, ArrayList<String> strings) {
                            AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
                                @Override
                                public void AdListen() {
                                    BranchStr = strings.get(pos).toString();

                                    new BankPreferences(context).putPrefString(BankPreferences.STATE_NAME, StateStr);
                                    new BankPreferences(context).putPrefString(BankPreferences.DISTRICT_NAME, DistrictStr);
                                    new BankPreferences(context).putPrefString(BankPreferences.BRANCH_NAME, BranchStr);
                                    startActivity(new Intent(context, IFSCDetailsActivity.class)
                                            .putExtra(BankConstantsData.IFSC_BANK, BankStr)
                                            .putExtra(BankConstantsData.IFSC_STATE, StateStr)
                                            .putExtra(BankConstantsData.IFSC_DISTRICT, DistrictStr)
                                            .putExtra(BankConstantsData.IFSC_BRANCH, BranchStr));
                                    finish();
                                }
                            });
                        }
                    });
                    RvIFSCBranch.setAdapter(branchAdapter);
                    EdtIFSCSearch.setText("");
                    EdtIFSCSearch.setHint("Select Branch");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ImgBack:
                onBackPressed();
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
            e.toString();
        }
    }

    @Override
    public void onBackPressed() {
        if (RvIFSCBranch.getVisibility() == View.VISIBLE) {
            BranchStr = "";
            IFSCType = "DISTRICT";
            BankInitActions();
            RvIFSCBranch.setVisibility(View.GONE);
            RvIFSCDistrict.setVisibility(View.VISIBLE);
            RvIFSCState.setVisibility(View.GONE);
            RvIFSCBank.setVisibility(View.GONE);
        } else if (RvIFSCDistrict.getVisibility() == View.VISIBLE) {
            BranchStr = "";
            DistrictStr = "";
            IFSCType = "STATE";
            BankInitActions();
            RvIFSCBranch.setVisibility(View.GONE);
            RvIFSCDistrict.setVisibility(View.GONE);
            RvIFSCState.setVisibility(View.VISIBLE);
            RvIFSCBank.setVisibility(View.GONE);
        } else if (RvIFSCState.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (IFSCType.equalsIgnoreCase("BANK")) {
            BankStr = BankList.get(i).toString();
            IFSCType = "STATE";
            BankInitActions();
        } else if (IFSCType.equalsIgnoreCase("STATE")) {
            StateStr = StateList.get(i).toString();
            IFSCType = "DISTRICT";
            BankInitActions();
        } else if (IFSCType.equalsIgnoreCase("DISTRICT")) {
            DistrictStr = DistrictList.get(i).toString();
            IFSCType = "BRANCH";
            BankInitActions();
        } else if (IFSCType.equalsIgnoreCase("BRANCH")) {
            AdverClass.ShowLayoutInterstitialAd(context, new AdverClass.setAdListerner() {
                @Override
                public void AdListen() {
                    BranchStr = BranchList.get(i).toString();
                    startActivity(new Intent(context, IFSCDetailsActivity.class)
                            .putExtra(BankConstantsData.IFSC_BANK, BankStr)
                            .putExtra(BankConstantsData.IFSC_STATE, StateStr)
                            .putExtra(BankConstantsData.IFSC_DISTRICT, DistrictStr)
                            .putExtra(BankConstantsData.IFSC_BRANCH, BranchStr));
                }
            });
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (IFSCType.equalsIgnoreCase("BANK")) {
            bankAdapter.getFilter().filter(s.toString());
        } else if (IFSCType.equalsIgnoreCase("STATE")) {
            stateAdapter.getFilter().filter(s.toString());
        } else if (IFSCType.equalsIgnoreCase("DISTRICT")) {
            districtAdapter.getFilter().filter(s.toString());
        } else if (IFSCType.equalsIgnoreCase("BRANCH")) {
            branchAdapter.getFilter().filter(s.toString());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}