package com.yunhetong.sdk.fast;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.yunhetong.sdk.YhtContent;
import com.yunhetong.sdk.YhtSdk;
import com.yunhetong.sdk.base.HttpCallBackListener;
import com.yunhetong.sdk.base.YhtContract;
import com.yunhetong.sdk.base.YhtContractParter;
import com.yunhetong.sdk.tool.YhtLog;
import com.yunhetong.sdk.tool.YhtDialogUtil;


/**
 * 合同详情页
 * 调用方法启动：ContractDetailActivity.gotoContractDetailAct();
 */
public class ContractDetailActivity extends SdkBaseActivity implements ConfirmAlertDialog.ConfirmAlertDialogListener, HttpCallBackListener<String> , View.OnClickListener
{
    private static final String TAG = ContractDetailActivity.class.getSimpleName();
    public static final int request_code = 3;
    public static final int result_code_invalid = 4;
    public static final int result_code_signfinish = 5;

    /**
     * 如果第三方是从Activity跳转过来的 调用本方法
     * 请求码是 ContractDetailActivity.REQUEST_CODE
     *
     * @param act
     * @param contractId 用户合同Id
     */
    public static void gotoContractDetailActForResult(Activity act, String contractId) {
        Intent intent = new Intent(act, ContractDetailActivity.class);
        intent.putExtra(CONTRACT_ID, contractId);
        act.startActivityForResult(intent, request_code);
    }

    /**
     * 如果第三方是从Fragment跳转过来的 调用本方法
     * 请求码是 ContractDetailActivity.REQUEST_CODE
     *
     * @param fg
     * @param contractId 用户合同Id
     */
    public static void gotoContractDetailActForResult(Fragment fg, String contractId) {
        Intent intent = new Intent(fg.getActivity(), ContractDetailActivity.class);
        intent.putExtra(CONTRACT_ID, contractId);
        fg.startActivityForResult(intent, request_code);
    }


