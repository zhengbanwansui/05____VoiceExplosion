package Test;

import PPT.PPTString;
import PPT.PPTTextSave;
import PPT.PPTgetText;
import com.alibaba.nls.client.example.CreateTokenDemo;
import com.alibaba.nls.client.example.SpeechTranscriberWithMicrophoneDemo;
import java.util.ArrayList;

public class JavaText {
    public static void main(String[] args) {
        //first step is getText.
        String FilePath = "C:\\Users\\14419\\Desktop\\ppt.ppt";
        PPTTextSave PS = new PPTgetText().getPPTandPPTX(FilePath);
        PS.Rule6extra();
        //测试输出
        System.out.println("------------------------------------未排序测试------------------------------------");
        ArrayList<ArrayList<PPTString>> PPTstr = PS.getArrayListArrayListPPTString();
        for(ArrayList<PPTString> strList : PPTstr){
            for(PPTString temp_str : strList){
                System.out.println(temp_str.textStr + temp_str.x + " / " + temp_str.y + " / " + temp_str.w + " / " + temp_str.h);
            }
        }
        System.out.println("------------------------------------排序后测试------------------------------------");

        System.out.println("------------------------------------排序COMPLETE------------------------------------");
        //second step is getVoice.
        String appKey = "wpkf6dIwcNpFWhqh";
        String token  = CreateTokenDemo.getTokenFromCloud();
        SpeechTranscriberWithMicrophoneDemo saber = new SpeechTranscriberWithMicrophoneDemo(appKey, token);
        saber.process(PS);
        saber.shutdown();
    }
}
