package com.example.triage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import defaultPackage.EmergencyRoom;
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
import android.widget.Toast;
/**
 * Activity that displays patient specific information.
 * Option to view previous records, or add new data
 */
public class PatientInfoActivity extends Activity {

	private Patient patient;
	
	@Override
	/**
	 * Sets the appropriate layout of this activity based on the attributes of this Patient object.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_info);
		setTitle("Patient Information");
		Intent intent = getIntent();
		patient = (Patient) intent.getSerializableExtra(EmergencyRoom.patientTag);
		String name = patient.getName();
		String healthNum = patient.getHealthCardNum();
		String birthDate = patient.getBirthDate();
		Vitals patientVitals = patient.getVitals();
		
		TextView name_patient = (TextView) findViewById(R.id.name_patient);
		TextView patient_birthdate = (TextView) findViewById(R.id.patient_birthdate);
		TextView health_num_patient = (TextView) findViewById(R.id.health_num_patient);
		
		TextView diastolic = (TextView) findViewById(R.id.diastolic_catch);
		TextView systolic = (TextView) findViewById(R.id.systolic_catch);
		TextView heartRate = (TextView) findViewById(R.id.heart_rate_catch);
		TextView tempPatient = (TextView) findViewById(R.id.temperature_catch);
		TextView symptoms = (TextView) findViewById(R.id.symptoms_description_catch);
		TextView urgency = (TextView) findViewById(R.id.urgency_level_field);
		TextView seenByDoctor = (TextView) findViewById(R.id.seen_by_doctor_catch);

		
		name_patient.setText(name);
		patient_birthdate.setText(birthDate);
		health_num_patient.setText(healthNum);
		Log.d("here", "all good");
		ArrayList<Date> keys = new ArrayList<Date>(patientVitals.getVitSymps().keySet());
		//TreeMap<Date, String[]> vit_symps_map = new TreeMap<Date, String[]>(patient_vitals.getVitSymps());
		Log.d("here", "maybe not");
		if (!(keys.isEmpty())) {
			Date current_date = keys.get(keys.size() - 1);
			String[] current_vit_symps = patientVitals.getVitSymps().get(current_date);
			
			tempPatient.setText(current_vit_symps[0]);
			diastolic.setText(current_vit_symps[1]);
			systolic.setText(current_vit_symps[2]);
			heartRate.setText(current_vit_symps[3]);
			symptoms.setText(current_vit_symps[4]);
			urgency.setText("" + patient.getUrgency());
			

			
		}
		else {
			tempPatient.setText("N/A");
			diastolic.setText("N/A");
			systolic.setText("N/A");
			heartRate.setText("N/A");
			symptoms.setText("N/A");
			urgency.setText("N/A");
		}
		if (patient.getSeenByDoctorStatus()){
			seenByDoctor.setText(patient.getSeenByDoctor().toString());
		}
		else{
			seenByDoctor.setText("No");
		}
	}
	/**
	 * Launches EditVitalsActivity for adding new vital and symptom information.
	 */
	public void editRecordsOnClick(View view) {
		Intent intent = new Intent(this, EnterVitalsActivity.class);
		intent.putExtra(EmergencyRoom.patientTag, patient);
		startActivity(intent);
	}
	/**
	 * Launches ViewAllRecordsActivity for viewing old vital and symptom information
	 * @param view
	 */
	public void viewRecordsOnClick(View view) {
		Intent intent = new Intent (this, ViewAllRecordsActivity.class);
		intent.putExtra(EmergencyRoom.patientTag, patient);
		startActivity(intent);
	}
	/**
	 * OnClick for Add Prescription button, only works for physicians
	 * Launches AddPrescriptionActivity
	 * @param view
	 */
	public void addPrescriptionOnClick(View view){
		if (EmergencyRoom.getInstance().getUserType().equals("physician")) {
			Intent intent = new Intent(this,AddPrescriptionActivity.class);
			intent.putExtra(EmergencyRoom.patientTag, patient);
			startActivity(intent);	
		}
		else {
			Toast.makeText(this, "Only physicians can add prescriptions", Toast.LENGTH_SHORT).show();
		}
		
		

	}
	
	/**
	 * OnClick for seen by doctor button. Launches TimeDialogActivity
	 * for entering time of visit.
	 * ???
	 * ONLY WORKS FOR NURSES?????
	 * ???
	 * @param view
	 */
	public void seenByDoctorOnClick(View view){
		if (EmergencyRoom.getInstance().getUserType().equals("nurse")) {
			Intent intent = new Intent(this, TimeDialogActivity.class);
			intent.putExtra(EmergencyRoom.patientTag, patient);
			startActivity(intent);
		}
		else {
			Toast.makeText(this, "Only nurses can record the date and time when a patient has been seen by a doctor",
					Toast.LENGTH_SHORT).show();
		}
		
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
