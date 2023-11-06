package com.balance.bankbalancecheck;
import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class LoadData extends Application {
    private static LoadData loadData;
    private RequestQueue queue;
    private static Context context;

    private LoadData(Context context) {
        LoadData.context = context;
        queue = getQueue();
    }

    public static synchronized LoadData getInstance(Context context) {
        if (loadData == null) {
            loadData = new LoadData(context);
        }
        return loadData;
    }

    public RequestQueue getQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return queue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getQueue().add(req);
    }
}