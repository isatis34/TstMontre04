package com.lbo.tstMontre04;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import Region.FRXX.ClinicomLink.cli.Pat.ClassListofPatientsAppt;
import Region.FRXX.ClinicomLink.cli.Pat.ClassPatient;

//import Region.FRXX.ClinicomLink.cli.PatS.ClassPatientSearchIdentity;

public class PatientApptAdapter extends BaseExpandableListAdapter {

	public Region.FRXX.ClinicomLink.cli.Pat.ClassListofPatientsAppt ClassPatients;
	private int[] colors = new int[] { 0xFF007DFF, 0xFFFFD3E3, 0xFF888888 };
	private Context context;
	//HashMap<String, Region.FRXX.ClinicomLink.cli.PatS.ClassListOfClassEpisodeAUTO> listEpisodes = new HashMap<String, Region.FRXX.ClinicomLink.cli.PatS.ClassListOfClassEpisodeAUTO>();
	private LayoutInflater inflater;

	public PatientApptAdapter(Context context, Region.FRXX.ClinicomLink.cli.Pat.ClassListofPatientsAppt patients) {
		this.context = context;
		this.ClassPatients = patients;
		inflater = LayoutInflater.from(context);
	}

	public void clearAdapter()
	{
		if (ClassPatients != null)
			ClassPatients.clear();
		notifyDataSetChanged();
	}
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView grouptxt = null;
		if (convertView == null) {
			convertView = inflater.inflate( R.layout.line_def_lv_patientsappt, null);
		}
		Region.FRXX.ClinicomLink.cli.Pat.ClassPatientAppt patient = getPatient(groupPosition);

		if (patient.Sex.equalsIgnoreCase("F"))
			convertView.setBackgroundColor(colors[1]);
		else if (patient.Sex.equalsIgnoreCase("M"))
			convertView.setBackgroundColor(colors[0]);
		else
			convertView.setBackgroundColor(colors[2]);

		grouptxt = (TextView) convertView.findViewById(R.id.nom);
		grouptxt.setText(patient.FirstName);
		grouptxt = (TextView) convertView.findViewById(R.id.prenom);
		grouptxt.setText(patient.LastName);
		if ((patient.Sex.equalsIgnoreCase("F")) && (patient.MaidenName != null)
				&& (!patient.MaidenName.isEmpty())) {
			grouptxt = (TextView) convertView.findViewById(R.id.nomJF);
			grouptxt.setText("(" + patient.MaidenName + ")");
			grouptxt.setVisibility(View.VISIBLE);
		}
		grouptxt = (TextView) convertView.findViewById(R.id.age);
		grouptxt.setText(patient.Age);
		grouptxt = (TextView) convertView.findViewById(R.id.statusEpisode);

		grouptxt = (TextView) convertView.findViewById(R.id.ApptDTTM);
		grouptxt.setText(patient.ApptDate + " " + patient.ApptTime);

		grouptxt = (TextView) convertView.findViewById(R.id.ApptStatus);
		grouptxt.setText(patient.ApptStatus);

		//grouptxt.setText(patient.PatientRDT.PSTS);

