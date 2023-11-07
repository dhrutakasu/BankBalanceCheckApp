package com.balance.bankbalancecheck.BUtils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balance.bankbalancecheck.AdverClass;
import com.balance.bankbalancecheck.BUi.BActivities.HomeScreenActivity;
import com.balance.bankbalancecheck.R;

public class ExitDialog extends Dialog {
    private final HomeScreenActivity activity;
    public ExitListener exitListener;

    public interface ExitListener {

        void onExit();
    }

    public ExitDialog(HomeScreenActivity activity, Context context, ExitListener exitListener) {
        super(context);
        this.activity = activity;
        this.exitListener = exitListener;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_exit);
        AdverClass.ShowLayoutNativeAds(activity, ((ProgressBar) findViewById(R.id.progressBarAd)), (RelativeLayout) findViewById(R.id.RlAdver));
        TextView TvDialogExit = (TextView) findViewById(R.id.TxtExit);
        TextView TxtCancel = (TextView) findViewById(R.id.TxtCancel);
        TxtCancel.setOnClickListener(view -> {
            dismiss();
        });
        TvDialogExit.setOnClickListener(view -> {
            exitListener.onExit();
        });
    }
}
