//
//  YHTContractContentViewController.m
//  CloudContract_SDK
//
//  Created by 吴清正 on 16/5/10.
//  Copyright © 2016年 dazheng_wu. All rights reserved.
//

#import "YHTContractContentViewController.h"
#import "YHT_MBProgressHUD+Wqz.h"
#import "YHTContractWebView.h"
#import "YHTContractPartner.h"
#import "YHTContractOperateMenu.h"
#import "UIImage+Wqz.h"
#import "YHTContract.h"
@interface YHTContractContentViewController ()<YHTHttpRequestDelegate>

@property (nonatomic, strong)YHTContractPartner *partner;

@property (nonatomic, strong)YHTContractOperateMenu *operateMenu;

@property (nonatomic, strong)YHTContractWebView *webView;

@property (nonatomic, strong)NSNumber *contractID;

@property (nonatomic, assign)BOOL DMContractSigned;
@property (nonatomic,assign)ContractOperateType operateType ;
@property (nonatomic ,strong) UIButton * btnSigned ;

@end

@implementation YHTContractContentViewController

+ (instancetype)instanceWithContractID:(NSNumber *)contractID{
  
  YHTContractContentViewController *vc = [[YHTContractContentViewController alloc] init];
    vc.contractID = contractID;

    return vc;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.title = @"签署合同";
    self.view.backgroundColor = [UIColor whiteColor];
    CGSize frameSize = self.view.frame.size ;
    self.webView = [YHTContractWebView instanceWithContractID:_contractID delegate:nil];
  CGFloat webHeight = frameSize.height - 48 ;
    self.webView.frame = CGRectMake(0, 0, frameSize.width, webHeight);
    [self.view addSubview:self.webView];
  
//  UIButton * btnCancel = [UIButton buttonWithType:UIButtonTypeSystem] ;
//  btnCancel.frame = CGRectMake( 0 , webHeight, frameSize.width/2, 48);
//  [btnCancel setBackgroundColor:[UIColor colorWithRed:0.95 green:0.95 blue:0.95 alpha:1]] ;
//  [btnCancel setTitle:@"作废合同" forState:UIControlStateNormal] ;
//  [btnCancel setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
//  [btnCancel addTarget:self action:@selector(btnCancelClick) forControlEvents:UIControlEventTouchUpInside];
//    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//        [self webViewRefresh];
//    });
//  
//  [self.view addSubview:btnCancel];
  
  UIButton * btnSure = [UIButton buttonWithType:UIButtonTypeSystem] ;
  btnSure.frame = CGRectMake( 0 , webHeight, frameSize.width, 48);
  [btnSure setBackgroundColor:[UIColor colorWithRed:0x1c/255.0 green:0xad/255.0 blue:0xf9/255.0 alpha:1]] ;
  [btnSure setTitle:@"签署合同" forState:UIControlStateNormal] ;
  [btnSure setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
  [btnSure addTarget:self action:@selector(btnSureClick) forControlEvents:UIControlEventTouchUpInside];
  [self.view addSubview:btnSure];
  
  self.btnSigned = [UIButton buttonWithType:UIButtonTypeSystem] ;
  self.btnSigned.frame = CGRectMake( 0 , webHeight, frameSize.width, 48);
  [self.btnSigned setBackgroundColor:[UIColor colorWithRed:0.95 green:0.95 blue:0.95 alpha:1]] ;
  [self.btnSigned setTitle:@"报名充值" forState:UIControlStateNormal] ;
  [self.btnSigned setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
  [self.btnSigned addTarget:self action:@selector(handleConfirmContractSignedButtonCllicked) forControlEvents:UIControlEventTouchUpInside];
  self.btnSigned.hidden = true ;
  [self.view addSubview:self.btnSigned];
  
  dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
    [self webViewRefresh];
  });
  
  self.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"返回" style:UIBarButtonItemStylePlain target:self action:@selector(handleBackButtonClicked)];
  //  [self rightBarButtonItemRefresh];
}

- (void)buttonClick{
    [self.operateMenu show];
}
-(void) btnCancelClick{
  [self popTableView:ContractOperateType_Invalid];
}
-(void) btnSureClick{
  [self popTableView:ContractOperateType_Sign];
}
- (void)popTableView:(ContractOperateType )operateType {
  self.operateType = operateType ;
  NSString *str = operateType == ContractOperateType_Sign? @"确认签署" : @"确认作废";
  
  UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:str
                                                      message:[NSString stringWithFormat:@"是否%@？",str]
                                                     delegate:self
                                            cancelButtonTitle:@"取消"
                                            otherButtonTitles:@"确认",
                            nil];
  [alertView show];
  
}
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
  if (buttonIndex == 0) {
    return;
  }else{
    if (!self.partner.hasSign) {
      [self didPushWithViewController];
      return;
    }
    [self didSelectedOperate:self.operateType];
  }
  
}
- (void)webViewRefresh{
    [self.webView refresh];

    [[YHTContractManager sharedManager] viewContactWithContractID:_contractID
                                                              tag:@"ViewContract"
                                                         delegate:self];

}

