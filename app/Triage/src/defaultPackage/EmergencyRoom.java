package defaultPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * 
 * @author Asier
 * 
 */
public class EmergencyRoom {
	private static Map<String, Patient> patients;
	private static DatabaseManager dbManager;
	private static String userType;
	private class InvalidTypeException extends Exception{};
	public static final String patientTable = "patient_records";
	public static final String loginTable = "login_information";
	public static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");


	/**
	 * TEMPORARY, ACTS AS a constructor kinda, will fix and changle EmergencyRoom to singleton once servers are up
	 * @param context
	 * @param fileName
	 */
	
	//FILENAME NOT USED
	public static void loadPatients(Context context, String fileName) {
		dbManager = new DatabaseManager(context);
		patients = new TreeMap<String, Patient>();
		
		if(dbManager.databaseExists()){
			//Read from database
			
			dbManager.open();
			loadPatientsFromDB();
			Log.d("EXISTS","EXISTS");
		}else{
			//Read from textfile
			try {
				
				populate(openFile(context, fileName));
			} catch (Exception e) { // Change to specific exception
				e.printStackTrace();
			}
			//Save to patient data to database for future use
			Log.d("PATIENTS",Arrays.asList(getPatients()).toString());
			dbManager.open();
			for(Patient p: getPatients()){
				savePatient(p);
			}
			
			
		}
	}
	
