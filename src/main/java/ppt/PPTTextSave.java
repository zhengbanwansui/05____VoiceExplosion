package ppt;

import org.apache.poi.hslf.usermodel.HSLFTextParagraph;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**此类存储一个PPT文所有文本信息*/
public class PPTTextSave {

    private ArrayList<ArrayList<PPTString>> PPTStr = new ArrayList<>();
    public ArrayList<ArrayList<String>> last = new ArrayList<>();
    public ArrayList<ArrayList<String>> notes = new ArrayList<>();
    public String[][] notesArr;

    public void setNotesArr() {
        notesArr = new String[notes.size() + 1][5];
        for (int i = 0; i < notes.size() + 1; i++) {
            for (int j = 0; j < 5; j++) {
                notesArr[i][j] = "";
            }
        }
        int temp_page = 0;
        for (ArrayList<String> note : notes) {
            temp_page++;
            for (String str : note) {
                if (str.length() != 0) {
                    if (str.charAt(0) == 'a' || str.charAt(0) == 'A') {
                        notesArr[temp_page][1] = str.replaceAll("[Aa]", "");
                    }
                    if (str.charAt(0) == 'b' || str.charAt(0) == 'B') {
                        notesArr[temp_page][2] = str.replaceAll("[Bb]", "");
                    }
                    if (str.charAt(0) == 'c' || str.charAt(0) == 'C') {
                        notesArr[temp_page][3] = str.replaceAll("[Cc]", "");
                    }
                    if (str.charAt(0) == 'd' || str.charAt(0) == 'D') {
                        notesArr[temp_page-1][4] = str.replaceAll("[Dd]", "");
                    }
                }
            }
        }
    }

    /**
     * 向PPTStr加入一空页
     */
    public void addPage(){
        PPTStr.add( new ArrayList<PPTString>());
        System.out.println("向PPTStr加入一空页,现在页数：" + PPTStr.size());
    }

    /**
     * 向某一页加入一个文本框
     * @param page 真实页数
     * @param temp_str 字符串
     * @param x 横坐标
     * @param y 纵坐标
     * @param w 宽度
     * @param h 高度
     */
    public void add(int page,String temp_str,int x,int y,int w,int h){
        PPTStr.get(page-1).add( new PPTString(temp_str,x,y,w,h) );
        System.out.println("向某一页加入一个文本框的全部信息");
    }

    /**
     * 返回ArrayList<ArrayList<>>对象
     * @return
     */
    public ArrayList<ArrayList<PPTString>> getArrayListArrayListPPTString(){
        return PPTStr;
    }

    /**
     * PPT文字去杂项文本
     */
    public void removeNoUsingText(){
        // 循环检测, 只留下中文和部分标点
        for(int i=0; i<PPTStr.size(); i++){
            PPTStr.get(i);
            Iterator<PPTString> it = PPTStr.get(i).iterator();
            while(it.hasNext()){
                PPTString x = it.next();
                // 筛选出中文和标点   "\\s*" "[a-zA-Z]" "[0-9]" "\\pS*$|\\pP*$" "\\pS|\\pP"
                x.textStr = x.textStr.replaceAll("[^\\u4e00-\\u9fa5：；，。！？]", "");
                // 去掉太短的文本
                if(x.textStr.length() < 2){
                    it.remove();
                }
            }
        }
    }

    /**
     * PPT文字分词标注词性
     */
    public void cutText(){
        for(ArrayList<PPTString> AP : PPTStr){
            for(PPTString str : AP){
                // 这个函数是每个文本框对象的方法
                str.cutStrAndArr();
            }
        }
    }

    /** 【已修改】
     * 末端排序，末端文本的value = 999 无文本框的页不处理
     */
    public void textSortByLast() {
        // 第几个文本是最短的
        int ans_j;
        // 最短距离的平方
        long lowest;
        // 当前真实页数
        int real_page = 0;
        // 遍历PPTStr每页加上value999标志
        for (ArrayList<PPTString> pptStrings : PPTStr) {
            real_page++;
            ans_j = 0;// 初始化
            lowest = 999999999999999999L;// 初始化
            // 遍历每个文本框
            for (int j = 0; j < pptStrings.size(); j++) {
                PPTString textBox = pptStrings.get(j);
                // 找出最后一个文本框
                // 960 x 540 是画布大小
                long distance = (960 - textBox.vert1) * (960 - textBox.vert1)
                        + (540 - textBox.vert2) * (540 - textBox.vert2);
                if (distance < lowest) {
                    ans_j = j;
                    lowest = distance;
                }
            }
            // 如果没有文本框
            if (pptStrings.size() == 0) {
                System.out.println("获得最后的文本：NULL 第" + real_page +"页无文本");
            } else {// 否则输出搜索到的末端文本是哪一句
                pptStrings.get(ans_j).value = 999;
                System.out.println("获得最后的文本：" + pptStrings.get(ans_j).textStr);
            }
        }// 遍历PPTStr每页完成

    }

    /** 【需要修改】
     * PPT末端词语集合, 保存到 ArrayList<ArrayList<String>> last 中
     */
    public void saveLast() {
        // 遍历每页
        for (int i = 0; i < PPTStr.size(); i++) {
            // 遍历每个文本框
            boolean found = false;
            for (int j = 0; j < PPTStr.get(i).size(); j++) {

                if (PPTStr.get(i).get(j).value == 999) {
                    found = true;
                    // 如果文本框是最后则存储其中的每个末端词语
                    PPTString box = PPTStr.get(i).get(j);
                    last.add(new ArrayList<String>());
                    // 接下来遍历box中的词语
                    int size = box.cutedStr.size();
                    if(size == 1) {
                        last.get(i).add(box.cutedStr.get(size-1));
                    }
                    else if(size == 2) {
                        for(int temp_i=1; temp_i<=2; temp_i++) {
                            last.get(i).add(box.cutedStr.get(size-temp_i));
                        }
                    }
                    else if(size == 3) {
                        for(int temp_i=1; temp_i<=3; temp_i++) {
                            last.get(i).add(box.cutedStr.get(size-temp_i));
                        }
                    }
                    else if(size == 4) {
                        for(int temp_i=1; temp_i<=4; temp_i++) {
                            last.get(i).add(box.cutedStr.get(size-temp_i));
                        }
                    }
                    else if(size == 5) {
                        for(int temp_i=1; temp_i<=5; temp_i++) {
                            last.get(i).add(box.cutedStr.get(size-temp_i));
                        }
                    }
                    else if(size >= 6) {
                        for(int temp_i=1; temp_i<=6; temp_i++) {
                            last.get(i).add(box.cutedStr.get(size-temp_i));
                        }
                    }

                }
            }
            if (!found) {
                // 增加一页
                last.add(new ArrayList<String>());
                // 空页的末端词语只有一个, 设置为下面这个
                last.get(i).add("魉魍魅魑");
            }
        }
    }

}
