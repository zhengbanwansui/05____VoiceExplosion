/*
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;

public class lalala {
//    直接抽取幻灯片的全部内容  不适合本项目
//    public static String readDoc1(InputStream is) throws IOException{
//        PowerPointExtractor extractor=new PowerPointExtractor(is);
//        return extractor.getText();
//    }
//    main ------>      --------------------------------------------------
//     String cont=readDoc1(fin);                                        |
//                //System.out.println(cont);                            |
//                //fin.close();                 <-------------------    |
//    --------------------------------------------------------------------
//    一张幻灯片一张幻灯片地读取 需要存储每张幻灯片的内容 供之后的匹配使用
//    public static void readDoc(InputStream is) throws IOException{
//        SlideShow ss=new SlideShow(new HSLFSlideShow(is));
//        Slide[] slides=ss.getSlides();
//        for(int i=0;i<slides.length;i++){
//            //读取一张幻灯片的标题
//            String title=slides[i].getTitle();
//            System.out.println("标题:"+title);
//            //读取一张幻灯片的内容(包括标题)
//            TextRun[] runs=slides[i].getTextRuns();
//            for(int j=0;j<runs.length;j++){
//                System.out.println(runs[j].getText());
//            }
//        }
//    }
//    main------------------------------->         ----------------|
//    File file = new File("C:\\\\Windows (F)\\\\旧版测试.ppt");
//        try{
//            FileInputStream fin;
//            fin=new FileInputStream(file);
//            readDoc(fin);
//            fin.close();
//        }catch(IOException e){
//            e.printStackTrace();
//        }----------------------------------------------------------|

    public static String getTextFromPPT( String filePath) throws IOException{
        //创建文件流读取ppt路径
        FileInputStream in = new FileInputStream(filePath);
        //
        PowerPointExtractor extractor = new PowerPointExtractor(in);
        String resultString = extractor.getText();
        extractor.close();
        return resultString;
    }

    public static String getTextFromPPTX( String filePath) throws IOException{
        String resultString = null;
        StringBuilder sb = new StringBuilder();
        FileInputStream in = new FileInputStream(filePath);
        try {
            XMLSlideShow xmlSlideShow = new XMLSlideShow(in);
            List<XSLFSlide> slides = xmlSlideShow.getSlides();
            for(XSLFSlide slide:slides){
                CTSlide rawSlide = slide.getXmlObject();
                CTGroupShape gs = rawSlide.getCSld().getSpTree();
                CTShape[] shapes = gs.getSpArray();
                    for(CTShape shape:shapes){
                        CTTextBody tb = shape.getTxBody();
                        if(null==tb){
                           continue;
                        }
                        CTTextParagraph[] paras = tb.getPArray();
                        for(CTTextParagraph textParagraph:paras){
                            CTRegularTextRun[] textRuns = textParagraph.getRArray();
                            for(CTRegularTextRun textRun:textRuns){
                                sb.append(textRun.getT());
                            }
                        }
                    }
            }
               resultString = sb.toString();
               xmlSlideShow.close();
            } catch (Exception e) {
                e.printStackTrace();
               }
            return resultString;
            }

    public static void main(String[] args){
        String filePath1 = "C:\\Windows (F)\\旧版测试.ppt";
        String filePath2 = "C:\\Windows (F)\\新版测试.pptx";
        try{
            String ans1 = getTextFromPPT(filePath1);
            String ans2 = getTextFromPPTX(filePath2);
            System.out.println(ans1);
            System.out.println(ans2);
        }catch(Exception e){
            System.out.println(e);
        }
    }
}



*/



























/*public static String getTextFromPPTX( String filePath) throws IOException{

        StringBuilder sb = new StringBuilder();
        FileInputStream in = new FileInputStream(filePath);
        try {
            XMLSlideShow xmlSlideShow = new XMLSlideShow(in);
            //创建pptXML对象
            List<XSLFSlide> slides = xmlSlideShow.getSlides();

            for(XSLFSlide slide:slides){
                CTSlide rawSlide = slide.getXmlObject();
                CTGroupShape gs = rawSlide.getCSld().getSpTree();
                CTShape[] shapes = gs.getSpArray();
                for(CTShape shape:shapes){
                    CTTextBody tb = shape.getTxBody();
                    if(null==tb){
                        continue;
                    }
                    CTTextParagraph[] paras = tb.getPArray();
                    for(CTTextParagraph textParagraph:paras){
                        CTRegularTextRun[] textRuns = textParagraph.getRArray();
                        for(CTRegularTextRun textRun:textRuns){
                            sb.append(textRun.getT());
                        }
                    }
                }
            }
            String resultString = sb.toString();
            xmlSlideShow.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }*/