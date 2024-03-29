package ppt;

import java.io.FileInputStream;
import java.lang.String;
import java.util.ArrayList;

import org.apache.poi.hslf.usermodel.HSLFAutoShape;
import org.apache.poi.hslf.usermodel.HSLFNotes;
import org.apache.poi.hslf.usermodel.HSLFPictureShape;
import org.apache.poi.hslf.usermodel.HSLFShape;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFSlideShowImpl;
import org.apache.poi.hslf.usermodel.HSLFTextBox;
import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFConnectorShape;
import org.apache.poi.xslf.usermodel.XSLFNotes;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.sl.usermodel.Line;
import org.apache.poi.sl.usermodel.PlaceableShape;

public class PPTgetText {

    GetPowerPoint gppt;

    public void setGetPowerPoint(GetPowerPoint gppt) {
        this.gppt = gppt;
    }

    //ppt testing ^_^
//    public PPTTextSave GPPT(String filePath) {
//        System.out.println("采用了PPTgetText方法");
//        //创建PPTTextSave对象
//        PPTTextSave PS = new PPTTextSave();
//        try{
//            HSLFSlideShow ppt = new HSLFSlideShow(new HSLFSlideShowImpl(filePath));
//            // get slides
//            int page_i = 0;
//            // 遍历每一张幻灯片
//            for (HSLFSlide slide : ppt.getSlides()) {
//                PS.addPage();
//                page_i++;
//                // 提取注释
//                HSLFNotes note = slide.getNotes();
//                ArrayList<String> newNote = new ArrayList<>();
//                System.out.println("note.getpara:   " + note.getTextParagraphs());
//                for (HSLFTextParagraph para : note.getTextParagraphs().get(0)) {
//                    newNote.add(para.toString().replaceAll("[\n]", ""));
//                }
//                PS.notes.add(newNote);
//                // 提取文本
//                for (HSLFShape sh : slide.getShapes()) {
//                    // 后添加的文本框能识别
//                    if (sh instanceof HSLFTextBox) {
//                        String temp_str = ((HSLFTextBox) sh).getText();
//                        java.awt.geom.Rectangle2D anchor = sh.getAnchor();
//                        System.out.println("work with TextBox x 1");
//                        if(temp_str.length() != 0) {
//                            PS.add(page_i, temp_str, (int) anchor.getX(), (int) anchor.getY(), (int) anchor.getWidth(), (int) anchor.getHeight());
//                        }
//                    } else if (sh instanceof Line) {
//                        // work with Line
//                        System.out.println("work with Line x 1");
//                    } else if (sh instanceof HSLFAutoShape) {
//                        // work with AutoShape
//                        System.out.println("work with AutoShape x 1");
//                        String temp_str = ((HSLFAutoShape) sh).getText();
//                        java.awt.geom.Rectangle2D anchor = sh.getAnchor();
//                        if(temp_str.length() != 0) {
//                            PS.add(page_i, temp_str, (int) anchor.getX(), (int) anchor.getY(), (int) anchor.getWidth(), (int) anchor.getHeight());
//                        }
//                    } else if (sh instanceof HSLFPictureShape) {
//                        // work with Picture
//                        System.out.println("work with Picture x 1");
//                    }
//                }
//            }
//        } catch(Exception e) {
//            System.out.println("zjx：GPPT异常" + e);
//        }
//        return PS;
//    }

    //PPTX testing ^_^
//    public PPTTextSave GPPTX(String filePath) {
//        PPTTextSave PS = new PPTTextSave();
//        try{
//            XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(filePath));
//            // get slides
//            int page_i = 0;
//            // 遍历每一张幻灯片
//            for (XSLFSlide slide : ppt.getSlides()) {
//                PS.addPage();
//                page_i++;
//                // 提取注释
//                XSLFNotes note = slide.getNotes();
//                ArrayList<String> newNote = new ArrayList<>();
//                if (note != null) {
//                    for (XSLFTextParagraph para : note.getTextParagraphs().get(1)) {
//                        newNote.add(para.getText());
//                    }
//                }
//                PS.notes.add(newNote);
//                // 提取文本
//                for (XSLFShape sh : slide.getShapes()) {
//                    if (sh instanceof XSLFTextShape) {
//                        java.awt.geom.Rectangle2D anchor = ((PlaceableShape)sh).getAnchor();
//                        String temp_str = ((XSLFTextShape) sh).getText();
//                        //System.out.println("Text:" + temp_str + " X:" + anchor.getX() + " Y:" + anchor.getY());
//                        if(temp_str.length() != 0){
//                            PS.add(page_i,temp_str,(int)anchor.getX(),(int)anchor.getY(),(int)anchor.getWidth(),(int)anchor.getHeight());
//                        }
//                    } else if (sh instanceof XSLFConnectorShape) {
//                        // 获取到一个ConnectorShape
//                    } else if (sh instanceof XSLFPictureShape) {
//                        // 获取到一个PictureShape
//                    }
//                }
//            }
//        } catch(Exception e) { System.out.println("zjx：GPPTX异常" + e); }
//        return PS;
//    }

    //类型判断给出对应函数
    public PPTTextSave getPPTandPPTX(String FilePath) {
        PPTTextSave PS = new PPTTextSave();
        if(FilePath.charAt(FilePath.length()-1) == 't' || FilePath.charAt(FilePath.length()-1) == 'T'){
            setGetPowerPoint(new GetPowerPointPPT());
            PS = gppt.getPowerPoint(FilePath);
        }
        else if(FilePath.charAt(FilePath.length()-1) == 'x' || FilePath.charAt(FilePath.length()-1) == 'X'){
            setGetPowerPoint(new GetPowerPointPPTX());
            PS = gppt.getPowerPoint(FilePath);
        }
        return PS;
    }
}
