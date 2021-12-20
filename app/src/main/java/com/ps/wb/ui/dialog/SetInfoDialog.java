package com.ps.wb.ui.dialog;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.ps.wb.R;
import com.ps.wb.ui.base.BaseDialog;

import java.util.Arrays;

@SuppressLint("SimpleDateFormat")
public class SetInfoDialog extends BaseDialog implements View.OnClickListener {
    private String[] types;
    private SetInfoListener listener;
    private TextView weight;
    private RecyclerView rvType;
    private TextView sub, add;
    private int weightKG = 1;
    private Button sure;
    private String type;


    public SetInfoDialog(Activity context) {
        super(context);

    }

    @SuppressLint("SetTextI18n")
    public void setInfo(int w, String type) {
        this.weightKG = w == 0 ? 1 : w;
        this.type = type.equals("") ? null : type;
        weight.setText(weightKG+"公斤");
        if (weightKG == 10) {
            add.setEnabled(false);
            sub.setEnabled(true);
        } else sub.setEnabled(weightKG != 1);
        initData();
    }

    public void setListener(SetInfoListener listener) {
        this.listener = listener;
    }

    @Override
    public int initLayoutId() {
        return R.layout.dialog_set_info;
    }

    @Override
    public float initHeightPercent() {
        return 1.0f;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void initView(View view) {
        weight = view.findViewById(R.id.weight);
        rvType = view.findViewById(R.id.rv_type);
        sub = view.findViewById(R.id.sub);
        add = view.findViewById(R.id.add);
        sure = view.findViewById(R.id.sure);

        view.findViewById(R.id.exit).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.sub).setOnClickListener(v -> {
            add.setEnabled(true);
            if (weightKG > 1) {
                weightKG--;
                weight.setText(weightKG + "公斤");
                if (weightKG == 1)
                    sub.setEnabled(false);
            }
        });
        view.findViewById(R.id.add).setOnClickListener(v -> {
            sub.setEnabled(true);
            if (weightKG < 10)
                weightKG++;
            if (weightKG == 10)
                add.setEnabled(false);
            weight.setText(weightKG + "公斤");
        });
        view.findViewById(R.id.sure).setOnClickListener(v -> {
            if (listener != null)
                listener.onSetInfo(type, weightKG);
            dismiss();
        });
    }


    @Override
    public void initData() {
        types = new String[]{"餐饮", "文件", "生鲜", "蛋糕", "鲜花", "钥匙", "数码", "服饰", "快递", "其他"};
        rvType.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rvType.setAdapter(new TypeAdapter(types));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void initListener() {

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

    }

    public interface SetInfoListener {
        void onSetInfo(String type, int weight);
    }

    class TypeHolder extends RecyclerView.ViewHolder {
        CheckedTextView item;

        public TypeHolder(@NonNull View itemView) {
            super(itemView);
            item = (CheckedTextView) itemView;
        }
    }

    class TypeAdapter extends RecyclerView.Adapter<SetInfoDialog.TypeHolder> {
        public boolean[] shows;

        public TypeAdapter(String[] types) {
            shows = new boolean[types.length];
            Arrays.fill(shows, false);
            if (!TextUtils.isEmpty(type))
                for (int i = 0; i < types.length; i++) {
                    if (types[i].equals(type)) {
                        shows[i] = true;
                        sure.setEnabled(true);
                        break;
                    }
                }
        }

        @NonNull
        @Override
        public TypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TypeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_type, parent, false));
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onBindViewHolder(@NonNull TypeHolder holder, int position) {
            holder.item.setText(types[position]);
            holder.item.setChecked(shows[position]);
            holder.item.setOnClickListener(v -> {
                Arrays.fill(shows, false);
                shows[position] = true;
                type = types[position];
                notifyDataSetChanged();
                sure.setEnabled(true);
            });
        }

        @Override
        public int getItemCount() {
            return types.length;
        }
    }
}