    private static final String CONTRACT_ID = "yht_contract_id";
    private static final byte contractsign = 1;
    private static final byte contractInvalit = 2;
    private YhtWebView mYhtWebView;
    private String contractId;
    private YhtContract mRequestContractData;
    private static MenuStatus menuStatus = MenuStatus.none;
    private Button btnCancel , btnSure ;
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId() ==btnCancel.getId()){
            showInvalidDialog();
        }else{
            if(menuStatus == MenuStatus.signed){
                setResult(result_code_signfinish);
                onBackPressed();
            }else{
                showSignDialog();
            }
        }
    }

    public enum MenuStatus {
        sign, invalid, signAndInvalid, signed, none
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(YhtResourceFinder.findContentView(this, "yht_contract_detail_layout"));
        contractId = getContractData();
        initBar();
        initView();
        contractDetail();
    }


    private String getContractData() {
        Intent intent = getIntent();
        if (null == intent) return null;
        return intent.getStringExtra(CONTRACT_ID);
    }


    private void initBar() {
        getBar().setTitle(" ");
    }

    /**
     * 弹出菜单对话框
     */
    private void showSignDialog() {
        YhtDialogUtil.getComfireAlertDialog2(this, this, contractsign, "", "确认签署？").show();
    }

    /**
     * 弹出菜单对话框
     */
    private void showInvalidDialog() {
        YhtDialogUtil.getComfireAlertDialog2(this, this, contractInvalit, "", "确认作废？").show();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        FrameLayout layout = findView(YhtResourceFinder.findView(this, "yht_layout_content"));
        mYhtWebView = new YhtWebView(this);
        layout.addView(mYhtWebView);
        btnCancel = findView(YhtResourceFinder.findView(this,"btn_cancel")) ;
        btnSure = findView(YhtResourceFinder.findView(this,"btn_sure"));
        btnCancel.setOnClickListener(this);
        btnSure.setOnClickListener(this);

    }


    /**
     * 合同详情请求
     */
    @Override
    public void contractDetail() {
        getProgressDialog().show();
        YhtSdk.getInstance().yhtContractDetailRequest(this, contractId);
    }

    /**
     * 加载详情界面
     */
    private void loadViewContractDetail() {
        String contractId = String.valueOf(mRequestContractData.getParter().getContractId());
//        String contractUrl = YhtContent.URL_CONTRACTDETAILVIEW + "?contractId=" + contractId + "&token=" + TokenManager.getInstance().getToken();
        String contractUrl = YhtSdk.getInstance().yhtContractUrl(contractId);
        mYhtWebView.loadUrl(contractUrl);
    }

    /**
     * 合同签署请求
     */
    @Override
    public void contractSign() {
        getProgressDialog().show();
        YhtSdk.getInstance().yhtContractSignRequest(this, contractId);
    }


    /**
     * 合同作废请求
     */
    @Override
    public void contractInvalid() {
        getProgressDialog().show();
        YhtSdk.getInstance().yhtContractInvalidRequest(this, contractId);
    }


    /**
     * 对话框确认
     *
     * @param targetId
     * @param dialog
     * @param param
     */
    @Override
    public void ComfireAlertDialog2Sure(byte targetId, AlertDialog dialog, Object param) {
        dialog.dismiss();
        switch (targetId) {
            case contractsign:
                if (null == mRequestContractData) return;
                if (mRequestContractData.getParter().isHasSign()) {//判断是否有签名
                    contractSign();
                } else {
                    SignGeneratorActivity.gotoSignGeneratorActForResult(this);//没有签名跳转绘制签名页 有返回结果
                }
                break;
            case contractInvalit:
                contractInvalid();
                break;
            default:
                break;
        }
    }

    @Override
    public void ComfireAlertDialog2Cancel(byte targetId, AlertDialog dialog) {
        dialog.dismiss();
    }


    private void setMenuState(YhtContract contract) {
        getSupportActionBar().setTitle(contract.getTitle());
        YhtContractParter parter = contract.getParter();
        if(parter.isHasSign()){
            menuStatus = MenuStatus.signed ;
            btnCancel.setVisibility(View.GONE);
            btnSure.setText("报名充值");
            btnSure.setCompoundDrawables(null , null , null , null);
        }else{
            if (parter.isSign() && parter.isInvalid()) {
                menuStatus = MenuStatus.signAndInvalid;
            }
            if (parter.isSign() && !parter.isInvalid()) {
                menuStatus = MenuStatus.sign;
                btnCancel.setVisibility(View.GONE);
            }
            if (!parter.isSign() && parter.isInvalid()) {
                menuStatus = MenuStatus.invalid;
                btnCancel.setVisibility(View.GONE);
                btnSure.setVisibility(View.GONE);
            }
            if (!parter.isSign() && !parter.isInvalid()) {
                menuStatus = MenuStatus.none;
                btnCancel.setVisibility(View.GONE);
                btnSure.setVisibility(View.GONE);
            }
        }

     //   invalidateOptionsMenu();

    }

    @Override
    public void onHttpSucceed(String url, String object, int RequestCode) {
        getProgressDialog().dismiss();
        switch (RequestCode) {
            case YhtContent.REQUESTCODE_CONTRACTDETAIL:
                YhtContract contract = YhtContract.jsonToBean(object);
                YhtLog.e(TAG, "返回-合同详情 :" + contract.toString());
                mRequestContractData = contract;
                if (null != mYhtWebView && null != mRequestContractData) {
                    setMenuState(mRequestContractData);
                    loadViewContractDetail();
                }
                break;
            case YhtContent.REQUESTCODE_CONTRACTINVALID:
                YhtLog.e(TAG, "合同作废成功");
                contractDetail();
                Intent intent = new Intent();
                setResult(result_code_invalid, intent);
                this.finish();
                break;
            case YhtContent.REQUESTCODE_CONTRACTSIGN:
                contractDetail();
                YhtLog.e(TAG, "合同签署成功");
                Intent intent2 = new Intent();
                setResult(result_code_signfinish, intent2);
                // todo
                //this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onHttpFail(String url, String msg, int RequestCode) {
        getProgressDialog().dismiss();
        YhtLog.e(TAG, "失败原因 :" + msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        /*mYhtWebView.destroyDrawingCache();
        switch (RequestCode) {
            case YhtContent.REQUESTCODE_CONTRACTDETAIL:
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                break;
            case YhtContent.REQUESTCODE_CONTRACTINVALID:
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                break;
            case YhtContent.REQUESTCODE_CONTRACTSIGN:
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SignGeneratorActivity.requestCode && resultCode == RESULT_OK) {
            contractSign();
        }
    }


    /**
     * 资源销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mYhtWebView != null) {
            mYhtWebView.setVisibility();
            mYhtWebView.removeAllViews();
            mYhtWebView.destroy();
            mYhtWebView = null;
            ViewGroup view = (ViewGroup) getWindow().getDecorView();
            view.removeAllViews();
        }

    }

}
