package com.robot.brainhelperlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelperManager {
    private List<String> actions;
    private static HelperManager instance;
    private Context mContext;
    private BroadcastReceiver receiver;

    private List<OnActionListener> listeners;


    public static HelperManager getInstance() {
        if (instance == null){
            synchronized (HelperManager.class){
                instance = new HelperManager();
            }
        }
        return instance;
    }

    public HelperManager() {
        actions = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public void addListener(OnActionListener listener){
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }


    public void regist(Context context, String... as){
        mContext = context;
        if (as!=null || as.length >0){
            actions.addAll(Arrays.asList(as));
        }

        if (actions.size()>0){
            IntentFilter filter = new IntentFilter();
            for (String action:actions){
                filter.addAction(action);
            }
            mContext.registerReceiver(receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    for (OnActionListener listener:listeners){
                        if (listener.listenerActions()!= null){
                            if (listener.listenerActions().contains(intent.getAction())){
                                listener.onAction(intent);
                            }
                        }else{
                            listener.onAction(intent);
                        }
                    }
                }
            },filter);
        }
    }

    public void unRegist(){
        if (receiver!=null){
            mContext.unregisterReceiver(receiver);
            mContext = null;
        }
    }


    public void send(int action){
        Intent intent = new Intent("com.robot.brain");
        intent.putExtra("data",action);
        mContext.sendBroadcast(intent);
    }

    public void next(){
        send(1);
    }

    public void endQ(){
        send(0);
    }






    public interface OnActionListener{
        void onAction(Intent intent);
        List<String> listenerActions();
    }



}
