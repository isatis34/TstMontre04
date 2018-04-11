package com.lbo.tstMontre04;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Xml;
import android.widget.Button;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.xmlpull.v1.XmlSerializer;

import java.text.DateFormat;

/**
 * Created by lbogni on 23/03/2018.
 */

public class PrefsActivity extends PreferenceActivity
{
	private EditTextPreference editTextPreferenceprefServerAddress;
	public static PrefsActivity Instance;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Instance = this;
		super.onCreate(savedInstanceState);
		// add the xml resource
		addPreferencesFromResource(R.xml.usersettings);


		Preference button;
		button = (Preference) findPreference("button_Load_Prefs");
		button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference arg0)
			{
				try
				{
					PrefsActivity.Instance.LoadPreferencesFromFile(MainActivity.Instance.ApplicationDirectory + "/Settings.xml");
					return true;
				}

				catch (final Exception e)
				{
					Toast.makeText(PrefsActivity.this, "Erreur button_Load_Prefs:\n" + e.toString(), Toast.LENGTH_LONG).show();
				}
				return true;
			}
		});
		button = (Preference) findPreference("button_Save_Prefs");
		button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference arg0)
			{
				try
				{
					PrefsActivity.Instance.SavePreferencesToFile(MainActivity.Instance.ApplicationDirectory + "/Settings.xml");
					return true;
				}

				catch (final Exception e)
				{
					Toast.makeText(PrefsActivity.this, "Erreur button_Save_Prefs:\n" + e.toString(), Toast.LENGTH_LONG).show();
				}
				return true;
			}
		});

		final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		final String[] arrPref = {"prefServerAddress", "prefServerPortweb", "prefServerNSP", "prefDateStart", "prefDateEnd", "prefWSTimeOut", "prefLocation", "prefRessource"};
		final int[] arrPrefSummary = {R.string.PrefServerAddressSummary, R.string.PrefServerPortwebSummary, R.string.PrefNSPSummary, R.string.PrefNbreJoursAvantSummary, R.string.PrefNbreJoursApresSummary, R.string.PrefWSTimeOutSummary, R.string.PrefLocalisationSummary, R.string.PrefRessourceSummary};

		for (int i = 0; i < arrPref.length; i++)
		{
			final EditTextPreference EditTextPreference = (EditTextPreference) findPreference(arrPref[i]);
			String value = GetValue(sharedPreferences.getString(arrPref[i], ""), getResources().getString(arrPrefSummary[i]));
			EditTextPreference.setSummary(value);
			final int finalI = i;
			EditTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
			{
				@Override
				public boolean onPreferenceChange(Preference preference, Object o)
				{
					try
					{
						String value = GetValue(o, getResources().getString(arrPrefSummary[finalI]));

						SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
						//sharedPreferences.edit().putString(arrPref[finalI], value).apply();
						EditTextPreference.setSummary(value);
					}
					catch (Exception e)
					{
						Log.d(MainActivity.Instance.getClass().getPackage().toString(), e.toString());
					}
					return true;
				}
			});
		}
	}

	private Boolean LoadPreferencesFromFile(String filePath)
	{
		XmlPullParserFactory factory = null;
		try
		{
			factory = XmlPullParserFactory.newInstance();
		}
		catch (XmlPullParserException e1)
		{
			return false;
		}
		factory.setNamespaceAware(true);
		XmlPullParser parser;
		try
		{
			parser = factory.newPullParser();
		}
		catch (XmlPullParserException e1)
		{
			return false;
		}
		try
		{
			File f = new File(filePath);
			FileInputStream fIn = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(fIn);

			// auto-detect the encoding from the stream
			parser.setInput(isr);
			int eventType = parser.getEventType();
			boolean done = false;
			boolean inPrefs = false;
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PrefsActivity.this);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			String name = null;
			String value = null;
			while (eventType != XmlPullParser.END_DOCUMENT && !done)
			{
				switch (eventType)
				{
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						name = parser.getName();
						value = null;
						if (name.equalsIgnoreCase("Preferences"))
						{
							inPrefs = true;
						}
						break;
					case XmlPullParser.TEXT:
						value = parser.getText();
					case XmlPullParser.END_TAG:
						if (name.equalsIgnoreCase("Preferences"))
						{
							inPrefs = false;
						} else
						{
							if (inPrefs)
							{
								if (name.equalsIgnoreCase("server_preference_Text") || name.equalsIgnoreCase("server_AccessCode_preference")
										|| name.equalsIgnoreCase("server_NSP_preference") || name.equalsIgnoreCase("server_Hospital_preference")
										|| name.equalsIgnoreCase("search_preferences_doctor_code"))
								{
									editor.putString(name, value);
									EditTextPreference myPrefText = (EditTextPreference) PrefsActivity.this.findPreference(name);
									myPrefText.setText(value);
								} else
								{
									if ((name.equalsIgnoreCase("test_network_at_startup_preference")) || (name.equalsIgnoreCase("debug_Clinicom_preference"))
											|| (name.equalsIgnoreCase("debug_WS_preference")))
									{
										Boolean valueBool = Boolean.parseBoolean(value);
										editor.putBoolean(name, valueBool);
										CheckBoxPreference myPref = (CheckBoxPreference) PrefsActivity.this.findPreference(name);
										myPref.setChecked(valueBool);
									} else
									{
										if ((name.equalsIgnoreCase("server_preference")) || (name.equalsIgnoreCase("server_port_preference_web"))
												|| (name.equalsIgnoreCase("server_port_preference")) || (name.equalsIgnoreCase("search_type_preference"))
												|| (name.equalsIgnoreCase("search_preferences_ward_code"))
												|| (name.equalsIgnoreCase("search_preferences_GGUrg_Service_code"))
												|| (name.equalsIgnoreCase("search_GGUrg_Service_Presentation_preference")))
										{
											// Boolean valueBool =
											// Boolean.parseBoolean(value);
											editor.putString(name, value);
											ListPreference myPref = (ListPreference) PrefsActivity.this.findPreference(name);
											myPref.setValue(value);
										}
									}
								}
								/**
								 * else { try { editor.putString(name, value);
								 * editor.commit(); EditTextPreference myPrefText =
								 * (EditTextPreference)
								 * PrefsActivity.this.findPreference(name);
								 * myPrefText.setText(value); } catch (Exception e)
								 * { Log.e("com.lbo.clinicom", "Erreur sur " +
								 * name); } }
								 */
							}
						}
				}
				eventType = parser.next();
			}
			editor.commit();
			return true;
		}
		catch (FileNotFoundException e)
		{
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
			return false;
		}
		catch (IOException e)
		{
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
			return false;
		}
		catch (Exception e)
		{
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	private Boolean SavePreferencesToFile(String filePath)
	{
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try
		{
			//serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "   ");
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			// also set the line separator
			//serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", "\n");
			serializer.setOutput(writer);

			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "Preferences");
			SimpleDateFormat DateFormatDateTime = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			String currentDateTimeString = DateFormatDateTime.format(new Date()); // DateFormat.getDateInstance().format(new Date());
			serializer.attribute("", "DTTM", currentDateTimeString);

			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PrefsActivity.this);
			Map<String, ?> keys = sharedPreferences.getAll();
			for (Map.Entry<String, ?> entry : keys.entrySet())
			{
				serializer.startTag("", entry.getKey());
				serializer.text(entry.getValue() == null ? "" : entry.getValue().toString());
				serializer.endTag("", entry.getKey());
			}
			serializer.endTag("", "Preferences");
			serializer.endDocument();
			File file = new File(filePath);
			FileOutputStream fop = new FileOutputStream(file);
			if (!file.exists())
			{
				file.createNewFile();
			}
			fop.write(writer.toString().getBytes());
			fop.flush();
			fop.close();
			return true;
		}
		catch (Exception e)
		{
			Toast.makeText(getApplicationContext(), "Load: " + filePath + "\n" + e.toString(), Toast.LENGTH_SHORT).show();
			return false;
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
