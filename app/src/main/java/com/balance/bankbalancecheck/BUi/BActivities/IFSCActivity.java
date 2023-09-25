package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BUi.BAdapters.BankAdapter;
import com.balance.bankbalancecheck.BUi.BAdapters.DistrictAdapter;
import com.balance.bankbalancecheck.BUi.BAdapters.StateAdapter;
import com.balance.bankbalancecheck.BUtilsClasses.BankPreferences;
import com.balance.bankbalancecheck.BuildConfig;
import com.balance.bankbalancecheck.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class IFSCActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Context context;
    private ImageView ImgBack,ImgShareApp ;
    private TextView TxtTitle;
    private RecyclerView RvIFSCBank, RvIFSCState, RvIFSCDistrict, RvIFSCBranch;
    private String IFSCType;
    private ArrayList<String> BankList = new ArrayList<>();
    private ArrayList<String> StateList = new ArrayList<>();
    private ArrayList<String> DistrictList = new ArrayList<>();
    private ArrayList<String> BranchList = new ArrayList<>();
    private String BankStr, StateStr, DistrictStr, BranchStr;
    private AutoCompleteTextView AutoIFSCSearch;
    private ImageView IvIFSCSearch;

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
        AutoIFSCSearch = (AutoCompleteTextView) findViewById(R.id.AutoIFSCSearch);
        IvIFSCSearch = (ImageView) findViewById(R.id.IvIFSCSearch);
        if (new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, "").isEmpty()) {
            IFSCType = "BANK";
        } else {
            IFSCType = "STATE";
        }
        RvIFSCBank.setVisibility(View.VISIBLE);
        RvIFSCState.setVisibility(View.GONE);
        RvIFSCDistrict.setVisibility(View.GONE);
        RvIFSCBranch.setVisibility(View.GONE);
    }

    private void BankInitListeners() {
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
        AutoIFSCSearch.setOnItemClickListener(this);
    }

    private void BankInitActions() {
        ImgBack.setVisibility(View.VISIBLE);
        TxtTitle.setText(R.string.ifsc_code);
        RvIFSCBank.setLayoutManager(new LinearLayoutManager(context));
        RvIFSCState.setLayoutManager(new LinearLayoutManager(context));
        RvIFSCDistrict.setLayoutManager(new LinearLayoutManager(context));
        RvIFSCBranch.setLayoutManager(new LinearLayoutManager(context));

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
            BankAdapter bankAdapter = new BankAdapter(context, BankList, new BankAdapter.BankListener() {
                @Override
                public void BankClick(int pos, ArrayList<String> strings) {
                    BankStr = strings.get(pos).toString();
                    IFSCType = "STATE";
                    BankInitActions();
                    RvIFSCBank.setVisibility(View.GONE);
                    RvIFSCState.setVisibility(View.VISIBLE);
                    RvIFSCBranch.setVisibility(View.GONE);
                    RvIFSCDistrict.setVisibility(View.GONE);
                }
            });
            RvIFSCBank.setAdapter(bankAdapter);
            AutoIFSCSearch.setText("");
            AutoIFSCSearch.setHint("Select Bank");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, BankList);
            AutoIFSCSearch.setThreshold(1);
            AutoIFSCSearch.setAdapter(adapter);
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
            StateAdapter stateAdapter = new StateAdapter(context, StateList, new StateAdapter.BankListener() {
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
            AutoIFSCSearch.setText("");
            AutoIFSCSearch.setHint("Select State");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, StateList);
            AutoIFSCSearch.setThreshold(1);
            AutoIFSCSearch.setAdapter(adapter);
        } else if (IFSCType.equalsIgnoreCase("DISTRICT")) {
            TxtTitle.setText("Select District");
            try {
                AssetManager assetManager = getAssets();
                InputStreamReader streamReader = new InputStreamReader(assetManager.open("IFSC_Code/" + StateStr + ".txt"), "UTF-8");
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
                    return;
                }
                DistrictAdapter districtAdapter = new DistrictAdapter(context, DistrictList, new DistrictAdapter.BankListener() {
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
                RvIFSCState.setAdapter(districtAdapter);
                AutoIFSCSearch.setText("");
                AutoIFSCSearch.setHint("Select District");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, DistrictList);
                AutoIFSCSearch.setThreshold(1);
                AutoIFSCSearch.setAdapter(adapter);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (IFSCType.equalsIgnoreCase("BRANCH")) {
            TxtTitle.setText("Select Branch");
            try {
                AssetManager assetManager = getAssets();
                InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open("IFSC_Code/" + DistrictStr + ".txt"), "UTF-8");
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
                        if (split3.contains(StateStr + "->" + DistrictList)) {
                            BranchList.addAll(Arrays.asList(split3.split("->")[2].split("\\*")));
                            break;
                        }
                        i++;
                    }
                    bufferedReader.close();
                    inputStreamReader.close();
                    return;
                }
                AutoIFSCSearch.setText("");
                AutoIFSCSearch.setHint("Select Branch");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, BranchList);
                AutoIFSCSearch.setThreshold(1);
                AutoIFSCSearch.setAdapter(adapter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

      /*  if (s0.equalsIgnoreCase("STATE")) {
            AssetManager assets = getAssets();
            InputStreamReader inputStreamReader = new InputStreamReader(assets.open("IFSC_Code/" + n0 + ".txt"), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            q0.addAll(Arrays.asList(bufferedReader.readLine().split("\\*")));
            Log.d("tytyt", "" + q0.size());
            bufferedReader.close();
            inputStreamReader.close();
        } else if (s0.equalsIgnoreCase("DISTRICT")) {
            AssetManager assets2 = getAssets();
            InputStreamReader inputStreamReader2 = new InputStreamReader(assets2.open("IFSC_Code/" + n0 + ".txt"), "UTF-8");
            BufferedReader bufferedReader2 = new BufferedReader(inputStreamReader2);
            bufferedReader2.readLine();
            String[] split = bufferedReader2.readLine().split("\\*\\*");
            if (split.length > 0) {
                int length2 = split.length;
                while (true) {
                    if (i >= length2) {
                        break;
                    }
                    String str2 = split[i];
                    if (str2.contains(t0)) {
                        q0.addAll(Arrays.asList(str2.split("->")[1].split("\\*")));
                        break;
                    }
                    i++;
                }
                bufferedReader2.close();
                inputStreamReader2.close();
                return;
            }
            l0.setVisibility(0);
            r0.setVisibility(8);
        } else if (s0.equalsIgnoreCase("BRANCH")) {
            AssetManager assets3 = getAssets();
            InputStreamReader inputStreamReader3 = new InputStreamReader(assets3.open("IFSC_Code/" + n0 + ".txt"), "UTF-8");
            BufferedReader bufferedReader3 = new BufferedReader(inputStreamReader3);
            bufferedReader3.readLine();
            bufferedReader3.readLine();
            String[] split2 = bufferedReader3.readLine().split("\\*\\*");
            if (split2.length > 0) {
                int length3 = split2.length;
                while (true) {
                    if (i >= length3) {
                        break;
                    }
                    String str3 = split2[i];
                    if (str3.contains(t0 + "->" + o0)) {
                        q0.addAll(Arrays.asList(str3.split("->")[2].split("\\*")));
                        break;
                    }
                    i++;
                }
                bufferedReader3.close();
                inputStreamReader3.close();
                return;
            }
            l0.setVisibility(0);
            r0.setVisibility(8);
        }*/
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
            BranchStr = "";
            DistrictStr = "";
            StateStr = "";
            IFSCType = "BANK";
            BankInitActions();
            RvIFSCBranch.setVisibility(View.GONE);
            RvIFSCDistrict.setVisibility(View.GONE);
            RvIFSCState.setVisibility(View.GONE);
            RvIFSCBank.setVisibility(View.VISIBLE);
        } else {
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
            BranchStr = BranchList.get(i).toString();
            startActivity(new Intent(context, IFSCDetailsActivity.class)
                    .putExtra(BankConstantsData.IFSC_BANK, BankStr)
                    .putExtra(BankConstantsData.IFSC_STATE, StateStr)
                    .putExtra(BankConstantsData.IFSC_DISTRICT, DistrictStr)
                    .putExtra(BankConstantsData.IFSC_BRANCH, BranchStr));
        }
    }
}