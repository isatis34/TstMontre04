package com.lbo.tstMontre04;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
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
import java.util.Date;
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
	private EditTextPreference editTextPreferenceprefServerAddress;
	public static PrefsActivity Instance;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		try
		{
			Instance = this;
			super.onCreate(savedInstanceState);
			// add the xml resource
			addPreferencesFromResource(R.xml.usersettings);

			discoveryResult = new BroadcastReceiver()
			{

				@Override
				public void onReceive(Context context, Intent intent)
				{
					android.util.Log.e("TrackingFlow", "WWWTTTFFF");
					unregisterReceiver(this);
					remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					new Thread(reader).start();
				}
			};
			registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));

			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			if (adapter != null && adapter.isDiscovering())
			{
				adapter.cancelDiscovery();
			}
			adapter.startDiscovery();

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

			button = (Preference) findPreference("button_Receive_Prefs_Via_Bluetooth");
			button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
			{
				@Override
				public boolean onPreferenceClick(Preference arg0)
				{
					try
					{
						PrefsActivity.Instance.ReceivePreferencesViaBluetooth();
						return true;
					}

					catch (final Exception e)
					{
						Toast.makeText(PrefsActivity.this, "Erreur button_Receive_Prefs_Via_Bluetooth:\n" + e.toString(), Toast.LENGTH_LONG).show();
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
		}
		catch (Exception e)
		{
			Log.d(MainActivity.Instance.getClass().getPackage().toString(), e.toString());
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try{unregisterReceiver(discoveryResult);}catch(Exception e){e.printStackTrace();}
		if(socket != null){
			try{
				is.close();
				os.close();
				socket.close();
			}catch(Exception e){}
			CONTINUE_READ_WRITE = false;
		}
	}


	private boolean ReceivePreferencesViaBluetooth()
	{
		registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		new Thread(readerServer).start();
		return true;
	}

	private Boolean SendPreferencesViaBluetooth()
	{
		/*
		try
		{
			String directory = MainActivity.Instance.ApplicationDirectory;
			String filename = "/BTSettings.xml";
			if (SavePreferencesToFile(directory + filename, false))
			{
				File file = new File(directory, filename);
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_SEND);
				intent.setType("image/png");
				intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file) );
				startActivityForResult(intent, ACTIVITY_SEND_FILE);
			}
			return true;
		}
		catch (Exception e)
		{
			return false;
		}*/
		return false;
	}
	private BluetoothDevice remoteDevice;
	private BroadcastReceiver discoveryResult; /*= new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			android.util.Log.e("TrackingFlow", "WWWTTTFFF");
			unregisterReceiver(this);
			remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			new Thread(reader).start();
		}
	};*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == ACTIVITY_SEND_FILE) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {

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

	private boolean CONTINUE_READ_WRITE = true;
	private BluetoothSocket socket;
	private InputStream is;
	private OutputStreamWriter os;

	private Runnable readerServer = new Runnable() {
		public void run() {
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			UUID uuid = UUID.fromString("4e6d48e0-75df-22e3-981f-0800200c8a66");
			try {
				BluetoothServerSocket serverSocket = adapter.listenUsingRfcommWithServiceRecord("BLTServer", uuid);
				android.util.Log.e("TrackingFlow", "Listening...");
				socket = serverSocket.accept();
				android.util.Log.e("TrackingFlow", "Socket accepted...");
				is = socket.getInputStream();
				os = new OutputStreamWriter(socket.getOutputStream());
				new Thread(writter).start();

				int bufferSize = 1024;
				int bytesRead = -1;
				byte[] buffer = new byte[bufferSize];
				//Keep reading the messages while connection is open...
				while(CONTINUE_READ_WRITE){
					final StringBuilder sb = new StringBuilder();
					bytesRead = is.read(buffer);
					if (bytesRead != -1) {
						String result = "";
						while ((bytesRead == bufferSize) && (buffer[bufferSize-1] != 0)){
							result = result + new String(buffer, 0, bytesRead - 1);
							bytesRead = is.read(buffer);
						}
						result = result + new String(buffer, 0, bytesRead - 1);
						sb.append(result);
					}
					android.util.Log.e("TrackingFlow", "Read: " + sb.toString());
					//Show message on UIThread
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(MainActivity.Instance, sb.toString(), Toast.LENGTH_LONG).show();
						}
					});
				}
			} catch (IOException e) {e.printStackTrace();}
		}
	};

	private Runnable writter = new Runnable() {

		@Override
		public void run() {
			int index = 0;
			while(CONTINUE_READ_WRITE){
				try {
					os.write("Message From Server" + (index++) + "\n");
					os.flush();
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	private Runnable reader = new Runnable() {

		@Override
		public void run() {
			try {
				android.util.Log.e("TrackingFlow", "Found: " + remoteDevice.getName());
				UUID uuid = UUID.fromString("4e5d48e0-75df-11e3-981f-0800200c9a66");
				socket = remoteDevice.createRfcommSocketToServiceRecord(uuid);
				socket.connect();
				android.util.Log.e("TrackingFlow", "Connected...");
				os = new OutputStreamWriter(socket.getOutputStream());
				is = socket.getInputStream();
				android.util.Log.e("TrackingFlow", "WWWTTTFFF34243");
				new Thread(writter).start();
				android.util.Log.e("TrackingFlow", "WWWTTTFFF3wwgftggggwww4243: " + CONTINUE_READ_WRITE);
				int bufferSize = 1024;
				int bytesRead = -1;
				byte[] buffer = new byte[bufferSize];
				//Keep reading the messages while connection is open...
				while(CONTINUE_READ_WRITE){
					android.util.Log.e("TrackingFlow", "WWWTTTFFF3wwwww4243");
					final StringBuilder sb = new StringBuilder();
					bytesRead = is.read(buffer);
					if (bytesRead != -1) {
						String result = "";
						while ((bytesRead == bufferSize) && (buffer[bufferSize-1] != 0)){
							result = result + new String(buffer, 0, bytesRead - 1);
							bytesRead = is.read(buffer);
						}
						result = result + new String(buffer, 0, bytesRead - 1);
						sb.append(result);
					}

					android.util.Log.e("TrackingFlow", "Read: " + sb.toString());

					//Show message on UIThread
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(MainActivity.Instance, sb.toString(), Toast.LENGTH_LONG).show();
						}
					});
				}
			} catch (IOException e) {e.printStackTrace();}
		}
	};

	/*private Runnable writter = new Runnable() {

		@Override
		public void run() {
			int index = 0;
			while (CONTINUE_READ_WRITE) {
				try {
					os.write("Message From Client" + (index++) + "\n");
					os.flush();
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};*/
}
