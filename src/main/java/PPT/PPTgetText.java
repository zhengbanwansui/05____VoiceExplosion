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
    //PPT getText
    public PPTTextSave GPPT(String filePath){
        System.out.println("采用了PPTgetText方法");
        //创建PPTTextSave对象
        PPTTextSave PS = new PPTTextSave();
        try{
            //创建ppt对象
            FileInputStream in = new FileInputStream(filePath);
            HSLFSlideShow pptObject = new HSLFSlideShow(in);
            //遍历幻灯片
            int i = 0;
            for (HSLFSlide slide : pptObject.getSlides()) {
                i++;
                PS.addPage();
                //遍历一张幻灯片内的所有段落文字 slide.getTextParagraphs()
                for(List<HSLFTextParagraph> ParaList : slide.getTextParagraphs()){
                    PS.add(i,ParaList.get(0).toString());
                }
                PS.show(i);
            }
            in.close();
        }catch(Exception e){
            System.out.println("zjx异常：" + e);
        }
        return PS;
    }

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
                System.out.println("NumOfPage : " + ++page_i);
                for (HSLFShape sh : slide.getShapes()) {
                    if (sh instanceof HSLFTextBox) {
                        String temp_str = ((HSLFTextBox) sh).getText();
                        java.awt.geom.Rectangle2D anchor = sh.getAnchor();
                        //
                        //System.out.println("Text:" + temp_str + " X:" + anchor.getX() + " Y:" + anchor.getY());
                        if(temp_str.length() != 0) {
                            PS.add(page_i, temp_str, (int) anchor.getX(), (int) anchor.getY(), (int) anchor.getWidth(), (int) anchor.getHeight());
                        }
                        //
                    } else if (sh instanceof Line) {
                        // work with Line
                    } else if (sh instanceof HSLFAutoShape) {
                        // work with AutoShape
                    } else if (sh instanceof HSLFPictureShape) {
                        // work with Picture
                    }
                }
            }
        }catch(Exception e){
            System.out.println("zjx：NewGetText异常" + e);
        }
        return PS;
    }

    //PPTX getText
    public PPTTextSave GPPTX(String filePath){
        System.out.println("采用了PPTXgetText方法");
        //创建PPTTextSave对象
        PPTTextSave PS = new PPTTextSave();
        try{
            FileInputStream is = new FileInputStream(filePath);
            //创建pptXML对象
            XMLSlideShow pptxObject = new XMLSlideShow(is);
            //得到每个Slide幻灯片页
            List<XSLFSlide> slides = pptxObject.getSlides();
            //遍历每张幻灯片
            int whichSlide = 0;
            for(XSLFSlide slide : slides){
                System.out.println("PageNum:"+  ++whichSlide);
                PS.addPage();
                CTSlide rawSlide = slide.getXmlObject();
                CTGroupShape gs = rawSlide.getCSld().getSpTree();
                CTShape[] shapes = gs.getSpArray();
                //得到了shapes遍历shape
                for(CTShape shape:shapes){
                    CTTextBody tb = shape.getTxBody();
                    if(null==tb){
                        continue;
                    }
                    CTTextParagraph[] paras = tb.getPArray();
                    for(CTTextParagraph textParagraph:paras){
                        CTRegularTextRun[] textRuns = textParagraph.getRArray();
                        StringBuffer sb = new StringBuffer();
                        for(CTRegularTextRun textRun:textRuns){
                            sb.append(textRun.getT());
                        }
                        if(sb.toString().length() != 0)
                            PS.add(whichSlide,sb.toString());
                        sb.delete(0,sb.length());
                    }
                }
            }
            is.close();
        }catch(Exception e){
            System.out.println("zjx异常：" + e);
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
                System.out.println("NumOfPage : " + ++page_i);
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
