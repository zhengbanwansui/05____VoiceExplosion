package PPT;

import java.util.ArrayList;
import java.util.Iterator;

//此类不仅需要存储PPT每页的文字，还要能确定这些文字是否匹配完了
public class PPTTextSave {
    //每页文本的List
    ArrayList<ArrayList<PPTString>> PPTStr;
    public PPTTextSave(){
        PPTStr = new ArrayList<ArrayList<PPTString>>();
    }

    //测试PPTStr是否为空
    public void PPTStrEmpty(){
        System.out.println("PPTStr-size:" + PPTStr.size());
    }

    //向PPTStr加入一页
    public void addPage(){
        PPTStr.add( new ArrayList<PPTString>() );
        System.out.println("向PPTStr加入一页,现在长度：" + PPTStr.size());
    }

    //向某一页加入一段文本
    public void add(int page,String temp_str){
        PPTStr.get(page-1).add( new PPTString(temp_str) );
        System.out.println("向某一页加入一段文本");
    }

    //向某一页加入一段文本和位置
    public void add(int page,String temp_str,int x,int y,int w,int h){
        PPTStr.get(page-1).add( new PPTString(temp_str,x,y,w,h) );
        System.out.println("向某一页加入一段文本和位置");
    }

    //得到全部页存储的信息
    public ArrayList<ArrayList<PPTString>> getArrayListArrayListPPTString(){
        return PPTStr;
    }

    //得到某一页全部文本的list
    public ArrayList<PPTString> get(int page) {
        System.out.println("得到某一页全部文本的list");
        return PPTStr.get(page-1);
    }

    //输出某一页全部文本的list
    public void show(int page) {
        //for(String temp_str : PPTStr.get(page-1)){
        for(int i=0; i<PPTStr.get(page-1).size(); i++){
            System.out.println("文字：" + PPTStr.get(page-1).get(i).textStr + " 是否匹配：" + PPTStr.get(page-1).get(i).bool);
        }
        System.out.println("输出某一页全部文本的list");
    }

    // 原文处理，去杂项文本
    public void Rule6extra(){
        // 循环检测已获取到的文字是不是有问题，把不方便匹配的文字条目都删掉
        for(int i=0; i<PPTStr.size(); i++){
            PPTStr.get(i);
            Iterator<PPTString> it = PPTStr.get(i).iterator();
            while(it.hasNext()){
                PPTString x = it.next();
                // 两位数字的文本去掉 00 01 02 10 11 12
                if(x.textStr.length() == 2 && x.textStr.charAt(0) >= 48 && x.textStr.charAt(0) <= 57 && x.textStr.charAt(1) >= 48 && x.textStr.charAt(1) <= 57 ){
                    it.remove();
                }
            }
        }
    }
    // 原文处理，前后排序工作
    public void textSortByPlace(){

    }
}
