package com.yunhetong.sdk.base;


/**
 * SDK的请求接口
 */
public interface IYhtSdkRequest {

    /*************************合同的请求************************/
    /**
     * 合同详情
     * @param onCallBackListener 网络请求的回调
     * @param contractId 合同ID
     */
    void contractDetail(HttpCallBackListener<String> onCallBackListener, String contractId);

    /**
     * 合同作废
     * @param onCallBackListener
     * @param contractId
     */
    void contractInvalid(HttpCallBackListener<String> onCallBackListener, String contractId);

    /**
     * 合同签署
     * @param onCallBackListener
     * @param contractId
     */
    void contractSign(HttpCallBackListener<String> onCallBackListener, String contractId);

    /**************************签名的请求****************************/
    /**
     * 签名查看
     * @param onCallBackListener  网络请求的回调
     */
    void signDetail(HttpCallBackListener<String> onCallBackListener);

    /**
     * 签名删除
     * @param onCallBackListener
     */
    void signDelete(HttpCallBackListener<String> onCallBackListener);

    /**
     * 签名新增/绘制
     * @param onCallBackListener
     * @param signData
     */
    void signGenerate(HttpCallBackListener<String> onCallBackListener, String signData);
}
