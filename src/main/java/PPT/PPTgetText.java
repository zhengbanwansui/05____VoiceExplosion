package PPT;

import java.io.FileInputStream;
import java.util.List;
import java.lang.String;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;

public class PPTgetText {
    //PPT getText
    public PPTTextSave GPPT(String filePath){
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
    //PPTX getText
    public PPTTextSave GPPTX(String filePath){
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
    //类型判断给出对应函数
    public PPTTextSave getPPTandPPTX(String FilePath){
        PPTTextSave PS = new PPTTextSave();
        if(FilePath.charAt(FilePath.length()-1) == 't' || FilePath.charAt(FilePath.length()-1) == 'T'){
            PS = GPPT(FilePath);
        }
        else if(FilePath.charAt(FilePath.length()-1) == 'x' || FilePath.charAt(FilePath.length()-1) == 'X'){
            PS = GPPTX(FilePath);
        }
        return PS;
    }
}
