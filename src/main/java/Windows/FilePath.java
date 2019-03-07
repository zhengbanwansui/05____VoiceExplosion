package Windows;

public class FilePath {

    String s1;

    public FilePath(){
        // 获取jar包目录 / class文件目录
        s1 = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
    }
    public String filePath(String path) {
        //this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        // 转码
        try{s1 = java.net.URLDecoder.decode(s1,"utf-8"); }catch(Exception e){ e.getStackTrace(); }
        // 去文件名末尾
        while(s1.charAt(s1.length()-1) != '/') {
            s1 = s1.substring(0, s1.length()-1);
        }
        System.out.println("读取此文件 : " + s1 + path);
        // 加文件名传回
        return s1 + path;
    }
}
