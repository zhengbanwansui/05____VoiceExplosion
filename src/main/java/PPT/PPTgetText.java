package PPT;

import java.io.FileInputStream;
import java.util.List;
import java.lang.String;

import org.apache.poi.hslf.usermodel.*;
import org.apache.poi.sl.usermodel.Line;
import org.apache.poi.sl.usermodel.PlaceableShape;
import org.apache.poi.xslf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;

public class PPTgetText {

    //PPT testing ^_^
    public PPTTextSave GPPT(String filePath,int temp_no_use_int){
        System.out.println("采用了PPTgetText方法");
        //创建PPTTextSave对象
        PPTTextSave PS = new PPTTextSave();
        try{
            HSLFSlideShow ppt = new HSLFSlideShow(new HSLFSlideShowImpl(filePath));
            // get slides
            int page_i = 0;
            // 遍历每一张幻灯片
            for (HSLFSlide slide : ppt.getSlides()) {
                PS.addPage();
                page_i++;
                for (HSLFShape sh : slide.getShapes()) {
                    // 后添加的文本框能识别
                    if (sh instanceof HSLFTextBox) {
                        String temp_str = ((HSLFTextBox) sh).getText();
                        java.awt.geom.Rectangle2D anchor = sh.getAnchor();
                        System.out.println("work with TextBox x 1");
                        if(temp_str.length() != 0) {
                            PS.add(page_i, temp_str, (int) anchor.getX(), (int) anchor.getY(), (int) anchor.getWidth(), (int) anchor.getHeight());
                        }
                    } else if (sh instanceof Line) {
                        // work with Line
                        System.out.println("work with Line x 1");
                    } else if (sh instanceof HSLFAutoShape) {
                        // work with AutoShape
                        System.out.println("work with AutoShape x 1");
                        String temp_str = ((HSLFAutoShape) sh).getText();
                        java.awt.geom.Rectangle2D anchor = sh.getAnchor();
                        if(temp_str.length() != 0) {
                            PS.add(page_i, temp_str, (int) anchor.getX(), (int) anchor.getY(), (int) anchor.getWidth(), (int) anchor.getHeight());
                        }
                    } else if (sh instanceof HSLFPictureShape) {
                        // work with Picture
                        System.out.println("work with Picture x 1");
                    }
                }
            }
        }catch(Exception e){
            System.out.println("zjx：NewGetText异常" + e);
        }
        return PS;
    }

    //PPTX testing ^_^
    public PPTTextSave GPPTX(String filePath,int temp_no_use_int){
        PPTTextSave PS = new PPTTextSave();
        try{
            XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(filePath));
            // get slides
            int page_i = 0;
            // 遍历每一张幻灯片
            for (XSLFSlide slide : ppt.getSlides()) {
                PS.addPage();
                page_i++;
                for (XSLFShape sh : slide.getShapes()) {
                    if (sh instanceof XSLFTextShape) {
                        java.awt.geom.Rectangle2D anchor = ((PlaceableShape)sh).getAnchor();
                        String temp_str = ((XSLFTextShape) sh).getText();
                        //System.out.println("Text:" + temp_str + " X:" + anchor.getX() + " Y:" + anchor.getY());
                        if(temp_str.length() != 0){
                            PS.add(page_i,temp_str,(int)anchor.getX(),(int)anchor.getY(),(int)anchor.getWidth(),(int)anchor.getHeight());
                        }
                    } else if (sh instanceof XSLFConnectorShape) {
                        // 获取到一个ConnectorShape
                    } else if (sh instanceof XSLFPictureShape) {
                        // 获取到一个PictureShape
                    }
                }
            }
        }catch(Exception e){ System.out.println("zjx：NewGetText异常" + e); }
        return PS;
    }

    //类型判断给出对应函数
    public PPTTextSave getPPTandPPTX(String FilePath){
        PPTTextSave PS = new PPTTextSave();
        if(FilePath.charAt(FilePath.length()-1) == 't' || FilePath.charAt(FilePath.length()-1) == 'T'){
            PS = GPPT(FilePath,312445342);
        }
        else if(FilePath.charAt(FilePath.length()-1) == 'x' || FilePath.charAt(FilePath.length()-1) == 'X'){
            PS = GPPTX(FilePath,543452432);
        }
        return PS;
    }
}
