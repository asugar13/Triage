package com.example.triage;

import java.util.ArrayList;

import defaultPackage.EmergencyRoom;
import defaultPackage.Patient;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PatientsDisplayActivity extends Activity {
	private EmergencyRoom myEmergRoom;
	private ArrayList<Patient> patients;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patients_display);
		myEmergRoom = new EmergencyRoom(this,"patient_records.txt");
		//Get list of patients
		patients = (ArrayList<Patient>) myEmergRoom.getPatients();
		//Get list view, and populate with adapter
		ListView patientsList = (ListView) findViewById(R.id.listView1);
		patientsList.setAdapter(new patientsAdapter(this,R.layout.patient_list_row,patients));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.patients_display, menu);
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
	 * 
	 * 
	 *
	 */
	private class patientsAdapter extends BaseAdapter{
		Context context;
		int layoutId;
		ArrayList<Patient> patientList;
		public patientsAdapter(Context context,int layoutId,ArrayList<Patient> patientList){
			this.context = context;
			this.layoutId = layoutId;
			this.patientList = patientList;
		}
		
		@Override
		public int getCount() {
			return patientList.size();
		}

		@Override
		public Object getItem(int position) {
			return patientList.get(position);
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
			final Patient thisPatient = (Patient) getItem(position);
			//if the row is null instantiate it from the given xml layoutID
			if(row == null){
				LayoutInflater mInflator = ((Activity) context).getLayoutInflater();
	            row = mInflator.inflate(layoutId, parent, false);
			}
			
			//Get the text views from the layout
			TextView nameTView = (TextView) row.findViewById(R.id.patientNameText);
			TextView healthCardTView = (TextView) row.findViewById(R.id.patientHealthNumerText);
			TextView birthDateTView = (TextView) row.findViewById(R.id.patientBirthText);
			
			//Assign values to the text views
			nameTView.setText(thisPatient.getName());
			healthCardTView.setText(thisPatient.getHealthCardNum());
			birthDateTView.setText(thisPatient.getBirthDate());
			
			//Add a clicklistener to the list items
			row.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//Launch the patientInfo class with the patient from this row
					Intent intent = new Intent(context,PatientInfoActivity.class);
					intent.putExtra("Patient_Tag", thisPatient);
					startActivity(intent);
				}
			});
			
			return row;
		}
		
	}

	
}
