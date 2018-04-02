package Region.FRXX.ClinicomLink.cli.Pat;

// Export de Region.FRXX.ClinicomLink.cli.Pat.ClassListofPatientsAppt.CLS
// DTTM: 20/02/2018 09:27:20


// NSP T2015-FRXX-SUP
// Commande :
// D ##class(MXS.ClassAutoCreate).CreateClassForKSoap2("Region.FRXX.ClinicomLink.cli.Pat.ClassListofPatientsAppt", "Region.FRXX.ClinicomLink.cli.Pat", 0, "STD")
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Vector;
import android.net.ParseException;
import java.util.Hashtable;
import java.io.Serializable;
import org.ksoap2.serialization.PropertyInfo;


import org.ksoap2.serialization.SoapObject;
import android.util.Base64;


import android.util.Log;
public class ClassListofPatientsAppt extends Vector<Region.FRXX.ClinicomLink.cli.Pat.ClassPatientAppt> implements Serializable
{
	private static final long serialVersionUID = 107804178967L;
	public ClassListofPatientsAppt(){}
	public ClassListofPatientsAppt(SoapObject soapObject) {
		try {
			SoapObject soapObject01 = soapObject;
			for (int i = 0; i < soapObject01.getPropertyCount(); i++) {
				SoapObject pii = (SoapObject) soapObject01.getProperty(i);
				Region.FRXX.ClinicomLink.cli.Pat.ClassPatientAppt obj = new Region.FRXX.ClinicomLink.cli.Pat.ClassPatientAppt(pii);
				this.add(obj);
			}
		} catch (Exception e) {
			Log.d("com.lbo.tstMontre04", e.toString());
		}
	}

	public int getPropertyCount() {
		return 0;
	}
	public Object getProperty(int arg0) {
		switch(arg0)
		{

			default: return null;
		}
	}


	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		switch(index)
		{

			default:break;
		}
	}


	public void setProperty(int index, Object value) {
		switch(index)
		{

			default:break;
		}
	}



}
