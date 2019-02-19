package Windows;

import javax.swing.*;
import java.awt.*;

public class MVJPanel extends JPanel {
    private Point loc = null;
    private Point tmp = null;
    private boolean isDragged = false;
    public void setDragable(final Win jFrame) {
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent e) {
                isDragged = false;
                jFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            public void mousePressed(java.awt.event.MouseEvent e) {
                tmp = new Point(e.getX(), e.getY());
                isDragged = true;
                jFrame.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }
        });
        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent e) {
                if(isDragged) {
                    loc = new Point(jFrame.getLocation().x + e.getX() - tmp.x, jFrame.getLocation().y + e.getY() - tmp.y);
                    jFrame.setLocation(loc);
                }
            }
        });
    }
}
