package com.lbo.tstMontre04;

import android.content.Context;
import android.util.Log;
import java.util.List;
import java.util.ArrayList;

public class LoggingExceptionHandler implements Thread.UncaughtExceptionHandler
{
	private final static String TAG = "com.lbo.tstMontre04";
	//private final static String ERROR_FILE = MyAuthException.class.getSimpleName() + ".error";

	private final Context context;
	private final Thread.UncaughtExceptionHandler rootHandler;

	public LoggingExceptionHandler(Context context)
	{
		this.context = context;
		// we should store the current exception handler -- to invoke it for all not handled exceptions ...
		rootHandler = Thread.getDefaultUncaughtExceptionHandler();
		// we replace the exception handler now with us -- we will properly dispatch the exceptions ...
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(final Thread thread, final Throwable ex)
	{
		try
		{
			Log.d(TAG, "called for " + ex.getClass());
			// assume we would write each error in one file ...
		}
		catch (Exception e)
		{
			Log.e(TAG, "Exception Logger failed!", e);
		}

	}
}