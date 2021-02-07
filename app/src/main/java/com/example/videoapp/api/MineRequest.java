package com.example.videoapp.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.videoapp.util.StringUtils;

import java.util.HashMap;

public class MineRequest {

    public static void login(String account,String pwd,SeanCallBack callBack){
        HashMap<String,Object> map =  new HashMap<>();
        map.put("mobile",account);
        map.put("password",pwd);
//                {"mobile":account,"pwd":pwd};
        SeanRequestManager.config(ApiConfig.LOGIN,map).postRequest(null,callBack);
    }
    public static void regster(String account,String pwd,SeanCallBack callBack){
        HashMap<String,Object> map =  new HashMap<>();
        map.put("mobile",account);
        map.put("password",pwd);
//                {"mobile":account,"pwd":pwd};
        SeanRequestManager.config(ApiConfig.REGISTER,map).postRequest(null,callBack);
    }

    public static void getVideoList(Context context, String token,Integer page,Integer pageSize,Integer type, SeanCallBack callBack){
        HashMap<String,Object> map =  new HashMap<>();
         if (StringUtils.isEmpty(token)){
             return;
         }
        map.put("token",token);
         map.put("page",page);
         map.put("limit",pageSize);
         map.put("categoryId",type);
//                {"mobile":account,"pwd":pwd};
        SeanRequestManager.config(ApiConfig.VIDEO_LIST,map).getRequest(context,callBack);
    }

    public static void getTitleList(Context context, String token, SeanCallBack callBack){
        HashMap<String,Object> map =  new HashMap<>();
        if (StringUtils.isEmpty(token)){
            return;
        }
        map.put("token",token);
//                {"mobile":account,"pwd":pwd};
        SeanRequestManager.config(ApiConfig.VIDEO_CATEGORY_LIST,map).getRequest(context,callBack);
    }


    public static void getNewList(Context context, String token,Integer page,Integer pageSize, SeanCallBack callBack){
        HashMap<String,Object> map =  new HashMap<>();
        if (StringUtils.isEmpty(token)){
            return;
        }
        map.put("token",token);
        map.put("page",page);
        map.put("limit",pageSize);

//                {"mobile":account,"pwd":pwd};
        SeanRequestManager.config(ApiConfig.NEWS_LIST,map).getRequest(context,callBack);
    }

}
