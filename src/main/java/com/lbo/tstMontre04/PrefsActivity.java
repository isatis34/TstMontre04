package com.lbo.tstMontre04;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlSerializer;

import java.text.DateFormat;
import java.util.UUID;

/**
 * Created by lbogni on 23/03/2018.
 */

public class PrefsActivity extends PreferenceActivity
{
	private static final int ACTIVITY_SEND_FILE = 0;
	private static final int ACTIVITY_CHOOSE_FILE = 1;
	private EditTextPreference editTextPreferenceprefServerAddress;
	public static PrefsActivity Instance;
	private FileObserver observer;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		try
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
						PrefsActivity.Instance.SavePreferencesToFile(MainActivity.Instance.ApplicationDirectory + "/Settings.xml", true);
						return true;
					}

					catch (final Exception e)
					{
						Toast.makeText(PrefsActivity.this, "Erreur button_Save_Prefs:\n" + e.toString(), Toast.LENGTH_LONG).show();
					}
					return true;
				}
			});

			button = (Preference) findPreference("button_Send_Prefs_Via_Bluetooth");
			button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
			{
				@Override
				public boolean onPreferenceClick(Preference arg0)
				{
					try
					{
						PrefsActivity.Instance.SendPreferencesViaBluetooth();
						return true;
					}

					catch (final Exception e)
					{
						Toast.makeText(PrefsActivity.this, "Erreur button_Share_Prefs_Via_Bluetooth:\n" + e.toString(), Toast.LENGTH_LONG).show();
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

			Bundle b = getIntent().getExtras();
			String action = b.getString("ACTION");
			if (action != null)
			{
				if (action.equalsIgnoreCase("LOAD_PREF_FROM_PREF"))
				{
					AskAndImportPrefFile(b.getString("FILENAME"));
				}
			}
		}
		catch (Exception e)
		{
			Log.d(MainActivity.Instance.getClass().getPackage().toString(), e.toString());
		}
	}

	public void AskAndImportPrefFile(final String file)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(PrefsActivity.Instance);

		builder.setTitle("Importation");
		builder.setMessage("Importer les préférences ?");

		builder.setPositiveButton("Oui", new DialogInterface.OnClickListener()
		{

			public void onClick(DialogInterface dialog, int which)
			{
				Log.v(MainActivity.Instance.getClass().getPackage().toString(), "bluetooth [" + android.os.Environment.getExternalStorageDirectory().toString() + "/bluetooth :" + file + "]");
				LoadPreferencesFromFile(file);
				File fdelete = new File(file);
				if (fdelete.exists())
				{
					try
					{
						fdelete.delete();
					}
					catch (Exception e)
					{

					}
				}
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("Non", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{

				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public List<File> folderSearchBT(File src, String folder)
			throws FileNotFoundException
	{

		List<File> result = new ArrayList<File>();

		File[] filesAndDirs = src.listFiles();
		List<File> filesDirs = Arrays.asList(filesAndDirs);

		for (File file : filesDirs)
		{
			result.add(file); // always add, even if directory
			if (!file.isFile())
			{
				List<File> deeperList = folderSearchBT(file, folder);
				result.addAll(deeperList);
			}
		}
		return result;
	}

	public String searchForBluetoothFolder()
	{

		String splitchar = "/";
		File root = Environment.getExternalStorageDirectory();
		List<File> btFolder = null;
		String bt = "bluetooth";
		try
		{
			btFolder = folderSearchBT(root, bt);
		}
		catch (FileNotFoundException e)
		{
			Log.e("FILE: ", e.getMessage());
		}

		for (int i = 0; i < btFolder.size(); i++)
		{

			String g = btFolder.get(i).toString();

			String[] subf = g.split(splitchar);

			String s = subf[subf.length - 1].toUpperCase();

			boolean equals = s.equalsIgnoreCase(bt);

			if (equals)
				return g;
		}
		return null; // not found
	}

	private Boolean SendPreferencesViaBluetooth()
	{
		try
		{
			String directory = MainActivity.Instance.ApplicationDirectory;
			String filename = "/RDV-BTSettings.xml";
			if (SavePreferencesToFile(directory + filename, false))
			{
				File file = new File(directory, filename);
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_SEND);
				intent.setType("image/png");
				intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
				startActivityForResult(intent, ACTIVITY_SEND_FILE);
			}
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case ACTIVITY_SEND_FILE:
			{
				// Make sure the request was successful
				if (resultCode == RESULT_OK)
				{

				}
				break;
			}
			case ACTIVITY_CHOOSE_FILE:
			{
				if (resultCode == RESULT_OK)
				{
					Uri uri = data.getData();
					String filePath = uri.getPath();
					LoadPreferencesFromFile(filePath);
				}
				break;
			}
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
						parser.next();

						value = parser.getText();
						Object myPref = PrefsActivity.this.findPreference(name);
						if (myPref instanceof EditTextPreference)
						{
							editor.putString(name, value);
							EditTextPreference myPrefText = (EditTextPreference) myPref;
							myPrefText.setText(value);
						} else
						{
							if (myPref instanceof CheckBoxPreference)
							{
								Boolean valueBool = Boolean.parseBoolean(value);
								editor.putBoolean(name, valueBool);
								CheckBoxPreference CheckBoxPreference = (CheckBoxPreference) myPref;
								CheckBoxPreference.setChecked(valueBool);
							}
						}
						break;

				}
				eventType = parser.next();
			}
			editor.commit();
			Toast.makeText(getApplicationContext(), "Chargement de \n" + filePath + " réussi.", Toast.LENGTH_SHORT).show();
			return true;
		}
		catch (FileNotFoundException e)
		{
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
			return false;
		}
		catch (IOException e)
		{
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
			return false;
		}
		catch (Exception e)
		{
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
			return false;
		}
	}

	private Boolean SavePreferencesToFile(String filePath, boolean displayInfos)
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
			if (displayInfos)
				Toast.makeText(getApplicationContext(), "Sauvegarde de \n" + filePath + " réussie.", Toast.LENGTH_LONG).show();
			return true;
		}
		catch (Exception e)
		{
			Toast.makeText(getApplicationContext(), "Sauvegarde: " + filePath + "\n" + e.toString(), Toast.LENGTH_LONG).show();
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
