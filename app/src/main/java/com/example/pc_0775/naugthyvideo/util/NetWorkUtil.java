package com.example.pc_0775.naugthyvideo.util;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

/**
 * Created by PC-0775 on 2018/8/19.
 */

public class NetWorkUtil {

    private static NetWorkUtil netWorkUtil;
    private static final Gson gson = new Gson();
    private static final OkHttpClient client = new OkHttpClient();
    private static Uri uri;

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

    public static Gson getGson(){
        return gson;
    }

    public static void sendRequestWithOkHttp(final String address, final int what, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(address)
                        .build();
                final Message message = Message.obtain();
                message.what = what;
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
                        String responseData = response.body().string();
                        message.obj = responseData;
                        if (null != handler) {
                            handler.sendMessage(message);
                        }
                    }
                });

            }
        }).start();
    }

    public static Uri createUri(String url, HashMap<String, String> parameters){
        Uri.Builder uriBuilder = Uri.parse(url).buildUpon();

        for(Map.Entry<String, String> entry : parameters.entrySet()){
            uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
        }

        Uri uri = uriBuilder.build();
        return uri;
    }

    public static <T> T parseJsonWithGson(String jsonData, Class<?> cls){
        return (T)gson.fromJson(jsonData, cls);
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

    public static Uri getUri() {
        return uri;
    }

    public static void setUri(Uri uri) {
        NetWorkUtil.uri = uri;
    }

}
