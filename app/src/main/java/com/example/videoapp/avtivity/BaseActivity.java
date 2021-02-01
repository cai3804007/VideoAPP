package com.example.videoapp.avtivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.videoapp.MainActivity;

public abstract class BaseActivity extends AppCompatActivity {
    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(initLayout());
        initView();
        initData();
    }

    protected abstract int initLayout();
    protected abstract void initView();
    protected abstract void initData();



    public void showToast(String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }

    public void showToastSync(String msg){
        Looper.prepare();
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void  navigateTo(Class cls){
        Intent login = new Intent(mContext, cls);
        startActivity(login);
    }

    protected void saveStringToSp(String key,String value){
        SharedPreferences sp = getSharedPreferences("sean_data",MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,value);
        edit.commit();
    }

}
