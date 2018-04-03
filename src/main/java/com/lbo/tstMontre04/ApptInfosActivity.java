package com.lbo.tstMontre04;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import Region.FRXX.ClinicomLink.cli.Pat.ClassPatientAppt;

public class ApptInfosActivity extends AppCompatActivity
{
	private ClassPatientAppt patient = null;
	private int[] colors = new int[]{0xFF007DFF, 0xFFFFD3E3, 0xFF888888};
	private static final int ACTIVITY_RECORD_SOUND = 0;
	private File imageToStore = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		CheckBox CheckBox;
		TextView TextView = null;
		LinearLayout LinearLayout = null;

		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_appt_infos);
		patient = (ClassPatientAppt) getIntent().getSerializableExtra("ClassPatient");

		LinearLayout = (LinearLayout) findViewById(R.id.LLApptInfos);
		if (patient.Sex.equalsIgnoreCase("F"))
			LinearLayout.setBackgroundColor(colors[1]);
		else if (patient.Sex.equalsIgnoreCase("M"))
			LinearLayout.setBackgroundColor(colors[0]);
		else
			LinearLayout.setBackgroundColor(colors[2]);

		TextView = (TextView) findViewById(R.id.nom);
		TextView.setText(patient.FirstName);
		TextView = (TextView) findViewById(R.id.prenom);
		TextView.setText(patient.LastName);
		if ((patient.Sex.equalsIgnoreCase("F")) && (patient.MaidenName != null)
				&& (!patient.MaidenName.isEmpty()))
		{
			TextView = (TextView) findViewById(R.id.nomJF);
			TextView.setText("(" + patient.MaidenName + ")");
			TextView.setVisibility(View.VISIBLE);
		}
		TextView = (TextView) findViewById(R.id.age);
		TextView.setText(patient.Age);
		TextView = (TextView) findViewById(R.id.statusEpisode);

		TextView = (TextView) findViewById(R.id.ApptDTTM);
		TextView.setText(patient.ApptDate + " " + patient.ApptTime);

		TextView = (TextView) findViewById(R.id.ApptStatus);
		TextView.setText(patient.ApptStatus);

		int[] checkboxes = {R.id.checkbox_Arrive, R.id.checkbox_Vu, R.id.checkbox_Parti};
		for (int checkbox : checkboxes)
		{
			CheckBox = (CheckBox) findViewById(checkbox);
			CheckBox.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					onCheckboxClicked(v);
				}
			});
		}
		Button btn_RecordVoice = (Button) findViewById(R.id.btn_RecordVoice);
		btn_RecordVoice.setOnClickListener(new View.OnClickListener()
										   {
											   @Override
											   public void onClick(View v)
											   {

												   try
												   {
													   imageToStore = File.createTempFile("TrakCare_" + patient.IPP + "_", ".voice", Environment.getExternalStorageDirectory());
												   }
												   catch (IOException e)
												   {
													   e.printStackTrace();
												   }
												   Log.d(MainActivity.Instance.getClass().getPackage().toString(), "imageToStore:" + imageToStore);
												   Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
												   intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageToStore));
												   startActivityForResult(intent, ACTIVITY_RECORD_SOUND);
											   }
										   }
		);
	}

	/* Called when the second activity's finished */
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case ACTIVITY_RECORD_SOUND:
				if (resultCode == RESULT_OK)
				{
					Log.d(MainActivity.Instance.getClass().getPackage().toString(), "imageToStore:" + imageToStore);
					Uri uri = data.getData();
					try
					{
						String filePath = getAudioFilePathFromUri(uri);
						SendFile(uri, filePath);
					}
					catch (IOException e)
					{
						throw new RuntimeException(e);
					}

				}
				break;
		}
	}

	private String getAudioFilePathFromUri(Uri uri)
	{
		Cursor cursor = getContentResolver()
				.query(uri, null, null, null, null);
		cursor.moveToFirst();
		int index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
		return cursor.getString(index);
	}

	private void SendFile(Uri uri, String fileName) throws IOException
	{
		byte[] byteBufferString = getByte(fileName);

		MainActivity.Instance.SetBusy(this, true, "Appel serveur", "Envoi du fichier");
		try
		{
			MainActivity.BackgroundSaveBinaryFileThread BackgroundSaveBinaryFileThread = MainActivity.Instance.new BackgroundSaveBinaryFileThread();
			String DataType = (fileName.substring(fileName.lastIndexOf("."))).toUpperCase().replace(".", "");
			BackgroundSaveBinaryFileThread.setRunning(true, "", DataType, byteBufferString, uri, fileName);
			BackgroundSaveBinaryFileThread.start();
		}
		catch (Exception e)
		{
			MainActivity.Instance.SetBusy(this, false, null, null);
		}
	}

	private byte[] getByte(String path)
	{
		byte[] getBytes = {};
		try
		{
			File file = new File(path);
			getBytes = new byte[(int) file.length()];
			InputStream is = new FileInputStream(file);
			is.read(getBytes);
			is.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return getBytes;
	}

	public void onCheckboxClicked(View view)
	{
		// Is the view now checked?
		boolean checked = ((CheckBox) view).isChecked();

		// Check which checkbox was clicked
		switch (view.getId())
		{
			case R.id.checkbox_Arrive:
				if (checked)
				{
				} else
				{
				}
				break;
			case R.id.checkbox_Vu:
				if (checked)
				{
				} else
				{
				}
				break;
			case R.id.checkbox_Parti:
				if (checked)
				{
				} else
				{
				}
				break;
		}
	}

}
