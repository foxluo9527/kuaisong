package com.scrb.baselib.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.ps.wb.base.R;


public class CleanDialog extends Dialog {
    private Context context;
    private OnDialogListener onDialogListener;

    public CleanDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
        this.context=context;
    }
    public interface OnDialogListener{
        void onSure();
    }

    public void  setDialogListener(OnDialogListener onDialogListener){
        this.onDialogListener=onDialogListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_clean,null);
        RelativeLayout rlSure = view.findViewById(R.id.rl_sure);
        RelativeLayout rlCanel = view.findViewById(R.id.rl_cancel);
        setContentView(view);
        rlSure.setOnClickListener(v -> {
            onDialogListener.onSure();
            dismiss();
        });
        rlCanel.setOnClickListener(v -> dismiss());
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width=WindowManager.LayoutParams.MATCH_PARENT;
        params.height= WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(params);
    }
}
