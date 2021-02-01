package com.example.videoapp.api;

import android.content.Context;
import android.util.Log;

import com.example.videoapp.util.AppConfig;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SeanRequestManager {
    public  static  SeanRequestManager manager = new SeanRequestManager();
    private static OkHttpClient client;
    private static String requestURL;
    private static HashMap<String,Object> mParams;

    public static SeanRequestManager config(String url,HashMap<String,Object> params){
        client = new OkHttpClient().newBuilder().build();
        requestURL = ApiConfig.BASE_URl + url;
        mParams = params;
        return manager;
    }

    public void postRequest(Context context,final  SeanCallBack callBack){
        //1.第一步创建OKHttpClient

        JSONObject jsonObject = new JSONObject(mParams);
        String jsonStr = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(MediaType
                                             .parse("application/json;charset=utf-8"),jsonStr);
        //第三步创建Rquest
        Request request = new Request.Builder().url(requestURL)
                .addHeader("contentType", "application/json;charset=UTF-8")
                .post(requestBody).build();
        //第四步创建call回调对象
        final Call call = client.newCall(request);
        //第五步发起请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                callBack.onSuccess(result);
            }
        });



    }


}
