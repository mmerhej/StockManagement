package com.grizly.posstockapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.Locale;


/**
 * Created by koa on 7/27/2015.
 */
public class ApplicationContext extends Application {

    private static ApplicationContext instance;
    public static int AppTheme = R.style.AppTheme;

    public ApplicationContext() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance.setTheme(AppTheme);

        Fresco.initialize(this);


        final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread paramThread, Throwable paramThrowable) {

						/*if(paramThrowable.getMessage()!=null)
                            logger.error("Exception :: "
	                	+ Methods.getPref(getApplicationContext(), Config.PREF_KEY_USER_ID)
	                	+"\n " + paramThrowable.getMessage().toString());*/
                        //Do your own error handling here
                        if (oldHandler != null)
                            //Delegates to Android's error handling
                            oldHandler.uncaughtException(paramThread, paramThrowable);
                        else
                            System.exit(2); //Prevents the service/app from freezing
                    }
                });
    }

    private Locale locale = null;

    public static int getAppTheme() {
        return AppTheme;
    }

    public static void setAppTheme(int appTheme) {
        AppTheme = appTheme;
        instance.setTheme(AppTheme);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (locale != null) {
            Locale.setDefault(locale);
            Configuration config = new Configuration(newConfig);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }
}
