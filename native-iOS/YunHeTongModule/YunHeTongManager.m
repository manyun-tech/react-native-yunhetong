//
//  YunHeTongManager.m
//  Yml
//
//  Created by damai on 1/23/17.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import "YunHeTongManager.h"
#import "DMCloudContractListener.h"

@implementation YunHeTongManager


RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(contractOptions:(NSDictionary *) info whenCompleted:(RCTResponseSenderBlock) callback) {
  
  [[DMCloudContractListener sharedManager] contractOperationWithToken:info[@"token"] contractID:info[@"contract_id"] completion:^(NSString *code, NSString *errorMsg) {
   
    if (errorMsg) {
      
      if (callback) {
        
        callback(@[@{@"message": errorMsg}, [NSNull null]]);
      }
    } else {
      
      if (callback) {
        
        callback(@[[NSNull null], code]);
      }
    }
  }];
}


@end
