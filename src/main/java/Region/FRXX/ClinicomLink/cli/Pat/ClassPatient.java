package Region.FRXX.ClinicomLink.cli.Pat;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Created by lbogni on 13/02/2018.
 */

public class ClassPatient implements KvmSerializable, Serializable
{
	public String PatientId;

	public String FirstName;

	public String LastName;

	public String MaidenName;

	public String DateOfBirth;

	public String Sex;

	public String IPP;

	public String Age;

	public String Tag;

	public ClassPatient()
	{

	}

	public ClassPatient(SoapObject soapObject)
	{
		try
		{
			this.PatientId = soapObject.getProperty("PatientId").toString();
		}
		catch (Exception e)
		{
		}
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
			this.MaidenName = soapObject.getProperty("MaidenName").toString();
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
		try
		{
			this.IPP = soapObject.getProperty("IPP").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.Age = soapObject.getProperty("Age").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.Tag = soapObject.getProperty("Tag").toString();
		}
		catch (Exception e)
		{
		}
	}

	public int getPropertyCount()
	{
		return 12;
	}

	public Object getProperty(int arg0)
	{
		switch (arg0)
		{
			case 0:
				return PatientId;
			case 1:
				return FirstName;
			case 2:
				return LastName;
			case 3:
				return MaidenName;
			case 4:
				return DateOfBirth;
			case 5:
				return Sex;
			case 6:
				return IPP;
			case 7:
				return Age;
			case 8:
				return Sex;
			case 9:
				return IPP;
			case 10:
				return Age;
			case 11:
				return Tag;
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
				info.name = "PatientId";
				break;
			case 1:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "FirstName";
				break;
			case 2:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "LastName";
				break;
			case 3:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "MaidenName";
				break;
			case 4:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "DateOfBirth";
				break;
			case 5:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "Sex";
				break;
			case 6:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "IPP";
				break;
			case 7:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "Age";
				break;
			case 8:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "Sex";
				break;
			case 9:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "IPP";
				break;
			case 10:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "Age";
				break;
			case 11:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "Tag";
				break;
		}
	}

	public void setProperty(int index, Object value)
	{
		switch (index)
		{
			case 0:
				PatientId = value.toString();
				break;
			case 1:
				FirstName = value.toString();
				break;
			case 2:
				LastName = value.toString();
				break;
			case 3:
				MaidenName = value.toString();
				break;
			case 4:
				DateOfBirth = value.toString();
				break;
			case 5:
				Sex = value.toString();
				break;
			case 6:
				IPP = value.toString();
				break;
			case 7:
				Age = value.toString();
				break;
			case 8:
				Sex = value.toString();
				break;
			case 9:
				IPP = value.toString();
				break;
			case 10:
				Age = value.toString();
				break;
			case 11:
				Tag = value.toString();
				break;
		}
	}
}
