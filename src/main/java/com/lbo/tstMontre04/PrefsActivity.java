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

		@SuppressWarnings("deprecation") final EditTextPreference editTextPreferenceprefServerAddress = (EditTextPreference) findPreference("prefServerAddress");
		String value = GetValue(sharedPreferences.getString("prefServerAddress", ""), "Adresse/nom du serveur");
		editTextPreferenceprefServerAddress.setSummary(value);
		editTextPreferenceprefServerAddress.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object o) {

				String value = GetValue(o, "Adresse/nom du serveur");

				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				sharedPreferences.edit().putString("prefServerAddress", value).apply();
				editTextPreferenceprefServerAddress.setSummary(value);

				return true;
			}
		});

		@SuppressWarnings("deprecation") final EditTextPreference editTextPreferenceprefServerPortweb = (EditTextPreference) findPreference("prefServerPortweb");
		value = GetValue(sharedPreferences.getString("prefServerPortweb", ""), "Port Web du serveur");
		editTextPreferenceprefServerPortweb.setSummary(value);
		editTextPreferenceprefServerPortweb.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object o) {

				String value = GetValue(o, "Port Web du serveur");

				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				sharedPreferences.edit().putString("prefServerPortweb", value).apply();
				editTextPreferenceprefServerPortweb.setSummary(value);

				return true;
			}
		});

		@SuppressWarnings("deprecation") final EditTextPreference editTextPreferenceprefServerNSP = (EditTextPreference) findPreference("prefServerNSP");
		value = GetValue(sharedPreferences.getString("prefServerNSP", ""), "Namespace à utiliser");
		editTextPreferenceprefServerNSP.setSummary(value);
		editTextPreferenceprefServerNSP.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object o) {

				String value = GetValue(o, "Namespace à utiliser");
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				sharedPreferences.edit().putString("prefServerNSP", value).apply();
				editTextPreferenceprefServerNSP.setSummary(value);

				return true;
			}
		});

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
