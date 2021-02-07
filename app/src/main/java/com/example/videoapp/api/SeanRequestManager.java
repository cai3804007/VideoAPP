package com.example.videoapp.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.MainThread;

import com.blankj.utilcode.util.ThreadUtils;
import com.example.videoapp.MainActivity;
import com.example.videoapp.avtivity.LoginActivity;
import com.example.videoapp.util.AppConfig;
import com.example.videoapp.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

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
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(result);
                    }
                });
            }
        });
    }

    public void getRequest(Context context, final SeanCallBack callback) {
        SharedPreferences sp = context.getSharedPreferences("sean_data", MODE_PRIVATE);
        String token = sp.getString("token", "");
        String url = getAppendUrl(requestURL, mParams);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("token", token)
                .get()
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", e.getMessage());
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String result = response.body().string();
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(result);
                    }
                });





//                    JSONObject jsonObject = new JSONObject(result);
//                    String code = jsonObject.getString("code");
//                    if (code.equals("401")) {
//                        Intent in = new Intent(context, LoginActivity.class);
//                        context.startActivity(in);
//                    }

            }
        });
    }

    private String getAppendUrl(String url, Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (StringUtils.isEmpty(buffer.toString())) {
                    buffer.append("?");
                } else {
                    buffer.append("&");
                }
                buffer.append(entry.getKey()).append("=").append(entry.getValue());
            }
            url += buffer.toString();
        }
        return url;
    }




}
