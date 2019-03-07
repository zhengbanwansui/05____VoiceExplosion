package regexp;

public class RegExp {
    public static void regularExpress(){
        String str = "1234567890asdqwe萨达是多少，大胃王大，厄齐尔群翁群无。";
        str = str.replaceAll("[^\\u4e00-\\u9fa5。，]","");
        System.out.println(str);
    }
}
