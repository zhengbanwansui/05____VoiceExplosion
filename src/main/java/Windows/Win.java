package Windows;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Win extends JFrame{

    public String filePath = "NULL";
    private JPanel jp1;
    private JLabel jl1,jl2,jl3,jl4;

    public Win()
    {
        try{
            Image icon_img = ImageIO.read(this.getClass().getResource("..\\icon.png"));
            this.setIconImage(icon_img);
        }catch(Exception e){
            e.getStackTrace();
        }
        initInside();
        setSize(400,300);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(400,300);
        setTitle("基于语音识别的PPT控制系统");
        drag();
    }

    private void initInside(){
        jp1 = new JPanel();
        jp1.setBackground(Color.CYAN);
        jl1 = new JLabel("将文件拖拽到下方框内即可");// 上
        jl2 = new JLabel(" ");// 下
        jl3 = new JLabel(" ");// 左
        jl4 = new JLabel(" ");// 右
        jl1.setFont(new Font("宋体",1,24));
        jl3.setSize(50,50);
        jl4.setSize(50,50);
        setLayout(new BorderLayout(130,50));
        this.add(jl1,BorderLayout.NORTH);
        this.add(jp1,BorderLayout.CENTER);
        this.add(jl2,BorderLayout.SOUTH);
        this.add(jl3,BorderLayout.WEST);
        this.add(jl4,BorderLayout.EAST);
    }

    private void drag(){
        new DropTarget(jp1,DnDConstants.ACTION_COPY_OR_MOVE,new DropTargetAdapter()
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
                                jl2.setText("读取PPT : " + temp);
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
