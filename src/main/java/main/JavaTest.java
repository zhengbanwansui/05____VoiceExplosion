package main;

import ppt.KeyString;
import ppt.PPTString;
import ppt.PPTTextSave;
import ppt.PPTgetText;
import windows.Win;
import com.alibaba.nls.client.example.CreateTokenDemo;
import com.alibaba.nls.client.example.SpeechTranscriberWithMicrophoneDemo;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import javax.swing.*;
import java.util.ArrayList;

public class JavaTest {
    // 老师 演讲者 学生 把感兴趣的和新功能做好
    // 核心功能做好 控制指令拿掉 说到第五页
    // 注释优先度最高 注释没有 再去判别内容
    // 语义理解的包塞进去
    // 把它试明白
    public static void main(String[] args) {
        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼创建可视窗口▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
        Win win = new Win();
        try {
            // 设置UI风格
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼等待传入文件▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
        while(win.filePath.equals("NULL")){
            // 延迟50ms, 防止资源占用过高
            try { Thread.sleep(50); } catch (InterruptedException e) { e.printStackTrace(); }
        }
        win.changeDragArea(1);
        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼获取PPT文字▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
        String FilePath = win.filePath;
        // 不限制PPT文件大小
        ZipSecureFile.setMinInflateRatio(-1);
        // 获取PPT文字
        PPTTextSave PS = new PPTgetText().getPPTandPPTX(FilePath);
        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼获取注释文字▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
        ArrayList<ArrayList<String>> notes = PS.notes;
        int i = 0;
        for(ArrayList<String> note : notes){
            System.out.println("【 第" + ++i + "页注释 】");
            for(String temp_str : note){
                System.out.println("["+temp_str +"]");
            }
        }
        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼PPT文字处理▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
        // PPT文字处理, 去杂项文本(大于等于两个字的保留)
        PS.removeNoUsingText();
        // PPT文字处理, NLP 切词标注词性, 去人名, 去特殊符号 + 提取关键词
        PS.cutText();
        // PPT文字处理，末端位置的排序工作，末端文本的value赋值为999
        PS.textSortByLast();
        // PPT文字处理, 末端词语集合, 处理完保存到PS的last中
        PS.saveLast();
        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼文字测试输出▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
        ArrayList<ArrayList<PPTString>> PPTstr = PS.getArrayListArrayListPPTString();
        for(ArrayList<PPTString> strList : PPTstr){
            System.out.println("【 第" + ++i + "页 】");
            for(PPTString temp_str : strList){
                System.out.println("["+temp_str.textStr+"]");
            }
        }
        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼切词测试输出▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
        i = 0;
        for(ArrayList<PPTString> strList : PPTstr){
            System.out.println("【 第" + ++i + "页 】");
            for(PPTString temp_str : strList){
                System.out.println("["+temp_str.cutedStr+"]");
                System.out.println("["+temp_str.cutedArr+"]");
            }
        }
        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼关键词测试输出▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
        i = 0;
        for(ArrayList<PPTString> slideTxts : PS.getArrayListArrayListPPTString()) {
            System.out.print("第" + ++i + "页的关键词是\n");
            for(PPTString str : slideTxts) {
                for(KeyString ks : str.cutedKeyWords) {
                     System.out.print("(" + ks.str + ")");
                 }
                System.out.print("\n");
             }
        }
        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼末端测试输出▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
        i = 0;
        for (ArrayList<String> ArrayListString : PS.last) {
            System.out.println("第" + ++i + "页的末端词语是 : " + ArrayListString);
        }
        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼测试输出完毕▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
        // 生成Token
        String akId = "LTAIQTaHAkBNH2yt";
        String akSecret = "sXF07GtGOZlVLMKLs5wh7EH9T4m5mA";
        String token  = CreateTokenDemo.getTokenFromCloud(akId, akSecret);
        // 选择appKey
        String 通用普通话 = "wpkf6dIwcNpFWhqh";
        String 演讲领域   = "r9hblBRwX3wKR7KY";
        String 出行领域   = "tY8erI0KqWBhmyW7";
        String 医疗领域   = "35r32kJyBqzq6e0S";
        String 四川方言   = "0KFh18PbuTbyXSWX";
        String 湖北方言   = "aDLpVjxKIWY7oX40";
        String 新零售领域 = "bOitytpWGObws7AT";
        String 政法庭审   = "u2brGdPHVCy0I8E3";

        String 金融领域   = "0VbQuGQu32b1dNtd";
        String 粤语       = "JiI6AJwyyCmdRtQy";
        String 其他方言   = "4sqleDkRpKmyyw0S";
        String appKey;
        String pullListString = win.pullListLanguage.getSelectedItem().toString();
        float sampleRate = 16000.0F;
        if (pullListString == "通用普通话") {
            appKey = 通用普通话;
        } else if (pullListString == "演讲领域") {
            appKey = 演讲领域;
        } else if (pullListString == "出行领域") {
            appKey = 出行领域;
        } else if (pullListString == "医疗领域") {
            appKey = 医疗领域;
        } else if (pullListString == "四川方言") {
            appKey = 四川方言;
        } else if (pullListString == "湖北方言") {
            appKey = 湖北方言;
        } else if (pullListString == "新零售领域") {
            appKey = 新零售领域;
        } else if (pullListString == "政法庭审") {
            appKey = 政法庭审;
        } else if (pullListString == "金融领域") {
            appKey = 金融领域;
            sampleRate = 8000.0F;
        } else if (pullListString == "粤语") {
            appKey = 粤语;
            sampleRate = 8000.0F;
        } else if (pullListString == "其他方言") {
            appKey = 其他方言;
            sampleRate = 8000.0F;
        } else {
            appKey = " ";
        }
        // 判断是语音识别还是语音合成
        String pullListPlayType = win.pullListPlayType.getSelectedItem().toString();
        if (pullListPlayType.equals("您来演讲")) {
            SpeechTranscriberWithMicrophoneDemo moChiZou = new SpeechTranscriberWithMicrophoneDemo(appKey, token, PS, sampleRate);
            moChiZou.process(win);
            moChiZou.shutdown();
        } else if (pullListPlayType.equals("小音帮您讲")) {
            win.changeDragArea(3);
            System.out.println("这块还没写");
        }

    }
}
