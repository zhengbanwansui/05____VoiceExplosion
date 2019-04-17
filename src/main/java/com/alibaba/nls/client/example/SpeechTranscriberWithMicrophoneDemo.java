package com.alibaba.nls.client.example;

import CheckSame.IKAnalyzerUtil;
import PPT.*;
import Windows.Win;
import com.alibaba.nls.client.protocol.InputFormatEnum;
import com.alibaba.nls.client.protocol.NlsClient;
import com.alibaba.nls.client.protocol.SampleRateEnum;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriber;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriberListener;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriberResponse;
import PPT.ClickEvent;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

import static CheckSame.CheckTheSame.participle;

/**
 * 阿里云 - 使用麦克风音频流的实时音频流识别
 */
public class SpeechTranscriberWithMicrophoneDemo {

    private String appKey;
    private NlsClient client;
    private PPTTextSave PS;
    private String responseString = "NULL";             // 识别结果
    private int responseIndex = 0;
    private int oldIndex = 0;
    private String answerString = "NULL";       // 识别结果[安全过滤]
    private int page = 1;                       // 当前页数
    private boolean blackBreak = false;               // 幻灯片匹配结束条件，触发此条件后除了第五法都失效，第五法退回一页后，此条件变成false重启算法流程（未完成此判断过程）
    private ClickEvent PC = new ClickEvent();   // 定义PPT控制类

    /**
     * 语音识别的主进程, 在识别出结果后进行配准
     * @param win 界面窗口对象
     */
    public void process(Win win) {
        SpeechTranscriber transcriber = null;
        try {
            // Step1 创建实例,建立连接
            transcriber = new SpeechTranscriber(client, getTranscriberListener());
            transcriber.setAppKey(appKey);
            // 输入音频编码方式
            transcriber.setFormat(InputFormatEnum.PCM);
            // 输入音频采样率
            transcriber.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K);
            // 是否返回中间识别结果
            transcriber.setEnableIntermediateResult(true);
            // 是否生成并返回标点符号
            transcriber.setEnablePunctuation(true);
            // 是否将返回结果规整化,比如将一百返回为100
            transcriber.setEnableITN(false);
            // Step2 此方法将以上参数设置序列化为json发送给服务端,并等待服务端确认
            transcriber.start();
            // Step3 读取麦克风数据
            AudioFormat audioFormat = new AudioFormat(16000.0F, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
            TargetDataLine targetDataLine = (TargetDataLine)AudioSystem.getLine(info);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            int nByte = 0;
            final int bufSize = 6400;
            byte[] buffer = new byte[bufSize];
            win.changeDragArea(2);
            System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼准备完毕，开始识别过程▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
            while ((nByte = targetDataLine.read(buffer, 0, bufSize)) > 0) {
                //语音识别出结果后|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
                if (responseIndex > oldIndex) {
                    win.appendVoiceRecognizeRecord(responseString);
                    oldIndex++;
                    boolean rExactMat = false, rKeyMat = false, rLastMat = false, rAwake = false, rResCut = false;
                    // 指令唤醒算法
                    if (win.compareInt[0] == 1) {
                        rAwake = ruleAwake(win);
                    }
                    if (rAwake) {
                        System.out.println("指令唤醒翻页了 " + "现在是第" + page + "页 ");
                        winUpdatePageAndCom(win);
                        continue;
                    }
                    // 播放到最后黑屏的时候brek为真
                    if (page > PS.getArrayListArrayListPPTString().size()){
                        blackBreak = true;
                    } else {
                        blackBreak = false;
                    }
                    // 未播放到最后黑屏的时候进行匹配算法
                    if (!blackBreak) {
                        // 空页不匹配
                        if (PS.getArrayListArrayListPPTString().get(page-1).size() == 0){
                            winUpdatePageAndCom(win);
                            continue;
                        }
                        // 语音过滤算法
                        rResCut = ruleResponseCut();
                        if (rResCut) {
                            // 末端匹配算法
                            if (win.compareInt[3] == 1) {
                                rLastMat = ruleLastMatch();
                            }
                            if (rLastMat) {
                                System.out.println("末端匹配翻页了 " + "现在是第" + page + "页");
                                winUpdatePageAndCom(win);
                                continue;
                            }
                            winUpdatePageAndCom(win);
                            // 精准匹配算法
                            if (win.compareInt[1] == 1) {
                                rExactMat = ruleExactMatch();
                            }
                            if (rExactMat) {
                                System.out.println("精准匹配翻页了 " + "现在是第" + page + "页");
                                winUpdatePageAndCom(win);
                                continue;
                            }
                            winUpdatePageAndCom(win);
                            // 关键词匹配算法
                            if (win.compareInt[2] == 1) {
                                rKeyMat = ruleKeyMatch();
                            }
                            winUpdatePageAndCom(win);
                            if (rKeyMat) {
                                System.out.println("关键词匹配翻页了 " + "现在是第" + page + "页");
                                winUpdatePageAndCom(win);
                                continue;
                            }
                            winUpdatePageAndCom(win);
                        }
                    }
                }
                //语音识别出结果后|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

                //发送麦克风声音数据buffer
                transcriber.send(buffer);
            }
            // Step5 通知服务端语音数据发送完毕,等待服务端处理完成
            transcriber.stop();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            // Step6 关闭连接
            if (null != transcriber) {
                transcriber.close();
            }
        }
    }

