package Region.FRXX.ClinicomLink.cli.Pat;

import android.util.Log;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by lbogni on 13/02/2018.
 */

public class ClassListofPatients extends Vector<ClassPatient> implements KvmSerializable
{
	private static final long serialVersionUID = 84514037258L;

	public ClassListofPatients()
	{
	}

	public ClassListofPatients(SoapObject soapObject)
	{
		if (soapObject == null) return;
		try
		{
			SoapObject soapObject01 = soapObject;
			for (int i = 0; i < soapObject01.getPropertyCount(); i++)
			{
				SoapObject pii = (SoapObject) soapObject01.getProperty(i);
				ClassPatient obj = new ClassPatient(pii);
				this.add(obj);
			}
		}
		catch (Exception e)
		{
			Log.d("com.lbo.tstMontre04", e.toString());
		}
	}

	public int getPropertyCount()
	{
		return 0;
	}

	public Object getProperty(int arg0)
	{
		switch (arg0)
		{

			default:
				return null;
		}
	}

	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
	{
		switch (index)
		{

			default:
				break;
		}
	}

	public void setProperty(int index, Object value)
	{
		switch (index)
		{

			default:
				break;
		}
	}

}

