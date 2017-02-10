package com.yunhetong.sdk;

import android.text.TextUtils;


import com.yunhetong.sdk.base.HttpCallBackListener;
import com.yunhetong.sdk.base.IYhtSdkRequest;
import com.yunhetong.sdk.base.TokenManager;
import com.yunhetong.sdk.base.YhtHttpClient;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 云合同SDK的所有接口实现
 * 单例模式
 */
final class SdkRequestManaer implements IYhtSdkRequest {


    /**
     * @param onCallBackListener 网络请求的回调
     * @param contractId         合同ID
     */
    @Override
    public void contractDetail(HttpCallBackListener<String> onCallBackListener, String contractId) {
        if (null == onCallBackListener && TextUtils.isEmpty(contractId)) return;
        String url = YhtContent.URL_CONTRACTDETAIL;
        WeakReference<HttpCallBackListener> wrOnCallBackListener = new WeakReference<HttpCallBackListener>(onCallBackListener);
        Map<String, String> map = new HashMap<>();
        map.put("contractId", contractId);
        // 请求码Content.REQUESTCODE_CONTRACTDETAIL
        // 在接收返回接口时，需要根据请求码来区分不同的请求结果
        YhtHttpClient.getInstance().yhtNetworkPost(url, YhtContent.REQUESTCODE_CONTRACTDETAIL, map, wrOnCallBackListener.get());
    }


    //合同的URL
    public static String getContractUrl(String contractId) {
        String contractUrl = YhtContent.URL_CONTRACTDETAILVIEW + "?contractId=" + contractId + "&token=" + TokenManager.getInstance().getToken();
        return contractUrl;
    }


    @Override
    public void contractInvalid(HttpCallBackListener<String> onCallBackListener, String contractId) {
        if (null == onCallBackListener && TextUtils.isEmpty(contractId)) return;
        String url = YhtContent.URL_CONTRACTINVALID;
        WeakReference<HttpCallBackListener> wrOnCallBackListener = new WeakReference<HttpCallBackListener>(onCallBackListener);
        Map<String, String> map = new HashMap<>();
        map.put("contractId", contractId);
        YhtHttpClient.getInstance().yhtNetworkPost(url, YhtContent.REQUESTCODE_CONTRACTINVALID, map, wrOnCallBackListener.get());
    }

    @Override
    public void contractSign(HttpCallBackListener<String> onCallBackListener, String contractId) {
        if (null == onCallBackListener && TextUtils.isEmpty(contractId)) return;
        String url = YhtContent.URL_CONTRACTSIGN;
        WeakReference<HttpCallBackListener> wrOnCallBackListener = new WeakReference<HttpCallBackListener>(onCallBackListener);
        Map<String, String> map = new HashMap<>();
        map.put("contractId", contractId);
        YhtHttpClient.getInstance().yhtNetworkPost(url, YhtContent.REQUESTCODE_CONTRACTSIGN, map, wrOnCallBackListener.get());
    }

    @Override
    public void signDetail(HttpCallBackListener<String> onCallBackListener) {
        String url = YhtContent.URL_SIGNDETAIL;
        WeakReference<HttpCallBackListener> wrOnCallBackListener = new WeakReference<HttpCallBackListener>(onCallBackListener);
        YhtHttpClient.getInstance().yhtNetworkGet(url, YhtContent.REQUESTCODE_SIGNDETAIL, null, wrOnCallBackListener.get());
    }

    @Override
    public void signDelete(HttpCallBackListener<String> onCallBackListener) {
        String url = YhtContent.URL_SIGNDELETE;
        WeakReference<HttpCallBackListener> wrOnCallBackListener = new WeakReference<HttpCallBackListener>(onCallBackListener);
        YhtHttpClient.getInstance().yhtNetworkPost(url, YhtContent.REQUESTCODE_SIGNDELETE, null, wrOnCallBackListener.get());
    }

    @Override
    public void signGenerate(HttpCallBackListener<String> onCallBackListener, String signData) {
        if (null == onCallBackListener && TextUtils.isEmpty(signData)) return;
        String url = YhtContent.URL_SIGNGENERATE;
        WeakReference<HttpCallBackListener> wrOnCallBackListener = new WeakReference<HttpCallBackListener>(onCallBackListener);
        Map<String, String> map = new HashMap<>();
        map.put("sign", signData);
        YhtHttpClient.getInstance().yhtNetworkPost(url, YhtContent.REQUESTCODE_SIGNGENERATE, map, wrOnCallBackListener.get());
    }

}
