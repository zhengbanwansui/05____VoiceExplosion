package PPT;

import java.util.ArrayList;
import java.util.Iterator;

//此类不仅需要存储PPT每页的文字，还要能确定这些文字是否匹配完了
public class PPTTextSave {
    // 每页文本的List,这是一个双层的list，类似二元数组，基础类型是PPTString类型
    // 每个PPTString对象，代表一个文本框的全部信息
    private ArrayList<ArrayList<PPTString>> PPTStr;
    public ArrayList<ArrayList<String>> last = new ArrayList<>();

    // 构造函数
    public PPTTextSave(){
        PPTStr = new ArrayList<ArrayList<PPTString>>();
    }

    //测试PPTStr是否为空，保留方法，不调用
    public void PPTStrEmpty(){
        System.out.println("PPTStr-size:" + PPTStr.size());
    }

    //向PPTStr加入一空页
    public void addPage(){
        PPTStr.add( new ArrayList<PPTString>() );
        System.out.println("向PPTStr加入一空页,现在页数：" + PPTStr.size());
    }

    //向某一页加入一个文本框的全部信息
    public void add(int page,String temp_str){
        PPTStr.get(page-1).add( new PPTString(temp_str) );
        System.out.println("向某一页加入一个文本框的全部信息");
    }

    //向某一页加入一个文本框的全部信息
    public void add(int page,String temp_str,int x,int y,int w,int h){
        PPTStr.get(page-1).add( new PPTString(temp_str,x,y,w,h) );
        System.out.println("向某一页加入一个文本框的全部信息");
    }

    //得到全部页存储的全部信息
    public ArrayList<ArrayList<PPTString>> getArrayListArrayListPPTString(){
        return PPTStr;
    }

    //得到某一页全部文本框的list，类似于得到一个一元数组
    public ArrayList<PPTString> get(int page) {
        System.out.println("得到全部页存储的全部信息");
        return PPTStr.get(page-1);
    }

    //输出某一页全部文本框的全部信息
    public void show(int page) {
        for(int i=0; i<PPTStr.get(page-1).size(); i++){
            System.out.println("文字：" + PPTStr.get(page-1).get(i).textStr);
            System.out.println(" 是否匹配：" + PPTStr.get(page-1).get(i).bool);
            System.out.println(" X:"+PPTStr.get(page-1).get(i).x+" Y:"+PPTStr.get(page-1).get(i).y);
            System.out.println(" W:"+PPTStr.get(page-1).get(i).w+" H:"+PPTStr.get(page-1).get(i).h);
        }
        System.out.println("输出某一页全部文本的全部信息");
    }

    // PPT文字处理，去杂项文本
    public void removeNoUsingText(){
        // 循环检测已获取到的文字是不是有问题，把不方便匹配的文字条目都删掉
        for(int i=0; i<PPTStr.size(); i++){
            PPTStr.get(i);
            Iterator<PPTString> it = PPTStr.get(i).iterator();
            while(it.hasNext()){
                PPTString x = it.next();
                // 去掉 空格、回车、英文
                // 去掉句尾的标点
                x.textStr = x.textStr.replaceAll("\\s*", "");
                x.textStr = x.textStr.replaceAll("[a-zA-Z]","");
                x.textStr = x.textStr.replaceAll("[0-9]","");
                //x.textStr = x.textStr.replaceAll("\\pS*$|\\pP*$","");
                x.textStr = x.textStr.replaceAll("\\pS|\\pP","");
                if(x.textStr.length() == 0){
                    it.remove();
                }
                // 去掉空文本
            }
        }
    }

    // PPT文字处理, 分词标注词性
    public void cutText(){
        for(ArrayList<PPTString> AP : PPTStr){
            for(PPTString str : AP){
                str.cutStrAndArr();
            }
        }
    }

    // PPT文字处理，末端位置的排序工作，末端文本的value赋值为999，意思是最后面的号
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
                long distance = (960-textBox.vert1) * (960-textBox.vert1) + (540-textBox.vert2) * (540-textBox.vert2);
                if(distance < lowest){
                    ans_j = j;
                    lowest = distance;
                }
            }
            System.out.println("获得最后的文本：" + PPTStr.get(i).get(ans_j).textStr);
            PPTStr.get(i).get(ans_j).value = 999;
        }

    }

    // PPT文字处理, 末端词语集合, 处理完保存到PS的last (ArrayList<ArrayList<String>> last)
    public void saveLast(){

        for(int i=0; i<PPTStr.size(); i++){

            for(int j=0; j<PPTStr.get(i).size(); j++){

                if(PPTStr.get(i).get(j).value == 999){
                    // 如果这个文本框是最后一个文本框则存储其中的每个末端词语
                    PPTString box = PPTStr.get(i).get(j);
                    last.add(new ArrayList<String>());
                    // 接下来遍历box中的词语
                    int size = box.cutedStr.size();
                    // 取一个
                    if(size == 1){
                        last.get(i).add( box.cutedStr.get(size-1) );
                    }
                    // 取两个
                    else if(size == 2){
                        last.get(i).add( box.cutedStr.get(size-2) );
                        last.get(i).add( box.cutedStr.get(size-1) );
                    }
                    // 取三个
                    else if(size > 2){
                        last.get(i).add( box.cutedStr.get(size-3) );
                        last.get(i).add( box.cutedStr.get(size-2) );
                        last.get(i).add( box.cutedStr.get(size-1) );
                    }
                }
            }
        }
    }

}

