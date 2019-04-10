package Windows;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BGJPanel extends JPanel {

    private ImageIcon icon;
    private Image img;

    public BGJPanel(String backgroundFileName) {
        icon = new ImageIcon(new FilePath().filePath(backgroundFileName));
        img=icon.getImage();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //下面这行是为了背景图片可以跟随窗口自行调整大小，可以自己设置成固定大小
        g.drawImage(img, 0, 0,this.getWidth(), this.getHeight(), this);
    }

    public void changeImage(String fileString) {
        ImageIcon icon1;
        Image img1;
        icon1 = new ImageIcon(new FilePath().filePath(fileString));
        img1=icon1.getImage();
        img = img1;
    }

}
