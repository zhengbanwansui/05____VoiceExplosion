package PPT.test;

import PPT.PPTString;
import PPT.PPTTextSave;
import PPT.PPTgetText;
import java.util.ArrayList;

public class PPTgetTextTest {
    public static void main(String[] args) {
        PPTgetText PG = new PPTgetText();
        PPTTextSave PS = PG.GPPTX("C:\\Users\\14419\\Desktop\\无法提取的内容.pptx");

        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼文字测试输出▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
        ArrayList<ArrayList<PPTString>> PPTstr = PS.getArrayListArrayListPPTString();
        int i = 0;
        for (ArrayList<PPTString > strList : PPTstr) {
            System.out.println("【 第" + ++i + "页 】");
            for (PPTString temp_str : strList) {
                System.out.println("["+temp_str.textStr+"]");
            }
        }

    }
}
