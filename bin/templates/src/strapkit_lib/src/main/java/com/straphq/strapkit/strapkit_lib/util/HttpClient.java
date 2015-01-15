package com.straphq.strapkit.strapkit_lib.util;

import android.text.TextUtils;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by martinza on 1/8/15.
 */
public class HttpClient implements Serializable {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";

    interface HttpClientCallback {
        void OnHttpComplete(String callback, String info);
    }

    public HttpClientCallback callback;

    public String url;

    public String method;

    public String data;

    public String headers;
    private String success;
    private String failure;

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }

    public void processHTTPCall(final HttpClientCallback callback) {

        OkHttpClient httpClient = new OkHttpClient();

        Request.Builder requestBuilder = new Request.Builder().url(url);
        Request request = null;

        if (TextUtils.isEmpty(this.method)) {
            this.method = GET;
        }

        switch (this.method) {
            case POST:

                break;
            case PUT:

                break;
            case DELETE:

                break;
            default:
                request = requestBuilder.get().build();
                break;
        }

        try {
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                if (callback != null) {
                    callback.OnHttpComplete(success, response.body().string());
                }
            } else {
                if (callback != null) {
                    callback.OnHttpComplete(failure, "'" + response.message() + "'");
                }
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.OnHttpComplete(failure, "'" + e.getMessage() + "'");
            }
        }
    }
}
