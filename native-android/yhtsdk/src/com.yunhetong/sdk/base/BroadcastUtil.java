package com.yunhetong.sdk.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.yunhetong.sdk.YhtContent;

/**
 * 本地广播的注册开启与关闭
 */
public class BroadcastUtil {


    Context mContext;
    /**
     * 广播
     **/
    private BroadcastReceiver mReceiver;

    public BroadcastUtil(Context context) {
        mContext = context;
    }


    public void registerReceiver(final IRequestListener listener) {
        LocalBroadcastManager mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter filter = new IntentFilter();
        filter.addAction(YhtContent.CONTRACTDETAIL_TAG);
        filter.addAction(YhtContent.CONTRACTINVALID_TAG);
        filter.addAction(YhtContent.CONTRACTSIGN_TAG);
        filter.addAction(YhtContent.SIGNDETAIL_TAG);
        filter.addAction(YhtContent.SIGNDELETE_TAG);
        filter.addAction(YhtContent.SIGNGENERATE_TAG);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (null == intent) return;
                String action = intent.getAction();
                if (action.equals(YhtContent.CONTRACTDETAIL_TAG)) {
                    listener.contractDetail();
                } else if (action.equals(YhtContent.CONTRACTINVALID_TAG)) {
                    listener.contractInvalid();
                } else if (action.equals(YhtContent.CONTRACTSIGN_TAG)) {
                    listener.contractSign();
                } else if (action.equals(YhtContent.SIGNDETAIL_TAG)) {
                    listener.signDetail();
                } else if (action.equals(YhtContent.SIGNDELETE_TAG)) {
                    listener.signDelete();
                } else if (action.equals(YhtContent.SIGNGENERATE_TAG)) {
                    listener.signGerenater();
                }
            }
        };
        mLocalBroadcastManager.registerReceiver(mReceiver, filter);
    }

    public void unregisterReceiver() {
        if (LocalBroadcastManager.getInstance(mContext) != null && mReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
        }
    }


    public static void sendBroadCast(Context context) {
        if (YhtHttpClient.currNeedRequestStatusCode == 0) return;
        LocalBroadcastManager mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
        Intent intent = new Intent();
        switch (YhtHttpClient.currNeedRequestStatusCode) {
            case YhtContent.REQUESTCODE_CONTRACTDETAIL:
                intent.setAction(YhtContent.CONTRACTDETAIL_TAG);
                mLocalBroadcastManager.sendBroadcast(intent);
                break;
            case YhtContent.REQUESTCODE_CONTRACTINVALID:
                intent.setAction(YhtContent.CONTRACTINVALID_TAG);
                mLocalBroadcastManager.sendBroadcast(intent);
                break;
            case YhtContent.REQUESTCODE_CONTRACTSIGN:
                intent.setAction(YhtContent.CONTRACTSIGN_TAG);
                mLocalBroadcastManager.sendBroadcast(intent);
                break;
            case YhtContent.REQUESTCODE_SIGNDELETE:
                intent.setAction(YhtContent.SIGNDELETE_TAG);
                mLocalBroadcastManager.sendBroadcast(intent);
                break;
            case YhtContent.REQUESTCODE_SIGNGENERATE:
                intent.setAction(YhtContent.SIGNGENERATE_TAG);
                mLocalBroadcastManager.sendBroadcast(intent);
                break;
            case YhtContent.REQUESTCODE_SIGNDETAIL:
                intent.setAction(YhtContent.SIGNDETAIL_TAG);
                mLocalBroadcastManager.sendBroadcast(intent);
                break;
            default:
                break;
        }
    }


    /**
     * SDK所以请求接口
     */
    public interface IRequestListener {
        void contractDetail();

        void contractInvalid();

        void contractSign();

        void signDetail();

        void signDelete();

        void signGerenater();

    }

}
