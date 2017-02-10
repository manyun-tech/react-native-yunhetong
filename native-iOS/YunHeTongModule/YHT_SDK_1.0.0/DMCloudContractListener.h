//
//  DMCloudContractListener.h
//  Yml
//
//  Created by damai on 1/23/17.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YHTSdk.h"

@interface DMCloudContractListener : NSObject

//获得'ResetTokenUtil'单例
+ (DMCloudContractListener *)sharedManager;


/**
 * token : 合同的token
 * contractID : 合同的contractID
 * code 0: 失败，1：签署成功，2：作废合同
 * errorMsg 错误信息
 */
- (void) contractOperationWithToken:(NSString *)token contractID:(NSString *)contractID completion:(void (^)(NSString *code, NSString *errorMsg)) completion;

@end
