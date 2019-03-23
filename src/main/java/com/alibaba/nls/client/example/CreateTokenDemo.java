package com.alibaba.nls.client.example;

import com.alibaba.nls.client.AccessToken;

public class CreateTokenDemo {
    public static String getTokenFromCloud(){
        String akId = "LTAIQTaHAkBNH2yt";
        String akSecrete = "sXF07GtGOZlVLMKLs5wh7EH9T4m5mA";
        try {
            AccessToken accessToken = AccessToken.apply(akId, akSecrete);
            return accessToken.getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取失败";
    }
}