		/*
		 * Region.FRXX.ClinicomLink.cli.PatS.ClassListOfClassEpisodeAUTO
		 * ClassListOfClassEpisodeAUTO = new
		 * Region.FRXX.ClinicomLink.cli.PatS.ClassListOfClassEpisodeAUTO();
		 * Region.FRXX.ClinicomLink.cli.PatS.ClassEpisodeAUTO ClassEpisodeAUTO = new
		 * ClassEpisodeAUTO(); ClassEpisodeAUTO.EPIITN = "##NULL##";
		 * ClassListOfClassEpisodeAUTO.add(ClassEpisodeAUTO);
		 * listEpisodes.put(patient.ITN, ClassListOfClassEpisodeAUTO);
		 */
		return convertView;
	}

	public Region.FRXX.ClinicomLink.cli.Pat.ClassPatientAppt getPatient(int index) {
		return ClassPatients.get(index);
	}

	@Override
	public int getGroupCount() {
		return ClassPatients == null ? 0 : ClassPatients.size();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	/*-----------------------------------------------*/

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		try {
		/*
			TextView TextView;
			String trDate;
			Date date;

			Region.FRXX.ClinicomLink.cli.Pat.ClassPatient patient = getPatient(groupPosition);
			if (patient == null)
				return null;
			Region.FRXX.ClinicomLink.cli.PatS.ClassListOfClassEpisodeAUTO ClassListOfClassEpisodeAUTO = listEpisodes
					.get(patient.ITN);
			if (ClassListOfClassEpisodeAUTO == null)
				return null;
			Region.FRXX.ClinicomLink.cli.PatS.ClassEpisodeAUTO ClassEpisodeAUTO = ClassListOfClassEpisodeAUTO
					.get(childPosition);
			if (ClassEpisodeAUTO == null)
				return null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.line_def_lv_episodes,
						null);
			}
			convertView.setLongClickable(true);
			TextView = (TextView) convertView.findViewById(R.id.numeroEpisode);
			TextView.setText(ClassEpisodeAUTO.EPIS);
			TextView = (TextView) convertView.findViewById(R.id.statusEpisode);
			TextView.setText(ClassEpisodeAUTO.PSTS);
			TextView = (TextView) convertView.findViewById(R.id.us);
			TextView.setText(ClassEpisodeAUTO.PUSDESC);
			TextView = (TextView) convertView.findViewById(R.id.uf);
			TextView.setText(ClassEpisodeAUTO.PUFDESC);

			if (MainActivity.Debug_Mode_preference) {
				TextView = (TextView) convertView.findViewById(R.id.patientITN);
				TextView.setText("PatITN: " + patient.ITN);
				TextView.setVisibility(View.VISIBLE);
				TextView = (TextView) convertView.findViewById(R.id.episodeNum);
				TextView.setText("EpiITN: " + ClassEpisodeAUTO.EPIITN);
				TextView.setVisibility(View.VISIBLE);
			}
			try {
				if ((ClassEpisodeAUTO.PEDATE != null)
						&& ((!ClassEpisodeAUTO.PEDATE.isEmpty()))) {
					date = new SimpleDateFormat("yyyyMMddhhmm")
							.parse(ClassEpisodeAUTO.PEDATE);
					trDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
				} else
					trDate = "";
				TextView = (TextView) convertView
						.findViewById(R.id.dateDebutEpisode);
				TextView.setText(trDate);
				if ((ClassEpisodeAUTO.PDDATE != null)
						&& ((!ClassEpisodeAUTO.PDDATE.isEmpty()))) {
					date = new SimpleDateFormat("yyyyMMddhhmm")
							.parse(ClassEpisodeAUTO.PDDATE);
					trDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
				} else
					trDate = "";
				TextView = (TextView) convertView
						.findViewById(R.id.dateFinEpisode);
				TextView.setText(trDate);
				return convertView;
			} catch (Exception e) {
				Log.d("com.lbo.clinicom", e.getMessage());
			}
*/
			return convertView;
		} catch (Exception e) {
			Log.e("com.lbo.clinicom", e.getMessage());
			return convertView;
		}
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		/*Region.FRXX.ClinicomLink.cli.Pat.ClassPatient patient = getPatient(groupPosition);

		Region.FRXX.ClinicomLink.cli.PatS.ClassListOfClassEpisodeAUTO ClassListOfClassEpisodeAUTO = listEpisodes
				.get(patient.ITN);
		Region.FRXX.ClinicomLink.cli.PatS.ClassEpisodeAUTO ClassEpisodeAUTO = ClassListOfClassEpisodeAUTO
				.get(childPosition);
		return ClassEpisodeAUTO;*/
		return null;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		Region.FRXX.ClinicomLink.cli.Pat.ClassPatientAppt patient = getPatient(groupPosition);
		if (patient == null)
			return 0;
		/*Region.FRXX.ClinicomLink.cli.PatS.ClassListOfClassEpisodeAUTO ClassListOfClassEpisodeAUTO = listEpisodes
				.get(patient.ITN);
		return ClassListOfClassEpisodeAUTO == null ? 0
				: ClassListOfClassEpisodeAUTO.size();
				*/
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return ClassPatients.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	/*
	public void setEpisodes(
			String ITN,
			Region.FRXX.ClinicomLink.cli.PatS.ClassListOfClassEpisodeAUTO classListOfClassEpisodeAUTOResult) {

		Region.FRXX.ClinicomLink.cli.PatS.ClassListOfClassEpisodeAUTO ClassListOfClassEpisodeAUTO = null;
		ClassListOfClassEpisodeAUTO = listEpisodes.get(ITN);
		if (ClassListOfClassEpisodeAUTO != null) {
			listEpisodes.remove(ITN);
		}
		listEpisodes.put(ITN, classListOfClassEpisodeAUTOResult);
		ClassListOfClassEpisodeAUTO = listEpisodes.get(ITN);
		this.notifyDataSetChanged();
	}
	*/
	public void setPatients(Region.FRXX.ClinicomLink.cli.Pat.ClassListofPatientsAppt patients) {
		this.ClassPatients = patients;
		this.notifyDataSetChanged();
	}

	/*public Region.FRXX.ClinicomLink.cli.PatS.ClassEpisodeAUTO getEpisode(String _ITN,
			int childPosition) {
		Region.FRXX.ClinicomLink.cli.PatS.ClassListOfClassEpisodeAUTO ClassListOfClassEpisodeAUTO = listEpisodes
				.get(_ITN);
		Region.FRXX.ClinicomLink.cli.PatS.ClassEpisodeAUTO ClassEpisodeAUTO = ClassListOfClassEpisodeAUTO
				.get(childPosition);
		return ClassEpisodeAUTO;
	}
	*/
}

/*
 * public class PatientAdapter extends SimpleAdapter { public
 * Region.FRXX.ClinicomLink.cli.Pat.ClassPatients ClassPatients;
 * 
 * private int[] colors = new int[] { 0xFF007DFF, 0xFFFFD3E3, 0xFF888888 };
 * 
 * public PatientAdapter(Context context, List<HashMap<String, String>> items,
 * int resource, String[] from, int[] to) { super(context, items, resource,
 * from, to); }
 * 
 * public Region.FRXX.ClinicomLink.cli.Pat.ClassPatient getPatient(int index) { return
 * ClassPatients.get(index); }
 * 
 * @Override public View getView(int position, View convertView, ViewGroup
 * parent) {
 * 
 * Log.d("com.lbo.clinicom", "Dans PatientAdaptergetView()"); View view =
 * super.getView(position, convertView, parent);
 * 
 * Region.FRXX.ClinicomLink.cli.Pat.ClassPatient ClassPatient =
 * ClassPatients.get(position); if (ClassPatient == null) return view; if
 * (ClassPatient.Sexe.equalsIgnoreCase("F")) view.setBackgroundColor(colors[1]);
 * else if (ClassPatient.Sexe.equalsIgnoreCase("M"))
 * view.setBackgroundColor(colors[0]); else view.setBackgroundColor(colors[2]);
 * 
 * LinearLayout vwParentRow = (LinearLayout) view; Button btnChild = (Button)
 * vwParentRow.getChildAt(0); btnChild.setTag(position); //int colorPos =
 * position % colors.length; //view.setBackgroundColor(colors[colorPos]);
 * 
 * 
 * return view; } }
 */