package com.example.triage;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import defaultPackage.Patient;
import defaultPackage.Vitals;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ViewAllRecordsActivity extends Activity {
	
	private Patient patient;
	@Override
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
	 * Custom adapter for listview, populates textviews with patient info
	 * 
	 *
	 */
	private class patientHistoryAdapter extends BaseAdapter{
		Context context;
		int layoutId;
		TreeMap<Date, String[]> vitals;
		ArrayList<Date> sortedDates = new ArrayList<Date>();
		
		public patientHistoryAdapter(Context context,int layoutId,Vitals vitals){
			this.context = context;
			this.layoutId = layoutId;
			this.vitals = vitals.getAllVitals();
			sortedDates = (ArrayList) vitals.getAllVitals().keySet();
			
		}
		
		@Override
		public int getCount() {
			return sortedDates.size();
		}

		@Override
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
		 * 
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			//The patient for this position in the list
			final String[] vitalsData = getItem(position);
			//if the row is null instantiate it from the given xml layoutID
			if(row == null){
				LayoutInflater mInflator = ((Activity) context).getLayoutInflater();
	            row = mInflator.inflate(layoutId, parent, false);
			}
			
			//Get the text views from the layout
			TextView bpTView = (TextView) row.findViewById(R.id.patientBPText);
			TextView hrTView = (TextView) row.findViewById(R.id.patientHRText);
			TextView sympTView = (TextView) row.findViewById(R.id.patientSympText);
			TextView tempTView = (TextView) row.findViewById(R.id.patientTempText);
			
			//Assign values to the text views
			bpTView.setText(vitalsData[1] + "/" + vitalsData[2]);
			hrTView.setText(vitalsData[3]);
			sympTView.setText(vitalsData[4]);
			tempTView.setText(vitalsData[0]);
			
			return row;
		}
		
	}
	
	
	
	
	
}
