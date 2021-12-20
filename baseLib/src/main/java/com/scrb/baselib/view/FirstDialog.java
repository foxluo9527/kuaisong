package com.scrb.baselib.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ps.wb.base.R;


/**
 * 第一次进入的弹窗
 */
public class FirstDialog extends Dialog{

    private Context mContext;
    private OnDialogListener onDialogListener;

    public FirstDialog(Context context){
        super(context, R.style.DialogStyle);
        this.mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public interface OnDialogListener{
        void onNotAgree();
        void onAgree();
        void onUserAgreement();
        void onPrivacy();
    }

    public void setOnDialogListener(OnDialogListener onDialogListener){
        this.onDialogListener=onDialogListener;
    }
    private void initView() {
        View view =View.inflate(mContext,R.layout.dialog_first,null);
        final TextView content = view.findViewById(R.id.content);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(
                mContext.getString(R.string.dialog_first_content) + " ");
        SpannableString sAgreement = new SpannableString(mContext.getString(R.string.dialog_first_agreement));
        sAgreement.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                onDialogListener.onUserAgreement();
            }
        }, 0, sAgreement.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.append(sAgreement);

        stringBuilder.append("和");

        SpannableString sPrivacy = new SpannableString(mContext.getString(R.string.dialog_first_privacy));
        sPrivacy.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                onDialogListener.onPrivacy();
            }
        }, 0, sPrivacy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.append(sPrivacy);

        content.setMovementMethod(LinkMovementMethod.getInstance());
        content.setText(stringBuilder);
        content.setHighlightColor(mContext.getResources().getColor(R.color.blue_005));

        final TextView tvAgree = view.findViewById(R.id.tv_agree);
        final TextView tvNotAgree = view.findViewById(R.id.tv_not_agree);
        setContentView(view);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);

        tvNotAgree.setOnClickListener(v -> {
            onDialogListener.onNotAgree();
            dismiss();
        });
        tvAgree.setOnClickListener(v -> {
            onDialogListener.onAgree();
            dismiss();
        });

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);

    }

}
