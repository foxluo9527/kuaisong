package com.ps.wb.ui.dialog;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ps.wb.R;
import com.ps.wb.ui.base.BaseDialog;
import com.ps.wb.ui.view.XFlowLayout;

@SuppressLint("SimpleDateFormat")
public class InputDialog extends BaseDialog implements View.OnClickListener {
    private InputDownListener listener;
    private EditText editText;
    private XFlowLayout flowLayout;
    private TextView info;
    public InputDialog(Activity context) {
        super(context);
    }

    public void setListener(InputDownListener listener) {
        this.listener = listener;
    }

    @Override
    public int initLayoutId() {
        return R.layout.dialog_input_mark;
    }

    @Override
    public float initHeightPercent() {
        return 1.0f;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void initView(View view) {
        flowLayout = view.findViewById(R.id.fl);
        editText = view.findViewById(R.id.et_content);
        info=view.findViewById(R.id.tv_info);
        view.findViewById(R.id.down).setOnClickListener(v->{
            if (listener!=null)
                listener.onInputDown(editText.getText().toString());
            dismiss();
        });
        view.findViewById(R.id.back).setOnClickListener(v->dismiss());
    }
    public void setContent(String content){
        editText.setText(content);
    }

    @Override
    public void initData() {

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void initListener() {
        for (int i = 0; i < flowLayout.getChildCount(); i++) {
            View v = flowLayout.getChildAt(i);
            if (v instanceof TextView)
                v.setOnClickListener(tv -> editText.append(((TextView) tv).getText().toString()+" "));
        }
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                info.setText(editText.getText().toString().length()+"/50字");
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

    }

    public interface InputDownListener {
        void onInputDown(String content);
    }
}
