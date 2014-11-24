package com.example.triage;

import java.util.Date;
import java.util.TreeMap;

import java.util.Calendar;

import defaultPackage.EmergencyRoom;
import defaultPackage.Patient;
import defaultPackage.Vitals;
import defaultPackage.DatabaseManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
/**
 * Activity for adding new patients
 *
 */
public class AddNewPatient extends Activity {
	
	private DatePicker birthdatePicker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_patient);
		birthdatePicker = (DatePicker) findViewById(R.id.birthdatePicker);
		setTitle("Add New Patient");
		
	}
	/**
	 * OnClick for save button
	 * Gets all entered data from EditTexts, creates new patient
	 * saves new patient to EmergencyRoom.
	 * Starts PatientDisplayActivity to display the new patient
	 * @param view
	 */
	public void saveNewPatient(View view) {
		
		
		
		Intent intent = new Intent(this, PatientsDisplayActivity.class);
		String year = String.valueOf(birthdatePicker.getYear());
		String month = String.valueOf(birthdatePicker.getMonth());
		String day = String.valueOf(birthdatePicker.getDayOfMonth());
		
		EditText nameText = (EditText) findViewById(R.id.new_name);
		EditText hcnText = (EditText) findViewById(R.id.new_hcn);
		EditText temperatureText = (EditText) findViewById(R.id.new_temp);
		EditText diastolicText = (EditText) findViewById(R.id.new_diastolic);
		EditText systolicText = (EditText) findViewById(R.id.new_systolic);
		EditText heart_rateText = (EditText) findViewById(R.id.new_heartRate);
		EditText symptomsText = (EditText) findViewById(R.id.new_symptoms);
		
		String name = nameText.getText().toString();
		String[] full_name = name.split(" ");
		String hcn = hcnText.getText().toString();
		String temperature = temperatureText.getText().toString();
		String diastolic = diastolicText.getText().toString();
		String systolic = systolicText.getText().toString();
		String heart_rate = heart_rateText.getText().toString();
		String symptoms = symptomsText.getText().toString();
		String[] vitalInfo = {temperature, diastolic, systolic, heart_rate,
				symptoms};
		
		boolean allEmpty = true;
		Vitals vitals = new Vitals(); 
		for (int i=0; i<vitalInfo.length; i++) {
			if (vitalInfo[i].equals("")) {
				vitalInfo[i] = "N/A";
			}
			else {
				allEmpty = false;
			}
		}
		if (!allEmpty){
			vitals.add(vitalInfo);
		}
		String birthdate = year + "-" + month + "-" + day;
		Patient patient = new Patient(full_name, birthdate, hcn,vitals,new TreeMap<Date,String>());
		EmergencyRoom.getInstance().savePatient(patient);
		startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new_patient, menu);
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
