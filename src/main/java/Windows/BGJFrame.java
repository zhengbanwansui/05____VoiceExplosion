package Windows;

import javax.swing.*;
import java.awt.*;

public class BGJFrame extends JFrame {
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g; //强转成2D
        ImageIcon ii1 = new ImageIcon(new FilePath().filePath("menu.png"));
        int width = 651;
        int height = 495;
        g2.drawImage(ii1.getImage(), 0, 0, width , height,null);
    }
}
