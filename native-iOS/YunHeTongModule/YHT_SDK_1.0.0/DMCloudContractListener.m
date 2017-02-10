//
//  DMCloudContractListener.m
//  Yml
//
//  Created by damai on 1/23/17.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import "DMCloudContractListener.h"
#import "YHTContractContentViewController.h"

@interface DMCloudContractListener ()<YHTContractContentViewControllerDelegate>

@property (nonatomic, strong) void (^callback)(NSString *code, NSString *errorMsg);

@end

@implementation DMCloudContractListener

#pragma mark - YHTResetTokenDelegate


+ (instancetype)sharedManager {
  
  static DMCloudContractListener *staticSocialObject = nil;
  static dispatch_once_t onceToken;
  dispatch_once(&onceToken, ^{
  
    staticSocialObject = [[DMCloudContractListener alloc] init];
  });
  return staticSocialObject;
}

/**
 * token : 合同的token
 * contractID : 合同的contractID
 * code @"0": 失败，@"1"：签署成功，@"2"：作废合同
 * errorMsg 错误信息
 */

- (void) contractOperationWithToken:(NSString *)token contractID:(NSString *)contractID completion:(void (^)(NSString *code, NSString *errorMsg)) completion {
    
    _callback = completion;
    [[YHTTokenManager sharedManager] setTokenWithString:token];
    YHTContractContentViewController *vc = [YHTContractContentViewController instanceWithContractID:@(contractID.longLongValue)];
    vc.signDelegate = self;
    UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:vc];
    UIViewController *controller = [UIApplication sharedApplication].keyWindow.rootViewController;
    dispatch_async(dispatch_get_main_queue(), ^{
        [controller presentViewController:nav animated:YES completion:^{
            
        }];
    });
}

#pragma mark - YHTContractContentViewControllerDelegate

- (void)contractSignFailed:(NSString *)errorMsg {
  
  _callback(@"0", errorMsg);
}

- (void)contractExpired {
  
  _callback(@"2", @"");

}

- (void)contractSignSucceed {
  
  _callback(@"1", @"");
}

@end
