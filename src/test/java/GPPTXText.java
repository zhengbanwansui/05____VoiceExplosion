import org.apache.poi.sl.usermodel.PlaceableShape;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFConnectorShape;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import ppt.PPTTextSave;

import java.io.FileInputStream;

public class GPPTXText {
    public static void main(String[] args) {
        PPTTextSave PS = new PPTTextSave();
        try{
            XMLSlideShow ppt = new XMLSlideShow(new FileInputStream("C:\\Users\\14419\\Desktop\\长宽度测试.pptx"));
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

                        System.out.println(" X:" + (int)anchor.getX() + " Y:" + (int)anchor.getY() +
                                " height:" + (int)anchor.getHeight());
                        System.out.println(" X:" + (int)anchor.getX() + " Y:" + (int)anchor.getY() +
                                " height:" + (int)((XSLFTextShape) sh).getTextHeight());
                        System.out.println(" ");
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
    }
}
