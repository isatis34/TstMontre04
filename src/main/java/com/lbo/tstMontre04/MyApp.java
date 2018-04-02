package com.lbo.tstMontre04;


import android.app.Application;

/**
 * Created by lbogni on 15/02/2018.
 */

public class MyApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		new LoggingExceptionHandler(this);
	}
}
