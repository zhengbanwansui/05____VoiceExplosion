package PPT.test;

import PPT.KeyString;
import PPT.PPTString;

public class PPTStringTest {
    public static void main(String[] args) {
        PPTString str = new PPTString("社会主义万岁,共产党天下第一,然而日子还得过下去,所以我要保持清醒",12,34,45,56);
        str.cutStrAndArr();
        System.out.println(str.cutedKeyWords);
        for(KeyString k : str.cutedKeyWords){
            System.out.println(k.bool + k.str + k.score);
        }
    }
}
