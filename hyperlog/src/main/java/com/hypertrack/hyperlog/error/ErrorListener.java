package com.hypertrack.hyperlog.error;

import com.android.volley.VolleyError;

public interface ErrorListener {
        /**
         * Callback method that an error has been occurred with the
         * provided error code and optional user-readable message.
         */
        public void onErrorResponse(VolleyError error);
    }