- (void)rightBarButtonItemRefresh{
   if (_DMContractSigned) {
      
      self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"确定" style:UIBarButtonItemStylePlain target:self action:@selector(handleConfirmContractSignedButtonCllicked)];
      //
    } else {
        self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithImage:[UIImage imagesNamedFromCustomYHTSdkBundle:@"nav_more_n"]
                                                                                  style:UIBarButtonItemStylePlain
                                                                                 target:self
                                                                                 action:@selector(buttonClick)];
    }
}

#pragma mark - YHTHttpRequestDelegate
- (void)request:(YHTHttpRequest *)request didFailWithError:(NSError *)error{
//    [YHT_MBProgressHUD showError:error.localizedDescription];
  if (_signDelegate && [_signDelegate respondsToSelector:@selector(contractSignFailed:)]) {
    
    [_signDelegate contractSignFailed:error.localizedDescription];
    [self.navigationController popToRootViewControllerAnimated:YES];
  }
}

- (void)request:(YHTHttpRequest *)request didFinishLoadingWithResult:(id)result{
  NSLog(@"request %@" , request.tag);
  if ([request.tag isEqualToString:@"ViewContract"]) {
        self.partner = [YHTContractPartner instanceWithDict:result[@"value"][@"partner"]];
     //   [self rightBarButtonItemRefresh];
    }else if ([request.tag isEqualToString:@"SignContract"]){
      
      _DMContractSigned = YES;
    //  [self rightBarButtonItemRefresh];
    //  self.btnSigned.frame = CGRectMake( 0 , self.view.frame.size.height - 48 , self.view.frame.size.width, 48);
      self.btnSigned.hidden = false ;
      
      dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [self webViewRefresh];
      });
      
      [self webViewRefresh];
      [self.navigationController popToRootViewControllerAnimated:YES];
    }else if ([request.tag isEqualToString:@"InvalidContract"]){
     
        _DMContractSigned = NO;
      if (_signDelegate && [_signDelegate respondsToSelector:@selector(contractExpired)]) {
        
        [_signDelegate contractExpired];
        [self.navigationController popToRootViewControllerAnimated:YES];
      }
      [self webViewRefresh];
    //  [self rightBarButtonItemRefresh];
    }
}

#pragma mark - GET/SET
- (YHTContractPartner *)partner{
    if (!_partner) {
        _partner = [[YHTContractPartner alloc] init];
    }

    return _partner;
}

- (YHTContractOperateMenu *)operateMenu{
    if (self.partner == nil || [self.partner titlesAndOperateTypes] == nil) {
        self.navigationItem.rightBarButtonItem = nil;
        return nil;
    }

    _operateMenu = [YHTContractOperateMenu instanceWithContract:self.partner delegate:self];

    return _operateMenu;
}

#pragma mark - ContractOperateDelegate
- (void)didSelectedOperate:(ContractOperateType)__operateType{
    if(__operateType == ContractOperateType_Sign){
        [[YHTContractManager sharedManager] signContractWithContractID:self.contractID
                                                                   tag:@"SignContract"
                                                              delegate:self];

    }else if (__operateType == ContractOperateType_Invalid) {
        [[YHTContractManager sharedManager] invalidContractWithContractID:self.contractID
                                                                      tag:@"InvalidContract"
                                                                 delegate:self];
        
    }
}

- (void)didPushWithViewController{
    [self.navigationController pushViewController:[YHTSignMadeViewController instanceWithDelegate:self] animated:YES];
}

- (void)onMadeSignSuccessed:(id)__id{
    [[YHTContractManager sharedManager] signContractWithContractID:self.contractID
                                                               tag:@"SignContract"
                                                          delegate:self];
}

#pragma mark - actions

- (void)handleBackButtonClicked {
  
  [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)handleConfirmContractSignedButtonCllicked {
  
  if (_signDelegate && [_signDelegate respondsToSelector:@selector(contractSignSucceed)]) {
    
    [_signDelegate contractSignSucceed];
    [self dismissViewControllerAnimated:YES completion:nil];
  }
}
@end
