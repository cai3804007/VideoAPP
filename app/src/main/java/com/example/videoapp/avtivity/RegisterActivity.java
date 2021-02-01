package com.example.videoapp.avtivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.videoapp.R;
import com.example.videoapp.api.MineRequest;
import com.example.videoapp.api.SeanCallBack;
import com.example.videoapp.util.StringUtils;

public class RegisterActivity extends BaseActivity {
    EditText userText;
    EditText pwdText;

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        userText = findViewById(R.id.reTex);
        pwdText = findViewById(R.id.pwdText);
    }

    @Override
    protected void initData() {

    }

    public void regesterClick(View view){
        //trim去掉前后空格
        String acc = userText.getText().toString().trim();
        String pwd = pwdText.getText().toString();
        regster(acc,pwd);
    }

    public void regster(String account,String pwd){
        if (StringUtils.isEmpty(account)){
            showToast("请输入账号");
            return;
        }
        if (StringUtils.isEmpty(pwd)){
            showToast("请输入密码");
            return;
        }
        MineRequest.regster(account, pwd, new SeanCallBack() {
            @Override
            public void onSuccess(String response) {
                showToastSync(response);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("onFailure",e.getMessage());
            }
        });
    }


}