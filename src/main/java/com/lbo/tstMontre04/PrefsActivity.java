package com.lbo.tstMontre04;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by lbogni on 23/03/2018.
 */

public class PrefsActivity extends PreferenceActivity
{
	private EditTextPreference editTextPreferenceprefServerAddress;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// add the xml resource
		addPreferencesFromResource(R.xml.usersettings);

		final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		final String[] arrPref = {"prefServerAddress", "prefServerPortweb", "prefServerNSP", "prefDateStart", "prefDateEnd", "prefWSTimeOut"};
		final int[] arrPrefSummary = {R.string.PrefServerAddressSummary, R.string.PrefServerPortwebSummary, R.string.PrefNSPSummary, R.string.PrefNbreJoursAvantSummary, R.string.PrefNbreJoursApresSummary, R.string.PrefWSTimeOutSummary};

		for (int i = 0 ; i < arrPref.length ; i++)
		{
			final EditTextPreference EditTextPreference = (EditTextPreference) findPreference(arrPref[i]);
			String value = GetValue(sharedPreferences.getString(arrPref[i], ""), getResources().getString(arrPrefSummary[i]));
			EditTextPreference.setSummary(value);
			final int finalI = i;
			EditTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object o) {

					String value = GetValue(o, getResources().getString(arrPrefSummary[finalI]));

					SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
					sharedPreferences.edit().putString(arrPref[finalI], value).apply();
					EditTextPreference.setSummary(value);

					return true;
				}
			});
		}
	}

	private static String GetValue(Object Value, String Text)
	{
		String value = "";
		if ((Value == null) || (Value.toString() == ""))
			value = Text;
		else
			value = Text + " (" + Value.toString() + ")";
		return value;
	}
}
