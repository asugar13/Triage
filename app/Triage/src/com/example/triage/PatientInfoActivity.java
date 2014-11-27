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
 * Option to view previous records, or add new data.
 */
public class PatientInfoActivity extends Activity {
	/** The given patient who's information to display.*/
	private Patient patient;
	
	@Override
	/**
	 * Sets the appropriate layout of this activity based on the attributes of this Patient object.
	 * Populates the Textviews with patients information.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_info);
		setTitle("Patient Information");
		Intent intent = getIntent();
		patient = (Patient) intent.getSerializableExtra(EmergencyRoom.PATIENT_TAG);
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
		
		//Set personal information
		name_patient.setText(name);
		patient_birthdate.setText(birthDate);
		health_num_patient.setText(healthNum);
		
		ArrayList<Date> keys = new ArrayList<Date>(patientVitals.getVitSymps().keySet());
		
		if (!(keys.isEmpty())) {
			Date current_date = keys.get(keys.size() - 1);
			String[] current_vit_symps = patientVitals.getVitSymps().get(current_date);
			
			tempPatient.setText(current_vit_symps[0]);
			diastolic.setText(current_vit_symps[1]);
			systolic.setText(current_vit_symps[2]);
			heartRate.setText(current_vit_symps[3]);
			symptoms.setText(current_vit_symps[4]);
			
			int urgencyValue = patient.getUrgency();
			String urgencyText;
			switch(urgencyValue){
				case 1:
					urgencyText = "Non Urgent";
					break;
				case 2:
					urgencyText = "Less Urgent";
					break;
				case 3:
					urgencyText = "Urgent";
					break;
				case 4:
					urgencyText = "Urgent";
					break;
				default: 
					urgencyText = "N/A";
					break;
			
			}
			urgency.setText(urgencyText);
		}
		else {
			tempPatient.setText("N/A");
			diastolic.setText("N/A");
			systolic.setText("N/A");
			heartRate.setText("N/A");
			symptoms.setText("N/A");
			//Special case if new patient age <2 and no vitals
			if(patient.getUrgency() == 1){
				urgency.setText("Non Urgent");
			}else{
				urgency.setText("N/A");

			}
		}
		
		if (patient.getSeenByDoctorStatus()){
			seenByDoctor.setText(patient.getSeenByDoctor().toString());
		}
		else{
			seenByDoctor.setText("No");
		}
}
	/**
	 * OnClick for BackToEmergencyRoom button, starts PatientsDisplayActivity.
	 * Only works for nurses.
	 * @param view View of the clicked button.
	 */
	public void backToEmergencyRoom(View view) {
		Intent intent = new Intent(this, PatientsDisplayActivity.class);
//		intent.putExtra(EmergencyRoom.PATIENT_TAG, patient);
		startActivity(intent);
	}
	
	/**
	 * Launches EditVitalsActivity for adding new vital and symptom information.
	 * @param view View object of clicked button.
	 */
	public void editRecordsOnClick(View view) {
		Intent intent = new Intent(this, EnterVitalsActivity.class);
		intent.putExtra(EmergencyRoom.PATIENT_TAG, patient);
		startActivity(intent);
	}
	/**
	 * Launches ViewAllRecordsActivity for viewing old vital and symptom information.
	 * @param view View of the button clicked.
	 */
	public void viewRecordsOnClick(View view) {
		Intent intent = new Intent (this, ViewAllRecordsActivity.class);
		intent.putExtra(EmergencyRoom.PATIENT_TAG, patient);
		startActivity(intent);
	}
	/**
	 * OnClick for Add Prescription button, only works for physicians, launches
	 * AddPrescriptionActivity.
	 * @param view the View of the clicked button.
	 */
	public void addPrescriptionOnClick(View view){
		if (EmergencyRoom.getInstance().getUserType().equals("physician")) {
			Intent intent = new Intent(this,AddPrescriptionActivity.class);
			intent.putExtra(EmergencyRoom.PATIENT_TAG, patient);
			startActivity(intent);	
		}
		else {
			Toast.makeText(this, "Only physicians can add prescriptions", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * OnClick for seen by doctor button. 
	 * Launches TimeDialogActivity, for entering time of visit, only works for nurses.
	 * @param view View of the button clicked.
	 */
	public void seenByDoctorOnClick(View view){
		if (EmergencyRoom.getInstance().getUserType().equals("nurse")) {
			Intent intent = new Intent(this, TimeDialogActivity.class);
			intent.putExtra(EmergencyRoom.PATIENT_TAG, patient);
			startActivity(intent);
		}
		else {
			Toast.makeText(this, "Only nurses can record the date and time when a patient has been seen by a doctor",
					Toast.LENGTH_SHORT).show();
		}	
	}

	@Override
	/**
	 * Creates the menu, inflating the given menu.
	 * @param The menu to inflate.
	 * @return return if the menu was successfully created.
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.patient_info, menu);
		return true;
	}

	@Override
	/**
	 * Handles selection of menu item.
	 * @param the selected menu item.
	 * @return if the selection was successfully handled.
	 */
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
