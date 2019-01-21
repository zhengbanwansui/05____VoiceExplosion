import PPT.PPTString;
import PPT.PPTTextSave;
import PPT.PPTgetText;
import com.alibaba.nls.client.example.CreateTokenDemo;
import com.alibaba.nls.client.example.SpeechTranscriberWithMicrophoneDemo;
import java.util.ArrayList;

public class JavaText {
    public static void main(String[] args) {
        //first step is getText.
        String FilePath = "C:\\\\Windows (F)\\新版测试.pptx";
        PPTTextSave PS = new PPTgetText().getPPTandPPTX(FilePath);
        ArrayList<ArrayList<PPTString>> PPTstr = PS.getArrayListArrayListPPTString();
        for(ArrayList<PPTString> strList : PPTstr){
            for(PPTString temp_str : strList){
                System.out.println(temp_str.textStr);
            }
        }
        //second step is getVoice.
        String appKey = "wpkf6dIwcNpFWhqh";
        String token  = new CreateTokenDemo().getTokenFromCloud();
        SpeechTranscriberWithMicrophoneDemo saber = new SpeechTranscriberWithMicrophoneDemo(appKey, token);
        saber.process(PPTstr);
        saber.shutdown();
    }
}
