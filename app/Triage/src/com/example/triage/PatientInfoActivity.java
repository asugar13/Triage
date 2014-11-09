package com.example.triage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import defaultPackage.Patient;
import defaultPackage.Vitals;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
/**
 * Activity that displays patient specific information.
 * Option to view previous records, or add new data
 */
public class PatientInfoActivity extends Activity {

	private Patient patient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_info);
		Intent intent = getIntent();
		patient = (Patient) intent.getSerializableExtra("Patient_Tag");
		String name = patient.getName();
		String health_num = patient.getHealthCardNum();
		String birthdate = patient.getBirthDate();
		Vitals patient_vitals = patient.getVitals();
		
		TextView name_patient = (TextView) findViewById(R.id.name_patient);
		TextView patient_birthdate = (TextView) findViewById(R.id.patient_birthdate);
		TextView health_num_patient = (TextView) findViewById(R.id.health_num_patient);
		
		TextView diastolic = (TextView) findViewById(R.id.diastolic_catch);
		TextView systolic = (TextView) findViewById(R.id.systolic_catch);
		TextView heartRate = (TextView) findViewById(R.id.heart_rate_catch);
		TextView temp_patient = (TextView) findViewById(R.id.temperature_catch);
		TextView symptoms = (TextView) findViewById(R.id.symptoms_description_catch);

		
		name_patient.setText(name);
		patient_birthdate.setText(birthdate);
		health_num_patient.setText(health_num);
		Log.d("here", "all good");
		ArrayList<Date> keys = new ArrayList<Date>(patient_vitals.getVitSymps().keySet());
		//TreeMap<Date, String[]> vit_symps_map = new TreeMap<Date, String[]>(patient_vitals.getVitSymps());
		Log.d("here", "maybe not");
		if (!(keys.isEmpty())) {
			Date current_date = keys.get(keys.size() - 1);
			String[] current_vit_symps = patient_vitals.getVitSymps().get(current_date);
			
			temp_patient.setText(current_vit_symps[0]);
			diastolic.setText(current_vit_symps[1]);
			systolic.setText(current_vit_symps[2]);
			heartRate.setText(current_vit_symps[3]);
			symptoms.setText(current_vit_symps[4]);

			
		}
		else {
			temp_patient.setText("N/A");
			diastolic.setText("N/A");
			systolic.setText("N/A");
			heartRate.setText("N/A");
			symptoms.setText("N/A");
		}
		
	}
	/**
	 * Launches EditVitalsActivity for adding new vital and symptom information.
	 */
	public void editRecordsOnClick(View view) {
		Intent intent = new Intent(this, EnterVitalsActivity.class);
		intent.putExtra("Patient_Tag", patient);
		startActivity(intent);
	}
	/**
	 * Launches ViewAllRecordsActivity for viewing old vital and symptom information
	 * @param view
	 */
	public void viewRecordsOnClick(View view) {
		Intent intent = new Intent (this, ViewAllRecordsActivity.class);
		intent.putExtra("Patient_Tag", patient);
		startActivity(intent);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.patient_info, menu);
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
}
