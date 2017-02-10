package com.yunhetong.sdk.fast;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.yunhetong.sdk.tool.YhtDialogUtil;

public class BaseActivity extends AppCompatActivity {

    /**
     * 进度条
     */
    private Dialog mProgressDialog;

    public Dialog getProgressDialog() {
        if (null == mProgressDialog) {
            mProgressDialog = YhtDialogUtil.getWaitDialog(this);
        }
        return mProgressDialog;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
    }

    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1c , 0xad ,0xf9)));
    }

    public ActionBar getBar(){
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        return actionBar;
    }
}
