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
    private BGJPanel rootPanel;
    private JPanel jp1;
    private MVJPanel jpTop;
    private JTextArea ja1;
    private JButton small,out;

    public Win()
    {
        // 去掉上面的窗口栏
        this.setUndecorated(true);
        try{
            Image icon_img = ImageIO.read(this.getClass().getResource("..\\icon.png"));
            this.setIconImage(icon_img);
        }catch(Exception e){
            e.getStackTrace();
        }
        initInside();
        setSize(1000,600);
        //this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(400,200);
        setTitle("基于语音识别的PPT控制系统");
        drag();
        this.setVisible(true);

    }

    private void initInside(){
        // 背景 插入this中作为根容器 绝对布局
        rootPanel = new BGJPanel();
        this.add(rootPanel);
        rootPanel.setLayout(null);
        // 透明顶栏需要实现自定义的拖拽移动最小化和关闭功能
        jpTop = new MVJPanel();
        jpTop.setDragable(this);
        jpTop.setBounds(0,0,1000,100);
        jpTop.setOpaque(false);
        jpTop.setLayout(null);
        // 左下透明栏drag函数触发拖拽功能
        jp1 = new JPanel();
        jp1.setOpaque(false);
        jp1.setBounds(0, 100, 300, 500);
        // 右下log文本栏
        ja1 = new JTextArea();
        ja1.setLineWrap(true);
        ja1.setText(">>> 此处输出Log信息");
        // 滚动条容器
        JScrollPane ja1roll = new JScrollPane(ja1);
        ja1roll.setBounds(350, 150, 600, 400);
        small = new JButton(new ImageIcon(new FilePath().filePath("small.png")));
        out   = new JButton(new ImageIcon(new FilePath().filePath("out.png")));
        small.setBounds(955,2,20,20);
        out  .setBounds(978,2,20,20);
        small.addActionListener(this);
        out  .addActionListener(this);
        jpTop.add(small);
        jpTop.add(out);
        rootPanel.add(jp1);
        rootPanel.add(ja1roll);
        rootPanel.add(jpTop);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == small){
            this.setExtendedState(ICONIFIED);
        }
        if(e.getSource() == out  ){
            System.exit(0);
        }
    }

    // jp1是拖拽目标区域
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
                                ja1.setText(">>> 读取PPT : " + temp + " 中... ... \n请稍后 ... ...");
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

    public void Log(String str){

        ja1.setText(str + "\n\n" + ja1.getText());

    }
}