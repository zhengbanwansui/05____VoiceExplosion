package Test;

import PPT.PPTString;
import PPT.PPTTextSave;
import PPT.PPTgetText;
import com.alibaba.nls.client.example.CreateTokenDemo;
import com.alibaba.nls.client.example.SpeechTranscriberWithMicrophoneDemo;
import org.apache.poi.openxml4j.util.ZipSecureFile;

import java.util.ArrayList;

public class JavaText {

    public static void main(String[] args) {

        // 不限制PPT文件大小
        ZipSecureFile.setMinInflateRatio(-1);
        // 获取PPT文字
        String FilePath = "C:\\Users\\14419\\Desktop\\答辩PPT.ppt";
        PPTTextSave PS = new PPTgetText().getPPTandPPTX(FilePath);
        // PPT文字处理，去杂项文本
        PS.removeNoUsingText();
        // PPT文字处理，末端位置的排序工作，末端文本的value赋值为999
        PS.textSortByLast();
        // PPT文字处理, 末端文字集合, 处理完保存到PS的lastSentence中
        PS.cutLastSentence();
        // PPT文字处理, 切词标注词性
        PS.cutText();


        System.out.println("-----------------------------------原文测试输出-----------------------------------");
        ArrayList<ArrayList<PPTString>> PPTstr = PS.getArrayListArrayListPPTString();
        int i = 0;
        for(ArrayList<PPTString> strList : PPTstr){
            System.out.println("【 第" + ++i + "页 】");
            for(PPTString temp_str : strList){
                System.out.println("["+temp_str.textStr+"]");
            }
        }
        System.out.println("-----------------------------------末端测试输出-----------------------------------");
        for(String str : PS.lastSentence){
            System.out.println(str);
        }
        System.out.println("-----------------------------------测试输出完毕-----------------------------------");


        String appKey = "wpkf6dIwcNpFWhqh";
        String token  = CreateTokenDemo.getTokenFromCloud();
        SpeechTranscriberWithMicrophoneDemo saber = new SpeechTranscriberWithMicrophoneDemo(appKey, token, PS);
        saber.process();
        saber.shutdown();
    }
}
