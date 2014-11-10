package com.example.triage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.TreeMap;

import defaultPackage.Patient;
import defaultPackage.Vitals;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
/**
 * Activity for displaying all vitals and symptom information for a specific patient 
 *
 */
public class ViewAllRecordsActivity extends Activity {
	/**The given patient - display there information */
	private Patient patient;
	@Override
	/**
	 * Sets the appropriate layout of this activity based on patient_records.txt and vital_history_row.xml.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_all_records);
		Intent intent = getIntent();
		patient = (Patient) intent.getSerializableExtra("Patient_Tag");
		TextView name_patient = (TextView) findViewById(R.id.patient_name);
		name_patient.setText(patient.getName());
		
		if(!patient.getVitals().isEmpty){
			ListView recordsList = (ListView) findViewById(R.id.patientHistoryList);
			recordsList.setAdapter(new patientHistoryAdapter(this,R.layout.vital_history_row,patient.getVitals()));
			
		}else{
			TextView noVitals = (TextView) findViewById(R.id.noPatientData);
			noVitals.setText("No Patient data found.");
		}	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_all_records, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	/**
	 * Custom adapter for listview, populates textviews all patient vitals info
	 * 
	 */
	private class patientHistoryAdapter extends BaseAdapter{
		Context context;
		int layoutId;
		TreeMap<Date, String[]> vitals;
		ArrayList<Date> sortedDates = new ArrayList<Date>();
		
		/**
		 * Instantiates PatientHistoryAdapter
		 * 
		 * @param context - context that adapter is used in
		 * @param layoutId layout for rows in listview
		 * @param vitals data to display
		 */
		public patientHistoryAdapter(Context context,int layoutId,Vitals vitals){
			this.context = context;
			this.layoutId = layoutId;
			this.vitals = vitals.getAllVitals();
			sortedDates = new ArrayList<Date>(vitals.getAllVitals().keySet());
			//Most recent first
			Collections.reverse(sortedDates);
		}
		
		@Override
		/**
		 * @return size (int) of list to fill Listview
		 */
		public int getCount() {
			return sortedDates.size();
		}

		@Override
		/**
		 * @return vital information for each row
		 */
		public String[] getItem(int position) {
			return vitals.get(sortedDates.get(position));
		}

		@Override
		public long getItemId(int position) {
			//Not sure about this right now
			return 0;
		}

		@Override
		/**
		 * @param position an int that specifies the position in the listview
		 * @param convertView a View for the specific row of the listview
		 * @param parent the parent view (ListView) that the convertView will be attached to
		 * @return row a view populated with the vitals info
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View row = convertView;
			//The patient for this position in the list
			final String[] vitalsData = getItem(position);
			Log.d("GETVIEW",Arrays.toString(vitalsData));

			//if the row is null instantiate it from the given xml layoutID
			if(row == null){
				LayoutInflater mInflator = ((Activity) context).getLayoutInflater();
	            row = mInflator.inflate(layoutId, parent, false);
			}
			
			//Get the text views from the layout
			TextView dateTView = (TextView) row.findViewById(R.id.patientDateText);
			TextView bpdTView = (TextView) row.findViewById(R.id.patientBPDText);
			TextView bpsTView = (TextView) row.findViewById(R.id.patientBPSText);
			TextView hrTView = (TextView) row.findViewById(R.id.patientHRText);
			TextView sympTView = (TextView) row.findViewById(R.id.patientSympText);
			TextView tempTView = (TextView) row.findViewById(R.id.patientTempText);
			
			//Assign values to the text views
			dateTView.setText(sortedDates.get(position).toString());
			bpdTView.setText(vitalsData[1]);
			bpsTView.setText(vitalsData[2]);
			hrTView.setText(vitalsData[3]);
			sympTView.setText(vitalsData[4]);
			tempTView.setText(vitalsData[0]);
			
			return row;
		}
		
	}
	
	
	
	
	
}
