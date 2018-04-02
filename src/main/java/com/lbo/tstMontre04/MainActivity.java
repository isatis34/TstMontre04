package com.lbo.tstMontre04;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ExpandableListView;
import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.net.Uri;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault12;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.EOFException;
import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Region.FRXX.ClinicomLink.cli.Pat.ClassListofPatientsAppt;
import Region.FRXX.ClinicomLink.cli.Pat.ClassPatientAppt;
import Region.FRXX.ClinicomLink.cli.PatS.ClassPatientSearchByRBResource;
import Region.FRXX.ClinicomLink.cli.PatS.ClassPatientSearchIdentity;
import Region.FRXX.ClinicomLink.cli.Wrd.ClassContext;
import Region.FRXX.ClinicomLink.cli.Pat.ClassListofPatients;
import Region.FRXX.ClinicomLink.cli.Pat.ClassPatient;

public class MainActivity extends AppCompatActivity
{
	public static MainActivity Instance;
	private Boolean Debug_WS_preference = true;
	final static String Address = "172.24.76.197";
	final static String Port_Web = "57772";
	final static String NameSpace = "lbo";

	private ExpandableListView LVPatients = null;
	private String SnackbarText = "";
	private ProgressDialog progressDialog;
	private SwipeRefreshLayout gridSwipeRefresh;

	private PatientAdapter PatientAdapter = null;
	private PatientApptAdapter PatientApptAdapter = null;

	private EditText textDateStart = null;
	private EditText textDateEnd = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		try
		{
			this.Instance =this;

			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);

			FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
			fab.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					Snackbar.make(view, SnackbarText, Snackbar.LENGTH_LONG)
							.setAction("Action", null).show();
				}
			});
			/*if (!WaitForWifiConnexion())
			{
				finish();
				System.exit(0);
			}*/

			if (!isNetworkAvailable())
			{
				Log.d(this.getClass().getPackage().toString(), "Réseau non connecté");

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.dialog_message_NetworkNotConnected)
						.setTitle(R.string.dialog_title_NetworkNotConnected)
						.setPositiveButton("OK", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								finish();
								System.exit(0);
							}
						})
				;
				AlertDialog dialog = builder.create();
				dialog.show();
				return;
			}
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
			if (sharedPrefs.getBoolean("prefUseCisco", false))
			{
				try
				{
					Intent anyConnectIntent = new Intent(
							"com.cisco.anyconnect.vpn.android.PRIMARY_ACTIVITY_ACTION_CONNECT_INTENT");

		/*anyConnectIntent.putExtra(
				"com.cisco.anyconnect.vpn.android.CONNECTION_ACTIVITY_CONNECT_KEY",
				"anyconnect://create?name=iscmtp&host=sslvpn.intersystems.com");*/

		/*anyConnectIntent.putExtra(
				"com.cisco.anyconnect.vpn.android.CONNECTION_ACTIVITY_CONNECT_KEY",
				"anyconnect:connect?name=sslvpn.intersystems.com&prefill_username=iscinternal\\lbogni&prefill_password=Screwy!06");*/

			/*anyConnectIntent.putExtra(
					"com.cisco.anyconnect.vpn.android.CONNECTION_ACTIVITY_CONNECT_KEY",
					"anyconnect://connect/?name=a"); //&host=sslvpn.intersystems.com&prefill_username=lbogni&prefill_password=Screwy%2106&prefill_group_list=Remote");*/
					anyConnectIntent.putExtra(
							"com.cisco.anyconnect.vpn.android.CONNECTION_ACTIVITY_CONNECT_KEY",
							"anyconnect://connect/?name=a&host=sslvpn.intersystems.com&prefill_username=lbogni&prefill_password=Screwy%2106&prefill_group_list=Remote");
					//String aa = anyConnectIntent.getStringExtra("com.cisco.anyconnect.vpn.android.CONNECTION_ACTIVITY_CONNECT_KEY");
					startActivity(anyConnectIntent);
				}
				catch (Exception exc)
				{
					Log.d(this.getClass().getPackage().toString(), exc.toString());
				}
			}

			// region Gestion textDateStart
			textDateStart = (EditText) findViewById(R.id.textDateStart);
			textDateStart.setInputType(InputType.TYPE_NULL);
			textDateStart.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					final Calendar cldr = Calendar.getInstance();
					int day = cldr.get(Calendar.DAY_OF_MONTH);
					int month = cldr.get(Calendar.MONTH);
					int year = cldr.get(Calendar.YEAR);
					// date picker dialog
					DatePickerDialog picker = new DatePickerDialog(MainActivity.this,
							new DatePickerDialog.OnDateSetListener()
							{
								@Override
								public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
								{
									SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yy");
									textDateStart.setText(format1.format(cldr.getTime()));
								}
							}, year, month, day);
					picker.show();
				}
			});
			// endregion
			// region Gestion textDateEnd
			textDateEnd = (EditText) findViewById(R.id.textDateEnd);
			textDateEnd.setInputType(InputType.TYPE_NULL);
			textDateEnd.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					final Calendar cldr = Calendar.getInstance();
					int day = cldr.get(Calendar.DAY_OF_MONTH);
					int month = cldr.get(Calendar.MONTH);
					int year = cldr.get(Calendar.YEAR);
					// date picker dialog
					DatePickerDialog picker = new DatePickerDialog(MainActivity.this,
							new DatePickerDialog.OnDateSetListener()
							{
								@Override
								public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
								{
									SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yy");
									textDateEnd.setText(format1.format(cldr.getTime()));
								}
							}, year, month, day);
					picker.show();
				}
			});
			// endregion

			Button btnShowDateEnd = (Button) findViewById(R.id.btnShowDateEnd);
			btnShowDateEnd.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					LinearLayout buttonsDateEnd = (LinearLayout) findViewById(R.id.buttonsDateEnd);
					if (buttonsDateEnd.getVisibility() == View.VISIBLE)
						buttonsDateEnd.setVisibility(View.GONE);
					else
						buttonsDateEnd.setVisibility(View.VISIBLE);
				}
			});

			LVPatients = (ExpandableListView) findViewById(R.id.listPatients);
			registerForContextMenu(LVPatients);
			LVPatients.setClickable(true);
			gridSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
			gridSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

				@Override
				public void onRefresh() {

					RefreshListPatients();
				}
			});
			LVPatients.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
				{

					Object o = LVPatients.getItemAtPosition(position);
					launchApptInfosActivity(o);
				}
			});
			PatientApptAdapter = new PatientApptAdapter(MainActivity.this, null);
			PatientAdapter = new PatientAdapter(MainActivity.this, null);

			//LVPatients.setAdapter(PatientAdapter);
			//RefreshListPatients();

			LVPatients.setAdapter(PatientApptAdapter);
			//GetAllPatientsByResource();
			RefreshListPatients();
		}
		catch (Exception e)
		{
			Log.d(this.getClass().getPackage().toString(), e.getMessage());
		}
	}

	private boolean WaitForWifiConnexion()
	{
		//return true;
		try
		{

			Message Message = handler.obtainMessage();
			Bundle Bundle = new Bundle();
			Bundle.putString("ERROR_TEXT", "");
			Bundle.putString("ACTION", "TEST_NETWORK");
			Message.setData(Bundle);

			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm == null)
				return false;
			NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifiNetwork != null)
			{
				if (!wifiNetwork.isConnected())
				{
					WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
					wifiManager.reconnect();
				}
				long mseconds = System.currentTimeMillis();
				while (!wifiNetwork.isConnected())
				{
					try
					{
						Thread.sleep(10);
						if ((System.currentTimeMillis() - mseconds) >= 30000)
							break;
					}
					catch (InterruptedException e)
					{
						Bundle.putString("ERROR_TEXT", e.getMessage());
					}
				}
				if (wifiNetwork.isConnected())
				{
					try
					{

						InetAddress inet;
						try
						{
							inet = InetAddress.getByName(Address);
							if (!inet.isReachable(2000))
								throw new Exception(Address + " n'est pas joignable.");

						}
						catch (Exception e)
						{
							Bundle.putString("ERROR_TEXT", e.getMessage());
							return false;
						}
						try
						{
							Socket socket = new Socket();
							socket.connect(new InetSocketAddress(Address, Integer.parseInt(Port_Web)), 2000);
							socket.close();
							return true;
						}
						catch (Exception e)
						{
							Bundle.putString("ERROR_TEXT", e.getMessage());
							return false;
						}
					}
					finally
					{
						handler.sendMessage(Message);
					}
				} else
					return false;
			}
			return false;
		}
		catch (Exception e)
		{
			Log.d(this.getClass().getPackage().toString(), e.getMessage());
		}
		return false;
	}

	private void launchApptInfosActivity(Object o)
	{
		try
		{
			Intent intent = new Intent(this, ApptInfosActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("ClassPatient", (Serializable) o);
			intent.putExtras(bundle);
			this.startActivity(intent);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.listPatients)
		{
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.menu_list, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		try
		{
			ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
			switch (item.getItemId())
			{
				case R.id.Action_Infos_RDV:
				{
					Object o = LVPatients.getItemAtPosition((int) info.id);
					launchApptInfosActivity(o);
					return true;
				}
				case R.id.Action2:
					return true;
				case R.id.Refresh:
					RefreshListPatients();
					// remove stuff here
					return true;
				default:
					return super.onContextItemSelected(item);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

	private boolean RefreshListPatients()
	{
		PatientApptAdapter.clearAdapter();

		SetBusy(true, "Appel serveur", "Chargement des RDVs");
		BackgroundLoadPatientsByRessourcesThread BackgroundLoadPatientsByRessourcesThread = new BackgroundLoadPatientsByRessourcesThread();
		BackgroundLoadPatientsByRessourcesThread.setRunning(true, "DCD1", "Ben B. JACOBS", "01/01/2011", "08/09/2014");
		BackgroundLoadPatientsByRessourcesThread.start();
		return true;

	}

	private void SetBusy(Boolean busy, String title, String text)
	{
		setProgressBarIndeterminateVisibility(busy);

		if (busy)
		{
			if (progressDialog != null)
			{
				progressDialog.setTitle(title);
				progressDialog.setMessage(text);
				progressDialog.show();
			} else
				progressDialog = ProgressDialog.show(this, title, text);
		} else if (progressDialog != null)
		{
			gridSwipeRefresh.setRefreshing(false);//
			progressDialog.dismiss();
		}
	}

	private Boolean GetAllPatientsByName()
	{
		String CLASSNAME = "Region.FRXX.ClinicomLink.WebServices.Wrd.ClassDocumentsServices";
		final String METHOD_NAME = "SearchIdentity";
		final String NAMESPACE = "http://www.issas.fr";
		final String SOAP_ACTION = String.format("%s/%s.%s", NAMESPACE, CLASSNAME, METHOD_NAME);
		// "http://172.24.76.207:8973/csp/dsb-dvp-650/com.siemens.med.hs.WebServices.pats.ClassPatientSearchWS.cls";
		final String URL = String.format("http://%s:%s/csp/%s/%s.cls", Address, Port_Web, NameSpace, CLASSNAME);
		PropertyInfo pi;

		PatientAdapter.setPatients(null);

		Message Message = handler.obtainMessage();
		Bundle Bundle = new Bundle();
		Bundle.putString("ACTION", METHOD_NAME.toUpperCase());
		Message.setData(Bundle);

		try
		{
			SnackbarText = "";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

			ClassContext ClassContext = new ClassContext();
			pi = new PropertyInfo();
			pi.setName("ClsINOUTContext");
			pi.setValue(ClassContext);
			pi.setType(ClassContext.class);
			request.addProperty(pi);

			ClassPatientSearchIdentity ClassPatientSearchIdentity = new ClassPatientSearchIdentity();
			ClassPatientSearchIdentity.FirstName = "ROBERT";

			pi = new PropertyInfo();
			pi.setName("ClsINPatientSearchIdentity");
			pi.setValue(ClassPatientSearchIdentity);
			pi.setType(ClassPatientSearchIdentity.getClass());
			request.addProperty(pi);

			ClassListofPatients ClassListofPatients = new ClassListofPatients();
			pi = new PropertyInfo();
			pi.setName("ClsOUTListofPatients");
			pi.setValue(ClassListofPatients);
			pi.setType(ClassListofPatients.getClass());
			request.addProperty(pi);

			/*
			 * Set the web service envelope
			 */
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = false;
			envelope.avoidExceptionForUnknownProperty = true;
			envelope.setOutputSoapObject(request);
			envelope.setAddAdornments(false);

			envelope.addMapping(NAMESPACE, "ClassContext", ClassContext.class);
			envelope.addMapping(NAMESPACE, "ClassPatientSearchIdentity", ClassPatientSearchIdentity.class);
			envelope.addMapping(NAMESPACE, "ClassListofPatients", ClassListofPatients.class);
			envelope.addMapping(NAMESPACE, "ClassPatient", ClassPatient.class);

			HttpTransportSE ht = new HttpTransportSE(URL);

			org.ksoap2.transport.ServiceConnection ServiceConnection = ht.getServiceConnection();
			ServiceConnection.setRequestProperty("Expect", "100-continue");
			ServiceConnection.setRequestProperty("KeepAlive", "false");
			ServiceConnection.setRequestProperty("Connection", "close");

			ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
			headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

			ht.debug = Debug_WS_preference;
			StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
			StrictMode.setThreadPolicy(tp);
			Log.d(this.getClass().getPackage().toString(), "Before call WS (URL: " + URL + ")");
			ht.call(SOAP_ACTION, envelope, headerPropertyArrayList);
			Log.d(this.getClass().getPackage().toString(), "After call WS");
			if (ht.debug)
			{
				Log.d(this.getClass().getPackage().toString(), ht.requestDump);
				Log.d(this.getClass().getPackage().toString(), ht.responseDump);
			}
			if (envelope.bodyIn instanceof SoapFault12)
			{
				String str = ((SoapFault12) envelope.bodyIn).faultstring;
				Log.d(this.getClass().getPackage().toString(), str);
				Bundle.putString("ERROR_TITLE", "Erreur " + METHOD_NAME);
				Message.setData(Bundle);
				Bundle.putString("ERROR_TEXT", str);
				Message.setData(Bundle);
				handler.sendMessage(Message);
				return false;
			}
			SoapObject response = (SoapObject) envelope.bodyIn;

			Boolean Return = Boolean.parseBoolean(response.getPropertyAsString(METHOD_NAME + "Result"));
			ClassContext ClassContextResult = new ClassContext(
					(SoapObject) response.getProperty("ClsINOUTContext"));

			if ((ClassContextResult.Error != null) && (!ClassContextResult.Error.isEmpty()))
			{

				Bundle.putString("ERROR_TITLE", "Erreur WS");
				Message.setData(Bundle);
				Bundle.putString("ERROR_TEXT", ClassContextResult.Error);
				Message.setData(Bundle);
				handler.sendMessage(Message);

				return false;
			}
			ClassListofPatients = new ClassListofPatients(
					(SoapObject) response.getProperty("ClsOUTListofPatients"));
			//Bundle.putString("ERROR_TITLE", "Reset Arduino");
			//Message.setData(Bundle);
			//Bundle.putString("ERROR_TEXT", "Reset Arduino lancé");
			//Message.setData(Bundle);
			Bundle.putSerializable("PATIENTS", ClassListofPatients);
			Message.setData(Bundle);
			handler.sendMessage(Message);

			return true;

		}
		catch (SoapFault12 e)
		{
			Log.d(this.getClass().getPackage().toString(), e.getMessage());
			Bundle.putString("ERROR_TITLE", "Erreur " + METHOD_NAME);
			Message.setData(Bundle);
			Bundle.putString("ERROR_TEXT", e.getMessage());
			Message.setData(Bundle);
			handler.sendMessage(Message);
			return false;
		}
		catch (final EOFException e)
		{
			Log.d(this.getClass().getPackage().toString(), "EOFException");
			Bundle.putString("ERROR_TITLE", "Erreur " + METHOD_NAME);
			Message.setData(Bundle);
			Bundle.putString("ERROR_TEXT", "EOFException");
			Message.setData(Bundle);
			handler.sendMessage(Message);
			return false;
		}
		catch (final Exception e)
		{
			Log.d(this.getClass().getPackage().toString(), e.getMessage());
			Bundle.putString("ERROR_TITLE", "Erreur " + METHOD_NAME);
			Message.setData(Bundle);
			Bundle.putString("ERROR_TEXT", e.getMessage());
			Message.setData(Bundle);
			handler.sendMessage(Message);
			return false;
		}
	}

	private Boolean GetAllPatientsByResource()
	{
		String CLASSNAME = "Region.FRXX.ClinicomLink.WebServices.Wrd.ClassDocumentsServices";
		final String METHOD_NAME = "SearchByRBResource";
		final String NAMESPACE = "http://www.issas.fr";
		final String SOAP_ACTION = String.format("%s/%s.%s", NAMESPACE, CLASSNAME, METHOD_NAME);
		// "http://172.24.76.207:8973/csp/dsb-dvp-650/com.siemens.med.hs.WebServices.pats.ClassPatientSearchWS.cls";
		final String URL = String.format("http://%s:%s/csp/%s/%s.cls", Address, Port_Web, NameSpace, CLASSNAME);
		PropertyInfo pi;

		PatientAdapter.setPatients(null);

		Message Message = handler.obtainMessage();
		Bundle Bundle = new Bundle();
		Bundle.putString("ACTION", METHOD_NAME.toUpperCase());
		Message.setData(Bundle);

		try
		{
			SnackbarText = "";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

			ClassContext ClassContext = new ClassContext();
			pi = new PropertyInfo();
			pi.setName("ClsINOUTContext");
			pi.setValue(ClassContext);
			pi.setType(ClassContext.class);
			request.addProperty(pi);

			ClassPatientSearchByRBResource ClassPatientSearchByRBResource = new ClassPatientSearchByRBResource();
			ClassPatientSearchByRBResource.Location = "DCD1";
			ClassPatientSearchByRBResource.ResourceCode = "Ben B. JACOBS";
			ClassPatientSearchByRBResource.StartDate = "01/01/2011";
			ClassPatientSearchByRBResource.EndDate = "08/09/2014";

			pi = new PropertyInfo();
			pi.setName("ClsINPatientSearchByRBResource");
			pi.setValue(ClassPatientSearchByRBResource);
			pi.setType(ClassPatientSearchByRBResource.getClass());
			request.addProperty(pi);

			ClassListofPatientsAppt ClassListofPatientsAppt = new ClassListofPatientsAppt();
			pi = new PropertyInfo();
			pi.setName("ClsOUTListofPatients");
			pi.setValue(ClassListofPatientsAppt);
			pi.setType(ClassListofPatientsAppt.getClass());
			request.addProperty(pi);

			/*
			 * Set the web service envelope
			 */
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = false;
			envelope.avoidExceptionForUnknownProperty = true;
			envelope.setOutputSoapObject(request);
			envelope.setAddAdornments(false);

			envelope.addMapping(NAMESPACE, "ClassContext", ClassContext.class);
			envelope.addMapping(NAMESPACE, "ClassPatientSearchByRBResource", ClassPatientSearchByRBResource.class);
			envelope.addMapping(NAMESPACE, "ClassListofPatients", ClassListofPatientsAppt.class);
			envelope.addMapping(NAMESPACE, "ClassPatientAppt", ClassPatientAppt.class);

			HttpTransportSE ht = new HttpTransportSE(URL);

			org.ksoap2.transport.ServiceConnection ServiceConnection = ht.getServiceConnection();
			ServiceConnection.setRequestProperty("Expect", "100-continue");
			ServiceConnection.setRequestProperty("KeepAlive", "false");
			ServiceConnection.setRequestProperty("Connection", "close");

			ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
			headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

			ht.debug = Debug_WS_preference;
			StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
			StrictMode.setThreadPolicy(tp);
			Log.d(this.getClass().getPackage().toString(), "Before call WS (URL: " + URL + ")");
			ht.call(SOAP_ACTION, envelope, headerPropertyArrayList);
			Log.d(this.getClass().getPackage().toString(), "After call WS");
			if (ht.debug)
			{
				Log.d(this.getClass().getPackage().toString(), ht.requestDump);
				Log.d(this.getClass().getPackage().toString(), ht.responseDump);
			}
			if (envelope.bodyIn instanceof SoapFault12)
			{
				String str = ((SoapFault12) envelope.bodyIn).faultstring;
				Log.d(this.getClass().getPackage().toString(), str);
				Bundle.putString("ERROR_TITLE", "Erreur " + METHOD_NAME);
				Message.setData(Bundle);
				Bundle.putString("ERROR_TEXT", str);
				Message.setData(Bundle);
				handler.sendMessage(Message);
				return false;
			}
			SoapObject response = (SoapObject) envelope.bodyIn;

			Boolean Return = Boolean.parseBoolean(response.getPropertyAsString(METHOD_NAME + "Result"));
			ClassContext ClassContextResult = new ClassContext(
					(SoapObject) response.getProperty("ClsINOUTContext"));

			if ((ClassContextResult.Error != null) && (!ClassContextResult.Error.isEmpty()))
			{

				Bundle.putString("ERROR_TITLE", "Erreur WS");
				Message.setData(Bundle);
				Bundle.putString("ERROR_TEXT", ClassContextResult.Error);
				Message.setData(Bundle);
				handler.sendMessage(Message);

				return false;
			}
			ClassListofPatientsAppt = new ClassListofPatientsAppt(
					(SoapObject) response.getProperty("ClsOUTListofPatients"));
			//Bundle.putString("ERROR_TITLE", "Reset Arduino");
			//Message.setData(Bundle);
			//Bundle.putString("ERROR_TEXT", "Reset Arduino lancé");
			//Message.setData(Bundle);
			Bundle.putSerializable("PATIENTS_APPT", ClassListofPatientsAppt);
			Message.setData(Bundle);
			handler.sendMessage(Message);

			return true;

		}
		catch (SoapFault12 e)
		{
			Log.d(this.getClass().getPackage().toString(), e.getMessage());
			Bundle.putString("ERROR_TITLE", "Erreur " + METHOD_NAME);
			Message.setData(Bundle);
			Bundle.putString("ERROR_TEXT", e.getMessage());
			Message.setData(Bundle);
			handler.sendMessage(Message);
			return false;
		}
		catch (final EOFException e)
		{
			Log.d(this.getClass().getPackage().toString(), "EOFException");
			Bundle.putString("ERROR_TITLE", "Erreur " + METHOD_NAME);
			Message.setData(Bundle);
			Bundle.putString("ERROR_TEXT", "EOFException");
			Message.setData(Bundle);
			handler.sendMessage(Message);
			return false;
		}
		catch (final Exception e)
		{
			Log.d(this.getClass().getPackage().toString(), e.getMessage());
			Bundle.putString("ERROR_TITLE", "Erreur " + METHOD_NAME);
			Message.setData(Bundle);
			Bundle.putString("ERROR_TEXT", e.getMessage());
			Message.setData(Bundle);
			handler.sendMessage(Message);
			return false;
		}
	}

	private boolean isNetworkAvailable()
	{
		ConnectivityManager connectivityManager
				= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{

			boolean retry = true;
			while (retry)
			{
				try
				{
					Bundle Bundle = msg.getData();
					if (Bundle.getString("ERROR_TEXT") != null)
					{
						/*if (timer != null)
						{
							timer.cancel();
							SetControlActive(true);
						}*/
						SetBusy(false, "", "");
						showDialogAlertDefault(Bundle.getString("ERROR_TITLE"), Bundle.getString("ERROR_TEXT"), false);
						return;
					}
					if (Bundle.getString("ACTION") != null)
					{
						if (Bundle.getString("ACTION").equalsIgnoreCase("SEARCHIDENTITY"))
						{
							ClassListofPatients ClassPatientsResult = (ClassListofPatients) Bundle
									.getSerializable("PATIENTS");
							displayPatients(ClassPatientsResult);
							return;
						}
						if (Bundle.getString("ACTION").equalsIgnoreCase("FETCH_PATIENTS_BY_RESSOURCE"))
						{
							SetBusy(false, "", "");
							ClassListofPatientsAppt ClassListofPatientsAppt = (ClassListofPatientsAppt) Bundle
									.getSerializable("PATIENTS_APPT");
							displayPatients(ClassListofPatientsAppt);
							return;
						}
						if (Bundle.getString("ACTION").equalsIgnoreCase("TEST_NETWORK"))
						{
							if ((Bundle.getString("ERROR") == null) || (Bundle.getString("ERROR").isEmpty()))
							{
								SetBusy(false, "", "");
							} else
							{
								SetBusy(false, "", "");
								showDialogAlertDefault("Erreur handleMessage", Bundle.getString("ERROR"), false);
							}
						}
					}

					showDialogAlertDefault("Erreur handleMessage", "Message inconnu: " + Bundle.getString("ACTION"), false);
					retry = false;
				}
				catch (Exception e)
				{
					showDialogAlertDefault("Erreur handleMessage", e.getMessage(), false);
				}
			}
		}
	};

	private void displayPatients(ClassListofPatientsAppt patients)
	{
		try
		{
			SnackbarText = patients.size() + " RDV" + (patients.size() > 1 ? "s" : "");
			PatientApptAdapter.setPatients(patients);
		}
		catch (Exception e)
		{
			showDialogAlertDefault("Erreur", e.toString(), false);
		}
	}

	protected void displayPatients(ClassListofPatients patients)
	{
		try
		{
			SnackbarText = patients.size() + " RDV" + (patients.size() > 1 ? "s" : "");
			PatientAdapter.setPatients(patients);
		}
		catch (Exception e)
		{
			showDialogAlertDefault("Erreur", e.toString(), false);
		}
	}

	protected void showDialogAlertDefault(String title, String message, boolean terminate)
	{
		// SetBusy(false, "", "");
		AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(MainActivity.this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		if (terminate)
		{
			alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
			{
				@Override
				public void onDismiss(DialogInterface dialog)
				{
					finish();
					System.exit(0);
				}
			});
		}
		alertDialog.show();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		switch (id)
		{
			case R.id.action_settings:
			{
				startActivity(new Intent(this, PrefsActivity.class));
				return true;
			}
			case R.id.menu_refresh:
			{
				RefreshListPatients();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	public class BackgroundLoadPatientsByRessourcesThread extends Thread
	{
		volatile boolean running = false;
		volatile int cnt;
		volatile String CodeRessource;
		volatile String Location;
		volatile String StartDate;
		volatile String EndDate;

		void setRunning(boolean b, String location, String codeRessource, String startDate, String endDate)
		{
			running = b;
			cnt = 10;
			Location = location;
			CodeRessource = codeRessource;
			StartDate = startDate;
			EndDate = endDate;
		}

		@Override
		public void run()
		{
			Message Message = handler.obtainMessage();
			Bundle Bundle = new Bundle();
			Bundle.putString("ACTION", "FETCH_PATIENTS_BY_RESSOURCE");
			Message.setData(Bundle);
			try
			{
				String CLASSNAME = "Region.FRXX.ClinicomLink.WebServices.Wrd.ClassDocumentsServices";
				final String METHOD_NAME = "SearchByRBResource";
				final String NAMESPACE = "http://www.issas.fr";
				final String SOAP_ACTION = String.format("%s/%s.%s", NAMESPACE, CLASSNAME, METHOD_NAME);
				// "http://172.24.76.207:8973/csp/dsb-dvp-650/com.siemens.med.hs.WebServices.pats.ClassPatientSearchWS.cls";
				final String URL = String.format("http://%s:%s/csp/%s/%s.cls", Address, Port_Web, NameSpace, CLASSNAME);
				PropertyInfo pi;

				PatientAdapter.setPatients(null);

				try
				{
					SnackbarText = "";
					SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

					ClassContext ClassContext = new ClassContext();
					pi = new PropertyInfo();
					pi.setName("ClsINOUTContext");
					pi.setValue(ClassContext);
					pi.setType(ClassContext.class);
					request.addProperty(pi);

					ClassPatientSearchByRBResource ClassPatientSearchByRBResource = new ClassPatientSearchByRBResource();
					ClassPatientSearchByRBResource.Location = "DCD1";
					ClassPatientSearchByRBResource.ResourceCode = "Ben B. JACOBS";
					ClassPatientSearchByRBResource.StartDate = "01/01/2011";
					ClassPatientSearchByRBResource.EndDate = "08/09/2014";

					pi = new PropertyInfo();
					pi.setName("ClsINPatientSearchByRBResource");
					pi.setValue(ClassPatientSearchByRBResource);
					pi.setType(ClassPatientSearchByRBResource.getClass());
					request.addProperty(pi);

					ClassListofPatientsAppt ClassListofPatientsAppt = new ClassListofPatientsAppt();
					pi = new PropertyInfo();
					pi.setName("ClsOUTListofPatients");
					pi.setValue(ClassListofPatientsAppt);
					pi.setType(ClassListofPatientsAppt.getClass());
					request.addProperty(pi);

					/*
					 * Set the web service envelope
					 */
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
					envelope.dotNet = false;
					envelope.avoidExceptionForUnknownProperty = true;
					envelope.setOutputSoapObject(request);
					envelope.setAddAdornments(false);

					envelope.addMapping(NAMESPACE, "ClassContext", ClassContext.class);
					envelope.addMapping(NAMESPACE, "ClassPatientSearchByRBResource", ClassPatientSearchByRBResource.class);
					envelope.addMapping(NAMESPACE, "ClassListofPatients", ClassListofPatientsAppt.class);
					envelope.addMapping(NAMESPACE, "ClassPatientAppt", ClassPatientAppt.class);

					HttpTransportSE ht = new HttpTransportSE(URL);

					org.ksoap2.transport.ServiceConnection ServiceConnection = ht.getServiceConnection();
					ServiceConnection.setRequestProperty("Expect", "100-continue");
					ServiceConnection.setRequestProperty("KeepAlive", "false");
					ServiceConnection.setRequestProperty("Connection", "close");

					ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
					headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

					ht.debug = Debug_WS_preference;
					StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
					StrictMode.setThreadPolicy(tp);
					Log.d(this.getClass().getPackage().toString(), "Before call WS (URL: " + URL + ")");
					ht.call(SOAP_ACTION, envelope, headerPropertyArrayList);
					Log.d(this.getClass().getPackage().toString(), "After call WS");
					if (ht.debug)
					{
						Log.d(this.getClass().getPackage().toString(), ht.requestDump);
						Log.d(this.getClass().getPackage().toString(), ht.responseDump);
					}
					if (envelope.bodyIn instanceof SoapFault12)
					{
						String str = ((SoapFault12) envelope.bodyIn).faultstring;
						Log.d(this.getClass().getPackage().toString(), str);
						Bundle.putString("ERROR_TITLE", "Erreur " + METHOD_NAME);
						Message.setData(Bundle);
						Bundle.putString("ERROR_TEXT", str);
						Message.setData(Bundle);
						handler.sendMessage(Message);
						return;
					}
					SoapObject response = (SoapObject) envelope.bodyIn;

					Boolean Return = Boolean.parseBoolean(response.getPropertyAsString(METHOD_NAME + "Result"));
					ClassContext ClassContextResult = new ClassContext(
							(SoapObject) response.getProperty("ClsINOUTContext"));

					if ((ClassContextResult.Error != null) && (!ClassContextResult.Error.isEmpty()))
					{

						Bundle.putString("ERROR_TITLE", "Erreur WS");
						Message.setData(Bundle);
						Bundle.putString("ERROR_TEXT", ClassContextResult.Error);
						Message.setData(Bundle);
						handler.sendMessage(Message);

						return;
					}
					ClassListofPatientsAppt = new ClassListofPatientsAppt(
							(SoapObject) response.getProperty("ClsOUTListofPatients"));
					//Bundle.putString("ERROR_TITLE", "Reset Arduino");
					//Message.setData(Bundle);
					//Bundle.putString("ERROR_TEXT", "Reset Arduino lancé");
					//Message.setData(Bundle);
					Bundle.putSerializable("PATIENTS_APPT", ClassListofPatientsAppt);
					Message.setData(Bundle);
					handler.sendMessage(Message);

					return;

				}
				catch (SoapFault12 e)
				{
					Log.d(this.getClass().getPackage().toString(), e.getMessage());
					Bundle.putString("ERROR_TITLE", "Erreur " + METHOD_NAME);
					Message.setData(Bundle);
					Bundle.putString("ERROR_TEXT", e.getMessage());
					Message.setData(Bundle);
					handler.sendMessage(Message);
					return;
				}
				catch (final EOFException e)
				{
					Log.d(this.getClass().getPackage().toString(), "EOFException");
					Bundle.putString("ERROR_TITLE", "Erreur " + METHOD_NAME);
					Message.setData(Bundle);
					Bundle.putString("ERROR_TEXT", "EOFException");
					Message.setData(Bundle);
					handler.sendMessage(Message);
					return;
				}
				catch (final Exception e)
				{
					Log.d(this.getClass().getPackage().toString(), e.getMessage());
					Bundle.putString("ERROR_TITLE", "Erreur " + METHOD_NAME);
					Message.setData(Bundle);
					Bundle.putString("ERROR_TEXT", e.getMessage());
					Message.setData(Bundle);
					handler.sendMessage(Message);
					return;
				}
			}

			catch (final Exception e)
			{
				Bundle.putString("ERROR", e.getMessage());
				Message.setData(Bundle);
				handler.sendMessage(Message);

			}
		}
	}

	public class BackgroundSaveBinaryFileThread extends Thread
	{
		volatile boolean running = false;
		volatile int cnt;
		volatile String IPP;
		volatile String DateType;
		volatile String Filename;
		volatile Uri uri;
		volatile byte[] ByteBinaryData;

		void setRunning(boolean b, String _IPP, String dateType, byte[] byteBinaryData, Uri _uri, String filename)
		{
			running = b;
			cnt = 10;
			IPP = _IPP;
			DateType = dateType;
			ByteBinaryData = byteBinaryData;
			Filename = filename;
			uri = _uri;
		}

		@Override
		public void run()
		{
			Message Message = handler.obtainMessage();
			Bundle Bundle = new Bundle();
			Bundle.putString("ACTION", "SAVE_BINARY_DATA");
			Message.setData(Bundle);
			try
			{
				String CLASSNAME = "Region.FRXX.ClinicomLink.WebServices.Wrd.ClassDocumentsServices";
				final String METHOD_NAME = "SaveBinaryData";
				final String NAMESPACE = "http://www.issas.fr";
				final String SOAP_ACTION = String.format("%s/%s.%s", NAMESPACE, CLASSNAME, METHOD_NAME);
				// "http://172.24.76.207:8973/csp/dsb-dvp-650/com.siemens.med.hs.WebServices.pats.ClassPatientSearchWS.cls";
				final String URL = String.format("http://%s:%s/csp/%s/%s.cls", Address, Port_Web, NameSpace, CLASSNAME);
				PropertyInfo pi;

				try
				{
					SnackbarText = "";
					SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

					ClassContext ClassContext = new ClassContext();
					pi = new PropertyInfo();
					pi.setName("ClsINOUTContext");
					pi.setValue(ClassContext);
					pi.setType(ClassContext.class);
					request.addProperty(pi);

					pi = new PropertyInfo();
					pi.setName("strINIPP");
					pi.setValue(IPP);
					pi.setType(String.class);
					request.addProperty(pi);

					pi = new PropertyInfo();
					pi.setName("INFile");
					pi.setValue(ByteBinaryData);
					pi.setType(ByteBinaryData.getClass());
					request.addProperty(pi);

					/*
					 * Set the web service envelope
					 */
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
					envelope.dotNet = false;
					envelope.avoidExceptionForUnknownProperty = true;
					envelope.setOutputSoapObject(request);
					envelope.setAddAdornments(false);

					envelope.addMapping(NAMESPACE, "ClassContext", ClassContext.class);

					MarshalBase64 marshal = new MarshalBase64();
					marshal.register(envelope);

					HttpTransportSE ht = new HttpTransportSE(URL);

					org.ksoap2.transport.ServiceConnection ServiceConnection = ht.getServiceConnection();
					ServiceConnection.setRequestProperty("Expect", "100-continue");
					ServiceConnection.setRequestProperty("KeepAlive", "false");
					ServiceConnection.setRequestProperty("Connection", "close");

					ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
					headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

					ht.debug = Debug_WS_preference;
					StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
					StrictMode.setThreadPolicy(tp);
					Log.d(this.getClass().getPackage().toString(), "Before call WS (URL: " + URL + ")");
					ht.call(SOAP_ACTION, envelope, headerPropertyArrayList);
					Log.d(this.getClass().getPackage().toString(), "After call WS");
					if (ht.debug)
					{
						Log.d(this.getClass().getPackage().toString(), ht.requestDump);
						Log.d(this.getClass().getPackage().toString(), ht.responseDump);
					}
					if (envelope.bodyIn instanceof SoapFault12)
					{
						String str = ((SoapFault12) envelope.bodyIn).faultstring;
						Log.d(this.getClass().getPackage().toString(), str);
						Bundle.putString("ERROR_TITLE", "Erreur " + METHOD_NAME);
						Message.setData(Bundle);
						Bundle.putString("ERROR_TEXT", str);
						Message.setData(Bundle);
						handler.sendMessage(Message);
						return;
					}
					SoapObject response = (SoapObject) envelope.bodyIn;

					Boolean Return = Boolean.parseBoolean(response.getPropertyAsString(METHOD_NAME + "Result"));
					ClassContext ClassContextResult = new ClassContext(
							(SoapObject) response.getProperty("ClsINOUTContext"));

					if ((ClassContextResult.Error != null) && (!ClassContextResult.Error.isEmpty()))
					{

						Bundle.putString("ERROR_TITLE", "Erreur WS");
						Message.setData(Bundle);
						Bundle.putString("ERROR_TEXT", ClassContextResult.Error);
						Message.setData(Bundle);
						handler.sendMessage(Message);

						return;
					}
					getContentResolver().delete(uri, null, null);
					(new File(Filename)).delete();
				}
				catch (SoapFault12 e)
				{
					Log.d(this.getClass().getPackage().toString(), e.getMessage());
					Bundle.putString("ERROR_TITLE", "Erreur " + METHOD_NAME);
					Message.setData(Bundle);
					Bundle.putString("ERROR_TEXT", e.getMessage());
					Message.setData(Bundle);
					handler.sendMessage(Message);
					return;
				}
				catch (final EOFException e)
				{
					Log.d(this.getClass().getPackage().toString(), "EOFException");
					Bundle.putString("ERROR_TITLE", "Erreur " + METHOD_NAME);
					Message.setData(Bundle);
					Bundle.putString("ERROR_TEXT", "EOFException");
					Message.setData(Bundle);
					handler.sendMessage(Message);
					return;
				}
				catch (final Exception e)
				{
					Log.d(this.getClass().getPackage().toString(), e.getMessage());
					Bundle.putString("ERROR_TITLE", "Erreur " + METHOD_NAME);
					Message.setData(Bundle);
					Bundle.putString("ERROR_TEXT", e.getMessage());
					Message.setData(Bundle);
					handler.sendMessage(Message);
					return;
				}
			}

			catch (final Exception e)
			{
				Bundle.putString("ERROR", e.getMessage());
				Message.setData(Bundle);
				handler.sendMessage(Message);

			}
		}
	}
}
