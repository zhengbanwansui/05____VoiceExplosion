package Windows;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;

public class Win extends JFrame implements ActionListener {

    public String filePath = "NULL";
    private BGJPanel pptTargetPlace;
    private JButton small,out;

    public Win() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100,100,1600,900);
        try{
            Image icon_img = ImageIO.read(this.getClass().getResource("..\\icon.png"));
            this.setIconImage(icon_img);
        }catch(Exception e){
            e.getStackTrace();
        }
        initInside();
        drag();
        setVisible(true);
    }

    private void initInside() {
        setLayout(null);
        BGJPanel rootPanel = new BGJPanel("menu.png");
        rootPanel.setBounds(0,0,1600,900);
        rootPanel.setLayout(null);
        rootPanel.setBackground(Color.BLUE);
        small = new JButton();
        small.setBounds(1600-72,6,30,30);
        small.addActionListener(this);
        small.setIcon(new ImageIcon(new FilePath().filePath("small.png")));
        small.setPressedIcon(new ImageIcon(new FilePath().filePath("smallPressed.png")));
        out = new JButton();
        out.setBounds(1600-37, 6,30,30);
        out.addActionListener(this);
        out.setIcon(new ImageIcon(new FilePath().filePath("out.png")));
        out.setPressedIcon(new ImageIcon(new FilePath().filePath("outPressed.png")));
        MVJPanel dragArea = new MVJPanel();
        dragArea.setDragable(this);
        dragArea.setBounds(0,0,1600,100);
        dragArea.setLayout(null);
        dragArea.setOpaque(false);
        pptTargetPlace = new BGJPanel("circle.png");
        pptTargetPlace.setBounds(0, 400, 160, 160);
        pptTargetPlace.setOpaque(false);
        getContentPane().add(rootPanel);
        rootPanel.add(pptTargetPlace);
        rootPanel.add(dragArea);
        dragArea.add(small);
        dragArea.add(out);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == small){
            this.setExtendedState(ICONIFIED);
        }
        if(e.getSource() == out  ){
            System.exit(0);
        }
    }

    public void changeTipImage(String changeToImageFileName) {

        pptTargetPlace.changeImage(changeToImageFileName);
        this.getRootPane().updateUI();

    }

    private void drag() {
        new DropTarget(pptTargetPlace,DnDConstants.ACTION_COPY_OR_MOVE,new DropTargetAdapter()
        {
            public void drop(DropTargetDropEvent dtde)
            {
                try{

                    if(dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
                    {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        List<File>list=(List<File>)(dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));

                        String temp="";
                        for(File file:list)
                        {
                            temp+=file.getAbsolutePath();
                            if(temp.endsWith(".ppt") || temp.endsWith(".pptx")){
                                JOptionPane.showMessageDialog(null, "读取文件完成");
                                System.out.println("读取PPT : " + temp + " 中... ... ");
                                filePath = temp;
                            }
                            dtde.dropComplete(true);
                        }
                    }
                    else
                    {
                        dtde.rejectDrop();
                    }
                }catch(Exception e){e.printStackTrace();}
            }
        });
    }

}
