import PPT.PPTString;
import PPT.PPTTextSave;
import PPT.PPTgetText;
import com.alibaba.nls.client.example.CreateTokenDemo;
import com.alibaba.nls.client.example.SpeechTranscriberWithMicrophoneDemo;
import java.util.ArrayList;

public class JavaText {
    public static void main(String[] args) {
        //first step is getText.
        String FilePath = "C:\\\\ppt.pptx";
        PPTTextSave PS = new PPTgetText().getPPTandPPTX(FilePath);
        //测试输出
        System.out.println("------------------------------------测试输出------------------------------------");
        ArrayList<ArrayList<PPTString>> PPTstr = PS.getArrayListArrayListPPTString();
        for(ArrayList<PPTString> strList : PPTstr){
            for(PPTString temp_str : strList){
                System.out.println(temp_str.textStr);
            }
        }
        System.out.println("------------------------------------测试输出------------------------------------");
        //second step is getVoice.
        String appKey = "wpkf6dIwcNpFWhqh";
        String token  = new CreateTokenDemo().getTokenFromCloud();
        SpeechTranscriberWithMicrophoneDemo saber = new SpeechTranscriberWithMicrophoneDemo(appKey, token);
        saber.process(PS);
        saber.shutdown();
    }
}
