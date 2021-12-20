package com.scrb.baselib.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ps.wb.base.R;


public class GlideUtils {


    /**
     * 加载头像图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void intoHead(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.ic_head_null)
                .error(R.mipmap.ic_head_null)
                .into(imageView);
    }

    /**
     * 普通图片 （获取多张中第一张）
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void intoSingle(Context context, String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url)) {
            url = MyUtil.getSingleURL(url);
            Glide.with(context)
                    .load(url)
                    .placeholder(R.mipmap.ic_head_null)
                    .error(R.mipmap.ic_head_null)
                    .into(imageView);
        }

    }

    /**
     * 普通图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void intoNormal(Context context, String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.mipmap.ic_head_null)
                    .error(R.mipmap.ic_head_null)
                    .into(imageView);
        }

    }

    /**
     * 加载说说的图片 （无占位图）
     * @param context
     * @param url
     * @param imageView
     */
    public static void intoNoPlaceholder(Context context, String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url)&&MyUtil.checkUrl(url)) {
            url = MyUtil.getSingleURL(url);
            Glide.with(context)
                    .load(url)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                    .into(imageView);
        }

    }

    public static void intoRound(Context context, String url, int leftTop,int rightTop,int leftBottom,int rightBottom,ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(RoundRadiusTransform.getDefault(context,leftTop,rightTop,leftBottom,rightBottom)))
                .placeholder(R.mipmap.ic_head_null)
                .error(R.mipmap.ic_head_null)
                .into(imageView);

    }
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    public static void intoHeadRound(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(dp2px(context,45f))))
                .placeholder(R.mipmap.ic_head_null)
                .error(R.mipmap.ic_head_null)
                .into(imageView);

    }

}
