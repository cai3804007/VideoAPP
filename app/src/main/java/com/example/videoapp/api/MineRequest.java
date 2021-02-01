package com.example.videoapp.api;

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
}
