package com.example.triage;

import java.util.Calendar;
import java.util.Date;

import defaultPackage.EmergencyRoom;
import defaultPackage.Patient;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
/**
 * Activity for selecting time and date of doctor visitation for a patient. 
 *
 */
public class TimeDialogActivity extends Activity{
	/**TimePicker object for entering times.*/
	private TimePicker timePicker;
	/**DatePicker object for entering dates.*/
	private DatePicker datePicker;
	/**The patient to add the visit record to.*/
	private Patient patient;
	
	@Override
	/**
	 * Creates activity, initializing time picker,date picker, and patient objects.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_dialog);
		setTitle("Enter Date and Time of Appointment");
		timePicker = (TimePicker) findViewById(R.id.timePicker);
		datePicker = (DatePicker) findViewById(R.id.datePicker);
		
		Intent intent = getIntent();
		patient = (Patient) intent.getSerializableExtra(EmergencyRoom.PATIENT_TAG);
	}

	@Override
	/**
	 * Creates the menu, inflating the given menu.
	 * @param The menu to inflate.
	 * @return return if the menu was successfully created.
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_dialog, menu);
		return true;
	}
	/**
	 * Saves the selected date and time to the patient.
	 * @param view View of the clicked button.
	 */
	public void saveDateTimeOnClick(View view){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, datePicker.getYear());
		cal.set(Calendar.MONTH, datePicker.getMonth());
		cal.set(Calendar.DATE, datePicker.getDayOfMonth());
		cal.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
		cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
		Date date = cal.getTime();
		patient.setSeenByDoctor(date);
		Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

		Intent intent = new Intent(this, PatientInfoActivity.class);
		intent.putExtra(EmergencyRoom.PATIENT_TAG, patient);
		startActivity(intent);
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
