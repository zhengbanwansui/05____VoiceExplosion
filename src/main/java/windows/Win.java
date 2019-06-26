package windows;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * 图形用户界面类: 窗口采用JFrame作为底层容器, 同时监听窗口内所有事件
 * 结构如下注释:
 */
public class Win extends JFrame implements ActionListener {
    // 改变拖拽区图片flag
    public String filePath = "NULL";
    // 顶栏
    private JButton small,out;
    // 语音识别文本
    private JTextPane voiceText;                // 文字1
    private StyledDocument voiceTextDoc;        // 文字2
    private SimpleAttributeSet voiceTextStyle;  // 文字3
    private BGJPanel loading;
    private BGJPanel loadFinish;
    // 语音模型下拉列表
    public JComboBox pullDownList;
    // 拖拽区域
    private JPanel pptTargetPlace;
    // 配准算法开关
    private JButton[] compareBtn = new JButton[4];
    public int[] compareInt = {1,1,1,1};
    // 页数配准率
    private JLabel pageJLabel;
    private JLabel compareJLabel;
    // 定制翻页口令
    private JTextPane awakeText;                // 文字1
    private StyledDocument awakeTextDoc;        // 文字2
    private SimpleAttributeSet awakeTextStyle;  // 文字3
    private JRadioButton addOption,delOption,nextOption,lastOption;// 单选按钮
    private ArrayList<String> orderNextString;// 翻页文本
    private ArrayList<String> orderLastString;// 退回文本
    private JTextField awakeSentenceTextField;
    private JButton commitAwakeBtn;

    public ArrayList<String> getOrderNextString() {
        return orderNextString;
    }

    public ArrayList<String> getOrderLastString() {
        return orderLastString;
    }

