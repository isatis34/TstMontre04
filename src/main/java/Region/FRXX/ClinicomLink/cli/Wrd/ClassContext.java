package Region.FRXX.ClinicomLink.cli.Wrd;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

/**
 * Created by lbogni on 13/02/2018.
 */

public class ClassContext implements KvmSerializable
{
	public String Identifier;

	public String SessionId;

	public String Username;

	public String UserId;

	public String HospitalId;

	public String HospitalCode;

	public String HospitalDesc;

	public String LocationId;

	public String LocationDesc;

	public String ProfileId;

	public String ProfileDesc;

	public String Error;

	public String LanguageId;

	public String Region;

	public String SiteCode;

	public String GroupId;

	public String GroupCode;

	public String Tag;

	public ClassContext()
	{

	}

	public ClassContext(SoapObject soapObject)
	{
		try
		{
			this.Identifier = soapObject.getProperty("Identifier").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.SessionId = soapObject.getProperty("SessionId").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.Username = soapObject.getProperty("Username").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.UserId = soapObject.getProperty("UserId").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.HospitalId = soapObject.getProperty("HospitalId").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.HospitalCode = soapObject.getProperty("HospitalCode").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.HospitalDesc = soapObject.getProperty("HospitalDesc").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.LocationId = soapObject.getProperty("LocationId").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.LocationDesc = soapObject.getProperty("LocationDesc").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.ProfileId = soapObject.getProperty("ProfileId").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.ProfileDesc = soapObject.getProperty("ProfileDesc").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.Error = soapObject.getProperty("Error").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.LanguageId = soapObject.getProperty("LanguageId").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.Region = soapObject.getProperty("Region").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.SiteCode = soapObject.getProperty("SiteCode").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.GroupId = soapObject.getProperty("GroupId").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.GroupCode = soapObject.getProperty("GroupCode").toString();
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
		return 18;
	}

	public Object getProperty(int arg0)
	{
		switch (arg0)
		{
			case 0:
				return Identifier;
			case 1:
				return SessionId;
			case 2:
				return Username;
			case 3:
				return UserId;
			case 4:
				return HospitalId;
			case 5:
				return HospitalCode;
			case 6:
				return HospitalDesc;
			case 7:
				return LocationId;
			case 8:
				return LocationDesc;
			case 9:
				return ProfileId;
			case 10:
				return ProfileDesc;
			case 11:
				return Error;
			case 12:
				return LanguageId;
			case 13:
				return Region;
			case 14:
				return SiteCode;
			case 15:
				return GroupId;
			case 16:
				return GroupCode;
			case 17:
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
				info.name = "Identifier";
				break;
			case 1:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "SessionId";
				break;
			case 2:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "Username";
				break;
			case 3:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "UserId";
				break;
			case 4:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "HospitalId";
				break;
			case 5:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "HospitalCode";
				break;
			case 6:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "HospitalDesc";
				break;
			case 7:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "LocationId";
				break;
			case 8:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "LocationDesc";
				break;
			case 9:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "ProfileId";
				break;
			case 10:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "ProfileDesc";
				break;
			case 11:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "Error";
				break;
			case 12:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "LanguageId";
				break;
			case 13:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "Region";
				break;
			case 14:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "SiteCode";
				break;
			case 15:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "GroupId";
				break;
			case 16:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "GroupCode";
				break;
		}
	}

	public void setProperty(int index, Object value)
	{
		switch (index)
		{
			case 0:
				Identifier = value.toString();
				break;
			case 1:
				SessionId = value.toString();
				break;
			case 2:
				Username = value.toString();
				break;
			case 3:
				UserId = value.toString();
				break;
			case 4:
				HospitalId = value.toString();
				break;
			case 5:
				HospitalCode = value.toString();
				break;
			case 6:
				HospitalDesc = value.toString();
				break;
			case 7:
				LocationId = value.toString();
				break;
			case 8:
				LocationDesc = value.toString();
				break;
			case 9:
				ProfileId = value.toString();
				break;
			case 10:
				ProfileDesc = value.toString();
				break;
			case 11:
				Error = value.toString();
				break;
			case 12:
				LanguageId = value.toString();
				break;
			case 13:
				Region = value.toString();
				break;
			case 14:
				SiteCode = value.toString();
				break;
			case 15:
				GroupId = value.toString();
				break;
			case 16:
				GroupCode = value.toString();
				break;
		}
	}
}
