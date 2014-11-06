package com.example.triage;

import defaultPackage.Patient;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class PatientInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_info);
		Intent intent = getIntent();
		Patient patient = (Patient) intent.getSerializableExtra("Patient_Tag");
		String name = patient.getName();
		String health_num = patient.getHealthCardNum();
		String birthdate = patient.getBirthDate();
		
		TextView name_patient = (TextView) findViewById(R.id.name_patient);
		TextView patient_birthdate = (TextView) findViewById(R.id.patient_birthdate);
		TextView health_num_patient = (TextView) findViewById(R.id.health_num_patient);
		
		name_patient.setText(name);
		patient_birthdate.setText(birthdate);
		health_num_patient.setText(health_num);
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
