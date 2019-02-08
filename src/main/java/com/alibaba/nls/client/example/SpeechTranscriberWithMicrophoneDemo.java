package com.alibaba.nls.client.example;

import CheckSame.IKAnalyzerUtil;
import PPT.ClickEvent;
import PPT.PPTString;
import PPT.PPTTextSave;
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
    public void process() {
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
            while ((nByte = targetDataLine.read(buffer, 0, bufSize)) > 0) {

                if(responseIndex > oldIndex){
                    oldIndex++;
                    // 炽天覆七重圆环饱和匹配算法启动~~~
                    boolean r1 = false,r2 = false,r3 = false,r4 = false,r5 = false,r6 = false,r7 = false;
                    // 提前检测直接指令唤醒算法
                    r5 = Rule5();
                    if(!r5){
                        // 筛选出可用的answerString
                        r6 = Rule6();
                        // 如果结果存活到了answerString中，旧模式启动
                        if(r6){
                            r1 = Rule1();
                        }
                    }
                }
                if(brek)
                    break;
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

    // FINISH    旧匹配模式
    private boolean Rule1()//----------------------------------------------Rule1------------------------------
    {
        ArrayList<ArrayList<PPTString>> strList;
        strList = PS.getArrayListArrayListPPTString();
        // 无需判断是否有新识别出的文字因为在调用此函数的时候就确定了是有新的文字了
        // 精确度卡 0.98
        boolean rule1Worked = false;
        Vector<String> str1,str2;           // 第一法两个Vector字符串
        str1 = participle(answerString);
        double same = 0 ;                   //根据分词返回相似度
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
            // 每一段文本都匹配完  并且  页数不是最后一页
            if     (step == strList.get(page-1).size() && page <  strList.size()){
                PC.PPTControl(1);
                page++;
            }
            // 每一段文本都匹配完  并且  页数是最后一页
            else if(step == strList.get(page-1).size() && page == strList.size()){
                PC.PPTControl(1);
                brek = true;
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
    private void Rule3()//-------------------------------------------------Rule3------------------------------
    {
        // value = 999的文本框是末端
        // 要定义一个可以随时调用的变量来存储每页的末端的几个词语

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
        if(responseString.equals("下一页。") || responseString.equals("下页") || responseString.equals("翻篇儿")  || responseString.equals("翻页") || responseString.equals("翻页儿。") ){
            PC.PPTControl(1);
            System.out.println("------Rule5翻页------");
            //此页设为全部已读，翻页
            for(PPTString temp_str : strList.get(page-1)){
                temp_str.bool = true;
            }
            page++;
            if(page > strList.size()){
                brek = true;
            }
            rule5Worked = true;
        }
        else if( (responseString.equals("上一页") && page > 1) || (responseString.equals("上页") && page > 1) ){
            PC.PPTControl(2);
            System.out.println("------Rule5上一页------");
            ////此页设为全部已读，退回上页，上页设为全部已读
            for(PPTString temp_str : strList.get(page-1)){
                temp_str.bool = false;
            }
            page--;
            for(PPTString temp_str : strList.get(page-1)){
                temp_str.bool = false;
            }
            rule5Worked = true;
        }
        return rule5Worked;
    }

    // FINISH    识别文本筛查, 幻灯片文本筛查
    private boolean Rule6()//----------------------------------------------Rule6------------------------------
    {
        // 文本长度定制方案暂时不做，比较难弄
        boolean rule6Worked = false;
        if(responseString.length() == 0){
            // 空字符识别结果，拒绝赋值给answerString
        }else if(responseString.length() == 1){
            // 单字符识别结果，匹配作用极小，拒绝赋值给answerString
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
