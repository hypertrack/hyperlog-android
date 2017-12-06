package com.hypertrack.hyperlog.error;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.ParseError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

public class HLNetworkErrorUtil {

    private static final String TAG = HLNetworkErrorUtil.class.getSimpleName();

    public static int getErrorCode(VolleyError error) {

        if (error == null)
            return HLErrorCode.Code.UNHANDLED_ERROR;

        if (error instanceof ParseError ) {
            return HLErrorCode.Code.PARSE_ERROR;
        }

        if(error instanceof AuthFailureError){
            return HLErrorCode.Code.AUTH_ERROR;
        }

        if (error.networkResponse == null)
            return HLErrorCode.Code.UNHANDLED_ERROR;

        return error.networkResponse.statusCode;
    }

    public static String getMessage(VolleyError error) {

        String errorMessage = HLErrorCode.Message.UNHANDLED_ERROR;

        if (error == null)
            return errorMessage;

        if (!TextUtils.isEmpty(error.getMessage())) {
            return error.getMessage();
        }

        if (error.networkResponse == null)
            return errorMessage;

        String json = "";

        if (error.networkResponse.data != null && error.networkResponse.headers != null) {
            try {
                json = new String(
                        error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        switch (error.networkResponse.statusCode) {
            case HLErrorCode.Code.NETWORK_DISABLED_ERROR:
                errorMessage = HLErrorCode.Message.NETWORK_DISABLED_ERROR;
                break;
            case HLErrorCode.Code.NETWORK_UNAVAILABLE_ERROR:
                errorMessage = HLErrorCode.Message.NETWORK_UNAVAILABLE_ERROR;
                break;
            case HLErrorCode.Code.BAD_REQUEST:
                errorMessage = error.networkResponse.statusCode + ": " + HLErrorCode.Message.BAD_REQUEST + " " + json;
                break;
            case HLErrorCode.Code.FORBIDDEN_REQUEST:
                errorMessage = error.networkResponse.statusCode + ": " + HLErrorCode.Message.FORBIDDEN_REQUEST + " " + json;
                break;
            case HLErrorCode.Code.NOT_FOUND:
                errorMessage = error.networkResponse.statusCode + ": " + HLErrorCode.Message.NOT_FOUND + " " + json;
                break;
            case HLErrorCode.Code.NOT_ACCEPTABLE:
                errorMessage = error.networkResponse.statusCode + ": " + HLErrorCode.Message.NOT_ACCEPTABLE + " " + json;
                break;
            case HLErrorCode.Code.REQUEST_TIMEOUT:
                errorMessage = error.networkResponse.statusCode + ": " + HLErrorCode.Message.REQUEST_TIMEOUT + " " + json;
                break;
            case HLErrorCode.Code.GONE:
                errorMessage = error.networkResponse.statusCode + ": " + HLErrorCode.Message.GONE + " " + json;
                break;
            case HLErrorCode.Code.TOO_MANY_REQUESTS:
                errorMessage = error.networkResponse.statusCode + ": " + HLErrorCode.Message.TOO_MANY_REQUESTS + " " + json;
                break;
            case HLErrorCode.Code.INTERNAL_SERVER_ERROR:
            case HLErrorCode.Code.BAD_GATEWAY:
            case HLErrorCode.Code.SERVICE_UNAVAILABLE:
            case HLErrorCode.Code.GATEWAY_TIMEOUT:
                errorMessage = error.networkResponse.statusCode + ": " + HLErrorCode.Message.INTERNAL_SERVER_ERROR + " " + json;
                break;
            case HLErrorCode.Code.NOT_IMPLEMENTED_ON_SERVER:
                errorMessage = error.networkResponse.statusCode + ": " + HLErrorCode.Message.SERVICE_UNAVAILABLE + " " + json;
                break;
        }

        return errorMessage;
    }

    public static Exception getException(VolleyError error) {
        return new RuntimeException(getMessage(error));
    }

    public static boolean isInvalidTokenError(VolleyError error) {
        return error != null && error.networkResponse != null &&
                (error.networkResponse.statusCode == HLErrorCode.Code.AUTHORIZATION_TOKEN_NOT_PROVIDED
                        || error.networkResponse.statusCode == HLErrorCode.Code.FORBIDDEN_REQUEST
                        || error.networkResponse.statusCode == HLErrorCode.Code.NOT_FOUND);
    }

    public static boolean isInvalidTrackingSessionError(VolleyError error) {
        return error != null && error.networkResponse != null && error.networkResponse.statusCode == 409;
    }

    public static boolean isInvalidRequest(VolleyError error) {
        return error != null && error.networkResponse != null && error.networkResponse.statusCode >= 400
                && error.networkResponse.statusCode < 500;
    }
}
