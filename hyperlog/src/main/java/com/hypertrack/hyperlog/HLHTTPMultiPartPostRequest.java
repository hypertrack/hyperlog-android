/*
The MIT License (MIT)

Copyright (c) 2015-2017 HyperTrack (http://hypertrack.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/


package com.hypertrack.hyperlog;

import android.content.Context;
import android.os.Build;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hypertrack.hyperlog.utils.CustomGson;
import com.hypertrack.hyperlog.utils.HLDateTimeUtility;
import com.hypertrack.hyperlog.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

class HLHTTPMultiPartPostRequest<T> extends Request<T> {

    private static final String TAG = HLHTTPMultiPartPostRequest.class.getSimpleName();
    private final Gson mGson;
    private final Class<T> mResponseType;
    private final Response.Listener<T> mListener;

    private final Context context;
    private final byte[] multiPartRequestBody;
    private final String filename;
    private final String packageName;

    private final HashMap<String, String> additionalHeaders;
    private static final String HEADER_ENCODING = "Content-Encoding";
    private static final String ENCODING_GZIP = "gzip";
    private final String boundary = "HyperLog -" + System.currentTimeMillis();

    private boolean mGzipEnabled = false;

    HLHTTPMultiPartPostRequest(String url, byte[] multiPartRequestBody, String filename, HashMap<String, String> additionalHeaders,
                               Context context, Class<T> responseType, boolean compress,
                               Response.Listener<T> listener, Response.ErrorListener errorListener) {

        super(Method.POST, url, errorListener);
        this.context = context;
        if (compress)
            this.multiPartRequestBody = getRequestBody(multiPartRequestBody);
        else
            this.multiPartRequestBody = multiPartRequestBody;
        this.filename = filename;
        this.mResponseType = responseType;
        this.mListener = listener;
        this.mGson = CustomGson.gson();
        this.additionalHeaders = additionalHeaders;
        packageName = context.getPackageName();
    }

    private byte[] getCompressed(byte[] requestBody) {
        if (requestBody != null) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(requestBody.length);
                GZIPOutputStream gzipOutputStream;
                gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream, 32);
                gzipOutputStream.write(requestBody);
                gzipOutputStream.close();
                byte[] compressed = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.close();
                mGzipEnabled = true;
                return compressed;

            } catch (Exception exception) {
                HyperLog.e(TAG, "Exception occurred while getCompressed: " + exception);
                mGzipEnabled = false;
            } catch (OutOfMemoryError error) {
                HyperLog.e(TAG, "OutOfMemory Error occurred while getCompressed: " + error);
                mGzipEnabled = false;
            }
        }

        return null;
    }

    private byte[] getRequestBody(byte[] requestBody) {
        byte[] compressedRequestBody = getCompressed(requestBody);
        if (mGzipEnabled) {
            HyperLog.i(TAG, "Compressed FileSize: " + compressedRequestBody.length + " Bytes");
            return compressedRequestBody;
        } else {
            try {
                HyperLog.i(TAG, "Compressed FileSize: " + requestBody.length + " Bytes");
                return requestBody;
            } catch (Exception exception) {
                HyperLog.e(TAG, "Exception occurred while getRequestBody: " + exception);
            }
        }
        return null;
    }

    @Override
    public byte[] getBody() {
        return multiPartRequestBody;
    }

    /**
     * Utility method to decompress gzip. To be used when we start sending gzip responses.
     */
    public static String getDecompressed(byte[] compressed) throws IOException {
        try {
            final int BUFFER_SIZE = 32;
            ByteArrayInputStream is = new ByteArrayInputStream(compressed);
            GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
            StringBuilder string = new StringBuilder();
            byte[] data = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = gis.read(data)) != -1) {
                string.append(new String(data, 0, bytesRead));
            }
            gis.close();
            is.close();
            return string.toString();
        } catch (Exception exception) {
            HyperLog.e(TAG, "Exception occurred while getDecompressed: " + exception);
        }
        return null;
    }

    @Override
    public Map<String, String> getHeaders() {

        Map<String, String> params = new HashMap<>();
        params.put("User-Agent", context.getPackageName() + " (Android " + Build.VERSION.RELEASE + ")");
        params.put("Device-Time", HLDateTimeUtility.getCurrentTime());
        params.put("Device-ID", Utils.getDeviceId(context));
        params.put("App-ID", packageName);
        //Header for file upload
        params.put("Content-Disposition", "attachment; filename=" + filename);

        if (mGzipEnabled) {
            params.put(HEADER_ENCODING, ENCODING_GZIP);
        }

        if (this.additionalHeaders != null) {
            Iterator<Map.Entry<String, String>> iterator = this.additionalHeaders.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> header = iterator.next();
                params.put(header.getKey(), header.getValue());
            }
        }
        return params;
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {

        if (volleyError == null || volleyError.networkResponse == null)
            return super.parseNetworkError(volleyError);

        try {
            String json = new String(
                    volleyError.networkResponse.data, HttpHeaderParser.parseCharset(volleyError.networkResponse.headers));

            HyperLog.i(TAG, "Status Code: " + volleyError.networkResponse.statusCode +
                    " Data: " + json);

        } catch (Exception e) {
            HyperLog.e(TAG, "Exception occurred while HTTPPatchRequest parseNetworkError: " + e, e);
        }

        return super.parseNetworkError(volleyError);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));

            return Response.success(
                    mGson.fromJson(json, mResponseType), HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        HyperLog.i(TAG, "deliverResponse: ");
        if (mListener != null)
            mListener.onResponse(response);
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }
}