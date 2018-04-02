package Region.FRXX.ClinicomLink.cli.PatS;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

/**
 * Created by lbogni on 13/02/2018.
 */

public class ClassPatientSearchIdentity implements KvmSerializable
{
	public String FirstName;

	public String LastName;

	public String DateOfBirth;

	public String Sex;

	public ClassPatientSearchIdentity()
	{

	}

	public ClassPatientSearchIdentity(SoapObject soapObject)
	{
		try
		{
			this.FirstName = soapObject.getProperty("FirstName").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.LastName = soapObject.getProperty("LastName").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.DateOfBirth = soapObject.getProperty("DateOfBirth").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.Sex = soapObject.getProperty("Sex").toString();
		}
		catch (Exception e)
		{
		}
	}

	public int getPropertyCount()
	{
		return 4;
	}

	public Object getProperty(int arg0)
	{
		switch (arg0)
		{
			case 0:
				return FirstName;
			case 1:
				return LastName;
			case 2:
				return DateOfBirth;
			case 3:
				return Sex;
			default:
				return null;
		}
	}

	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
	{
		switch (index)
		{
			case 0:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "FirstName";
				break;
			case 1:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "LastName";
				break;
			case 2:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "DateOfBirth";
				break;
			case 3:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "Sex";
				break;
		}
	}

	public void setProperty(int index, Object value)
	{
		switch (index)
		{
			case 0:
				FirstName = value.toString();
				break;
			case 1:
				LastName = value.toString();
				break;
			case 2:
				DateOfBirth = value.toString();
				break;
			case 3:
				Sex = value.toString();
				break;
		}
	}
}

