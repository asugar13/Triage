package com.example.triage;

import defaultPackage.EmergencyRoom;
import defaultPackage.Patient;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

/**
 * Activity for nurses to enter and save new vital & symptom information 
 * for a given patient.
 */
public class EnterVitalsActivity extends Activity {

	private Patient patient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_vitals);
		Intent intent = getIntent();
		patient = (Patient) intent.getSerializableExtra(EmergencyRoom.patientTag);
		TextView name_patient = (TextView) findViewById(R.id.patient_name);
		name_patient.setText(patient.getName());
		setTitle("Add Vital Signs and Symptoms");
	}

	/**
	 * OnClick for save button
	 * Adds and saves the new vitals written to patient.
	 * @param view
	 */
	public void saveData(View view) {
		boolean allEmpty = true;
		boolean correctTypes = true;
		Intent intent = new Intent(this, PatientInfoActivity.class);
		EditText temperatureText = (EditText) findViewById(R.id.temperature_field);
		EditText diastolicText = (EditText) findViewById(R.id.diastolic_field);
		EditText systolicText = (EditText) findViewById(R.id.systolic_field);
		EditText heart_rateText = (EditText) findViewById(R.id.heart_rate_field);
		EditText symptomsText = (EditText) findViewById(R.id.symptoms_field);

		
		String temperature = temperatureText.getText().toString();
		String diastolic = diastolicText.getText().toString();
		String systolic = systolicText.getText().toString();
		String heartRate = heart_rateText.getText().toString();
		String symptoms = symptomsText.getText().toString();

		String[] newVitals = { temperature, diastolic, systolic, heartRate,
				symptoms };
		
		if (symptoms.equals("")) {
			newVitals[newVitals.length -1] = "N/A";
		}
		else {
			allEmpty = false;
		}
		
		for (int i = 0; i < newVitals.length - 1; i++) {
			if (newVitals[i].equals("")) {
				newVitals[i] = "N/A";
			} 
			else if (!isDigits(newVitals[i])) {
				correctTypes = false;
			}
			else {
				allEmpty = false;
			}
		}

		if ((!allEmpty) && correctTypes) {
			patient.addVitals(newVitals);
			intent.putExtra(EmergencyRoom.patientTag, patient);
			startActivity(intent);
		} 
		else if (!correctTypes) {
			Toast.makeText(this, "Please make sure to enter valid records", Toast.LENGTH_SHORT)
			.show();
		}
		else {
			Toast.makeText(this, "No vitals entered", Toast.LENGTH_SHORT)
					.show();
		}

	}
	
	private boolean isDigits(String st){
		for (int i = 0 ; i < st.length(); i++) {
			char ch = st.charAt(i);
			if (!Character.isDigit(ch)) {
				return false;
			}
		}
		return true;
				
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

}
