package PPT;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 此类存储PPT每页的文字对象
 */
public class PPTTextSave {
    // 每页文本的List,这是一个双层的list，类似二元数组，基础类型是PPTString类型
    // 每个PPTString对象，代表一个文本框的全部信息
    private ArrayList<ArrayList<PPTString>> PPTStr = new ArrayList<>();
    public ArrayList<ArrayList<String>> last = new ArrayList<>();

    /**
     * 向PPTStr加入一空页
     */
    public void addPage(){
        PPTStr.add( new ArrayList<PPTString>() );
        System.out.println("向PPTStr加入一空页,现在页数：" + PPTStr.size());
    }

    /**
     * 向某一页加入一个文本框的全部信息
     */
    public void add(int page,String temp_str){
        PPTStr.get(page-1).add( new PPTString(temp_str) );
        System.out.println("向某一页加入一个文本框的全部信息");
    }

    /**
     * 向某一页加入一个文本框的全部信息
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

    /**
     * PPT文字处理，末端位置的排序工作，末端文本的value赋值为999，意思是最后面的号
     */
    public void textSortByLast(){
        // 第几个文本
        int ans_j;
        // 最短距离的平方
        long lowest;
        // 遍历PPTStr每页
        for(int i=0; i<PPTStr.size(); i++){
            ans_j = 0;
            lowest = 999999999999999999L;
            // 遍历第 i 页的所有文本
            for(int j=0; j<PPTStr.get(i).size(); j++){
                PPTString textBox = PPTStr.get(i).get(j);
                // 找出最后一个文本框
                // 960 x 540 是画布大小
                long distance = (960-textBox.vert1) * (960-textBox.vert1) + (640-textBox.vert2) * (640-textBox.vert2);
                if(distance < lowest){
                    ans_j = j;
                    lowest = distance;
                }
            }
            System.out.println("获得最后的文本：" + PPTStr.get(i).get(ans_j).textStr);
            PPTStr.get(i).get(ans_j).value = 999;
        }

    }

    /**
     * PPT末端词语集合, 处理完保存到 last变量中 (ArrayList<ArrayList<String>> last)
     */
    public void saveLast(){
        for(int i=0; i<PPTStr.size(); i++){

            for(int j=0; j<PPTStr.get(i).size(); j++){

                if(PPTStr.get(i).get(j).value == 999){
                    // 如果这个文本框是最后一个文本框则存储其中的每个末端词语
                    PPTString box = PPTStr.get(i).get(j);
                    last.add(new ArrayList<String>());
                    // 接下来遍历box中的词语
                    int size = box.cutedStr.size();
                    if(size == 1) {
                        last.get(i).add( box.cutedStr.get(size-1) );
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
        }
    }

}

