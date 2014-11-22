package com.example.triage;

import defaultPackage.Patient;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class AddPrescriptionActivity extends Activity {
	private Patient patient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_prescription);
		Intent intent  = getIntent();
		patient = (Patient) intent.getSerializableExtra("Patient_Tag");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_prescription, menu);
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
	 * Saves the prescription information to the patient
	 */
	public void savePrescriptionOnClick(View view){
		String prescriptionString = ((EditText) findViewById(R.id.prescription_text)).getText().toString();
		patient.addPrescription(prescriptionString);
		finish();
		
	}
	
	
	
}
