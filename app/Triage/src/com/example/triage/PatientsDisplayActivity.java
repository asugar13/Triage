package com.example.triage;

import java.util.ArrayList;

import defaultPackage.EmergencyRoom;
import defaultPackage.Patient;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Activity for displaying all patients (sorted/unsorted), nurses can search by health card number,
 * and add patients.
 * Implements OnItemSelectedListener for handling spinner selections.
 */
public class PatientsDisplayActivity extends Activity implements OnItemSelectedListener {
	/**List of all patient objects  */
	private ArrayList<Patient> patients;
	private String allPatientsSelection;
	private String sortedPatientsSelection;
	@Override
	/**
	 * Creates activity, populates drop down menu (spinner),
	 * sets selection of the spinner to (0) -> View all patients (unsorted)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patients_display);
		setTitle("Emergency Room");
		//Not sure about this

		
		String[] spinnerOptions = getResources().getStringArray(R.array.list_options);
		allPatientsSelection = spinnerOptions[0];
		sortedPatientsSelection = spinnerOptions[1];
		
		Spinner dropDown = (Spinner) findViewById(R.id.list_sort);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.list_options, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dropDown.setAdapter(adapter);
		dropDown.setOnItemSelectedListener(this);
		dropDown.setSelection(0);

	}
	/**
	 * OnClick for Add Patient button, starts AddNewPatient activity
	 * @param view
	 */
	public void addPatient(View view) {
		if (EmergencyRoom.getInstance().getUserType().equals("nurse")) {
			Intent intent = new Intent(this, AddNewPatient.class);
			startActivity(intent);
		}
		else {
			Toast.makeText(this, "Only nurses can add new patients", Toast.LENGTH_SHORT).show();
		}
		
	}
	/**
	 * Handles the search by health card number function. If a valid search, starts
	 * PatientInfoActivity to display the patients information
	 * @param view
	 */
	public void searchClick(View view){
		Patient result = null;
		String hCardNumber = "";

		EditText healthCardText = (EditText) this.findViewById(R.id.searchHCtext);
		hCardNumber = healthCardText.getText().toString();
		
		
		if(!(hCardNumber == "")){
			result = EmergencyRoom.getInstance().getPatientByHCNum(hCardNumber);

			if(result!=null){
				//Valid result -> display patient in patientInfoActivity
				Intent intent = new Intent(this,PatientInfoActivity.class);
				intent.putExtra(EmergencyRoom.patientTag,result);
				startActivity(intent);
			}
			else{
				//Invalid Healthcard number entered
				Toast.makeText(this, "No patient found or invalid health card number", Toast.LENGTH_SHORT).show();
			}
		}else{
			//Edit text was empty
			Toast.makeText(this, "Enter a health card number", Toast.LENGTH_SHORT).show();
		}

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.patients_display, menu);
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
	 * Custom adapter for listview for displaying patients.
	 * Populates the textViews in each layout with the appropriate patient information.
	 */
	private class patientsAdapter extends BaseAdapter{
		Context context;
		int layoutId;
		ArrayList<Patient> patientList;
		/**
		 * Initializes the patientsAdapter
		 * @param context used for inflating layout if layout is null
		 * @param layoutId refers to the layout for each row
		 * @param patientList list of patients whos data to display
		 */
		public patientsAdapter(Context context,int layoutId,ArrayList<Patient> patientList){
			this.context = context;
			this.layoutId = layoutId;
			this.patientList = patientList;
		}
		
		@Override
		/**
		 * @return int size of list to make
		 */
		public int getCount() {
			return patientList.size();
		}

		@Override
		/**
		 * @param position in the list
		 * @return Patient object appropriate for that position
		 */
		public Object getItem(int position) {
			return patientList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		/**
		 * Populates specific row in the listview (called for every row)
		 * @param position an int that specifies the position in the listview
		 * @param convertView a View for the specific row of the listview
		 * @param parent the parent view (ListView) that the convertView will be attached to
		 * @return row a view populated with the patient info
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			//The patient for this position in the list
			final Patient thisPatient = (Patient) getItem(position);
			//if the row is null instantiate it from the given xml layoutID
			if(row == null){
				LayoutInflater mInflator = ((Activity) context).getLayoutInflater();
	            row = mInflator.inflate(layoutId, parent, false);
			}
			
			//Get the text views from the layout
			TextView nameTView = (TextView) row.findViewById(R.id.patientNameText);
			TextView healthCardTView = (TextView) row.findViewById(R.id.patientHealthNumerText);
			TextView birthDateTView = (TextView) row.findViewById(R.id.patientBirthText);
			
			//Assign values to the text views
			nameTView.setText(thisPatient.getName());
			healthCardTView.setText(thisPatient.getHealthCardNum());
			birthDateTView.setText(thisPatient.getBirthDate());
			
			//Manages the click of the row
			row.setOnClickListener(new View.OnClickListener() {
				
				@Override
				/**
				 * Handles click events of listview rows. Displays patient info in PatientInfoActivity
				 */
				public void onClick(View v) {
					//Launch the patientInfo class with the patient from this row
					Intent intent = new Intent(context,PatientInfoActivity.class);
					intent.putExtra(EmergencyRoom.patientTag, thisPatient);
					startActivity(intent);
				}
			});
			
			return row;
		}
		
	}
	/**
	 * Handles selections of spinner, populating the listview of patients depending
	 * on the selection.
	 * @param parent the parent that the selected view (row) is in (The spinner)
	 * @param view
	 * @param position of selection in the spinner options
	 * @param id
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		//Get list of patients
		String selection = (String) parent.getItemAtPosition(position);
		Log.d("SELECTION",selection);
		
		if(selection.equals(allPatientsSelection)){
			patients = (ArrayList<Patient>) EmergencyRoom.getInstance().getPatients();
		}
		if(selection.equals(sortedPatientsSelection)){
			patients = (ArrayList<Patient>) EmergencyRoom.getInstance().getUnseenSortedPatients();
		}
		
		//Get list view, and populate with adapter
		ListView patientsList = (ListView) findViewById(R.id.listView1);
		patientsList.setAdapter(new patientsAdapter(this,R.layout.patient_list_row,patients));
		
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	
}
