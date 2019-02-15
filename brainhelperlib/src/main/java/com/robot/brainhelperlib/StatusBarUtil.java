package com.robot.brainhelperlib;

import android.content.Context;
import android.content.Intent;

public class StatusBarUtil {

    final static String show = "com.robot.brain.statusbar.show";
    final static String hide = "com.robot.brain.statusbar.hide";
    public static void show(Context context){
        Intent intent = new Intent(show);
        intent.setPackage("com.robot.brain");
        context.startService(intent);
    }

    public static void hide(Context context){
        Intent intent = new Intent(hide);
        intent.setPackage("com.robot.brain");
        context.startService(intent);
    }
}