	/**
	 * Gets the inputstream for a given filename
	 * @param context
	 * @param fileName
	 * @return input stream for reading
	 */
	private static InputStream openFile(Context context, String fileName) {
		InputStream is = null;
		try {
			//Tries to read from data/data/Files directory
			is = context.openFileInput(fileName);
		} catch (FileNotFoundException e) {
			//If file not found, get .txt from assets
			try {
				return context.getAssets().open(fileName);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return is;
	}
	/**
	 * Gets the outputstream for writing to a given file
	 * @param context
	 * @param fileName
	 * @return
	 */
	private static FileOutputStream getOutputStream(Context context, String fileName) {
		try {
			//Tries to open file from /data/data/Files for reading
			return context.openFileOutput("patient_records.txt",
					context.MODE_PRIVATE);
			
		} catch (FileNotFoundException e) {
			//If file does not exist, creates it
			boolean fileCreated = new File(context.getFilesDir()+ "/patient_records.txt").mkdir();
			if (fileCreated) {
				try {
					return context.openFileOutput("patient_records.txt",
							context.MODE_PRIVATE);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * Returns a list of all of the patients
	 * @return ArrayList of all patients
	 */
	public static ArrayList<Patient> getPatients() {
		ArrayList<Patient> allPatients = new ArrayList<Patient>();
		for (String key : patients.keySet()) {
			allPatients.add(patients.get(key));
		}

		return allPatients;
	}
	/**
	 * Returns sorted list of unseen patients sorted by urgency
	 * @return ArrayList of sorted patients
	 */
	public static ArrayList<Patient> getUnseenSortedPatients(){
		ArrayList<Patient> sortedPatients = new ArrayList<Patient>();
		ArrayList<Patient> unsortedPatients = new ArrayList<Patient>();
		unsortedPatients.addAll((Collection<? extends Patient>) patients.entrySet());
		for (Patient p: unsortedPatients){
			if (p.getSeenByDoctorStatus()){
				sortedPatients.add(p);
			}
		}
		Collections.sort(sortedPatients, new Comparator<Patient>() {
	        @Override public int compare(Patient p1, Patient p2) {
	            return p2.getUrgency()- p1.getUrgency();//descending
	        }
		});
		return sortedPatients;
	}
	

	/**
	 * Find a patient by healthcardNumber
	 * @param hCardNum
	 * @return Patient object, or null if no patient
	 */
	public static Patient getPatientByHCNum(String hCardNum) {
		return patients.get(hCardNum);
	}
	
	/**
	 * Update the patient in emergency room, vitals have been added
	 * @param patient
	 */
	public static void updatePatient(Patient patient){
		//Necessary becuase .putExtra() passes a copy of Patient not reference
		patients.put(patient.getHealthCardNum(), patient);
		
	}

	/**
	*Calculates the urgency rating of the patient and adds to the patient's record
	*
	*@param patient The patient that is having their urgency calculate.
	*/
	public static void calcUrgency(Patient patient){
		int urgency = 0;
		String[] birthDate = patient.getBirthDate().split("-");
		int birthDay = Integer.parseInt(birthDate[0]) + Integer.parseInt(birthDate[1]) * 12 + Integer.parseInt(birthDate[2]) * 365;
		String[] currentDate = sdf.format(new Date()).split("-");
		int currentDay = Integer.parseInt(currentDate[0]) * 365 + Integer.parseInt(currentDate[1]) * 12 + Integer.parseInt(currentDate[2]);
		float age = ((float) (currentDay - birthDay)) / 365;
		Vitals vitals = patient.getVitals();
		
		String[] vitSymp = vitals.getAllVitals().get(vitals.getAllVitals().firstKey());
		int temp = Integer.parseInt(vitSymp[0]);
		int diastolic = Integer.parseInt(vitSymp[1]);
		int systolic = Integer.parseInt(vitSymp[2]);
		int heartRate = Integer.parseInt(vitSymp[3]);
		if (age < 2){
			urgency++;
		}
		if (temp >= 39){
			urgency++;
		}
		if (systolic >= 140){
			urgency++;
		}
		else if (diastolic >= 90){
			urgency++;
		}
		if (heartRate >= 100 || heartRate <= 50){
			urgency++;
		}
		patient.addUrgency(urgency);
	}
	
	/**
	 *  Populates patients Map 
	 * @param patients_stream inputStream from .txt file containing patients info
	 * @throws FileNotFoundException
	 */
	public static void populate(InputStream patients_stream)
			throws FileNotFoundException {
		Log.d("POPULATE","CALLED");
		Scanner scanner = new Scanner((patients_stream));
		String[] patient_on_file;
		while (scanner.hasNextLine()) {

			patient_on_file = scanner.nextLine().split(",");
			String hcn = patient_on_file[0];
			String birthdate = patient_on_file[2];
			String[] name = patient_on_file[1].split(" ");
			if (patient_on_file.length == 3) {
				patients.put(hcn, new Patient(name, birthdate, hcn,
						new Vitals()));
			} else {
				String[] vitalInfo = patient_on_file[3].split("&");
				patients.put(hcn, new Patient(name, birthdate, hcn, new Vitals(
						vitalInfo)));
			}
		}
		scanner.close();
	}
	
//	/**
//	 * Saves patient information to .txt file
//	 * @param context
//	 */
//	public static void savePatientData(Context context) {
//		try {
//			OutputStream outStream = getOutputStream(context,"patient_records.txt");
//			for (String hc : patients.keySet()) {
//				Log.d("PATIENTSTOSTRING",patients.get(hc).toString());
//				outStream.write((patients.get(hc).toString() + "\n")
//						.getBytes());
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * Saves patient data to SQLite database
	 * @param patient
	 */
	public static void savePatient(Patient patient){
		ContentValues patientValues = new ContentValues();
		patientValues.put("health_card_number", patient.getHealthCardNum());
		patientValues.put("name", patient.getName());
		patientValues.put("seen_by_doctor", "false");
		patientValues.put("date_of_birth", patient.getBirthDate());
		patientValues.put("vitals", patient.getVitals().toString());
		
		if(dbManager.rowExists("patient_records","health_card_number = " + patient.getHealthCardNum())){
			//Modify row
			String whereClause = "health_card_number = " + patient.getHealthCardNum();
			dbManager.modifyRow(patientTable,patientValues,whereClause);
		}else{
			//Add patient

			dbManager.addRow(patientValues, patientTable);
		}
	}
	
	
	public boolean logIn(String username, String password){
		//Change to work with .txt file
		String sqlWhere = "username = " + username + " AND password = " + password;
		if(dbManager.rowExists(loginTable,sqlWhere)){
			//Login successful
			setUserType(sqlWhere);
			return true;
		}else{
			//Login failed
			return false;
		}
	}
	
	private void setUserType(String sqlWhere){
		String[] columns = {"user_type"};
		Cursor c = dbManager.getRow(loginTable,sqlWhere,columns);
		String userType = c.getString(0);
		this.userType = userType;
		
	}
	
	private static void loadPatientsFromDB(){
		Cursor c = dbManager.getAllRows(patientTable);
		if (c.moveToFirst()){
			do{

				Vitals currentVitals;
				if(c.getString(4) !=null){
					currentVitals = new Vitals(c.getString(4).split("&"));
				}else{
					currentVitals = new Vitals();
				}
				Patient currentPatient = new Patient(c.getString(1).split(" "),c.getString(2),c.getString(0),currentVitals);
				patients.put(c.getString(0), currentPatient);
				
				c.moveToNext();
			}while(!c.isAfterLast());
		}
		
	}
	
	}
	


