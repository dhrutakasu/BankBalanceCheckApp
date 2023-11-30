package com.check.allbank.account.balance.banking.passbook.BUi.BActivities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.check.allbank.account.balance.banking.passbook.AdverClass;
import com.check.allbank.account.balance.banking.passbook.BConstants.BankConstantsData;
import com.check.allbank.account.balance.banking.passbook.BHelper.BankBalanceHelper;
import com.check.allbank.account.balance.banking.passbook.BModel.SMSModel;
import com.check.allbank.account.balance.banking.passbook.BUi.BAdapters.TranscationAdapter;
import com.check.allbank.account.balance.banking.passbook.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TransactionActivity extends AppCompatActivity implements View.OnClickListener, TranscationAdapter.TransactionListener {
    private Context context;
    private RelativeLayout RlTopBar;
    private ImageView ImgBack, ImgFilter;
    private TextView TxtTitle, TxtActiveBalanceAmount;
    private RecyclerView RvTranscations;
    private BankBalanceHelper helper;
    private String BankName;
    private String Type;
    private Calendar calendarStart = Calendar.getInstance();
    private Calendar calendarEnd = Calendar.getInstance();
    private boolean IsEndDate = false, IsStartDate = false;
    private String IsEndAmount = "", IsStartAmount = "";
    private ArrayList<SMSModel> SmsList;
    private TranscationAdapter transcationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcation);
        BankInitViews();
        BankInitListeners();
        BankInitActions();
    }

    private void BankInitViews() {
        context = this;
        ImgBack = (ImageView) findViewById(R.id.ImgBack);
        ImgFilter = (ImageView) findViewById(R.id.ImgFilter);
        RlTopBar = (RelativeLayout) findViewById(R.id.RlTopBar);
        TxtTitle = (TextView) findViewById(R.id.TxtTitle);
        TxtActiveBalanceAmount = (TextView) findViewById(R.id.TxtActiveBalanceAmount);
        RvTranscations = (RecyclerView) findViewById(R.id.RvTranscations);
    }

    private void BankInitListeners() {
        ImgBack.setOnClickListener(this);
        ImgFilter.setOnClickListener(this);
    }

    private void BankInitActions() {
        AdverClass.ShowLayoutBannerAds(context, ((ProgressBar) findViewById(R.id.progressBarAd)), (RelativeLayout) findViewById(R.id.RlAdver));
        if (!getIntent().getStringExtra(BankConstantsData.TRANSCATION).equalsIgnoreCase("")) {
            Type = getIntent().getStringExtra(BankConstantsData.TRANSCATION);
        } else {
            Type = "all";
        }
        BankName = getIntent().getStringExtra(BankConstantsData.BANK_NAME);
        helper = new BankBalanceHelper(context);
        RlTopBar.setBackgroundColor(getResources().getColor(R.color.white));
        ImgBack.setVisibility(View.VISIBLE);
        ImgFilter.setVisibility(View.VISIBLE);
        ImgBack.setColorFilter(getResources().getColor(R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        TxtTitle.setText(R.string.bank_transactions);
        TxtTitle.setTextColor(getResources().getColor(R.color.black));
        if (helper.getAllSMSType(BankName, Type).size()>=0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String Currency = "";
                if (helper.getAllSMSType(BankName, Type).get(0).getBodyMsg().contains("Rs")) {
                    Currency = "Rs.";
                }
                if (helper.getAllSMSType(BankName, Type).get(0).getBodyMsg().contains("INR")) {
                    Currency = "INR.";
                }
                TxtActiveBalanceAmount.setText(Currency + " " + helper.getAllSMSType(BankName, "all").get(0).getBalance());
            }
        }
        SmsList = helper.getAllSMSType(BankName, Type);
        RvTranscations.setLayoutManager(new LinearLayoutManager(context));
        transcationAdapter = new TranscationAdapter(context, SmsList, this);
        RvTranscations.setAdapter(transcationAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ImgBack:
                finish();
                break;
            case R.id.ImgFilter:
                GotoFilter();
                break;
        }
    }

    private void GotoFilter() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
        ConstraintLayout ConsAllTrans = dialog.findViewById(R.id.ConsAllTrans);
        ConstraintLayout ConsCredit = dialog.findViewById(R.id.ConsCredit);
        ConstraintLayout ConsDebit = dialog.findViewById(R.id.ConsDebit);
        TextView TxtAllTrans = dialog.findViewById(R.id.TxtAllTrans);
        TextView TxtCredit = dialog.findViewById(R.id.TxtCredit);
        TextView TxtDebit = dialog.findViewById(R.id.TxtDebit);
        TextView TxtStartDate = dialog.findViewById(R.id.TxtStartDate);
        TextView TxtEndDate = dialog.findViewById(R.id.TxtEndDate);
        EditText TxtStartAmount = dialog.findViewById(R.id.TxtStartAmount);
        EditText TxtEndAmount = dialog.findViewById(R.id.TxtEndAmount);
        ImageView IvBankClose = dialog.findViewById(R.id.IvBankClose);
        TextView TxtOkFilter = dialog.findViewById(R.id.TxtOkFilter);
        TextView TxtResetFilter = dialog.findViewById(R.id.TxtResetFilter);
        IvBankClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (Type.equalsIgnoreCase("all")) {
            TxtAllTrans.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_on, 0);
            TxtCredit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_off, 0);
            TxtDebit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_off, 0);
        } else if (Type.equalsIgnoreCase("credit")) {
            TxtAllTrans.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_off, 0);
            TxtCredit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_on, 0);
            TxtDebit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_off, 0);
        } else if (Type.equalsIgnoreCase("debit")) {
            TxtAllTrans.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_off, 0);
            TxtCredit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_off, 0);
            TxtDebit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_on, 0);
        }
        if (IsStartDate) {
            TxtStartDate.setText(calendarStart.get(Calendar.DAY_OF_MONTH) + "-" + (calendarStart.get(Calendar.MONTH) + 1) + "-" + calendarStart.get(Calendar.YEAR));
        }
        if (IsEndDate) {
            TxtEndDate.setText(calendarEnd.get(Calendar.DAY_OF_MONTH) + "-" + (calendarEnd.get(Calendar.MONTH) + 1) + "-" + calendarEnd.get(Calendar.YEAR));
        }

        TxtStartAmount.setText(IsStartAmount);
        TxtEndAmount.setText(IsEndAmount);
        ConsAllTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Type = "all";
                TxtAllTrans.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_on, 0);
                TxtCredit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_off, 0);
                TxtDebit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_off, 0);
            }
        });
        ConsCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Type = "credit";
                TxtAllTrans.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_off, 0);
                TxtCredit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_on, 0);
                TxtDebit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_off, 0);
            }
        });
        ConsDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Type = "debit";
                TxtAllTrans.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_off, 0);
                TxtCredit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_off, 0);
                TxtDebit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_on, 0);
            }
        });
        TxtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendarStart.set(Calendar.YEAR, year);
                        calendarStart.set(Calendar.MONTH, month);
                        calendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        TxtStartDate.setText(calendarStart.get(Calendar.DAY_OF_MONTH) + "-" + (calendarStart.get(Calendar.MONTH) + 1) + "-" + calendarStart.get(Calendar.YEAR));
                        IsStartDate = true;
                    }
                }, calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH), calendarStart.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });
        TxtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendarEnd.set(Calendar.YEAR, year);
                        calendarEnd.set(Calendar.MONTH, month);
                        calendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        TxtEndDate.setText(calendarEnd.get(Calendar.DAY_OF_MONTH) + "-" + (calendarEnd.get(Calendar.MONTH) + 1) + "-" + calendarEnd.get(Calendar.YEAR));
                        IsEndDate = true;
                    }
                }, calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH), calendarEnd.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        TxtResetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Type = "all";
                IsStartDate = false;
                IsEndDate = false;
                IsStartAmount = "";
                IsEndAmount = "";

                TxtStartDate.setText("");
                TxtEndDate.setText("");
                TxtStartAmount.setText("");
                TxtEndAmount.setText("");
                TxtAllTrans.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_on, 0);
                TxtCredit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_off, 0);
                TxtDebit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_off, 0);
            }
        });
        TxtOkFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsEndAmount = TxtEndAmount.getText().toString();
                IsStartAmount = TxtStartAmount.getText().toString();
                if (IsStartDate && !IsEndDate) {
                    Toast.makeText(context, "Please Set End Date.", Toast.LENGTH_SHORT).show();
                } else if (!IsStartDate && IsEndDate) {
                    Toast.makeText(context, "Please Set Start Date.", Toast.LENGTH_SHORT).show();
                } else if (!IsStartAmount.isEmpty() && IsEndAmount.isEmpty()
                        || IsStartAmount.isEmpty() && !IsEndAmount.isEmpty()) {
                    Toast.makeText(context, "Please Set Valid Amount.", Toast.LENGTH_SHORT).show();
                } else if (IsStartDate && IsEndDate && IsStartAmount.isEmpty() && IsEndAmount.isEmpty()) {
                    SmsList = helper.getAllSMSDateType(BankName, Type, calendarStart.getTimeInMillis(), calendarEnd.getTimeInMillis());
                    DialogDismiss(dialog);
                } else if (!IsStartAmount.isEmpty() && !IsEndAmount.isEmpty() && !IsStartDate && !IsEndDate) {
                    SmsList = helper.getAllSMSRupee(BankName, Type, IsStartAmount, IsEndAmount);
                    DialogDismiss(dialog);
                } else if (IsStartDate && IsEndDate && !IsStartAmount.isEmpty() && !IsEndAmount.isEmpty()) {
                    SmsList = helper.getAllSMSAAllData(BankName, Type, String.valueOf(calendarStart.getTimeInMillis()), String.valueOf(calendarEnd.getTimeInMillis()), IsStartAmount, IsEndAmount);
                    DialogDismiss(dialog);
                } else {
                    SmsList = helper.getAllSMSType(BankName, Type);
                    DialogDismiss(dialog);
                }
            }
        });
        dialog.show();
    }

    private void DialogDismiss(Dialog dialog) {
        dialog.dismiss();
        transcationAdapter = new TranscationAdapter(context, SmsList, TransactionActivity.this);
        RvTranscations.setAdapter(transcationAdapter);
    }

    @Override
    public void TransactionClick(int pos, ArrayList<SMSModel> strings) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_transcation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
        ImageView IvBankClose = dialog.findViewById(R.id.IvBankClose);
        TextView TxtBankName = dialog.findViewById(R.id.TxtBankName);
        TextView TxtBankMsg = dialog.findViewById(R.id.TxtBankMsg);
        TxtBankName.setText(strings.get(pos).getBankName());
        TxtBankMsg.setText(strings.get(pos).getBodyMsg());
        IvBankClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}