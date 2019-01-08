package com.robot.brainhelperlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.Arrays;
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

        if (actions.size() > 0) {
            IntentFilter filter = new IntentFilter();
            for (String action : actions) {
                filter.addAction(action);
            }
            mContext.registerReceiver(receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (onActionListener != null) {
                        if ("com.robot.brain.end".equals(intent.getAction())) {
                            onActionListener.onFinish(intent);
                        } else {
                            onActionListener.onAction(intent);
                        }
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


    public void send(int action) {
        Intent intent = new Intent("com.robot.brain");
        intent.putExtra("data", action);
        mContext.sendBroadcast(intent);
    }

    public void sendText(String action, Map<String, String> data) {
        Intent intent = new Intent(action);
        if (data != null)
            for (Map.Entry<String, String> entry : data.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        mContext.sendBroadcast(intent);
    }

    /**
     * 释放mic
     */
    public void releaseMic() {
        send(4);
    }

    /**
     * 处于语音收听状态
     */
    public void asr() {
        send(3);
    }

    /**
     * 处于唤醒状态
     */
    public void wake() {
        send(2);
    }

    /**
     * 下一个队列
     */
    public void next() {
        send(1);
    }

    /**
     * 结束队列
     */
    public void endQ() {
        send(0);
    }

    public interface OnActionListener {
        void onAction(Intent intent);

        void onFinish(Intent intent);
    }


}