    /**
     * 构造函数
     * @param appKey 应用id
     * @param token 验证许可
     * @param temp_PPTTextSave PPT文字存储对象被传入, 在本类中的识别函数里用到
     */
    public SpeechTranscriberWithMicrophoneDemo(String appKey, String token, PPTTextSave temp_PPTTextSave) {
        PS = temp_PPTTextSave;
        this.appKey = appKey;
        // Step0 创建NlsClient实例,应用全局创建一个即可,默认服务地址为阿里云线上服务地址
        client = new NlsClient(token);
    }

    /**
     * 侦听云服务器返回语音识别结果的函数, 在收到结果后
     * responseString被赋值 responseIndex被赋值 用于判断是否接受到了云语音识别的结果
     * @return
     */
    public SpeechTranscriberListener getTranscriberListener() {
        SpeechTranscriberListener listener = new SpeechTranscriberListener() {
            // 识别出中间结果.服务端识别出一个字或词时会返回此消息.仅当setEnableIntermediateResult(true)时,才会有此类消息返回
            @Override
            public void onTranscriptionResultChange(SpeechTranscriberResponse response) {
                /*System.out.println(
                        response.getStatus() +
                        "  【句中】" +
                        "  句子编号: " + response.getTransSentenceIndex() +
                        "  [" + response.getTransSentenceText() + "]"
                );*/

            }
            // 识别出一句话.服务端会智能断句,当识别到一句话结束时会返回此消息
            @Override
            public void onSentenceEnd(SpeechTranscriberResponse response) {
                System.out.println(
                    response.getStatus() +
                    "  【句末】" +
                    "  句子编号: " + response.getTransSentenceIndex() +
                    "  【" + response.getTransSentenceText() + "】"
                );
                // 接收到识别结果后保存到类变量responseString中由算法继续处理，此处不筛查
                responseString = response.getTransSentenceText();
                responseIndex  = response.getTransSentenceIndex();
            }
            // 识别完毕
            @Override
            public void onTranscriptionComplete(SpeechTranscriberResponse response) {
                System.out.println("name: " + response.getName() +
                        ", status: " + response.getStatus());
            }
        };
        return listener;
    }

    /**
     * 终止语音识别
     */
    public void shutdown() {

        client.shutdown();

    }

    /**
     * 翻页函数
     * 如果在1-n页内, 此页全部设置为已读, page+1, 调用翻页函数
     * 末页将不会继续翻页了, 防止bug
     */
    private void nextPage() {
        if(page <= PS.getArrayListArrayListPPTString().size()){
            ArrayList<PPTString> AP = PS.getArrayListArrayListPPTString().get(page-1);
            for(PPTString str : AP){
                str.bool = true;
            }
            PC.PPTControl(1);
            page++;
        }
        else if(page >  PS.getArrayListArrayListPPTString().size()){
            blackBreak = true;
        }
    }

    /**
     * 回页函数
     * 此页设为全部未读, 退回上页, 上页设为全部未读
     */
    private void lastPage() {
        ArrayList<ArrayList<PPTString>> strList = PS.getArrayListArrayListPPTString();
        PC.PPTControl(2);
        if(page <= PS.getArrayListArrayListPPTString().size()){
            for(PPTString temp_str : strList.get(page-1)){
                temp_str.bool = false;
            }
        }
        page--;
        for(PPTString temp_str : strList.get(page-1)){
            temp_str.bool = false;
        }
    }

    /**
     * 当前页数
     * 算出有多少框配准了
     * 算出一共多少框
     */
    private void winUpdatePageAndCom(Win win) {
        int maxNum = PS.getArrayListArrayListPPTString().get(page-1).size();
        int finishNum = 0;
        for(PPTString pptString : PS.getArrayListArrayListPPTString().get(page-1)){
            if(pptString.bool) {
                finishNum++;
            }
        }
        win.changePageAndCom(page, maxNum, finishNum);
    }

    /**
     * 语音唤醒匹配算法
     * @return
     */
    private boolean ruleAwake(Win win) {
        boolean rule5Worked = false;
        String managedResponseString = responseString.replaceAll("[。，！？：；]","");
        for (String nextString : win.getOrderNextString()) {
            if (managedResponseString.equals(nextString)) {
                nextPage();
                rule5Worked = true;
                break;
            }
        }
        for (String lastString : win.getOrderLastString()) {
            if (managedResponseString.equals(lastString)) {
                if(page != 1) {
                    lastPage();
                    rule5Worked = true;
                }
                break;
            }
        }
        return rule5Worked;

//        switch (managedResponseString) {
//            case "下一页":
//            case "下页":
//            case "翻篇":
//            case "翻篇儿":
//            case "翻页":
//            case "翻页儿":
//            case "翻到下一页":
//                nextPage();
//                rule5Worked = true;
//                break;
//            case "上一页":
//            case "上页":
//            case "返回上一页":
//            case "翻回上一页":
//            case "翻到上一页":
//                if(page != 1) {
//                    lastPage();
//                    rule5Worked = true;
//                }
//                break;
//            default:
//        }

    }

