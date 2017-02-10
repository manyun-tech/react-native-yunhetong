package com.yunhetong.sdk.fast;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yunhetong.sdk.R;
import com.yunhetong.sdk.YhtContent;
import com.yunhetong.sdk.YhtSdk;
import com.yunhetong.sdk.base.HttpCallBackListener;
import com.yunhetong.sdk.tool.ImageBase64Util;

public class SignGeneratorActivity extends SdkBaseActivity implements View.OnClickListener, HttpCallBackListener<String> {
    /**
     * 跳转本页的请求码
     */
    public static final int requestCode = 2;
    /**
     * 本页返回的签名数据
     */
    public static final String result_generator = "signDataStr";

    public static void gotoSignGeneratorActForResult(Activity context) {
        Intent intent = new Intent(context, SignGeneratorActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    public static void gotoSignGeneratorActForResult(Fragment frg) {
        Intent intent = new Intent(frg.getContext(), SignGeneratorActivity.class);
        frg.startActivityForResult(intent, requestCode);
    }

    private YhtSignDrawView mDrawView;
    private Button mSignCommit, mSignClear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yht_sign_generator);
        initBar();
        initView();
    }


    private void initBar() {
        getBar().setTitle("绘制签名");
    }

    private void initView() {
        mSignCommit = findView(YhtResourceFinder.findView(this, "yht_btn_sign_apply"));
        mSignClear = findView(YhtResourceFinder.findView(this, "yht_btn_sign_clear"));
        mDrawView = findView(YhtResourceFinder.findView(this, "yht_draw_panel"));
        mDrawView.setFragment(this);
        mSignCommit.setEnabled(true);
        mSignClear.setEnabled(true);
        mSignCommit.setOnClickListener(this);
        mSignClear.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == YhtResourceFinder.findView(this, "yht_btn_sign_apply")) {
            signGerenater();
        } else if (i == YhtResourceFinder.findView(this, "yht_btn_sign_clear")) {
            setClearAction();
        }
    }

    /**
     * 新增签名请求
     */
    @Override
    public void signGerenater() {
        if (mDrawView != null && mDrawView.isSign == true) {
            getProgressDialog().show();
            YhtSdk.getInstance().yhtSignGenerateRequest(this, getStringSign());
        } else {
            Toast.makeText(this, "未签名", Toast.LENGTH_SHORT).show();
        }
    }

    private String getStringSign() {
        if (mDrawView == null) return null;
        Bitmap bm = mDrawView.getmBitmap();
        return ImageBase64Util.BitmapToBase64(bm);
    }

    private void setClearAction() {
        setBtEnable(false);
        if (mDrawView != null) {
            mDrawView.isSign = false;
            mDrawView.getShouldClear().set(true);
            mDrawView.invalidate();
        }
    }

    //对清除按钮和创建按钮设置状态
    public void setBtEnable(boolean enable) {
        if (mSignCommit != null && mSignCommit.isEnabled() != enable) {
            mSignCommit.setEnabled(enable);
            mSignClear.setEnabled(enable);
        }
    }


    @Override
    public void onHttpSucceed(String url, String object, int RequestCode) {
        getProgressDialog().dismiss();
        if (RequestCode == YhtContent.REQUESTCODE_SIGNGENERATE) {
            String signviewStr = getStringSign();
            Intent intent = new Intent();
            intent.putExtra(result_generator, signviewStr);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onHttpFail(String url, String msg, int RequestCode) {
        getProgressDialog().dismiss();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDrawView != null)
            mDrawView.destroy();
    }

}
