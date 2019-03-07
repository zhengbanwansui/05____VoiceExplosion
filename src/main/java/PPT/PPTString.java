package PPT;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.recognition.impl.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

// 每个PPTString对象，代表一个文本框的全部信息
public class PPTString {
    // bool为false, 代表此文本框内文字没读完
    public boolean bool;
    // value默认为0 每页最后一个文本框value为999
    public double value;
    // level变量保留，代码中不调用
    public int level;
    // x，y，w，h是文本框的 x坐标 y坐标 宽度 高度
    // y: 文本框左上角点距离顶端距离
    // x: 文本框左上角点距离左端距离
    // w: 文本框的宽度
    // h: 文本框的高度
    public int x,y,w,h;
    // 文本框右下角的坐标，末端匹配用的
    public int vert1,vert2;
    // textStr代表存储的字符串
    public String textStr;
    // 分词后的字符串
    public List<String> cutedStr = new ArrayList<>();
    // 分词后的字符串的词性
    public List<String> cutedArr = new ArrayList<>();
    // 提取关键词后的关键词List
    public List<KeyString> cutedKeyWords = new ArrayList<>();
    // 构造函数1
    public PPTString(String textStr){
        bool = false;
        this.textStr = textStr;
        this.value   = 0;
        this.level   = 0;
        this.vert1   = 0;
        this.vert2   = 0;
    }
    // 构造函数2
    public PPTString(String textStr,int x,int y,int w,int h){
        bool = false;
        this.textStr = textStr;
        this.value   = 0;
        this.level   = 0;
        this.x       = x;
        this.y       = y;
        this.w       = w;
        this.h       = h;
        this.vert1   = this.x + this.w;
        this.vert2   = this.y + this.h;
    }

    // NLP自然语言处理
    public void cutStrAndArr(){
        // 切词标注词性
        // 切割textStr为cutedStr和cutedArr
        // (不存储null词性的和nr人名词性的词)
        Result result = ToAnalysis.parse(textStr);
        result.recognition( new NatureRecognition() );
        for(int i=0; i<result.size(); i++){
            if( ! result.get(i).getNatureStr().equals("nr") && ! result.get(i).getNatureStr().equals("null")&& ! result.get(i).getNatureStr().equals("w")){
                cutedStr.add(result.get(i).getName());
                cutedArr.add(result.get(i).getNatureStr());
            }
        }
        // 提取关键词
        // 提取textStr为cutedKeyWords
        KeyWordComputer kwc = new KeyWordComputer(11);
        String str = this.textStr;
        Collection<Keyword> keyresult = kwc.computeArticleTfidf(str);
        Iterator t = keyresult.iterator();
        while(t.hasNext()){
            Keyword kw = (Keyword)t.next();
            cutedKeyWords.add( new KeyString(kw.getName(), kw.getScore() ) );
        }
    }
}
