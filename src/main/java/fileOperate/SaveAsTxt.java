package fileOperate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SaveAsTxt
{
/*
File fp=new File("c:\\a.txt");
String inputStr="ABCDE";
PrintWriter pfp= new PrintWriter(fp);
pfp.print(inputStr);
pfp.close();
*/
    public static void saveAsTxt(String inputStr) throws FileNotFoundException {
        File file = new File(new FilePath().filePath("演讲记录.txt"));
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.print(inputStr);
        printWriter.close();
    }
}
