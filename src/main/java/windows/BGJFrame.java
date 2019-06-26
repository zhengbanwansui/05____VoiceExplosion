package windows;

import fileOperate.FilePath;

import javax.swing.*;
import java.awt.*;

/**
 * 带背景的JFrame
 */
public class BGJFrame extends JFrame {

    private String filePath;

    public BGJFrame(String filePath) {
        this.filePath = filePath;
    }

    public void changeImage(String newFilePath) {
        filePath = newFilePath;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g; //强转成2D
        ImageIcon ii1 = new ImageIcon(new FilePath().filePath(filePath));
        g2.drawImage(ii1.getImage(), 0, 0, this.getWidth() , this.getHeight(),null);
    }
}
