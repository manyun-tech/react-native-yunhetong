import {
    NativeModules
} from 'react-native';

let YunHeTongManager = NativeModules.YunHeTongManager;

export default {

    /**
     * 合同签署
     * params:
     *         token :合同的token
     *         contract_id :合同的contractId
     
     * callback: (code,err) => {}
     *           code ：  0:修改失败  1 合同签署  2 合同作废
     *           err  ：  null 成功   不为空则失败
     *
     */
    contractOptions: (params,callback) => {
        YunHeTongManager.contractOptions(params,callback);
    },

};
