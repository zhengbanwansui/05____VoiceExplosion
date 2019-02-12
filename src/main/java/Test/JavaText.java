package Test;

import PPT.PPTString;
import PPT.PPTTextSave;
import PPT.PPTgetText;
import Windows.Win;
import com.alibaba.nls.client.example.CreateTokenDemo;
import com.alibaba.nls.client.example.SpeechTranscriberWithMicrophoneDemo;
import org.apache.poi.openxml4j.util.ZipSecureFile;

import javax.swing.*;
import java.util.ArrayList;

public class JavaText {

    public static void main(String[] args) {
        // 创建可视化窗口Win
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Win win = new Win();
        win.setVisible(true);

        while(win.filePath.equals("NULL")){
            // 延迟50ms, 防止资源占用过高
            try { Thread.sleep(50); } catch (InterruptedException e) { e.printStackTrace(); }
        }
        String FilePath = win.filePath;
        // 不限制PPT文件大小
        ZipSecureFile.setMinInflateRatio(-1);
        // 获取PPT文字
        PPTTextSave PS = new PPTgetText().getPPTandPPTX(FilePath);
        // PPT文字处理, 去杂项文本(中文三个字以上的保留)
        PS.removeNoUsingText();
        // PPT文字处理, 切词标注词性, 去人名, 去特殊符号
        PS.cutText();
        // PPT文字处理，末端位置的排序工作，末端文本的value赋值为999
        PS.textSortByLast();
        // PPT文字处理, 末端词语集合, 处理完保存到PS的last中
        PS.saveLast();


        System.out.println("-----------------------------------原文测试输出-----------------------------------");
        ArrayList<ArrayList<PPTString>> PPTstr = PS.getArrayListArrayListPPTString();
        int i = 0;
        for(ArrayList<PPTString> strList : PPTstr){
            System.out.println("【 第" + ++i + "页 】");
            for(PPTString temp_str : strList){
                System.out.println("["+temp_str.textStr+"]");
            }
        }
        System.out.println("-----------------------------------切词测试输出-----------------------------------");
        i = 0;
        for(ArrayList<PPTString> strList : PPTstr){
            System.out.println("【 第" + ++i + "页 】");
            for(PPTString temp_str : strList){
                System.out.println("["+temp_str.cutedStr+"]");
                System.out.println("["+temp_str.cutedArr+"]");
            }
        }
        System.out.println("-----------------------------------末端测试输出-----------------------------------");
        System.out.println(PS.last);
        System.out.println("-----------------------------------测试输出完毕-----------------------------------");


        String appKey = "wpkf6dIwcNpFWhqh";
        String token  = CreateTokenDemo.getTokenFromCloud();
        SpeechTranscriberWithMicrophoneDemo saber = new SpeechTranscriberWithMicrophoneDemo(appKey, token, PS);
        saber.process();
        saber.shutdown();
    }
}
