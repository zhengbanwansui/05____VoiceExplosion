package ppt.test;
/**
 * 测试结果  经过PPT和PPTX的测试文件多次测试
 * PPTgetText类的函数: getPPTandPPTX可以比较准确地提取到PPT文件和PPTX文件内文本信息
 */
/**
 * 遍历输出内容时, 标题用【】, 内容用[]
 */

import ppt.PPTString;
import ppt.PPTTextSave;
import ppt.PPTgetText;
import java.util.ArrayList;

public class PPTgetTextTest {
    public static void main(String[] args) {
        PPTgetText PG = new PPTgetText();
        PPTTextSave PS = PG.getPPTandPPTX("C:\\Users\\14419\\Desktop\\测试.ppt");
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
