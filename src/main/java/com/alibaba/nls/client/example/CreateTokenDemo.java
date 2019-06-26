package com.alibaba.nls.client.example;

import com.alibaba.nls.client.AccessToken;

/**
 * 阿里云: 创建token
 */
public class CreateTokenDemo {
    public static String getTokenFromCloud(String akId, String akSecret){
        akId = "LTAIQTaHAkBNH2yt";
        akSecret = "sXF07GtGOZlVLMKLs5wh7EH9T4m5mA";
        try {
            AccessToken accessToken = AccessToken.apply(akId, akSecret);
            return accessToken.getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取失败";
    }
}
