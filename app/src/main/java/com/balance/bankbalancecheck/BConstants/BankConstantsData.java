package com.balance.bankbalancecheck.BConstants;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class BankConstantsData {
    public static final String SCHEMES_LINK = "SCHEMES_LINK";
    public static final String SCHEMES_TITLE = "SCHEMES_TITLE";
    public static String EMI_Pos="EMI_Pos";
    public static void hideKeyboardFromView(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {
        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            hideKeyboardFromView(activity, focusedView);
        }
    }
}
