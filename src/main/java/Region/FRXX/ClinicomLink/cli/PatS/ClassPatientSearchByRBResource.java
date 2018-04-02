package Region.FRXX.ClinicomLink.cli.PatS;


// Export de Region.FRXX.ClinicomLink.cli.PatS.ClassPatientSearchByRBResource.CLS
// DTTM: 21/02/2018 09:12:03


// NSP T2015-FRXX-SUP
// Commande :
// D ##class(MXS.ClassAutoCreate).CreateClassForKSoap2("Region.FRXX.ClinicomLink.cli.PatS.ClassPatientSearchByRBResource", "Region.FRXX.ClinicomLink.cli.PatS", 0, "STD")
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
public class ClassPatientSearchByRBResource implements KvmSerializable
{
	private static final long serialVersionUID = 122366901329L;
	public ClassPatientSearchByRBResource(){}
	public ClassPatientSearchByRBResource(String apptstatus,String enddate,String location,String resourcecode,String startdate)
	{
		ApptStatus=apptstatus;
		EndDate=enddate;
		Location=location;
		ResourceCode=resourcecode;
		StartDate=startdate;
	}



	public ClassPatientSearchByRBResource(SoapObject soapObject){
		try { this.ApptStatus = soapObject.getProperty("ApptStatus").toString(); } catch (Exception e) { }
		try { this.EndDate = soapObject.getProperty("EndDate").toString(); } catch (Exception e) { }
		try { this.Location = soapObject.getProperty("Location").toString(); } catch (Exception e) { }
		try { this.ResourceCode = soapObject.getProperty("ResourceCode").toString(); } catch (Exception e) { }
		try { this.StartDate = soapObject.getProperty("StartDate").toString(); } catch (Exception e) { }

	}


	public int getPropertyCount() {
		return 5;
	}
	public Object getProperty(int arg0) {
		switch(arg0)
		{
			case 0:
				return ApptStatus;
			case 1:
				return EndDate;
			case 2:
				return Location;
			case 3:
				return ResourceCode;
			case 4:
				return StartDate;

			default: return null;
		}
	}


	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		switch(index)
		{
			case 0:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "ApptStatus";
				break;
			case 1:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "EndDate";
				break;
			case 2:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "Location";
				break;
			case 3:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "ResourceCode";
				break;
			case 4:
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "StartDate";
				break;

			default:break;
		}
	}


	public void setProperty(int index, Object value) {
		switch(index)
		{
			case 0:
				ApptStatus = value.toString();
				break;
			case 1:
				EndDate = value.toString();
				break;
			case 2:
				Location = value.toString();
				break;
			case 3:
				ResourceCode = value.toString();
				break;
			case 4:
				StartDate = value.toString();
				break;

			default:break;
		}
	}


	public String ApptStatus; // %String
	public String EndDate; // %String
	public String Location; // %String
	public String ResourceCode; // %String
	public String StartDate; // %String

}
