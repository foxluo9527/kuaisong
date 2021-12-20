package com.ps.wb.ui.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

import com.ps.wb.R;


public abstract class BaseDialog {
    public View view;
    private AlertDialog dialog;
    public Activity activity;
    private Bundle data;
    public abstract int initLayoutId();

    /**
     * @return 弹窗占屏幕高度百分比
     */
    public abstract float initHeightPercent();

    public abstract void initView(View view);

    public abstract void initData();

    public abstract void initListener();

    public BaseDialog(Activity context, Bundle data){
        activity=context;
        this.data=data;
        init();
    }

    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }

    public BaseDialog(Activity context) {
        activity=context;
        init();
    }

    @SuppressLint("RestrictedApi")
    private void init(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        view = View.inflate(activity, initLayoutId(), null);
        builder.setView(view,0,0,0,0);
        builder.setCancelable(true);
        dialog = builder.show();
        dialog.setCanceledOnTouchOutside(true);
        Window dialogWindow = dialog.getWindow();
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        p.height= WindowManager.LayoutParams.MATCH_PARENT;
        p.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(p);
        dialogWindow.setWindowAnimations(R.style.windowAnim);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initView(view);
        initData();
        initListener();
    }
    public void show(){
        dialog.show();
    }
    public void dismiss(){
        dialog.dismiss();
    }
    public Activity getActivity(){
        return activity;
    }
}
