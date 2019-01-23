package PPT;

        import java.lang.Boolean;

//bool为false代表没读完
//textStr代表存储的字符串
public class PPTString {
    public Boolean bool;
    public double value;
    public String textStr;
    public PPTString(String textStr){
        bool = false;
        this.textStr = textStr;
        this.value   = 0;
    }
}
