package com.hypertrack.hyperlog.error;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.ParseError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

public class NetworkErrorUtil {

    private static final String TAG = NetworkErrorUtil.class.getSimpleName();

    public static int getErrorCode(VolleyError error) {

        if (error == null)
            return ErrorCode.Code.UNHANDLED_ERROR;

        if (error instanceof ParseError ) {
            return ErrorCode.Code.PARSE_ERROR;
        }

        if(error instanceof AuthFailureError){
            return ErrorCode.Code.AUTH_ERROR;
        }

        if (error.networkResponse == null)
            return ErrorCode.Code.UNHANDLED_ERROR;

        return error.networkResponse.statusCode;
    }

    public static String getMessage(VolleyError error) {

        String errorMessage = ErrorCode.Message.UNHANDLED_ERROR;

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
            case ErrorCode.Code.NETWORK_DISABLED_ERROR:
                errorMessage = ErrorCode.Message.NETWORK_DISABLED_ERROR;
                break;
            case ErrorCode.Code.NETWORK_UNAVAILABLE_ERROR:
                errorMessage = ErrorCode.Message.NETWORK_UNAVAILABLE_ERROR;
                break;
            case ErrorCode.Code.BAD_REQUEST:
                errorMessage = error.networkResponse.statusCode + ": " + ErrorCode.Message.BAD_REQUEST + " " + json;
                break;
            case ErrorCode.Code.FORBIDDEN_REQUEST:
                errorMessage = error.networkResponse.statusCode + ": " + ErrorCode.Message.FORBIDDEN_REQUEST + " " + json;
                break;
            case ErrorCode.Code.NOT_FOUND:
                errorMessage = error.networkResponse.statusCode + ": " + ErrorCode.Message.NOT_FOUND + " " + json;
                break;
            case ErrorCode.Code.NOT_ACCEPTABLE:
                errorMessage = error.networkResponse.statusCode + ": " + ErrorCode.Message.NOT_ACCEPTABLE + " " + json;
                break;
            case ErrorCode.Code.REQUEST_TIMEOUT:
                errorMessage = error.networkResponse.statusCode + ": " + ErrorCode.Message.REQUEST_TIMEOUT + " " + json;
                break;
            case ErrorCode.Code.GONE:
                errorMessage = error.networkResponse.statusCode + ": " + ErrorCode.Message.GONE + " " + json;
                break;
            case ErrorCode.Code.TOO_MANY_REQUESTS:
                errorMessage = error.networkResponse.statusCode + ": " + ErrorCode.Message.TOO_MANY_REQUESTS + " " + json;
                break;
            case ErrorCode.Code.INTERNAL_SERVER_ERROR:
            case ErrorCode.Code.BAD_GATEWAY:
            case ErrorCode.Code.SERVICE_UNAVAILABLE:
            case ErrorCode.Code.GATEWAY_TIMEOUT:
                errorMessage = error.networkResponse.statusCode + ": " + ErrorCode.Message.INTERNAL_SERVER_ERROR + " " + json;
                break;
            case ErrorCode.Code.NOT_IMPLEMENTED_ON_SERVER:
                errorMessage = error.networkResponse.statusCode + ": " + ErrorCode.Message.SERVICE_UNAVAILABLE + " " + json;
                break;
        }

        return errorMessage;
    }

    public static Exception getException(VolleyError error) {
        return new RuntimeException(getMessage(error));
    }

    public static boolean isInvalidTokenError(VolleyError error) {
        return error != null && error.networkResponse != null &&
                (error.networkResponse.statusCode == ErrorCode.Code.AUTHORIZATION_TOKEN_NOT_PROVIDED
                        || error.networkResponse.statusCode == ErrorCode.Code.FORBIDDEN_REQUEST
                        || error.networkResponse.statusCode == ErrorCode.Code.NOT_FOUND);
    }

    public static boolean isInvalidTrackingSessionError(VolleyError error) {
        return error != null && error.networkResponse != null && error.networkResponse.statusCode == 409;
    }

    public static boolean isInvalidRequest(VolleyError error) {
        return error != null && error.networkResponse != null && error.networkResponse.statusCode >= 400
                && error.networkResponse.statusCode < 500;
    }
}
