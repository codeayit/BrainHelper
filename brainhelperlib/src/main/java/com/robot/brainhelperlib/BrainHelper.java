package com.robot.brainhelperlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrainHelper {
    private List<String> actions;
    private static BrainHelper instance;
    private Context mContext;
    private BroadcastReceiver receiver;

    private OnActionListener onActionListener;

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    //    private List<OnActionListener> listeners;


//    public static HelperManager getInstance() {
////        if (instance == null){
////            synchronized (HelperManager.class){
//                instance = new HelperManager();
////            }
////        }
//        return instance;
//    }

    public BrainHelper(Context mContext) {
        this.mContext = mContext;
        actions = new ArrayList<>();
    }


//    public void addListener(OnActionListener listener){
//        if (!listeners.contains(listener)){
//            listeners.add(listener);
//        }
//    }


    public void regist(String... as) {
        if (as != null || as.length > 0) {
            actions.addAll(Arrays.asList(as));
        }

        actions.add("com.robot.brain.end");
        actions.add("tts_stop");

        if (actions.size() > 0) {
            IntentFilter filter = new IntentFilter();
            for (String action : actions) {
                filter.addAction(action);
            }
            mContext.registerReceiver(receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                        if ("com.robot.brain.end".equals(intent.getAction())) {
                            if (onActionListener != null) {
                                onActionListener.onFinish(intent);}
                        } else if("tts_stop".equals(intent.getAction())){
                            if (!TextUtils.isEmpty(ttsToken) && ttsToken.equals(intent.getStringExtra("tts_token"))){
                                if (onTtsListener!=null){
                                    onTtsListener.onStop();
                                }
                            }
                        }else {
                            onActionListener.onAction(intent);
                        }


//                    for (OnActionListener listener:listeners){
//                        if ("com.robot.brain.end".equals(intent.getAction())){
//                            listener.onFinish(intent);
//                        }else{
//                            if (listener.listenerActions()!= null){
//                                if (listener.listenerActions().contains(intent.getAction())){
//                                    listener.onAction(intent);
//                                }
//                            }else{
//                                listener.onAction(intent);
//                            }
//                        }
//                    }
                }
            }, filter);
        }
    }

    public void unRegist() {
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
            mContext = null;
        }
    }


    public void send(String action) {
        Intent intent = new Intent("com.robot.brain");
        intent.putExtra("data", action);
        mContext.sendBroadcast(intent);
    }

//    public void sendText(String action, Map<String, String> data) {
//        Intent intent = new Intent(action);
//        if (data != null)
//            for (Map.Entry<String, String> entry : data.entrySet()) {
//                intent.putExtra(entry.getKey(), entry.getValue());
//            }
//        mContext.sendBroadcast(intent);
//    }

    public void sendText(String action, Map<String, String> data) {
        Intent intent = new Intent("com.robot.brain");
        intent.putExtra("data",action);
        if (data != null)
            for (Map.Entry<String, String> entry : data.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        mContext.sendBroadcast(intent);
    }

    private String ttsToken;

    public void startTts(String text){
        ttsToken = String.valueOf(System.currentTimeMillis());
        Map<String,String> data = new HashMap<>();
        data.put("tts_text",text);
        data.put("tts_token",ttsToken);
        sendText("tts_start",data);
        if (onTtsListener!=null){
            onTtsListener.onStart();
        }
    }

    public void stopTts(){
        send("tts_stop");
    }




    /**
     * 释放mic
     */
    public void releaseMic() {
        send("releaseMic");
    }

    /**
     * 处于语音收听状态
     */
    public void startAsr() {
        send("startAsr");
    }

    public void stopAsr(){
        send("stopAsr");
    }

    /**
     * 处于唤醒状态
     */
    public void startWake(){
        send("startWake");
    }

    public void stopWake(){
        send("stopWake");
    }

    public void startTouch(){
        send("startTouch");
    }

    public void stopTouch(){
        send("stopTouch");
    }


    /**
     * 下一个队列
     */
    public void nextQ() {
        send("nextQ");
    }

    public void nextQ(Map<String, String> extras){
        sendText("nextQ",extras);
    }



    /**
     * 结束队列
     */
    public void endQ() {
        send("endQ");
    }


    public interface OnActionListener {
        void onAction(Intent intent);

        void onFinish(Intent intent);
    }


    public void setOnTtsListener(OnTtsListener onTtsListener) {
        this.onTtsListener = onTtsListener;
    }

    private OnTtsListener onTtsListener;

    public interface OnTtsListener{
        void onStart();
        void onStop();
    }


}
