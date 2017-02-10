package com.yunhetong.sdk.fast;

import android.os.Bundle;
import android.view.View;

import com.yunhetong.sdk.base.BroadcastUtil;


public class SdkBaseActivity extends BaseActivity implements BroadcastUtil.IRequestListener {
    public <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    BroadcastUtil mBroad;
    @Override
    protected void onResume() {
        super.onResume();
        mBroad = new BroadcastUtil(this);
        mBroad.registerReceiver(this);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBroad.unregisterReceiver();
    }


    @Override
    public void contractDetail() {

    }

    @Override
    public void contractInvalid() {

    }

    @Override
    public void contractSign() {

    }

    @Override
    public void signDetail() {

    }

    @Override
    public void signDelete() {

    }

    @Override
    public void signGerenater() {

    }




}
