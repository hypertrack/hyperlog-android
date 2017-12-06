package com.hypertrack.hyperlog.error;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by piyush on 25/02/17.
 */
public class HLErrorResponse {
    private int errorCode;
    private String errorMessage;

    public HLErrorResponse() {
        errorCode = HLErrorCode.Code.UNHANDLED_ERROR;
        errorMessage = HLErrorCode.Message.UNHANDLED_ERROR;
    }

    public HLErrorResponse(String errorMessage) {
        errorCode = HLErrorCode.Code.OTHER_ERROR;
        this.errorMessage = errorMessage;
    }

    public HLErrorResponse(VolleyError error) {
        processError(error);
    }

    public void processError(VolleyError error) {
        if (error != null) {
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                NetworkResponse networkResponse = new NetworkResponse(
                        HLErrorCode.Code.NETWORK_UNAVAILABLE_ERROR, null, null, false);
                error = new VolleyError(networkResponse);
            }

            if (error.networkResponse == null) {
                NetworkResponse networkResponse = new NetworkResponse(
                        HLErrorCode.Code.NO_RESPONSE_ERROR, null, null, false);

               error = new VolleyError(networkResponse);
                return;
            }
        }

        errorCode = HLNetworkErrorUtil.getErrorCode(error);
        errorMessage = HLNetworkErrorUtil.getMessage(error);
    }

    public HLErrorResponse(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public HLErrorResponse(HLErrorCode.Type type) {
        switch (type) {
            case NETWORK_DISABLED:
                errorCode = HLErrorCode.Code.NETWORK_DISABLED_ERROR;
                errorMessage = HLErrorCode.Message.NETWORK_DISABLED_ERROR;
                break;
            case NETWORK_UNAVAILABLE:
                errorCode = HLErrorCode.Code.NETWORK_UNAVAILABLE_ERROR;
                errorMessage = HLErrorCode.Message.NETWORK_UNAVAILABLE_ERROR;
                break;

            default:
                errorCode = HLErrorCode.Code.UNHANDLED_ERROR;
                errorMessage = HLErrorCode.Message.UNHANDLED_ERROR;
                break;
        }
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
