package com.example.triage;

import defaultPackage.EmergencyRoom;
import defaultPackage.Patient;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class EnterVitalsActivity extends Activity {

	private Patient patient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_vitals);
		Intent intent = getIntent();
		patient = (Patient) intent.getSerializableExtra("Patient_Tag");
		TextView name_patient = (TextView) findViewById(R.id.patient_name);
		name_patient.setText(patient.getName());
	}
	
	public void saveData(View view) {
		Intent intent = new Intent(this, PatientInfoActivity.class);
		EditText temperatureText = (EditText) findViewById(R.id.temperature_field);
		EditText diastolicText = (EditText) findViewById(R.id.diastolic_field);
		EditText systolicText = (EditText) findViewById(R.id.systolic_field);
		EditText heart_rateText = (EditText) findViewById(R.id.heart_rate_field);
		EditText symptomsText = (EditText) findViewById(R.id.symptoms_field);
		String temperature = temperatureText.getText().toString();
		String diastolic = diastolicText.getText().toString();
		String systolic = systolicText.getText().toString();
		String heart_rate = heart_rateText.getText().toString();
		String symptoms = symptomsText.getText().toString();
		String[] new_vitals = {temperature, diastolic, systolic, heart_rate, symptoms};
		patient.addVitals(new_vitals);
		EmergencyRoom.savePatientData(this);
		intent.putExtra("Patient_Tag", patient);
		startActivity(intent);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.enter_vitals, menu);
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
	
	public void getWrittenData(){
		
	}

	
	public void savePatientData(){
		getWrittenData();
		
		//Write patients
	}
}
