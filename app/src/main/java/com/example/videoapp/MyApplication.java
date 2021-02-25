package com.example.videoapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;

public class MyApplication extends Application {
    private static Context context;//全局上下文

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
//        SkinCompatManager.withoutActivity(this).loadSkin();

        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinAppCompatViewInflater())
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();      // 基础控件换肤初始化

        SharedPreferences sp = getSharedPreferences("sean", MODE_PRIVATE);
        String skin = sp.getString("skin", "");
        if (skin.equals("night")) {
            SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // 后缀加载
        } else {
            SkinCompatManager.getInstance().restoreDefaultTheme();
        }

    }

    //获取全局的上下文
    public static Context getContext(){
        return context;
    }

}
