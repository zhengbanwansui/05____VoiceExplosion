package com.alibaba.nls.client.example;

import com.alibaba.nls.client.AccessToken;

public class CreateTokenDemo {
    public static String getTokenFromCloud(){
        String akIdd = "LTAIQTaHAkBNH2yt";
        String akSecretee = "sXF07GtGOZlVLMKLs5wh7EH9T4m5mA";
        try {
            AccessToken accessToken = AccessToken.apply(akIdd, akSecretee);
            return accessToken.getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取失败";
    }
    /*public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("CreateTokenDemo need params: <AccessKeyId> <AccessKeySecret>");
            System.exit(-1);
        }
        String akId = args[0];
        String akSecrete = args[1];
        try {
            AccessToken accessToken = AccessToken.apply(akId, akSecrete);
            System.out.println("  ");
            System.out.println(
                    "创建token: " +
                            accessToken.getToken() +
                    // 有效时间，单位为秒
                    ", 有效时间，单位为秒: " +
                            accessToken.getExpireTime()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
