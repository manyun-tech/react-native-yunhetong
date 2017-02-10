package com.yunhetong.sdk.base;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yunhetong.sdk.tool.YhtLog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求
 */
public class YhtHttpClient {
    private static final String TAG = "YhtHttpClient";

    private static YhtHttpClient mHttpClient = null;
    private RequestQueue mQueue;

    private String APPID;
    private Context mContext;

    private YhtHttpClient() {
    }

    public static YhtHttpClient getInstance() {
        if (mHttpClient == null) {
            mHttpClient = new YhtHttpClient();
        }
        return mHttpClient;
    }

    public void initClient(Context context) {
        mContext = context;
        if (null == mQueue) {
//            HttpStack stack = new HurlStack(null, SSLTrustAllSocketFactory.getAndroidSocketFactory());
//            mQueue = Volley.newRequestQueue(mContext, stack);
            mQueue = Volley.newRequestQueue(context);
        }


        setAppId();
    }

    private void setAppId() {
        ApplicationInfo appInfo = null;
        try {
            appInfo = mContext.getPackageManager()
                    .getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);

            String bb = appInfo.metaData.getString("YhtSdk_AppId");
            String b = bb.replace("id_", "");
            APPID = b;
            YhtLog.e(TAG, "appid :" + APPID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SdkAppIdNullException("appId 没有找到,请在AndroidManifest中配置 <meta-data android:name=\"YhtSdk_AppId\" android:value=\"id_2016050516421\" />");
        }

    }

    public String getAppId() {
        return APPID;
    }


    /**
     * 临时保存的请求码
     * 如果请求前判断token失效或者请求后判定token失效,存储失效的请求码
     * 默认为0
     */
    public static byte currNeedRequestStatusCode = 0;

    /**
     * 凭证有效性检查
     *
     * @return
     */
    private boolean tokenValidationCheck(byte requestCode) {
        if (TokenManager.getInstance().isOverdue() || (TokenManager.getInstance().getToken() == null)) {
            currNeedRequestStatusCode = requestCode;
            YhtLog.e(TAG, "本地token超时，发起回调");
            TokenManager.getInstance().getTokenListener().onToken();
            //失效
            return true;
        }
        //未失效
        currNeedRequestStatusCode = 0;
        return false;
    }


    public void yhtNetworkPost(String url, byte requestCode, Map<String, String> params, HttpCallBackListener<String> onCallBack) {
        if (tokenValidationCheck(requestCode)) return;

        Map<String, String> map = params;
        if (null == map) {
            map = new HashMap<>();
        }
        map.put("token", TokenManager.getInstance().getToken());
        map.put("appid", getAppId());

        if (YhtLog.DEBUG) {
            paramstoString(map);
        }

        doNetworkPost(url, requestCode, map, onCallBack);
    }


    /**
     * POST
     *
     * @param url
     * @param requestCode
     * @param params
     * @param onCallBack
     */
    public void doNetworkPost(final String url, final byte requestCode, final Map<String, String> params, final HttpCallBackListener<String> onCallBack) {

        mQueue.cancelAll(requestCode);
        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                YhtLog.e(TAG, "onResponse { url :" + url + " data :" + response.toString() + "  }");
                RespondObject respondObject = RespondObject.parseJSONToRespond(response);
                if (respondObject.isOk()) {
                    TokenManager.getInstance().refreshToken();
                    if (onCallBack != null)
                        onCallBack.onHttpSucceed(url, response, requestCode);
                } else if (respondObject.isInvalid()) {
                    currNeedRequestStatusCode = requestCode;
                    YhtLog.e(TAG, "服务端返回参数判断--发起回调");
                    TokenManager.getInstance().getTokenListener().onToken();
                } else {
                    if (onCallBack != null)
                        onCallBack.onHttpFail(url, respondObject.getMessage(), requestCode);
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                YhtLog.e(TAG, "onErrorResponse { url : " + url + " error:" + error.getMessage() + "}");
                onCallBack.onHttpFail(url, "网络异常，请稍后再试", requestCode);
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        600000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        stringRequest.setTag(requestCode);
        mQueue.add(stringRequest);
        mQueue.start();
    }

    public void yhtNetworkGet(String url, byte requestCode, final Map<String, String> params, final HttpCallBackListener<String> onCallBack) {
        if (tokenValidationCheck(requestCode)) return;//有效性验证
        Map<String, String> map = params;
        if (null == map) {
            map = new HashMap<>();
        }
        map.put("token", TokenManager.getInstance().getToken());
        map.put("appid", getAppId());

        String paramsStr = paramstoString(map);
        paramsStr = paramsStr.substring(0, paramsStr.length() - 1);
        String urlParams = url + "?" + paramsStr.toString();
        doNetworkGet(urlParams, requestCode, onCallBack);
    }


    public void doNetworkGet(final String url, final byte requestCode, final HttpCallBackListener<String> onCallBack) {
        mQueue.cancelAll(requestCode);
        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                YhtLog.e(TAG, "onResponse { url :" + url + " data :" + response.toString() + "  }");
                RespondObject respondObject = RespondObject.parseJSONToRespond(response);
                if (respondObject.isOk()) {
                    TokenManager.getInstance().refreshToken();
                    if (onCallBack != null)
                        onCallBack.onHttpSucceed(url, response, requestCode);
                } else if (respondObject.isInvalid()) {
                    currNeedRequestStatusCode = requestCode;
                    TokenManager.getInstance().getTokenListener().onToken();
                } else {
                    if (onCallBack != null)
                        onCallBack.onHttpFail(url, respondObject.getMessage(), requestCode);
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                YhtLog.e(TAG, "onErrorResponse { url : " + url + " error:" + error.getMessage() + "}");
                onCallBack.onHttpFail(url, "网络异常，请稍后再试", requestCode);
            }
        };

        StringRequest stringRequest = new StringRequest(url, listener, errorListener);
        stringRequest.setTag(requestCode);
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        600000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        mQueue.add(stringRequest);
        mQueue.start();
    }


    private String paramstoString(Map<String, String> params) {
        if (params != null && params.size() > 0) {
            String paramsEncoding = "UTF-8";
            StringBuilder encodedParams = new StringBuilder();
            try {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                    encodedParams.append('=');
                    encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                    encodedParams.append('&');
                    YhtLog.e(TAG, "key= " + entry.getKey() + " and value= " + entry.getValue());
                }
                return encodedParams.toString();
            } catch (UnsupportedEncodingException uee) {
                throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
            }
        }
        return null;
    }


}
