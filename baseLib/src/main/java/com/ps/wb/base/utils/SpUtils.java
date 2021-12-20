package com.ps.wb.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.scrb.baselib.entity.User;
import com.scrb.baselib.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;


public class SpUtils {
    /**
     * 是否是第一次进入
     *
     * @param isFirst
     * @param context
     */
    public static void saveIsFirst(boolean isFirst, Context context) {
        SharedPreferences sp = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("isFirst", isFirst);
        edit.apply();
    }

    public static boolean getFirst(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        return sp.getBoolean("isFirst", true);
    }

    /**
     * 是否是第一次进入
     *
     * @param isFirst
     * @param context
     */
    public static void saveIsFirst2(boolean isFirst, Context context) {
        SharedPreferences sp = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("isFirst2", isFirst);
        edit.apply();
    }

    public static boolean getFirst2(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        return sp.getBoolean("isFirst2", true);
    }

    /**
     * 保存用户信息
     *
     * @param context
     * @return
     */
    public static void saveUserInfo(Context context, User user) {
        SharedPreferences sp = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        String jsonStr = JsonUtils.setJson(user);
        edit.putString("user", jsonStr);
        edit.apply();
    }

    public static User getUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        String userJson = sp.getString("user", null);
        return JsonUtils.getJson(userJson, User.class);
    }

    /**
     * 保存登录过的账号（切换账号）
     *
     * @param context
     * @param user
     */
    public static void saveAccount(Context context, User user) {
        SharedPreferences sp = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        List<User> list = getAccount(context);
        if (list == null) {
            list = new ArrayList<>();
        }
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == user.getId()) {
                    list.remove(list.get(i));
                }
            }
        }
        list.add(0, user);
        String strJson = JsonUtils.setJson(list);
        edit.putString("Account", strJson);
        edit.apply();
    }

    public static List<User> getAccount(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        String strJson = sp.getString("Account", "");
        if (!TextUtils.isEmpty(strJson)) {
            return JsonUtils.jsonToArrayList(strJson, User.class);
        }
        return new ArrayList<>();
    }

    public static void deleteAccount(Context context,int id){
        List<User> list = getAccount(context);
        if (list!=null&&list.size()>0){
            for (int i = 0; i <list.size() ; i++) {
                if (list.get(i).getId()==id){
                    list.remove(list.get(i));
                    break;
                }
            }
        }
        SharedPreferences sp = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        String strJson = JsonUtils.setJson(list);
        edit.putString("Account", strJson);
        edit.apply();
    }

    /**
     * 获取登录状态
     *
     * @param islogin
     * @param context
     */
    public static void saveLoginState(boolean islogin, Context context) {
        SharedPreferences sp = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("Loginstate", islogin);
        edit.apply();
    }


    public static boolean getLoginState(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        return sp.getBoolean("Loginstate", false);
    }


    /**
     * 保存URL
     *
     * @param context
     * @param URL
     */
    public static void saveURL(Context context, String URL) {
        SharedPreferences sp = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("URL", URL);
        edit.apply();
    }

    public static String getURL(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        return sp.getString("URL", "");
    }


    /**
     * 保存金币
     *
     * @param context
     * @param gold
     */
    public static void saveGold(Context context, int gold) {
        SharedPreferences sp = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        int goldCount = getGold(context)+gold;
        edit.putInt("Gold", goldCount);
        edit.apply();
    }

    public static int getGold(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        return sp.getInt("Gold", 0);
    }

    /**
     * 保存dPool
     *
     * @param context
     * @param dPools
     */
    public static void savedPool(Context context, List<String> dPools) {
        SharedPreferences sp = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        String strJson = JsonUtils.setJson(dPools);
        edit.putString("dPool", strJson);
        edit.apply();
    }

    public static List<String> getdPool(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        String strJson = sp.getString("dPool", "");
        if (!TextUtils.isEmpty(strJson)) {
            return JsonUtils.jsonToStrList(strJson);
        }
        return new ArrayList<>();
    }
}
