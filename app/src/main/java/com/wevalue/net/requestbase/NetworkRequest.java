package com.wevalue.net.requestbase;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.wevalue.WeValueApplication;
import com.wevalue.net.Platform;
import com.wevalue.net.RequestPath;
import com.wevalue.utils.Base64Coder;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SSLUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import jupush.ExampleUtil;

import javax.net.ssl.SSLSocketFactory;

import static com.android.volley.VolleyLog.TAG;


/**
 * Created by K on 2017/1/17.
 */

public class NetworkRequest {
    private SSLSocketFactory socketFactory;
    static OkHttpClient okHttpClient = new OkHttpClient();
    static String url = "";
    private static final int SUCC = 953;
    private static final int FAIL = 997;

    static Platform mPlatform;
    static String responseString = "";

    public NetworkRequest() {
        try {
            socketFactory = SSLUtil.getSSLSocketFactory(WeValueApplication.applicationContext.getAssets().open("pass.cer"));
            okHttpClient.setSslSocketFactory(socketFactory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlatform = Platform.get();
    }


    public static void getRequest(final String url, final Map<String, String> map, final WZHttpListener wzHttpListener) {
        map.put("logintoken", SharedPreferencesUtil.getUserToken(WeValueApplication.applicationContext));
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case SUCC:
                        LogUtils.e("netContentget", msg.obj.toString());
                        wzHttpListener.onSuccess(msg.obj.toString(), url);
                        break;
                    case FAIL:
                        LogUtils.e("netContentget", msg.obj.toString());
                        wzHttpListener.onFailure(msg.obj.toString());
                        break;
                }
            }
        };
        String trueUrl = url;
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String a = entry.getKey();
                String b = entry.getValue();
                trueUrl += "&" + entry.getKey() + "=" + entry.getValue();
            }
        }
        final Request request = new Request.Builder()
                .url(trueUrl)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                Message message = Message.obtain();
                message.what = FAIL;
                message.obj = e.toString();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(final Response response) {
                Message message = Message.obtain();
                try {
                    String str = response.body().string();
                    LogUtils.e("responsssss", "-0.1--" + response.body().string());
                    LogUtils.e("responsssss_1", "-1--str--" + str);
                    message.what = SUCC;
                    message.obj = Base64Coder.decodeString(str.substring(5, str.length())).replaceAll("#换行#", "\r\n");
                } catch (Exception e) {
                    e.printStackTrace();
                    message.what = FAIL;
                    message.obj = e.toString();
                }
                handler.sendMessage(message);
            }
        });
    }

    public static void postRequest(final String url, Map<String, Object> map, final WZHttpListener wzHttpListener) {
        map.put("logintoken", SharedPreferencesUtil.getUserToken(WeValueApplication.applicationContext));
        if (!ExampleUtil.isConnected(WeValueApplication.applicationContext)) {
            ShowUtil.showToast(WeValueApplication.applicationContext, "当前设备无网络，请检查网络后重试。");
            return;
        }
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case SUCC:
                        LogUtils.e("netContent", msg.obj.toString());
                        wzHttpListener.onSuccess(msg.obj.toString(), url);

                        break;
                    case FAIL:
                        wzHttpListener.onFailure(msg.obj.toString());
                        LogUtils.e("netContent", msg.obj.toString());
                        break;
                }
            }
        };
        boolean isEncode = false;
        for (int k = 0; k < RequestPath.EncodePath.length; k++) {
            if (url.equals(RequestPath.EncodePath[k])) {
                isEncode = true;
                break;
            }
        }
        MultipartBuilder builder = new MultipartBuilder();
        builder.type(MultipartBuilder.FORM);
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() instanceof String) {
                    if (isEncode) {
                        if (entry.getKey().equals("code") || entry.getKey().contains("usertrueimg")) {
                            builder.addFormDataPart(entry.getKey(), (String) entry.getValue());
                            LogUtils.e("url" + entry.getKey(), entry.getValue().toString());
                        } else {
                            builder.addFormDataPart(entry.getKey(), Base64Coder.encodeString((String) entry.getValue()));
                            LogUtils.e("url" + entry.getKey(), Base64Coder.encodeString((String) entry.getValue()));
                        }
                    } else {
                        builder.addFormDataPart(entry.getKey(), (String) entry.getValue());
                        LogUtils.e("url" + entry.getKey(), entry.getValue().toString());
                    }
                }
                if (entry.getValue() instanceof File) {
                    LogUtils.e("file!!!!", entry + "这是一个文件");
                    builder.addFormDataPart(entry.getKey(), ((File) entry.getValue()).getName(), RequestBody.create(null, (File) entry.getValue()));
                }
            }
        }
        LogUtils.e("url", url);
        RequestBody formBody = builder.build();
        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                Message message = Message.obtain();
                message.what = FAIL;
                message.obj = e.toString();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                Message message = Message.obtain();
                String str = response.body().string();
                try {
                    message.what = SUCC;
                    message.obj = Base64Coder.decodeString(str.substring(5, str.length())).replaceAll("#换行#", "\r\n");
                } catch (Exception e) {
                    e.printStackTrace();
//                    message.what = FAIL;
//                    message.obj = e.toString();
                }
                handler.sendMessage(message);
                if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                    Log.d(TAG, "Main Thread");
                } else {
                    Log.d(TAG, "Not Main Thread");
                }
            }
        });
    }

    /*不加密返回值的get请求*/
    public static void getRequestForNoSecurity(final String url1, Map<String, String> map, final WZHttpListener wzHttpListener) {
        if (!ExampleUtil.isConnected(WeValueApplication.applicationContext)) {
            ShowUtil.showToast(WeValueApplication.applicationContext, "当前设备无网络，请检查网络后重试。");
            return;
        }
        //查询超时时间
        final int CUD_SOCKET_TIMEOUT = 60000 * 5;
        //最大重试请求次数
        final int MAX_RETRIES = 0;
        RequestQueue mRequestQueue = Volley.newRequestQueue(WeValueApplication.applicationContext);
        url = url1;
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String a = entry.getKey();
                String b = entry.getValue();
                url += "&" + entry.getKey() + "=" + entry.getValue();
            }
        }
        StringRequest stringRequest = new StringRequest(url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                wzHttpListener.onSuccess(response.toString().replaceAll("#换行#", "\r\n"), url1);
                LogUtils.e(TAG, "responseInfo-----" + url + response.toString());
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    wzHttpListener.onFailure(error.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    ShowUtil.showToast(WeValueApplication.applicationContext, error.getMessage());
                }
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(CUD_SOCKET_TIMEOUT,
                MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }
}
