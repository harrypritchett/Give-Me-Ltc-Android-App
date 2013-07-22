package com.hpritch5.give_me_ltc;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefManager {
	
	private static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
	}

	/*
	 * Application's preferences
	 */

	public static String getApiKey(Context context) {
		return getPreference(context, "API_KEY", "");
	}

	public static void setApiKey(Context context, String value) {
		setPreference(context, "API_KEY", value);
	}

	/*
	 * Android preference helpers
	 */
	private static void setPreference(Context context, String key, String value) {
		Editor editor = getSharedPreferences(context).edit();
		editor.putString(key, value);
		editor.commit();
	}

	private static String getPreference(Context context, String key, String defaultValue) {
		return getSharedPreferences(context).getString(key, defaultValue);
	}
}
