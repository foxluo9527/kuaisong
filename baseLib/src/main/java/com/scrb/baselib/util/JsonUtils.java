package com.scrb.baselib.util;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Json工具
 *
 * @param <T>
 */
public class JsonUtils<T> {
    /**
     * 获取返回的response中的Success
     *
     * @param response 返回体
     * @return
     */
    public static boolean getSuccess(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean("success");
            return status;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取返回的msg
     *
     * @param response
     * @return
     */
    public static String getMsg(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String msg = jsonObject.getString("msg");
            return msg;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getStateForData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("state");
            return status;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取返回的response中的info
     *
     * @param response 返回体
     * @return
     */
    public static String getInfo(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String info = jsonObject.getString("info");
            return info;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "数据解析异常";
    }

    /**
     * 获取返回的response中的data
     *
     * @param response 返回体
     * @return
     */
    public static String getData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String data = jsonObject.getString("data");
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据dataname获取返回的response中的data
     *
     * @param response 返回体
     * @return
     */
    public static String getData(String response, String dataname) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getString(dataname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getCount(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String count = jsonObject.getString("count");
            return count;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getType(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String type = jsonObject.getString("type");
            return type;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getUrl(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String url = jsonObject.getString("url");
            return url;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static <T> T getJson(String jsonString, Class<T> clz) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(jsonString, clz);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String setJson(Object object) {
        try {
            Gson gson = new Gson();
            return gson.toJson(object);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {

        ArrayList<T> arrayList = null;
        try {
            Type type = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

            arrayList = new ArrayList<>();
            for (JsonObject jsonObject : jsonObjects) {
                arrayList.add(new Gson().fromJson(jsonObject, clazz));
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * json 转换为List<String>
     * @param json
     * @return
     */
    public static List<String> jsonToStrList(String json){
        ArrayList<String> arrayList = null;
        try {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            arrayList = new Gson().fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

}
