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

    public static void main(String[] args) {
        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼创建可视窗口▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
        Win win = new Win();
        try {
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
        int i = 0;
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
        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼测试输出完毕▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
        String appKey = "wpkf6dIwcNpFWhqh";
        String token  = CreateTokenDemo.getTokenFromCloud();
        SpeechTranscriberWithMicrophoneDemo saber = new SpeechTranscriberWithMicrophoneDemo(appKey, token, PS);
        // 开始运行主循环
        saber.process(win);
        saber.shutdown();
    }
}
