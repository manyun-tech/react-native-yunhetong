package com.yunhetong.sdk.fast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.yunhetong.sdk.YhtSdk;
import com.yunhetong.sdk.YhtContent;
import com.yunhetong.sdk.base.HttpCallBackListener;
import com.yunhetong.sdk.tool.YhtLog;
import com.yunhetong.sdk.base.YhtSign;
import com.yunhetong.sdk.tool.ImageBase64Util;

public class SignDetailActivity extends SdkBaseActivity implements View.OnClickListener, HttpCallBackListener<String> {
    private static final String TAG = "SignDetailActivity";

    public static void gotoSignDetailAct(Activity context) {
        Intent intent = new Intent(context, SignDetailActivity.class);
        context.startActivity(intent);
    }


    private Button mSignDelete;
    private ImageView mSignAdd, mSignView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(YhtResourceFinder.findContentView(this, "yht_sign_detail"));
        initBar();
        initView();
        signDetail();

    }


    private void initView() {
        mSignDelete = findView(YhtResourceFinder.findView(this, "yht_btn_delete"));
        mSignAdd = findView(YhtResourceFinder.findView(this, "yht_iv_add"));
        mSignView = findView(YhtResourceFinder.findView(this, "yht_iv_sign"));
        mSignDelete.setOnClickListener(this);
        mSignAdd.setOnClickListener(this);
    }

    private void initBar() {
        getBar().setTitle("签名查看");
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


    /**
     * 签名查询请求
     */
    @Override
    public void signDetail() {
        getProgressDialog().show();
        YhtSdk.getInstance().yhtSignDetailRequest(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == YhtResourceFinder.findView(this, "yht_btn_delete")) {
            signDelete();
        } else if (i == YhtResourceFinder.findView(this, "yht_iv_add")) {
            SignGeneratorActivity.gotoSignGeneratorActForResult(this);
        }
    }


    /**
     * 签名删除请求
     */
    @Override
    public void signDelete() {
        getProgressDialog().show();
        YhtSdk.getInstance().yhtSignDeleteRequest(this);
    }


    @Override
    public void onHttpSucceed(String url, String object, int RequestCode) {
        getProgressDialog().dismiss();
        switch (RequestCode) {
            case YhtContent.REQUESTCODE_SIGNDELETE:
                //签名删除成功
                showAddView();
                break;
            case YhtContent.REQUESTCODE_SIGNDETAIL:
                //签名查看成功
                YhtSign signData = YhtSign.jsonToBean(object);
                if (null != signData) {
                    showSignView(signData);
                } else {
                    showAddView();
                    Toast.makeText(this, "您还没有创建签名", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onHttpFail(String url, String msg, int RequestCode) {
        getProgressDialog().dismiss();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        /*switch (RequestCode) {
            case YhtContent.REQUESTCODE_SIGNDELETE:
                break;
            case YhtContent.REQUESTCODE_SIGNDETAIL:
                break;
            default:
                break;
        }*/
    }

    void showAddView() {
        if (mSignDelete == null || mSignView == null || mSignAdd == null) return;
        mSignView.setVisibility(View.GONE);
        mSignDelete.setVisibility(View.GONE);
        mSignAdd.setVisibility(View.VISIBLE);
    }

    void showSignView(String signStr) {
        if (mSignDelete == null || mSignView == null || mSignAdd == null) return;
        mSignAdd.setVisibility(View.GONE);
        mSignDelete.setVisibility(View.VISIBLE);
        mSignView.setVisibility(View.VISIBLE);
        mSignView.setImageBitmap(ImageBase64Util.Base64ToBitmap(signStr, 4));
    }

    void showSignView(YhtSign signData) {
        if (mSignDelete == null || mSignView == null || mSignAdd == null) return;
        mSignAdd.setVisibility(View.GONE);
        if (!signData.isUsed()) mSignDelete.setVisibility(View.VISIBLE);
        mSignView.setVisibility(View.VISIBLE);
        mSignView.setImageBitmap(ImageBase64Util.Base64ToBitmap(signData.getSignDate(), 4));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        YhtLog.e(TAG, "onActivityResult");
        if (requestCode == SignGeneratorActivity.requestCode && resultCode == Activity.RESULT_OK) {
            String signbt = data.getStringExtra(SignGeneratorActivity.result_generator);
            showSignView(signbt);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
