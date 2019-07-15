package ppt.test;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFNotes;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;

import java.io.FileInputStream;
import java.util.List;

public class getNote {

    public static void main(String[] args) {
        try{
            XMLSlideShow ppt = new XMLSlideShow(new FileInputStream("C:\\Users\\14419\\Desktop\\演讲助手小音同学答辩PPT.pptx"));
            // 遍历每一张幻灯片
            int i = 0;
            for (XSLFSlide slide : ppt.getSlides()) {
                i++;
                XSLFNotes xslfNotes = slide.getNotes();
                System.out.println(i + "页注释" + xslfNotes);
                List<List<XSLFTextParagraph>> parass = xslfNotes.getTextParagraphs();
                for (List<XSLFTextParagraph> paras : parass) {
                    for (XSLFTextParagraph para : paras) {
                        System.out.println(para.getText());
                    }
                }
            }
        }catch(Exception e){
            e.getStackTrace();
        }
    }

}
