package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balance.bankbalancecheck.AdverClass;
import com.balance.bankbalancecheck.BModel.Holiday;
import com.balance.bankbalancecheck.BUi.BAdapters.BankAdapter;
import com.balance.bankbalancecheck.BUi.BAdapters.HolidayAdapter;
import com.balance.bankbalancecheck.BUi.BAdapters.StateAdapter;
import com.balance.bankbalancecheck.BUtilsClasses.BankPreferences;
import com.balance.bankbalancecheck.BuildConfig;
import com.balance.bankbalancecheck.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class BankHolidayActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private Context context;
    private ImageView ImgBack, ImgShareApp;
    private TextView TxtTitle, TxtTitle2023, TxtTitle2024;
    private ProgressBar ProgressHoliday;
    private ArrayList<String> BankList = new ArrayList<>();
    private EditText EdtBankSearch;
    private BankAdapter bankAdapter;
    private StateAdapter stateAdapter;
    private String BankStr, StateStr;
    private ConstraintLayout ConsEdtHoliday, ConsHoliday;
    private RecyclerView RvHolidayList2023, RvHolidayList2024;
    private String HolidayType;
    private ArrayList<String> StateList = new ArrayList<>();
    private RecyclerView RvHolidayBank, RvHolidayState;
    private ArrayList<Holiday> holidays2023, holidays2024;
    private ConstraintLayout CardBank2023, CardBank2024;
    private AutoCompleteTextView AutoState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_holiday);
        BankInitViews();
        BankInitListeners();

    }

    private void BankInitViews() {
        context = this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        ImgShareApp = (ImageView) findViewById(R.id.ImgShareApp);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        TxtTitle2023 = (TextView) findViewById(R.id.TxtTitle2023);
        TxtTitle2024 = (TextView) findViewById(R.id.TxtTitle2024);
        RvHolidayBank = (RecyclerView) findViewById(R.id.RvHolidayBank);
        RvHolidayState = (RecyclerView) findViewById(R.id.RvHolidayState);
        ConsEdtHoliday = (ConstraintLayout) findViewById(R.id.ConsEdtHoliday);
        ConsHoliday = (ConstraintLayout) findViewById(R.id.ConsHoliday);
        CardBank2023 = (ConstraintLayout) findViewById(R.id.CardBank2023);
        CardBank2024 = (ConstraintLayout) findViewById(R.id.CardBank2024);
        EdtBankSearch = (EditText) findViewById(R.id.EdtBankSearch);
        RvHolidayList2023 = (RecyclerView) findViewById(R.id.RvHolidayList2023);
        RvHolidayList2024 = (RecyclerView) findViewById(R.id.RvHolidayList2024);
        ProgressHoliday = (ProgressBar) findViewById(R.id.ProgressHoliday);
        AutoState = (AutoCompleteTextView) findViewById(R.id.AutoState);
    }

    private void BankInitListeners() {
        ImgBack.setVisibility(View.VISIBLE);
        ImgShareApp.setVisibility(View.GONE);
        TxtTitle.setText(new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, ""));
        ImgBack.setOnClickListener(this);
        ImgShareApp.setOnClickListener(this);
        EdtBankSearch.addTextChangedListener(this);
        CardBank2023.setOnClickListener(this);
        CardBank2024.setOnClickListener(this);
        AutoState.setOnItemClickListener(this);
        RvHolidayBank.setLayoutManager(new LinearLayoutManager(context));
        RvHolidayState.setLayoutManager(new LinearLayoutManager(context));
        BankList = new ArrayList<>();
        StateList = new ArrayList<>();

        if (new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, "").isEmpty()) {
            HolidayType = "BANK";
            RvHolidayBank.setVisibility(View.VISIBLE);
            RvHolidayState.setVisibility(View.GONE);
            ConsEdtHoliday.setVisibility(View.VISIBLE);
            ConsHoliday.setVisibility(View.GONE);
            BankInitActions();
        } else if (new BankPreferences(context).getPrefString(BankPreferences.STATE_NAME, "").isEmpty()) {
            HolidayType = "STATE";
            BankStr = new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, "");
            RvHolidayBank.setVisibility(View.GONE);
            RvHolidayState.setVisibility(View.VISIBLE);
            ConsEdtHoliday.setVisibility(View.VISIBLE);
            ConsHoliday.setVisibility(View.GONE);
            BankInitActions();
        } else {
            try {
                AssetManager manager = getAssets();
                InputStreamReader streamReader = new InputStreamReader(manager.open("IFSC Code/" + new BankPreferences(context).getPrefString(BankPreferences.BANK_NAME, "") + ".txt"), "UTF-8");
                BufferedReader reader = new BufferedReader(streamReader);
                StateList.addAll(Arrays.asList(reader.readLine().split("\\*")));
                Log.d("tytyt", "" + StateList.size());
                reader.close();
                streamReader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, StateList);
            AutoState.setThreshold(1);
            AutoState.setAdapter(adapter);
            GotoGetHoliday(new BankPreferences(context).getPrefString(BankPreferences.STATE_NAME, ""));
            ConsEdtHoliday.setVisibility(View.GONE);
            ConsHoliday.setVisibility(View.VISIBLE);
        }
        RvHolidayList2023.setVisibility(View.GONE);
        RvHolidayList2024.setVisibility(View.GONE);
    }

    private void BankInitActions() {
        AdverClass.ShowLayoutBannerAds(context, ((ProgressBar) findViewById(R.id.progressBarAd)), (RelativeLayout) findViewById(R.id.RlAdver));
        int i = 0;
        if (HolidayType.equalsIgnoreCase("BANK")) {
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
            bankAdapter = new BankAdapter(context, BankList, (pos, strings) -> {
                BankStr = strings.get(pos).toString();
                new BankPreferences(context).putPrefString(BankPreferences.BANK_NAME, BankStr);
                HolidayType = "STATE";
                BankInitActions();
                RvHolidayBank.setVisibility(View.GONE);
                RvHolidayState.setVisibility(View.VISIBLE);
            });
            RvHolidayBank.setAdapter(bankAdapter);
            EdtBankSearch.setText("");
            EdtBankSearch.setHint("Select Bank");
        } else if (HolidayType.equalsIgnoreCase("STATE")) {
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
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, StateList);
            AutoState.setThreshold(1);
            AutoState.setAdapter(adapter);
            stateAdapter = new StateAdapter(context, StateList, new StateAdapter.BankListener() {
                @Override
                public void BankClick(int pos, ArrayList<String> strings) {
                    StateStr = strings.get(pos).toString();
                    new BankPreferences(context).putPrefString(BankPreferences.STATE_NAME, StateStr);
                    ConsEdtHoliday.setVisibility(View.GONE);
                    ConsHoliday.setVisibility(View.VISIBLE);
                    GotoGetHoliday(StateStr);
                }
            });
            RvHolidayState.setAdapter(stateAdapter);
            EdtBankSearch.setText("");
            EdtBankSearch.setHint("Select State");
        }
    }

    private void GotoGetHoliday(String stateStr) {
        AutoState.setText(stateStr);
        TxtTitle.setText("Holiday List");
        holidays2023 = new ArrayList<>();
        holidays2024 = new ArrayList<>();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                System.out.println("-------- list : " + stateStr);
                ProgressHoliday.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Void... voids) {
                String jsonData = "";
                try {
                    InputStream stream = getAssets().open("HolidayList.json");

                    int size = stream.available();
                    byte[] buffer = new byte[size];
                    stream.read(buffer);
                    stream.close();
                    jsonData = new String(buffer);
                } catch (IOException e) {
                }
                String finalJsonData = jsonData;
                runOnUiThread(() -> {
                    try {
                        parseJSON(finalJsonData, stateStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                return jsonData;
            }

            @Override
            protected void onPostExecute(String result) {
                if (holidays2023.size() > 0) {
                    CardBank2023.setVisibility(View.VISIBLE);
//                    RvHolidayList2023.setVisibility(View.INVISIBLE);
                } else {
                    CardBank2023.setVisibility(View.GONE);
                    RvHolidayList2023.setVisibility(View.GONE);
                }
                if (holidays2024.size() > 0) {
                    CardBank2024.setVisibility(View.VISIBLE);
//                    RvHolidayList2024.setVisibility(View.INVISIBLE);
                } else {
                    CardBank2024.setVisibility(View.GONE);
                    RvHolidayList2024.setVisibility(View.GONE);
                }
                AutoState.setVisibility(View.VISIBLE);
                ProgressHoliday.setVisibility(View.GONE);
                super.onPostExecute(result);
            }
        }.execute();
    }

    private void parseJSON(String json, String state) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        System.out.println("------ Holiday ss : " + jsonObject.toString());
        JSONObject states = jsonObject.getJSONObject("states");
        System.out.println("------ Holiday states : " + states.toString());
        JSONArray holidaysArray = states.getJSONArray(state);
        System.out.println("------ Holiday holidaysArray : " + holidaysArray.toString());
        for (int i = 0; i < holidaysArray.length(); i++) {
            JSONObject holidayObject = holidaysArray.getJSONObject(i);
            String date = holidayObject.getString("Date");
            String holidayName = holidayObject.getString("Holiday");
            String day = holidayObject.getString("Day");
            System.out.println("------ holidayName : " + holidayName);
            if (date.contains("2023")) {
                holidays2023.add(new Holiday(state, day, date, holidayName));
                RvHolidayList2023.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                RvHolidayList2023.setAdapter(new HolidayAdapter(context, holidays2023));
            } else {
                holidays2024.add(new Holiday(state, day, date, holidayName));
                RvHolidayList2024.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                RvHolidayList2024.setAdapter(new HolidayAdapter(context, holidays2024));
            }
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
            case R.id.CardBank2023:
                GotoSetUpList(RvHolidayList2023, TxtTitle2023);
                break;
            case R.id.CardBank2024:
                GotoSetUpList(RvHolidayList2024, TxtTitle2024);
                break;
        }
    }

    private void GotoSetUpList(View view, TextView txtTitle) {
        TxtTitle2023.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_right, 0);
        TxtTitle2024.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_right, 0);
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else {
            txtTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
            view.setVisibility(View.VISIBLE);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (HolidayType.equalsIgnoreCase("BANK")) {
            bankAdapter.getFilter().filter(s.toString());
        } else if (HolidayType.equalsIgnoreCase("STATE")) {
            stateAdapter.getFilter().filter(s.toString());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        StateStr = adapterView.getItemAtPosition(i).toString();
        new BankPreferences(context).putPrefString(BankPreferences.STATE_NAME, StateStr);
        GotoGetHoliday(StateStr);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        StateStr = parent.getItemAtPosition(position).toString();
//        new BankPreferences(context).putPrefString(BankPreferences.STATE_NAME, StateStr);
//        GotoGetHoliday(StateStr);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}