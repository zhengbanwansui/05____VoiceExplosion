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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.util.ArrayList;
import java.util.Vector;

import static CheckSame.CheckTheSame.participle;

//使用麦克风音频流的实时音频流识别
public class SpeechTranscriberWithMicrophoneDemo {
    public String answerString;
    private String appKey;
    private String accessToken;
    NlsClient client;
//#################################################################################################################
    //语音识别调用部分代码
    public void process(PPTTextSave PS) {
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
            String oldString = "NULL";
            answerString = "NULL";
            Vector<String> str1,str2;
            int page = 1;
            ArrayList<ArrayList<PPTString>> strList = PS.getArrayListArrayListPPTString();
            ClickEvent robotA = new ClickEvent();
            boolean brek = false;
            System.out.println("-------------------------准备完毕，开始识别过程----------------------------------------");
            while ((nByte = targetDataLine.read(buffer, 0, bufSize)) > 0) {
                //判断是否有新识别出的文字并进行匹配
                if(oldString != answerString){
                    oldString = answerString;
                    str1 = participle(oldString);
                    //循环匹配此页每段文本
                    for(int i=0; i<strList.get(page-1).size(); i++){
                        str2 = participle( strList.get(page-1).get(i).textStr);
                        double same = 0 ;//根据分词返回相似度
                        same = IKAnalyzerUtil.getSimilarity( str1 , str2 );
                        System.out.println( "相似度：" + same );
                        //相似度高，句子赋值为已匹配状态
                        if(same > 0.67){
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
                    if     (step == strList.get(page-1).size() && page < strList.size() ){
                        robotA.PPTControl(1);
                        page++;
                    }
                    // 每一段文本都匹配完  并且  页数是最后一页
                    else if(step == strList.get(page-1).size() && page == strList.size()){
                        robotA.PPTControl(1);
                        brek = true;
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
//#################################################################################################################
    public SpeechTranscriberWithMicrophoneDemo(String appKey, String token) {
        this.appKey = appKey;
        this.accessToken = token;
        // Step0 创建NlsClient实例,应用全局创建一个即可,默认服务地址为阿里云线上服务地址
        client = new NlsClient(accessToken);
    }

    //get方法 得到实时语音识别监听器方法
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
                if(response.getTransSentenceText().length() != 0){
                    answerString = response.getTransSentenceText();
                }
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
}
