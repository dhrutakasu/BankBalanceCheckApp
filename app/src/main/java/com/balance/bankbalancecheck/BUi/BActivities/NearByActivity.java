package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.balance.bankbalancecheck.BUi.BAdapters.NearByAdapter;
import com.balance.bankbalancecheck.R;

import java.util.ArrayList;

public class NearByActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private Context context;
    private ImageView ImgBack, IvSearchIFSC, IvCancelIFSC;
    private TextView TxtTitle;
    private RecyclerView RvNearBy;
    private EditText EdtSearchIFSC;
    private NearByAdapter nearByAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        IvSearchIFSC = (ImageView) findViewById(R.id.IvSearchIFSC);
        IvCancelIFSC = (ImageView) findViewById(R.id.IvCancelIFSC);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        EdtSearchIFSC = (EditText) findViewById(R.id.EdtSearchIFSC);
        RvNearBy = (RecyclerView) findViewById(R.id.RvNearBy);
    }

    private void BankInitListeners() {
        ImgBack.setOnClickListener(this);
        IvSearchIFSC.setOnClickListener(this);
        IvCancelIFSC.setOnClickListener(this);
        EdtSearchIFSC.addTextChangedListener(this);
    }

    private void BankInitActions() {
        ImgBack.setVisibility(View.VISIBLE);
        TxtTitle.setText(R.string.near_by);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("Bank");
        strings.add("Bank ATMs");
        strings.add("Petrol Pump");
        strings.add("Airports");
        strings.add("Gyms");
        strings.add("Hotels");
        strings.add("Movie Theaters");
        strings.add("Museums");
        strings.add("Medical");
        strings.add("Post Office");
        strings.add("Stadiums");
        strings.add("Train Stations");
        strings.add("Zoos");
        strings.add("Malls");
        strings.add("Restaurants");
        strings.add("Bars");
        strings.add("Car Rental");
        strings.add("Coffee Bars");
        strings.add("Gas Stations");
        strings.add("Hospitals");
        strings.add("Bus Stop");
        strings.add("Railway Station");
        strings.add("Tourism Place");
        strings.add("Adventure Park");
        strings.add("District Transport Office (DTO)");
        strings.add("Police Station");
        strings.add("Taxi Stand");
        strings.add("Temples");
        RvNearBy.setLayoutManager(new LinearLayoutManager(context));
        nearByAdapter = new NearByAdapter(context, strings);
        RvNearBy.setAdapter(nearByAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ImgBack:
                finish();
                break;
            case R.id.IvSearchIFSC:
                nearByAdapter.getFilter().filter(EdtSearchIFSC.getText().toString());
                break;
            case R.id.IvCancelIFSC:
                EdtSearchIFSC.setText("");
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            IvCancelIFSC.setVisibility(View.VISIBLE);
        } else {
            IvCancelIFSC.setVisibility(View.GONE);
        }
        nearByAdapter.getFilter().filter(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}