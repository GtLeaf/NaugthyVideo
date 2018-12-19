package com.example.pc_0775.naugthyvideo.util;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by PC-0775 on 2018/8/19.
 */

public class NetWorkUtil {

    private static NetWorkUtil netWorkUtil;
    private static final Gson gson = new Gson();
    private static final OkHttpClient client = new OkHttpClient();
    public static final HashMap<String, String> emptyMap = new HashMap<>();

    private NetWorkUtil(){

    }

    /**
     * 获取实例
     * @return
     */
    public static NetWorkUtil getInstant(){
        if(null == netWorkUtil){
            netWorkUtil = new NetWorkUtil();
        }
        return netWorkUtil;
    }

    /**
     * 获取内部维护的gson对象
     * @return
     */
    public static Gson getGson(){
        return gson;
    }

    /**
     * 发送get请求，异步
     * @param address
     * @param what
     * @param handler
     */
    public static void sendRequestWithOkHttp(final String address, final int what, final Handler handler){
        final Request request = new Request.Builder()
                .url(address)
                .build();
        final Message message = Message.obtain();
        message.what = what;
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        message.obj = null;
                        if (null != handler) {
                            handler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        message.obj = response.body().string();
                        if (null != handler) {
                            handler.sendMessage(message);
                        }
                    }
                });
            }
        }).start();
    }

    public static <T> T syncRequest(String address, Class<?> cls){
        Request request = new Request.Builder()
                .url(address)
                .build();
        String response = null;
        try {
            response = client.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (T)parseJsonWithGson(response,  cls);
    }

    public <T> T syncRequest(String address){
        Request request = new Request.Builder()
                .url(address)
                .build();
        String response = null;
        try {
            response = client.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parseJsonWithGson(response);
    }

    public static Uri createUri(String url, HashMap<String, String> parameters){
        Uri.Builder uriBuilder = Uri.parse(url).buildUpon();

        for(Map.Entry<String, String> entry : parameters.entrySet()){
            uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
        }

        return uriBuilder.build();
    }

    public static <T> T parseJsonWithGson(String jsonData, Class<?> cls){
        return (T)gson.fromJson(jsonData, cls);
    }

    public <T> T parseJsonWithGson(String jsonData){
        Class<T> tClass= null;
        ParameterizedType type = null;
        try {
            type = (ParameterizedType)this.getClass().getGenericSuperclass();
            tClass = (Class)type.getActualTypeArguments()[0];
        }catch (ClassCastException e){
            Log.e(TAG, "parseJsonWithGson: 未指定泛型");
        }

        return gson.fromJson(jsonData, tClass);
    }

    public static <T> T parseJsonWithGson(String jsonData, Type type){
        return (T)gson.fromJson(jsonData, type);
    }

    public static <T> List<T> parseJsonArray(String jsonData, Class<?> cls){
        List<T> tList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for(int i = 0; i<jsonArray.length(); i++){
                  T t = (T)gson.fromJson(jsonArray.getJSONObject(i).toString(), cls);
                tList.add(t);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tList;
    }

    public static String replace(String url, String key, String value) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(key)) {
            url = url.replaceAll("(" + key + "=[^&]*)", key + "=" + value);
        }
        return url;
    }

}
