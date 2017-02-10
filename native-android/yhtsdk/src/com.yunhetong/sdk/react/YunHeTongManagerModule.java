package com.yunhetong.sdk.react;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.yunhetong.sdk.BuildConfig;
import com.yunhetong.sdk.YhtSdk;
import com.yunhetong.sdk.fast.ContractDetailActivity;

/**
 * Created by weijiang on 16/11/16.
 */

public class YunHeTongManagerModule extends ReactContextBaseJavaModule implements LifecycleEventListener, ActivityEventListener {

    private Callback mCallback;

    public YunHeTongManagerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addLifecycleEventListener(this);
        reactContext.addActivityEventListener(this);
        //Log.e("-------", "YunHeTongManagerModule");
    }

    @Override
    public String getName() {
        return "YunHeTongManager";
    }

    @Override
    public void initialize() {
        super.initialize();
        //Log.e("-------", "initialize");
    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        //Log.e("-------", "onCatalystInstanceDestroy");
    }

    @ReactMethod
    public void contractOptions(ReadableMap readableMap, final Callback callback) {
        mCallback = callback;
        if (!readableMap.hasKey("token") || !readableMap.hasKey("contract_id")) {
            WritableMap event = Arguments.createMap();
            event.putString("message", "Error ! Miss required parameter");
            callback.invoke(event, "Error ! Miss required parameter");
            return;
        }

        String token = readableMap.getString("token");
        String contract_id = readableMap.getString("contract_id");
        YhtSdk.getInstance().setDebug(BuildConfig.DEBUG);
        YhtSdk.getInstance().setToken(token);
        ContractDetailActivity.gotoContractDetailActForResult(getCurrentActivity(), contract_id);

    }


    @Override
    public void onHostResume() {
        //Log.e("-------", "onHostResume");
    }

    @Override
    public void onHostPause() {
        //Log.e("-------", "onHostPause");
    }

    @Override
    public void onHostDestroy() {
        //Log.e("-------", "onHostDestroy");
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == ContractDetailActivity.request_code) {
            if (resultCode == ContractDetailActivity.result_code_invalid) {
                mCallback.invoke("2",null);
            }
            if (resultCode == ContractDetailActivity.result_code_signfinish) {
                mCallback.invoke("1",null);
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {

    }

}
