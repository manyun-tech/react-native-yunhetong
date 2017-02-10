package com.yunhetong.sdk.tool;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yunhetong.sdk.fast.ConfirmAlertDialog;
import com.yunhetong.sdk.fast.YhtResourceFinder;


/**
 * 获取Dialog的工具类
 *
 */
public class YhtDialogUtil {

    public static Dialog getWaitDialog(Context context, int resStr) {
        int resId  = YhtResourceFinder.findContentView(context,"yht_dialog_progress");
        View view = LayoutInflater.from(context).inflate(resId, null);
        int resId2 = YhtResourceFinder.findView(context,"yht_view_progress_dialog_tip");
        ((TextView) view.findViewById(resId2))
                .setText(context.getResources().getString(resStr));
        Dialog dialog = new Dialog(context, YhtResourceFinder.findStyle(context,"AlertDialogStyle"));/**R.style.AlertDialogStyle*/
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = YhtScreenUtil.dip2px(context,120); // 宽度
        lp.height = YhtScreenUtil.dip2px(context,120); // 高度
        dialogWindow.setAttributes(lp);
        return dialog;
    }

    public static Dialog getWaitDialog(Context context) {
        int resId  = YhtResourceFinder.findContentView(context,"yht_dialog_progress");
        View view = LayoutInflater.from(context).inflate(resId, null);
        int style  = YhtResourceFinder.findStyle(context,"AlertDialogStyle");
        Dialog dialog = new Dialog(context, style);
//        Dialog dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = YhtScreenUtil.dip2px(context,120); // 宽度
        lp.height = YhtScreenUtil.dip2px(context,120); // 高度
        dialogWindow.setAttributes(lp);
        return dialog;
    }

    public static ConfirmAlertDialog getComfireAlertDialog2(Context context,
                                                            ConfirmAlertDialog.ConfirmAlertDialogListener ls, byte targetId, Object params,
                                                            String msg) {
        ConfirmAlertDialog d = new ConfirmAlertDialog(context);
        d.setComfireAlertDialog2Click(ls);
        d.setTarget(targetId, params);
        d.confirm_alert_msg.setText(msg);
        return d;
    }

    public static ConfirmAlertDialog getComfireAlertDialog2(Context context,
                                                            ConfirmAlertDialog.ConfirmAlertDialogListener ls, byte targetId, Object params,
                                                            String msg, String no, String yes) {
        ConfirmAlertDialog d = new ConfirmAlertDialog(context);
        d.setComfireAlertDialog2Click(ls);
        d.setTarget(targetId, params);
        d.confirm_alert_msg.setText(msg);
        d.cancelBt.setText(no);
        d.sureBt.setText(yes);
        return d;
    }
}
