package PPT;

public class PPTString {
    // bool为false代表没读完
    public boolean bool;
    // value变量保留，代码中不调用
    public double value;
    // level变量保留，代码中不调用
    public int level;
    // x，y，w，h是文本框的 x坐标 y坐标 宽度 高度
    public int x,y,w,h;
    //textStr代表存储的字符串
    public String textStr;
    // 构造函数1
    public PPTString(String textStr){
        bool = false;
        this.textStr = textStr;
        this.value   = 0;
        this.level   = 0;
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
    }
}
