package com.scrb.baselib.util;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.util.Util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class RoundRadiusTransform extends BitmapTransformation {
    private Context context;
    //  此处用实际类的完整路径
    private static final String ID = "com.scrb.baselib.util.RoundRadiusTransform";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private int leftTop, rightTop, leftBottom, rightBottom;

    private static RoundRadiusTransform mInstance;


    /**
     * 需要设置圆角的部分
     *
     * @param leftTop     左上角
     * @param rightTop    右上角
     * @param leftBottom  左下角
     * @param rightBottom 右下角
     */
    public void setNeedCorner(int leftTop, int rightTop, int leftBottom, int rightBottom) {
        this.leftTop = GlideUtils.dp2px(context, leftTop);
        this.rightTop =  GlideUtils.dp2px(context, rightTop);
        this.leftBottom =  GlideUtils.dp2px(context, leftBottom);
        this.rightBottom =  GlideUtils.dp2px(context, rightBottom);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return TransformationUtils.roundedCorners(pool, toTransform, leftTop, rightTop, rightBottom, leftBottom);
    }

    public static RoundRadiusTransform getDefault(Context context, int leftTop, int rightTop, int leftBottom, int rightBottom) {
        if (mInstance == null) {
            synchronized (RoundRadiusTransform.class) {
                if (mInstance == null) {
                    mInstance=new RoundRadiusTransform();
                    mInstance.context=context;
                    mInstance.setNeedCorner(leftTop, rightTop, leftBottom, rightBottom);
                }
            }
        }
        return mInstance;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RoundRadiusTransform) {
            RoundRadiusTransform other = (RoundRadiusTransform) o;
            return leftTop == other.leftTop
                    &&rightTop==other.rightTop
                    &&leftBottom==other.leftBottom
                    &&rightBottom==other.rightBottom;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(ID.hashCode(), Util.hashCode(leftTop));
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

        byte[] radiusData = ByteBuffer.allocate(4).putInt(leftTop).array();
        messageDigest.update(radiusData);
    }
}