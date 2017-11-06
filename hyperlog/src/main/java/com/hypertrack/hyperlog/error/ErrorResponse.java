package com.hypertrack.hyperlog.error;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by piyush on 25/02/17.
 */
public class ErrorResponse {
    private int errorCode;
    private String errorMessage;

    public ErrorResponse() {
        errorCode = ErrorCode.Code.UNHANDLED_ERROR;
        errorMessage = ErrorCode.Message.UNHANDLED_ERROR;
    }

    public ErrorResponse(String errorMessage) {
        errorCode = ErrorCode.Code.OTHER_ERROR;
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(VolleyError error) {
        processError(error);
    }

    public void processError(VolleyError error) {
        if (error != null) {
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                NetworkResponse networkResponse = new NetworkResponse(
                        ErrorCode.Code.NETWORK_UNAVAILABLE_ERROR, null, null, false);
                error = new VolleyError(networkResponse);
            }

            if (error.networkResponse == null) {
                NetworkResponse networkResponse = new NetworkResponse(
                        ErrorCode.Code.NO_RESPONSE_ERROR, null, null, false);

               error = new VolleyError(networkResponse);
                return;
            }
        }

        errorCode = NetworkErrorUtil.getErrorCode(error);
        errorMessage = NetworkErrorUtil.getMessage(error);
    }

    public ErrorResponse(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(ErrorCode.Type type) {
        switch (type) {
            case NETWORK_DISABLED:
                errorCode = ErrorCode.Code.NETWORK_DISABLED_ERROR;
                errorMessage = ErrorCode.Message.NETWORK_DISABLED_ERROR;
                break;
            case NETWORK_UNAVAILABLE:
                errorCode = ErrorCode.Code.NETWORK_UNAVAILABLE_ERROR;
                errorMessage = ErrorCode.Message.NETWORK_UNAVAILABLE_ERROR;
                break;

            default:
                errorCode = ErrorCode.Code.UNHANDLED_ERROR;
                errorMessage = ErrorCode.Message.UNHANDLED_ERROR;
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