    /**
     * 构造函数, 初始化整个窗口
     */
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
        initOrderNextAndLastOfAwake();
        setVisible(true);
        changeAwakeRecord();
    }

    private void initInside() {
        setLayout(null);
        BGJPanel rootPanel = new BGJPanel("menu.png");
        rootPanel.setBounds(0,0,1600,900);
        rootPanel.setLayout(null);
        rootPanel.setBackground(Color.BLUE);
        // 顶栏
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
        pptTargetPlace = new JPanel();
        pptTargetPlace.setBounds(150,400,187,115);
        pptTargetPlace.setOpaque(false);
        // A 语音识别记录
        voiceText = new JTextPane();
        voiceText.setText("");
        voiceText.setBounds(0,0,353,140);
        voiceText.setEditable(false);
        voiceText.setBackground(new Color(0,60,88));
        voiceText.setBorder(BorderFactory.createLineBorder(new Color(0,246,255), 0));
        voiceTextDoc = voiceText.getStyledDocument();
        voiceTextStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(voiceTextStyle,new Color(0,246,255));
        StyleConstants.setFontSize(voiceTextStyle,18);
        StyleConstants.setFontFamily(voiceTextStyle,"黑体");
        voiceTextDoc.setCharacterAttributes(0,999999, voiceTextStyle,false);
        JScrollPane voiceTextScroll = new JScrollPane(voiceText);
        voiceTextScroll.setBounds(34,160,353,140);
        voiceTextScroll.setBorder(BorderFactory.createLineBorder(new Color(0,246,255), 2));
        // B 语音模型下拉列表
        pullDownList = new JComboBox();
        pullDownList.addItem("通用普通话");
        pullDownList.addItem("演讲领域");
        pullDownList.addItem("医疗领域");
        pullDownList.addItem("出行领域");
        pullDownList.addItem("政法庭审");
        pullDownList.addItem("金融领域");
        pullDownList.addItem("新零售领域");
        pullDownList.addItem("四川方言");
        pullDownList.addItem("湖北方言");
        pullDownList.addItem("粤语");
        pullDownList.addItem("其他方言");
        pullDownList.setForeground(new Color(0, 246, 255));
        pullDownList.setBackground(new Color(0,60,88));
        pullDownList.setBorder(BorderFactory.createLineBorder(new Color(0,246,255), 1));
        pullDownList.setFocusable(false);// 不可集中 所以没有集中的效果颜色了hhh
        pullDownList.setFont(new Font("黑体", Font.BOLD, 16));
        pullDownList.setBounds(18,400,120,30);
        pullDownList.setVisible(true);
        // B 拖拽区提示图
        loading = new BGJPanel("loading.png");
        loading.setBounds( 150,400,185,115);
        loading.setVisible(false);
        loadFinish = new BGJPanel("loadFinish.png");
        loadFinish.setBounds( 150,400,185,115);
        loadFinish.setVisible(false);
        // C 配准算法开关
        compareBtn[0] = new JButton(new ImageIcon(new FilePath().filePath("compBtn/1n.png")));
        compareBtn[0].setBounds(60, 656, 130, 42);
        compareBtn[0].setBorder(null);
        compareBtn[0].addActionListener(this);
        compareBtn[1] = new JButton(new ImageIcon(new FilePath().filePath("compBtn/2n.png")));
        compareBtn[1].setBounds(234, 656, 130, 42);
        compareBtn[1].setBorder(null);
        compareBtn[1].addActionListener(this);
        compareBtn[2] = new JButton(new ImageIcon(new FilePath().filePath("compBtn/3n.png")));
        compareBtn[2].setBounds(60, 735, 130, 42);
        compareBtn[2].setBorder(null);
        compareBtn[2].addActionListener(this);
        compareBtn[3] = new JButton(new ImageIcon(new FilePath().filePath("compBtn/4n.png")));
        compareBtn[3].setBounds(234, 735 , 130, 42);
        compareBtn[3].setBorder(null);
        compareBtn[3].addActionListener(this);
        // a 页数配准率
        pageJLabel = new JLabel("* * *");
        pageJLabel.setBounds(1367,150-35,200,100);
        pageJLabel.setFont(new Font("黑体",Font.PLAIN,40));
        pageJLabel.setForeground(new Color(255,84,157));
        compareJLabel = new JLabel("* * %");
        compareJLabel.setBounds(1367,212-35,200,100);
        compareJLabel.setFont(new Font("黑体",Font.PLAIN,40));
        compareJLabel.setForeground(new Color(255,84,157));
        changePageAndCom(0,1,0);
        // b 翻页指令设置
        JPanel awakeSentenceEditPanel = new JPanel();
        awakeSentenceEditPanel.setBounds(1200,365,380,255);
        awakeSentenceEditPanel.setOpaque(false);
        awakeSentenceEditPanel.setLayout(null);
        awakeText = new JTextPane();
        awakeText.setText(" ");
        awakeText.setBounds(0,0,353,100);
        awakeText.setEditable(false);
        awakeText.setBackground(new Color(0,60,88));
        awakeText.setBorder(BorderFactory.createLineBorder(new Color(0,246,255), 0));
        awakeTextDoc = awakeText.getStyledDocument();
        awakeTextStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(awakeTextStyle,new Color(1,246,255));
        StyleConstants.setFontSize(awakeTextStyle,16);
        StyleConstants.setFontFamily(awakeTextStyle,"黑体");
        awakeTextDoc.setCharacterAttributes(0,999998, awakeTextStyle,false);
        JScrollPane awakeTextScroll = new JScrollPane(awakeText);
        awakeTextScroll.setBounds(8,10,360,120);
        awakeTextScroll.setBorder(BorderFactory.createLineBorder(new Color(0,246,255), 2));

        addOption = new JRadioButton("添加");
        addOption.setBounds(8,150,60,30);
        addOption.setSelected(true);
        addOption.setBackground(new Color(0,246,255));
        delOption = new JRadioButton("删除");
        delOption.setBounds(70,150,60,30);
        delOption.setBackground(new Color(0,246,255));
        ButtonGroup optionBtnAddDel = new ButtonGroup();
        optionBtnAddDel.add(addOption);
        optionBtnAddDel.add(delOption);
        nextOption = new JRadioButton("翻页");
        nextOption.setBounds(8,200,60,30);
        nextOption.setSelected(true);
        nextOption.setBackground(new Color(0,246,255));
        lastOption = new JRadioButton("回页");
        lastOption.setBounds(70,200,60,30);
        lastOption.setBackground(new Color(0,246,255));
        ButtonGroup optionBtnNextLast = new ButtonGroup();
        optionBtnNextLast.add(nextOption);
        optionBtnNextLast.add(lastOption);

        awakeSentenceTextField = new JTextField();
        awakeSentenceTextField.setBackground(new Color(0,48,76));
        awakeSentenceTextField.setBounds(140,200,160,30);
        awakeSentenceTextField.setForeground(new Color(255, 255, 255));
        commitAwakeBtn = new JButton(new ImageIcon(new FilePath().filePath("commitAwakeBtn.png")));
        commitAwakeBtn.setBounds(310,200, 60,30);
        commitAwakeBtn.setBorder(null);
        commitAwakeBtn.setPressedIcon(new ImageIcon(new FilePath().filePath("commitAwakeBtnPressed.png")));
        commitAwakeBtn.addActionListener(this);

        // 添加组件
        getContentPane().add(rootPanel);
        rootPanel.add(pullDownList);
        rootPanel.add(pptTargetPlace);
        rootPanel.add(dragArea);
        rootPanel.add(voiceTextScroll);
        rootPanel.add(pageJLabel);
        rootPanel.add(compareJLabel);
        rootPanel.add(loading);
        rootPanel.add(loadFinish);
        rootPanel.add(awakeSentenceEditPanel);
        awakeSentenceEditPanel.add(awakeTextScroll);
        awakeSentenceEditPanel.add(addOption);
        awakeSentenceEditPanel.add(delOption);
        awakeSentenceEditPanel.add(nextOption);
        awakeSentenceEditPanel.add(lastOption);
        awakeSentenceEditPanel.add(awakeSentenceTextField);
        awakeSentenceEditPanel.add(commitAwakeBtn);
        for(int i = 0; i < 4; i++) {
            rootPanel.add(compareBtn[i]);
        }
        dragArea.add(small);
        dragArea.add(out);
    }

    /**
     * 添加语音识别文本
     * @param str 添加的文本
     */
    public void appendVoiceRecognizeRecord(String str) {
        if(str.length() != 0) {
            voiceText.setText(voiceText.getText()+"\n"+str);
            voiceTextDoc.setCharacterAttributes(0,999999, voiceTextStyle,false);
            voiceText.setCaretPosition(voiceText.getText().length());
        }
        this.getRootPane().updateUI();
    }

    /**
     * 更改唤醒词语文本
     */
    public void changeAwakeRecord() {
        StringBuffer sb = new StringBuffer("【翻页命令语】");
        int i = 0;
        for (String next : orderNextString) {
            sb.append("\n");
            sb.append(++i + "、");
            sb.append(next);
        }
        i = 0;
        sb.append("\n【回页命令语】");
        for (String last : orderLastString) {
            sb.append("\n");
            sb.append(++i + "、");
            sb.append(last);
        }
        awakeText.setText(sb.toString());
        awakeText.setCaretPosition(0);
        awakeTextDoc.setCharacterAttributes(0,999998, awakeTextStyle,false);
        this.getRootPane().updateUI();
    }

    /**
     * 初始化指令唤醒算法的下一页的指令词和上一页的指令词
     */
    public void initOrderNextAndLastOfAwake() {
        orderNextString = new ArrayList<>();
        orderLastString = new ArrayList<>();

        orderNextString.add("下一页");
        orderNextString.add("下页");
        orderNextString.add("翻页");
        orderNextString.add("翻页儿");
        orderNextString.add("翻篇");
        orderNextString.add("翻篇儿");
        orderNextString.add("翻到下一页");

        orderLastString.add("上一页");
        orderLastString.add("上页");
        orderLastString.add("返回上一页");
        orderLastString.add("翻回上一页");
        orderLastString.add("翻到上一页");

    }

    /**
     * 更改目前页数和配准进度的数值
     * @param page 哪一页
     * @param sumCompare 总文本数
     * @param finishCompare 已配准文本数
     */
    public void changePageAndCom(int page, int sumCompare, int finishCompare) {
        if(page < 10) {
            pageJLabel.setText("0" + " " + "0" + " " + page);
        }else if(page < 100){
            pageJLabel.setText("0" + " " + page / 10 + " " + page % 10);
        }else{
            pageJLabel.setText((page / 100) + " " + page % 100 / 10 + " " + page % 10);
        }
        double percentNum = (double) finishCompare / sumCompare * 100;
        int percentInt = (int)percentNum;
        if(percentInt > 50){// 大于50
            System.out.println("配准率" + percentInt);
            compareJLabel.setText(percentInt / 10 + " " + percentInt % 10 + " " + "%");
        }else{// 小于50
            compareJLabel.setText(" ");
        }
        this.getRootPane().updateUI();
    }

    /**
     * 改变拖拽区的文字提示
     * @param oneOrTwo int类型变量 1为第一张图 2为第二张图
     */
    public void changeDragArea(int oneOrTwo) {
        if (oneOrTwo == 1) {
            loading.setVisible(true);
            loadFinish.setVisible(false);
        } else {
            loading.setVisible(false);
            loadFinish.setVisible(true);
        }
        this.getRootPane().updateUI();
    }

    /**
     * 触发事件函数
     * @param e 事件参数
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == small){
            this.setExtendedState(ICONIFIED);
            return;
        }
        if (e.getSource() == out  ){
            System.exit(0);
            return;
        }
        for (int i = 0; i < 4; i++) {
            if (e.getSource() == compareBtn[i]) {
                char lastChar;
                if (compareInt[i] == 1) {
                    lastChar = 'f';
                    compareInt[i] = 0;
                } else {
                    lastChar = 'n';
                    compareInt[i] = 1;
                }
                compareBtn[i].setIcon(new ImageIcon(new FilePath().filePath("compBtn/" + (i+1) + lastChar + ".png")));
                this.getRootPane().updateUI();
                return;
            }
        }
        if (e.getSource() == commitAwakeBtn) {
            String fieldStr = awakeSentenceTextField.getText();
            fieldStr = fieldStr.replaceAll("[。，！？：；,./?!`~{}1234567890]","");
            if (addOption.isSelected() && nextOption.isSelected()) {
                System.out.println("增加翻页词");
                orderNextString.add(fieldStr);
            } else if (addOption.isSelected() && lastOption.isSelected()) {
                System.out.println("增加回页词");
                orderLastString.add(fieldStr);
            } else if (delOption.isSelected() && nextOption.isSelected()) {
                System.out.println("删除翻页词");
                for (int i = 0; i < orderNextString.size(); i++) {
                    if (orderNextString.get(i).equals(fieldStr)) {
                        orderNextString.remove(i);
                    }
                }
            } else if (delOption.isSelected() && lastOption.isSelected()) {
                System.out.println("删除回页词");
                for (int i = 0; i < orderLastString.size(); i++) {
                    if (orderLastString.get(i).equals(fieldStr)) {
                        orderLastString.remove(i);
                    }
                }
            }
            changeAwakeRecord();
        }
    }

    /**
     * JFrame的构造函数里调用, 拖拽dragArea可以控制整个窗口
     */
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
