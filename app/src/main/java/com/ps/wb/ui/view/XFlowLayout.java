package com.ps.wb.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.ps.wb.R;

import java.util.HashMap;
import java.util.Map;

/**
 * @SharksLee lishaojie
 */
public class XFlowLayout extends ViewGroup {
    private int childHorizontalSpace;
    private int childVerticalSpace;
    private Map<Object, Location> mLocationMap;

    public XFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        @SuppressLint("CustomViewStyleable")
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.XLFlowLayout);
        mLocationMap = new HashMap<>();
        childHorizontalSpace = attrArray.getDimensionPixelSize(R.styleable.XLFlowLayout_XLFlowLayout_childHorizontalSpace, 0);
        childVerticalSpace = attrArray.getDimensionPixelSize(R.styleable.XLFlowLayout_XLFlowLayout_childVerticalSpace, 0);
        attrArray.recycle();
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * 负责设置子控件的测量模式和大小 根据所有子控件设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mLocationMap != null && !mLocationMap.isEmpty()) {
            mLocationMap.clear();
        }
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        // 如果是warp_content情况下，记录宽和高
        int width = 0;
        int height = 0;
        /**
         * 记录每一行的宽度，width不断取最大宽度
         */
        int lineWidth = 0;
        /**
         * 每一行的高度，累加至height
         */
        int lineHeight = 0;

        int count = getChildCount();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        // 遍历每个子元素
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;
            // 测量每一个child的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            // 得到child的lp
            LayoutParams lp = child.getLayoutParams();
            // 当前子空间实际占据的宽度
            int childWidth = child.getMeasuredWidth() + childHorizontalSpace;
            // 当前子空间实际占据的高度
            int childHeight = child.getMeasuredHeight() + childVerticalSpace;

            if (lp != null && lp instanceof MarginLayoutParams) {
                MarginLayoutParams params = (MarginLayoutParams) lp;
                childWidth += params.leftMargin + params.rightMargin;
                childHeight += params.topMargin + params.bottomMargin;
            }

            /**
             * 如果加入当前child的宽度，超出了最大宽度，则的到目前最大宽度给width，累加height 然后开启新行
             */
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                width = Math.max(lineWidth, childWidth);// 取最大的
                lineWidth = childWidth; // 重新开启新行，开始记录
                // 叠加当前高度，
                height += lineHeight;
                // 开启记录下一行的高度
                lineHeight = childHeight;
                mLocationMap.put(child, new Location(left, top + height, childWidth + left - childHorizontalSpace, height + 

child.getMeasuredHeight() + top));
            } else {// 否则累加值lineWidth,lineHeight取最大高度
                mLocationMap.put(child, new Location(lineWidth + left, top + height, lineWidth + childWidth - 

childHorizontalSpace + left, height + child.getMeasuredHeight() + top));
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
        }
        width = Math.max(width, lineWidth) + getPaddingLeft() + getPaddingRight();
        height += lineHeight;
        sizeHeight += getPaddingTop() + getPaddingBottom();
        height += getPaddingTop() + getPaddingBottom();
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width, (modeHeight == MeasureSpec.EXACTLY) ? 

sizeHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;
            Location location = mLocationMap.get(child);
            child.layout(location.left, location.top, location.right, location.bottom);
        }
    }

    /**
     * 记录子控件的坐标
     */
    public static class Location {
        public int left;
        public int top;
        public int right;
        public int bottom;

        Location(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

    }
}