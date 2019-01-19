package PPT;

import java.util.ArrayList;
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
}
