package com.yunhetong.sdk;

import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yunhetong.sdk.base.HttpCallBackListener;
import com.yunhetong.sdk.base.Token;
import com.yunhetong.sdk.base.TokenManager;
import com.yunhetong.sdk.base.YhtHttpClient;
import com.yunhetong.sdk.tool.YhtLog;


/**
 *
 */
public final class YhtSdk {
    private static YhtSdk instance = new YhtSdk();
    private Application mApplication;


    private YhtSdk() {
    }

    public static YhtSdk getInstance() {
        return instance;
    }

    //---------------------------------------------------------

    /**
     * 打开调试模式
     */
    public void setDebug(boolean debug) {
        YhtLog.DEBUG = debug;
    }

    /**
     * 初始化云合同
     * 初始化回调
     * 在回调接口的方法里，第三方开发者实现获取token的异步请求
     *
     * @param application Activity 或者Application的上下文
     * @param listener    回调接口
     */
    public void initYhtSdk(Application application, Token.TokenListener listener) {
        mApplication = application;
        YhtHttpClient.getInstance().initClient(application);
        TokenManager.getInstance().setTokenListener(listener);
    }

    /**
     * 初始化Token(登录凭证)
     *
     * @param token Token登录凭证
     */
    public void setToken(String token) {
        if (TextUtils.isEmpty(token)) throw new NullPointerException("Token is null");
        TokenManager.getInstance().initToken(mApplication, token);
    }




    /*---------------------------合同与签名的请求-------------------------------------------*/

    /**
     * @param onCallBackListener 网络请求的响应回调
     * @param contractId         合同ID
     */
    public void yhtContractDetailRequest(@NonNull HttpCallBackListener<String> onCallBackListener, @NonNull String contractId) {
        new SdkRequestManaer().contractDetail(onCallBackListener, contractId);
    }

    /**
     * @param onCallBackListener 网络请求的响应回调
     * @param contractId         合同ID
     */
    public void yhtContractSignRequest(@NonNull HttpCallBackListener<String> onCallBackListener, @NonNull String contractId) {
        new SdkRequestManaer().contractSign(onCallBackListener, contractId);
    }

    /**
     * @param onCallBackListener 网络请求的响应回调
     * @param contractId         合同ID
     */
    public void yhtContractInvalidRequest(@NonNull HttpCallBackListener<String> onCallBackListener, @NonNull String contractId) {
        new SdkRequestManaer().contractInvalid(onCallBackListener, contractId);
    }

    /**
     * @param onCallBackListener 网络请求的响应回调
     * @param signData           签名数据
     */
    public void yhtSignGenerateRequest(@NonNull HttpCallBackListener<String> onCallBackListener, @NonNull String signData) {
        new SdkRequestManaer().signGenerate(onCallBackListener, signData);
    }

    /**
     * @param onCallBackListener 网络请求的响应回调
     */
    public void yhtSignDetailRequest(@NonNull HttpCallBackListener<String> onCallBackListener) {
        new SdkRequestManaer().signDetail(onCallBackListener);
    }

    /**
     * @param onCallBackListener 网络请求的响应回调
     */
    public void yhtSignDeleteRequest(@NonNull HttpCallBackListener<String> onCallBackListener) {
        new SdkRequestManaer().signDelete(onCallBackListener);
    }

    /**
     * @param contractId 合同Id
     */
    public String yhtContractUrl(@NonNull String contractId) {
        return SdkRequestManaer.getContractUrl(contractId);
    }
}
