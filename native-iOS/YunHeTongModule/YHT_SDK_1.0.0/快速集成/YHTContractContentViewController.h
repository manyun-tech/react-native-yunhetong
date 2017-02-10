//
//  YHTContractContentViewController.h
//  CloudContract_SDK
//
//  Created by 吴清正 on 16/5/10.
//  Copyright © 2016年 dazheng_wu. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YHTSdk.h"

@protocol YHTContractContentViewControllerDelegate <NSObject>

- (void)contractSignFailed:(NSString *)errorMsg;

- (void)contractExpired;

- (void)contractSignSucceed;

@end

@interface YHTContractContentViewController : UIViewController

/**
 *  'YHTContractContentViewController'实例方法
 *
 *  @param contractID 合同编号
 *
 *  @return 'YHTContractContentViewController'的实例
 */

+ (instancetype)instanceWithContractID:(NSNumber *)contractID;

@property (nonatomic, weak) id<YHTContractContentViewControllerDelegate>signDelegate;
@end
