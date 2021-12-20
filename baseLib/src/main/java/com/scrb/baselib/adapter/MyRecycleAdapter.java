package com.scrb.baselib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * 封装recycle适配器
 *
 * @param <T>
 */
public abstract class MyRecycleAdapter<T> extends RecyclerView.Adapter<MyViewHolder> {
    private List<T> dataList;
    private int[] layoutIds;
    private Context mContext;

    private MyViewHolder.OnItemClickListener onItemClickListener;

    public void onItemClick(MyViewHolder.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MyRecycleAdapter(Context context, List<T> dataList, int layoutId) {
        this.mContext = context;
        this.dataList = dataList;
        this.layoutIds = new int[]{layoutId};
    }

    public MyRecycleAdapter(Context context,List<T> dataList,int[] layoutIds){
        this.mContext = context;
        this.dataList = dataList;
        this.layoutIds = layoutIds;
    }

    public abstract void onBindItem(MyViewHolder viewHolder, int position, T item);

    public abstract MyViewHolder setViewHolder(View view, int viewType);

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = null;
        View view = LayoutInflater.from(mContext).inflate(layoutIds[viewType], parent, false);
        viewHolder = setViewHolder(view, viewType);
        viewHolder.onItemClickListener(onItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        onBindItem(holder, position, dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }


}
