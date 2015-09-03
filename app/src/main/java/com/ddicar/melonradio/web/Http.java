package com.ddicar.melonradio.web;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;

/**
 * HTTP请求类
 */
public class Http {
    private static final String TAG = "http";

//    public static final String SERVER = "http://182.92.160.208:3000/";

    Callback callback;


    private static final int TEST = 0;
    private static final int STAGING = 1;
    private static final int PROD = 2;

    private static final int version = STAGING;

    AsyncHttpClient client = new AsyncHttpClient();
    private static Http instance = null;

    private Http() {
    }

    public static Http getInstance() {
        if (instance == null) {
            instance = new Http();
        }
        return instance;
    }



    public static String SERVER() {
        if (version == STAGING) {
            return "http://192.168.1.100:8080/sample/";
        } else if (version == PROD) {
            return "http://182.92.160.208:3000/";
        } else {
            return "";
        }
    }

    /**
     * GET处理
     *
     * @param url    请求的URL
     * @param params HTTP请求的参数列表及值
     * @return HTTPResponse的Text
     * @throws WebException 发生的错误
     */
    public void get(final String url, final Map<String, String> params) {
        RequestParams realParams = new RequestParams();

        if (params != null) {
            Iterator<String> iterators = params.keySet().iterator();

            while (iterators.hasNext()) {
                String key = iterators.next();
                String value = params.get(key);
                realParams.put(key, value);
            }
        }
        Log.e(TAG, url);
        client.get(url, realParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                callback.onResponse(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e(TAG, "onFailure " + errorResponse + " " + throwable);
                callback.setWebException(new WebException("发生错误"));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e(TAG, "onFailure " + responseString + " " + throwable);
                callback.setWebException(new WebException("发生错误"));
            }
        });

    }


    /**
     * POST请求
     *
     * @param url    请求的URL
     * @param params HTTP请求的参数列表及值
     * @return HTTPResponse的Text
     * @throws WebException 发生的错误
     */
    public void post(final String url, final Map<String, Object> params) {

        RequestParams realParams = new RequestParams();

        if (params != null) {
            Iterator<String> iterators = params.keySet().iterator();

            while (iterators.hasNext()) {
                String key = iterators.next();
                Object value = params.get(key);
                realParams.put(key, value);
            }
        }

        Log.e(TAG, "post : " + url);
        client.post(url, realParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                callback.onResponse(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e(TAG, "onFailure " + errorResponse + " " + throwable);
                callback.setWebException(new WebException("发生错误"));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e(TAG, "onFailure " + responseString + " " + throwable);
                callback.setWebException(new WebException("发生错误"));
            }
        });

    }

    public static interface Callback {
        public void onResponse(JSONObject jsonObject);

        public void setWebException(WebException webException);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    // 上传代码，第一个参数，为要使用的URL，第二个参数，为表单内容，第三个参数为要上传的文件，可以上传多个文件，这根据需要页定
    public void post(final String url, final Map<String, String> params,
                     final File file) {

        RequestParams realParams = new RequestParams();

        if (params != null) {
            Iterator<String> iterators = params.keySet().iterator();

            while (iterators.hasNext()) {
                String key = iterators.next();
                Object value = params.get(key);
                realParams.put(key, value);
            }
        }

        try {
            realParams.put("pic", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "post : " + url);

        client.post(url, realParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                callback.onResponse(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e(TAG, "onFailure " + errorResponse + " " + throwable);
                callback.setWebException(new WebException("发生错误"));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e(TAG, "onFailure " + responseString + " " + throwable);
                callback.setWebException(new WebException("发生错误"));
            }
        });

    }
}
