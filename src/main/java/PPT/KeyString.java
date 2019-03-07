package PPT;

public class KeyString {
    public String  str;
    public boolean bool;
    public double  score;
    // 关键词 分数 关键词未读默认false
    public KeyString(String str, double score){
        this.str  = str;
        this.score= score;
        this.bool = false;
    }
}
