package com.alibaba.nls.client.example;

import com.alibaba.nls.client.AccessToken;

/**
 * 阿里云: 创建token
 */
public class CreateTokenDemo {
    public static String getTokenFromCloud(String akId, String akSecret){
        // 这两个值是阿里云服务的id和secret，需要自己申请
        akId = ""
        akSecret = ""
        try {
            AccessToken accessToken = AccessToken.apply(akId, akSecret);
            return accessToken.getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取失败";
    }
}
