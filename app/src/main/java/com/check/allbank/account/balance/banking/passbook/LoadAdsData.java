package com.check.allbank.account.balance.banking.passbook;
import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class LoadAdsData extends Application {
    private static LoadAdsData loadAdsData;
    private RequestQueue requestQueue;
    private static Context context;

    private LoadAdsData(Context context) {
        LoadAdsData.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized LoadAdsData getInstance(Context context) {
        if (loadAdsData == null) {
            loadAdsData = new LoadAdsData(context);
        }
        return loadAdsData;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}