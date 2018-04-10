package Region.FRXX.ClinicomLink.cli.Pat;


// Export de Region.FRXX.ClinicomLink.cli.Pat.ClassPatientAppt.CLS
// DTTM: 10/04/2018 19:01:35


// NSP T2015-FRXX-SUP
// Commande :
// D ##class(Region.FRXX.ClinicomLink.WebServices.Wrd.ClassDocumentsServices).CreateClassForKSoap2("Region.FRXX.ClinicomLink.cli.Pat.ClassPatientAppt", "Region.FRXX.ClinicomLink.cli.Pat", 0, "KSOAP,STD")

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Vector;

import android.net.ParseException;

import java.util.Hashtable;
import java.io.Serializable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;


import org.ksoap2.serialization.SoapObject;

import android.util.Base64;


import android.util.Log;

public class ClassPatientAppt implements Serializable, KvmSerializable
{
	private static final long serialVersionUID = 111119509047L;

	public ClassPatientAppt()
	{
	}

	public ClassPatientAppt(String age, String apptdate, String apptid, String apptstatus, String appttime, String ctloccode, String ctlocdesc, String dateofbirth, String episode, String episodeid, String firstname, String ipp, String lastname, String maidenname, String patientid, String sex, String tag)
	{
		Age = age;
		ApptDate = apptdate;
		ApptId = apptid;
		ApptStatus = apptstatus;
		ApptTime = appttime;
		CTLocCode = ctloccode;
		CTLocDesc = ctlocdesc;
		DateOfBirth = dateofbirth;
		Episode = episode;
		EpisodeId = episodeid;
		FirstName = firstname;
		IPP = ipp;
		LastName = lastname;
		MaidenName = maidenname;
		PatientId = patientid;
		Sex = sex;
		Tag = tag;
	}


	public ClassPatientAppt(SoapObject soapObject)
	{
		try
		{
			this.Age = soapObject.getProperty("Age").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.ApptDate = soapObject.getProperty("ApptDate").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.ApptId = soapObject.getProperty("ApptId").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.ApptStatus = soapObject.getProperty("ApptStatus").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.ApptTime = soapObject.getProperty("ApptTime").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.CTLocCode = soapObject.getProperty("CTLocCode").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.CTLocDesc = soapObject.getProperty("CTLocDesc").toString();
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
			this.Episode = soapObject.getProperty("Episode").toString();
		}
		catch (Exception e)
		{
		}
		try
		{
			this.EpisodeId = soapObject.getProperty("EpisodeId").toString();
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
			this.IPP = soapObject.getProperty("IPP").toString();
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
			this.PatientId = soapObject.getProperty("PatientId").toString();
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
			this.Tag = soapObject.getProperty("Tag").toString();
		}
		catch (Exception e)
		{
		}

	}


	@Override
	public int getPropertyCount()
	{
		return 17;
	}

	@Override
	public Object getProperty(int arg0)
	{
		switch (arg0)
		{
			case 0:
				return Age;
			case 1:
				return ApptDate;
			case 2:
				return ApptId;
			case 3:
				return ApptStatus;
			case 4:
				return ApptTime;
			case 5:
				return CTLocCode;
			case 6:
				return CTLocDesc;
			case 7:
				return DateOfBirth;
			case 8:
				return Episode;
			case 9:
				return EpisodeId;
			case 10:
				return FirstName;
			case 11:
				return IPP;
			case 12:
				return LastName;
			case 13:
				return MaidenName;
			case 14:
				return PatientId;
			case 15:
				return Sex;
			case 16:
				return Tag;

			default:
				return null;
		}
	}


	@Override
	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info)
	{
		switch (index)
		{
			case 0:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "Age";
				break;
			case 1:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "ApptDate";
				break;
			case 2:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "ApptId";
				break;
			case 3:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "ApptStatus";
				break;
			case 4:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "ApptTime";
				break;
			case 5:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "CTLocCode";
				break;
			case 6:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "CTLocDesc";
				break;
			case 7:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "DateOfBirth";
				break;
			case 8:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "Episode";
				break;
			case 9:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "EpisodeId";
				break;
			case 10:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "FirstName";
				break;
			case 11:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "IPP";
				break;
			case 12:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "LastName";
				break;
			case 13:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "MaidenName";
				break;
			case 14:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "PatientId";
				break;
			case 15:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "Sex";
				break;
			case 16:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "Tag";
				break;

			default:
				break;
		}
	}


	@Override
	public void setProperty(int index, Object value)
	{
		switch (index)
		{
			case 0:
				Age = value.toString();
				break;
			case 1:
				ApptDate = value.toString();
				break;
			case 2:
				ApptId = value.toString();
				break;
			case 3:
				ApptStatus = value.toString();
				break;
			case 4:
				ApptTime = value.toString();
				break;
			case 5:
				CTLocCode = value.toString();
				break;
			case 6:
				CTLocDesc = value.toString();
				break;
			case 7:
				DateOfBirth = value.toString();
				break;
			case 8:
				Episode = value.toString();
				break;
			case 9:
				EpisodeId = value.toString();
				break;
			case 10:
				FirstName = value.toString();
				break;
			case 11:
				IPP = value.toString();
				break;
			case 12:
				LastName = value.toString();
				break;
			case 13:
				MaidenName = value.toString();
				break;
			case 14:
				PatientId = value.toString();
				break;
			case 15:
				Sex = value.toString();
				break;
			case 16:
				Tag = value.toString();
				break;

			default:
				break;
		}
	}


	public String Age; // %String
	public String ApptDate; // %String
	public String ApptId; // %String
	public String ApptStatus; // %String
	public String ApptTime; // %String
	public String CTLocCode; // %String
	public String CTLocDesc; // %String
	public String DateOfBirth; // %String
	public String Episode; // %String
	public String EpisodeId; // %String
	public String FirstName; // %String
	public String IPP; // %String
	public String LastName; // %String
	public String MaidenName; // %String
	public String PatientId; // %String
	public String Sex; // %String
	public String Tag; // %String

}
