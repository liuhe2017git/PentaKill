package com.bwie.lianxi1_rxjava;


import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by lenovo on 2017/10/24.
 */

public class OkhttpUtils {
    private static OkHttpClient okHttpClient = null;
    private OkhttpUtils(){};
    public static OkHttpClient getOkhttpClient(){
        if(okHttpClient == null){
            synchronized (OkhttpUtils.class){
                if(okHttpClient == null){
                    okHttpClient = new OkHttpClient();
                }
            }
        }
        return okHttpClient;
    }
    public static void getOkhttp(String url,Callback callback){
        OkHttpClient okHttpClient = getOkhttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }
    public static void getOkhttpPost(String url, Map<String,String> params,Callback callback){
        OkHttpClient okHttpClient = getOkhttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        for (String map : params.keySet()) {
            builder.add(map, params.get(map));
        }
        Request request = new Request.Builder().post(builder.build()).url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }
}
