package com.grizly.posstockapp.utils;

import android.app.Activity;
import android.graphics.Color;

import com.grizly.posstockapp.ApplicationContext;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

public class SnackS {


	public static void snackAlert(Activity context, String text){

		SnackbarManager.show(Snackbar.with(context)
				.textColor(Color.parseColor("#ffffffff"))
				.color(Color.parseColor("#0cabd8"))
						//.type(SnackbarType.MULTI_LINE)
				.text(text), context);
	}

	public static void snackInfo(Activity context, String text){

		SnackbarManager.show(Snackbar.with(ApplicationContext.getContext())
				.text(text), context);
	}

	public static void snackAlertLong(Activity context, String text){

		SnackbarManager.show(Snackbar.with(ApplicationContext.getContext())
				.textColor(Color.parseColor("#ffffffff"))
				.color(Color.parseColor("#6C6D70"))
						//.type(SnackbarType.MULTI_LINE)
				.duration(5000)
				.text(text), context);
	}

	/**
	 * 
	 * @param context
	 * @param text
	 * 
	 * Duration 5 sec
	 */
	public static void snackInfoLarge(Activity context, String text){

		SnackbarManager.show(Snackbar.with(ApplicationContext.getContext())
				.type(SnackbarType.MULTI_LINE)
				.duration(5000)
				.text(text), context);
	}
}
