package com.alibaba.nls.client.example;

import CheckSame.IKAnalyzerUtil;
import PPT.ClickEvent;
import PPT.PPTString;
import PPT.PPTTextSave;
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

//使用麦克风音频流的实时音频流识别
public class SpeechTranscriberWithMicrophoneDemo {

    private boolean exit = false;
    private String appKey;
    private NlsClient client;
    private PPTTextSave PS;
    private String responseString = "NULL";             // 识别结果
    private int responseIndex = 0;
    private int oldIndex = 0;
    private String answerString = "NULL";       // 识别结果[安全过滤]
    private int page = 1;                       // 当前页数
    private boolean brek = false;               // 幻灯片匹配结束条件，触发此条件后除了第五法都失效，第五法退回一页后，此条件变成false重启算法流程（未完成此判断过程）
    private ClickEvent PC = new ClickEvent();   // 定义PPT控制类

    //语音识别调用部分代码
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
            System.out.println("-------------------------准备完毕，开始识别过程----------------------------------------");
            win.Log("<<< 准备完毕，开始识别过程, 请播放幻灯片");
            while ((nByte = targetDataLine.read(buffer, 0, bufSize)) > 0) {

                if(responseIndex > oldIndex) {
                    oldIndex++;
                    // 炽天覆七重圆环饱和匹配算法启动~~~
                    boolean r1 = false, r2 = false, r3 = false, r4 = false, r5 = false, r6 = false, r7 = false;
                    // 指令唤醒算法, 最大优先级
                    r5 = Rule5();
                    // 播放到最后黑屏的时候
                    if(page > PS.getArrayListArrayListPPTString().size()){
                        brek = true;
                    }else{
                        brek = false;
                    }
                    // 未触发唤醒, 未播放完, 未遇到空页
                    if(!r5 && !brek && PS.getArrayListArrayListPPTString().get(page-1).size() != 0){
                        // 执行responseString简单过滤器
                        r6 = Rule6();
                        // 如果responseString存活到了answerString中，PPT文本与识别文本进一步匹配模式启动
                        if(r6){
                            // 末端匹配
                            r3 = Rule3();
                            if(brek){
                                continue;
                            }
                            // 旧模式匹配
                            r1 = Rule1();
                            if(brek){
                                continue;
                            }
                        }
                    }
                }
                if(exit){
                    break;
                }
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

    public SpeechTranscriberWithMicrophoneDemo(String appKey, String token, PPTTextSave temp_PPTTextSave) {
        PS = temp_PPTTextSave;
        this.appKey = appKey;
        // Step0 创建NlsClient实例,应用全局创建一个即可,默认服务地址为阿里云线上服务地址
        client = new NlsClient(token);
    }

    public SpeechTranscriberListener getTranscriberListener() {
        SpeechTranscriberListener listener = new SpeechTranscriberListener() {
            // 识别出中间结果.服务端识别出一个字或词时会返回此消息.仅当setEnableIntermediateResult(true)时,才会有此类消息返回
            @Override
            public void onTranscriptionResultChange(SpeechTranscriberResponse response) {
                System.out.println(
                        response.getStatus() +
                        "  【句中】" +
                        "  句子编号: " + response.getTransSentenceIndex() +
                        "  [" + response.getTransSentenceText() + "]"
                );

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

    public void shutdown() {
        client.shutdown();
    }

    // 翻页函数 会对PS内文本进行初始化, 推进页面播放进程, 将本页设为已读, 进入下一页内容
    private void nextPage(){
        // 末页将不会继续翻页了, 防止bug
        if(page <= PS.getArrayListArrayListPPTString().size()){
            ArrayList<PPTString> AP = PS.getArrayListArrayListPPTString().get(page-1);
            for(PPTString str : AP){
                str.bool = true;
            }
            PC.PPTControl(1);
            page++;
        }
        if(page > PS.getArrayListArrayListPPTString().size()) {
            brek = true;
        }
    }

    // FINISH    旧匹配模式
    private boolean Rule1()//----------------------------------------------Rule1------------------------------
    {
        // 无需判断是否有新识别出的文字因为在调用此函数的时候就确定了是有新的文字了!!!
        // 精确度卡 0.98
        boolean rule1Worked = false;
        ArrayList<ArrayList<PPTString>> strList = PS.getArrayListArrayListPPTString();
        Vector<String> str1 = participle(answerString);
        Vector<String> str2;
        double same = 0;
        try{
            //循环匹配此页每段文本
            for(int i=0; i<strList.get(page-1).size(); i++) {
                str2 = participle( strList.get(page-1).get(i).textStr);
                same = IKAnalyzerUtil.getSimilarity( str1 , str2 );
                System.out.println( "相似度：" + same );
                //相似度高，句子赋值为已匹配状态
                if(same > 0.98){
                    strList.get(page-1).get(i).bool = true;
                }
            }
            //遍历此页，得到整页的匹配是否全部完成，step记录已经匹配了多少段文本
            int step = 0;
            for(PPTString str : strList.get(page-1)){
                if(str.bool){
                    step++;
                }
            }
            if(step == strList.get(page-1).size()){
                nextPage();
            }
            rule1Worked = true;
        }catch(Exception e){
            System.out.println(e);
        }
        return rule1Worked;
    }

    // UN FINISH 关键词匹配模式
    private void Rule2()//-------------------------------------------------Rule2------------------------------
    {

    }

    // UN FINISH 末端匹配
    private boolean Rule3()//-------------------------------------------------Rule3------------------------------
    {
        // PS.last是每页匹配用的词语库
        // 处理answerString
        String str = answerString;
        // 看str里面含不含PS.last里的词语
        int size = PS.last.get(page-1).size();
        boolean temp_bool = true;
        for(int i=0; i<size; i++){
            // 有一个关键字没有就赋值false
            if( !str.contains(PS.last.get(page-1).get(i)) ){
                temp_bool = false;
            }
        }
        if(temp_bool){
            nextPage();
        }
        return true;
    }

    // UN FINISH NLP语意提取匹配
    private void Rule4()//-------------------------------------------------Rule4------------------------------
    {

    }

    // FINISH    语音唤醒匹配
    private boolean Rule5()//----------------------------------------------Rule5------------------------------
    {
        // 再来点关键词
        ArrayList<ArrayList<PPTString>> strList;
        strList = PS.getArrayListArrayListPPTString();
        boolean rule5Worked = false;
        if(responseString.equals("下一页。") || responseString.equals("下页") || responseString.equals("翻篇儿")  || responseString.equals("翻篇")  || responseString.equals("翻页") || responseString.equals("翻页儿。") || responseString.equals("下雨") || responseString.equals("下一个") ){
            System.out.println("------Rule5唤醒下一页------");
            //此页设为全部已读，翻页
            nextPage();
            rule5Worked = true;
        }
        else if( (responseString.equals("上一页") && page >= 2) || (responseString.equals("上页") && page > 1) ){
            PC.PPTControl(2);
            System.out.println("------Rule5唤醒上一页------");
            ////此页设为全部已读，退回上页，上页设为全部已读
            if(page <= PS.getArrayListArrayListPPTString().size()){
                for(PPTString temp_str : strList.get(page-1)){
                    temp_str.bool = false;
                }
            }
            page--;
            for(PPTString temp_str : strList.get(page-1)){
                temp_str.bool = false;
            }
            rule5Worked = true;
        }
        else if( responseString.equals("结束进程") ){
            exit = true;
        }
        return rule5Worked;
    }

    // FINISH    responseString筛查, 很简单的过滤工作哦
    private boolean Rule6()//----------------------------------------------Rule6------------------------------
    {
        // 文本长度定制方案暂时不做，比较难弄
        boolean rule6Worked = false;
        if(responseString.length() == 0){
            // 空字符识别结果，拒绝赋值给answerString
        }else if(responseString.length() == 1){
            // 单字符识别结果，匹配作用小，拒绝赋值给answerString
        }else{
            // 识别结果经过筛查可以认为是有效的识别结果
            answerString = responseString;
            rule6Worked = true;
        }
        return rule6Worked;
    }

    // UN FINISH 其他方案
    private void Rule7()//-------------------------------------------------Rule7------------------------------
    {

    }
}
