package com.hypertrack.devicelogger.db.Utils;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Aman on 26/09/17.
 */

public class VolleyUtils {
    private static RequestQueue mRequestQueue;

    private static RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    public static <T> void addToRequestQueue(Context context, Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setShouldCache(true);
        req.setRetryPolicy(new DefaultRetryPolicy(5000, 4, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (mRequestQueue == null)
            getRequestQueue(context);
        mRequestQueue.add(req);
    }

    public static void cancelPendingRequests(Context context,Object tag) {
        if (mRequestQueue == null)
            getRequestQueue(context);
        mRequestQueue.cancelAll(tag);
    }
}
