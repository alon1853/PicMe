package com.me.plan.picme;

import android.app.Application;
import android.content.Context;

/**
 * Created by Alon on 05/03/2018.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }


    public static Context getMyContext(){
        return context;
    }
}
