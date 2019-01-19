import PPT.PPTString;
import PPT.PPTTextSave;
import PPT.PPTgetText;

import java.util.ArrayList;

public class JavaText {
    public static void main(String[] args) {
        //first step is getText.
        String FilePath = "C:\\\\Windows (F)\\新版测试.pptx";
        PPTTextSave PS = new PPTTextSave();
        PS = new PPTgetText().getPPTandPPTX(FilePath);
        ArrayList<ArrayList<PPTString>> PPTstr = PS.getArrayListArrayListPPTString();
        for(ArrayList<PPTString> strList : PPTstr){
            for(PPTString temp_str : strList){
                System.out.println(temp_str.textStr);
            }
        }
        //second step is getVoice.
        /*
        if (args.length < 2) {
            System.err.println("SpeechRecognizerDemo need params: <app-key> <token>");
            System.exit(-1);
        }

        String appKey = args[0];
        String token = args[1];

        SpeechTranscriberWithMicrophoneDemo demo = new SpeechTranscriberWithMicrophoneDemo(appKey, token);
        demo.process();
        demo.shutdown();
        */
    }
}