    /**
     * 精准匹配算法
     * @return
     */
    private boolean ruleExactMatch() {
        boolean rule1Worked = false;
        ArrayList<ArrayList<PPTString>> strList = PS.getArrayListArrayListPPTString();
        Vector<String> str1 = participle(answerString);
        double same = 0;
        try{
            // 循环匹配此页每段文本把匹配上的文本赋值为true
            for(int i=0; i<strList.get(page-1).size(); i++) {
                Vector<String> str2 = participle( strList.get(page-1).get(i).textStr);
                same = IKAnalyzerUtil.getSimilarity( str1 , str2 );
                System.out.println( "相似度：" + same );
                // 相似度高，句子赋值为已匹配状态
                if(same > 0.98){
                    strList.get(page-1).get(i).bool = true;
                }
            }
            // 遍历此页，得到整页的匹配是否全部完成，step记录已经匹配了多少段文本
            int step = 0;
            for(PPTString str : strList.get(page-1)){
                if(str.bool){
                    System.out.println("@@@str内容为@@@"+str.textStr+"的文字已精准匹配");
                    step++;
                }
            }
            System.out.println("@@@step is "+ step+"/"+strList.get(page-1).size() + "@@@ in page "+page);
            if(step == strList.get(page-1).size()){
                nextPage();
                rule1Worked = true;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return rule1Worked;
    }

    /**
     * 关键词匹配算法
     * @return
     */
    private boolean ruleKeyMatch() {
        boolean ruleKeyMatched = false;
        ArrayList<ArrayList<PPTString>> strList = PS.getArrayListArrayListPPTString();
        // 查找answerString中有没有每个文本框的关键词->
        // 遍历所有PPTString
        for(PPTString str : strList.get(page-1)){
            // 遍历str的每一个keyword
            for(KeyString ks : str.cutedKeyWords){
                if (answerString.contains(ks.str)) {
                    ks.bool = true;
                }
            }
            // 文本框全部keyword提到, 算这个文本框读完了
            int referok = 0;
            for(KeyString ks : str.cutedKeyWords){
                if (ks.bool) {
                    referok++;
                }
            }
            if (referok == str.cutedKeyWords.size()) {
                str.bool = true;
            }
        }
        // 遍历此页，得到整页的匹配是否全部完成，step记录已经匹配了多少段文本
        int step = 0;
        for(PPTString str : strList.get(page-1)){
            if(str.bool){
                System.out.println("@@@str内容为@@@"+str.textStr+"的文字已关键词匹配");
                step++;
            }
        }
        System.out.println("@@@step is "+ step+"/"+strList.get(page-1).size() + "@@@ in page "+page);
        if(step == strList.get(page-1).size()){
            nextPage();
            ruleKeyMatched = true;
        }

        return ruleKeyMatched;
    }

    /**
     * 末端匹配算法
     * @return
     */
    private boolean ruleLastMatch() {
        // PS.last是每页匹配用的词语库
        // 看answerString里面有多少个匹配上PS.last里的词语
        int maxMatchNum = PS.last.get(page-1).size();
        int matchedNum = 0;
        for (int i = 0; i < maxMatchNum; i++) {
            // 有一个关键字没有就赋值false
            if (answerString.contains(PS.last.get(page-1).get(i))){
                matchedNum++;
            }
        }
        switch (maxMatchNum) {
            case 1:
                if (matchedNum == 1) {
                    nextPage();
                    return true;
                }
                break;
            case 2:
                if (matchedNum >= 2) {
                    nextPage();
                    return true;
                }
                break;
            case 3:
                if (matchedNum >= 2) {
                    nextPage();
                    return true;
                }
                break;
            case 4:
                if (matchedNum >= 3) {
                    nextPage();
                    return true;
                }
                break;
            default:
                if (matchedNum >= 4) {
                    nextPage();
                    return true;
                }
        }
        return false;
    }

    /**
     * 识别文本过滤responseString筛查
     * @return
     */
    private boolean ruleResponseCut() {
        boolean rule6Worked = false;
        // 语音识别字符串正则过滤
        String temp_responseString = responseString.replaceAll("[^\\u4e00-\\u9fa5：；，。！？]","");
        if(temp_responseString.length() < 2){
            // 空字符识别结果，拒绝赋值给answerString
            // 单字符识别结果，匹配作用小，拒绝赋值给answerString
        }else{
            // 识别结果经过筛查可以认为是有效的识别结果
            answerString = temp_responseString;
            System.out.println("语音过滤为 : " + temp_responseString);
            rule6Worked = true;
        }
        return rule6Worked;
    }
}
