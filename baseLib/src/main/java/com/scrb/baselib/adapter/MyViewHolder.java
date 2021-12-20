package com.scrb.baselib.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 封装viewholder
 */
public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    protected MyViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        init(itemView);
    }

    private void init(View itemView) {
    }

    OnItemClickListener onItemClickListener;

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(getLayoutPosition(), v);
        }
    }

    public void onItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
}
