package com.example.triage;

import defaultPackage.EmergencyRoom;
import defaultPackage.Patient;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
/**
 * Activity for adding prescriptions to patients. This activity is only available for physicians.
 */
public class AddPrescriptionActivity extends Activity {
	private Patient patient;
	/**
	 * Creates activity, gets given patient from intent.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_prescription);
		Intent intent  = getIntent();
		patient = (Patient) intent.getSerializableExtra(EmergencyRoom.PATIENT_TAG);	
	}
	
	@Override
	/**
	 * Creates the menu, inflating the appropriate layout.
	 * @param The menu to inflate to.
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_prescription, menu);
		return true;
	}

	@Override
	/**
	 * Handles selections of menu items.
	 * @param the selected menu item.
	 * @return false to allow normal menu processing
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
	
	/**
	 * OnClick for "Save" button, saves the prescription information to the patient.
	 * Ends this activity, starting PatientInfoActivity with the update patient.
	 * @param The view of the selected button.
	 */
	public void savePrescriptionOnClick(View view){
		String prescriptionString = ((EditText) findViewById(R.id.prescription_text)).getText().toString();
		if(prescriptionString.equals("")){
			prescriptionString = " ";
		}
		patient.addPrescription(prescriptionString);
		Intent intent = new Intent(this, PatientInfoActivity.class);
		intent.putExtra(EmergencyRoom.PATIENT_TAG, patient);
		startActivity(intent);	
	}
}
