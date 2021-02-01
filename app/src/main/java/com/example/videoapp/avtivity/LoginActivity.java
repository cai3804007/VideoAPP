package com.example.videoapp.avtivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.videoapp.R;
import com.example.videoapp.api.MineRequest;
import com.example.videoapp.api.SeanCallBack;
import com.example.videoapp.models.LoginModel;
import com.example.videoapp.util.AppConfig;
import com.example.videoapp.util.StringUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    EditText userText;
    EditText pwdText;

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        userText = findViewById(R.id.userEditText);
        pwdText = findViewById(R.id.pwdEditText);
    }

    @Override
    protected void initData() {

    }

    public void loginClick(View view){
        //trim去掉前后空格
        String acc = userText.getText().toString().trim();
        String pwd = pwdText.getText().toString();
        login(acc,pwd);
    }

    private  void login(String account,String pwd){
        if (StringUtils.isEmpty(account)){
//            Toast.makeText(this,"请输入账号",Toast.LENGTH_SHORT).show();
            showToast("请输入账号");
            return;
        }
        if (StringUtils.isEmpty(pwd)){
//            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
            showToast("请输入密码");
            return;
        }

        MineRequest.login(account, pwd, new SeanCallBack() {
            @Override
            public void onSuccess(String response) {
                Log.e("onSuccess",response);
                Gson gson = new Gson();
                LoginModel model = gson.fromJson(response,LoginModel.class);
                if (model.code == 0){
                    String token = model.token;
//                    SharedPreferences sp = getSharedPreferences("sean_data",MODE_PRIVATE);
//                    SharedPreferences.Editor edit = sp.edit();
//                    edit.putString("token",token);
//                    edit.commit();
                    saveStringToSp("token",token);
                    navigateTo(HomeActivity.class);
                    showToastSync("登录成功");
                }else {
                    showToastSync("登录失败");
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("onFailure",e.getMessage());
            }
        });




//        OkHttpClient client = new OkHttpClient.Builder().build();
//        Map m = new HashMap();
//        m.put("mobile",account);
//        m.put("password",pwd);
//        JSONObject jsonObject = new JSONObject(m);
//        String  jsonString = jsonObject.toString();
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),jsonString);
//
//        Request request = new Request.Builder().url(AppConfig.Base_URL + "/app/login").addHeader("contentType", "application/json;charset=UTF-8")
//                .post(requestBody).build();
//        final Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("onFailure",e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                    final String result = response.body().string();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            showToast(result);
//                        }
//                    });
//            }
//        });




    }



}