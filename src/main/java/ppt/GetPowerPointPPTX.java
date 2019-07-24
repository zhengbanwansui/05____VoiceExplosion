package ppt;

import org.apache.poi.sl.usermodel.PlaceableShape;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFConnectorShape;
import org.apache.poi.xslf.usermodel.XSLFNotes;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import java.io.FileInputStream;
import java.util.ArrayList;

public class GetPowerPointPPTX implements GetPowerPoint {
    @Override
    public PPTTextSave getPowerPoint(String filePath) {
            PPTTextSave PS = new PPTTextSave();
            try{
                XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(filePath));
                // get slides
                int page_i = 0;
                // 遍历每一张幻灯片
                for (XSLFSlide slide : ppt.getSlides()) {
                    PS.addPage();
                    page_i++;
                    // 提取注释
                    XSLFNotes note = slide.getNotes();
                    ArrayList<String> newNote = new ArrayList<>();
                    if (note != null) {
                        for (XSLFTextParagraph para : note.getTextParagraphs().get(1)) {
                            newNote.add(para.getText());
                        }
                    }
                    PS.notes.add(newNote);
                    // 提取文本
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
            } catch(Exception e) { System.out.println("zjx：GPPTX异常" + e); }
            return PS;
        }
}
