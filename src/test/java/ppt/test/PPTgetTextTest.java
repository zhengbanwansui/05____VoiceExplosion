package ppt.test;
/**
 * 测试结果  经过PPT和PPTX的测试文件多次测试
 * PPTgetText类的函数: getPPTandPPTX可以比较准确地提取到PPT文件和PPTX文件内文本信息
 */
import ppt.PPTString;
import ppt.PPTTextSave;
import ppt.PPTgetText;
import java.util.ArrayList;
/**
 * 注释部分ppt已完成了
 * pptx部分有问题 好像提取不到信息
 */
public class PPTgetTextTest {
    public static void main(String[] args) {
        PPTgetText PG = new PPTgetText();
        // PPTTextSave PS = PG.GPPTX("C:\\Users\\14419\\Desktop\\测试.pptx");
        PPTTextSave PS = PG.GPPT("C:\\Users\\14419\\Desktop\\测试.ppt");
        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼文字测试输出▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
        ArrayList<ArrayList<PPTString>> PPTstr = PS.getArrayListArrayListPPTString();
        int i = 0;
        for (ArrayList<PPTString > strList : PPTstr) {
            System.out.println("【 第" + ++i + "页 】");
            for (PPTString temp_str : strList) {
                System.out.println("["+temp_str.textStr+"]");
            }
        }

        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼获取注释文字▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
        ArrayList<ArrayList<String>> notes = PS.notes;
        i = 0;
        for(ArrayList<String> note : notes){
            System.out.println("【 第" + ++i + "页注释 】");
            for(String temp_str : note){
                System.out.println("▼"+temp_str +"▼");
            }
        }

    }
}
