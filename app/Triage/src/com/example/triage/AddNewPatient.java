package com.example.triage;

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

public class AddNewPatient extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_patient);
		
	}
	
	public void saveNewPatient(View view) {
		Intent intent = new Intent(this, PatientsDisplayActivity.class);
		EditText nameText = (EditText) findViewById(R.id.new_name);
		EditText hcnText = (EditText) findViewById(R.id.new_hcn);
		EditText birthdateText = (EditText) findViewById(R.id.new_birthdate);
		
		String name = nameText.getText().toString();
		String[] full_name = name.split(" ");
		String hcn = hcnText.getText().toString();
		String birthdate = birthdateText.getText().toString();
		
		Vitals vitals = new Vitals();
		Patient patient = new Patient(full_name, birthdate, hcn,vitals);
		
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
