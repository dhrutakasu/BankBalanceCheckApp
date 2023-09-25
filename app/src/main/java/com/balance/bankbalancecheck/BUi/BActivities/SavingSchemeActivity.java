package com.balance.bankbalancecheck.BUi.BActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BUi.BAdapters.CalculatorsAdapter;
import com.balance.bankbalancecheck.BUtils.SchemesWebData;
import com.balance.bankbalancecheck.R;

import java.util.ArrayList;

public class SavingSchemeActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView RvSavingSchemes;
    private ArrayList<String> strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_scheme);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        RvSavingSchemes = (RecyclerView) findViewById(R.id.RvSavingSchemes);
    }

    private void BankInitListeners() {

    }

    private void BankInitActions() {
        strings = new ArrayList<>();
        strings.add(getString(R.string.public_pf));
        strings.add(getString(R.string.employee_pf));
        strings.add(getString(R.string.natinal_ps));
        strings.add(getString(R.string.sensor_cs));
        strings.add(getString(R.string.national_sc));
        strings.add(getString(R.string.post_office_ss));
        strings.add(getString(R.string.pm_vaya));
        strings.add(getString(R.string.pm_jan_dhan));
        RvSavingSchemes.setLayoutManager(new GridLayoutManager(context, 1));
        RvSavingSchemes.setAdapter(new CalculatorsAdapter(context, strings, new CalculatorsAdapter.setClickListener() {
            @Override
            public void CalculatorsClickListener(int position) {
                GotoSchemeActivity(position);
            }
        }));
    }

    private void GotoSchemeActivity(int position) {
        Intent intent=new Intent(context,ViewSchemesDetailsActivity.class);
        System.out.println("===== : "+new SchemesWebData().getSCHEME_PM_VAYA());
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
        intent.putExtra(BankConstantsData.SCHEMES_TITLE, strings.get(position).toString());
        startActivity(intent);
    }
}