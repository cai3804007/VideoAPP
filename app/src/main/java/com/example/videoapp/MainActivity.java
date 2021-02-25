package com.example.videoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.videoapp.avtivity.BaseActivity;
import com.example.videoapp.avtivity.HomeActivity;
import com.example.videoapp.avtivity.LoginActivity;
import com.example.videoapp.avtivity.RegisterActivity;

public class MainActivity extends BaseActivity {
    Button loginbtn;
    Button registerbtn;




//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        loginbtn = findViewById(R.id.loginbtn);
//        registerbtn = findViewById(R.id.regster);
//        loginbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent login = new Intent(MainActivity.this, LoginActivity.class);
////                startActivity(login);
//                navigateTo(LoginActivity.class);
//            }
//        });
//
//    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        loginbtn = findViewById(R.id.loginbtn);
        registerbtn = findViewById(R.id.regster);
    }

    @Override
    protected void initData() {
        String token = getStringFromSp("token");
        if (token.length() > 0){
            navigateTo(HomeActivity.class);
            finish();
        }

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent login = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(login);
                navigateTo(LoginActivity.class);
            }
        });
    }

    public void regsterClick(View v){
//        Intent regster = new Intent(MainActivity.this, RegisterActivity.class);
//        startActivity(regster);
        navigateTo(RegisterActivity.class);
    }


}