package defaultPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

/**
 * This class represents the hospital's emergency room.  
 * The class manages the loading and saving of patients to internal storage.
 * It also stores the patients during run time and calculates urgency based on 
 * patients information. 
 * The emergency room also authenticates users attempting to log in to the app.
 *  
 */
public class EmergencyRoom {
	private static EmergencyRoom instance = null;
	public static final String patientTag = "patient_tag";
	private Map<String, Patient> patients;
	private DatabaseManager dbManager;
	private String userType;
	private Context context;
	private class InvalidTypeException extends Exception{};
	
	public static final String patientTable = "patient_records";
	public static final String patientsTxtFileName = "patient_records.txt";
	public static final String passwordsTxtFileName = "passwords.txt";
	public static final String loginTable = "login_information";
	public static final SimpleDateFormat sdfNoTime = new SimpleDateFormat("dd-MM-yyyy");
	public static final SimpleDateFormat sdfTime = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	/**
	 * Singleton, constructor protected
	 */
	protected EmergencyRoom(){	
		patients = new TreeMap<String, Patient>();
	}
	/**
	 * Returns the instance of EmergencyRoom
	 * 
	 * @return this instance of the Emergency Room
	 */
	public static EmergencyRoom getInstance(){
		if(instance == null){
			instance = new EmergencyRoom();
		}
		return instance;	
	}
	/**
	 * Pass emergencyRoom application context for reading files from assets.
	 * 
	 * @param context used to access the application's assets
	 */
	public void setContext(Context context){
		this.context = context;
		//After context is set we can initialize the database
		dbManager = new DatabaseManager(context);
		dbManager.open();
		loadPatients(context,patientsTxtFileName);

	}
	
	/**
	 * Loads patients from database
	 * If database doesn't exist loads from .txt file, and copies to new database for future use
	 * 
	 * @param context used to access the applications assets
	 * @param fileName name of file to open
	 */
	private void loadPatients(Context context, String fileName) {
		
		if(dbManager.tableExists(patientTable)){
			//Read from database
			loadPatientsFromDB();
			Log.d("Patients TABLE","EXISTS");
		}else{
			Log.d("Patients TABLE","DOES NOT EXIST");
			//Read from textfile - ONLY HAPPENS FIRST TIME THE APP IS RUN
			try {
				populate(getInputStream(fileName));
			} catch (Exception e) { // Change to specific exception
				e.printStackTrace();
			}
			//Save patient data to database for future use.
			initPatientsDB();
			}
	
		}
	
	/**
	 * Save all patients data to database for future use
	 * Only executed first time the app is run.
	 */
	private void initPatientsDB(){
		Log.d("init","patients");
		//Creates patients table
		String[] patientColumns = {"'health_card_number'","'name'",
								"'date_of_birth'","'seen_by_doctor'",
								"'vitals'","'prescriptions'"};
		String[] patientColumnTypes = {"TEXT","TEXT","TEXT","TEXT","TEXT","TEXT"};
		dbManager.createTable(patientTable,patientColumns,patientColumnTypes);
		//Copies patients to database for future use.
		for(Patient p: getPatients()){
			savePatient(p);
		}
	}
	
	
	/**
	 * Gets the inputstream for a given filename in assets
	 * 
	 * @param context used to access the applications assets
	 * @param fileName name of file to open
	 * @return input stream of file for reading
	 */
	private  InputStream getInputStream(String fileName) {
		try {
			return context.getAssets().open(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d("File not found","!");
		return null;
	}
	
	/**
	 * Deprecated - uses SQLite database instead
	 * Gets the outputstream for writing to a given file
	 * 
	 * @param context used to write to apps internal storage
	 * @param fileName name of file to be opened for writing
	 * @return outputstream of given fileName
	 */
	private FileOutputStream getOutputStream(String fileName) {
		try {
			//Tries to open file from /data/data/Files for reading
			return context.openFileOutput(fileName,
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
	 * 
	 * @return ArrayList of all patients
	 */
	public ArrayList<Patient> getPatients() {
		ArrayList<Patient> allPatients = new ArrayList<Patient>();
		for (String key : patients.keySet()) {
			allPatients.add(patients.get(key));
		}

		return allPatients;
	}
	
	/**
	 * Returns sorted list of unseen patients sorted by urgency rating
	 * 
	 * @return ArrayList of sorted patients
	 */
	public ArrayList<Patient> getUnseenSortedPatients(){
		ArrayList<Patient> sortedPatients = new ArrayList<Patient>();
		ArrayList<Patient> unsortedPatients = getPatients();
		for (Patient p: unsortedPatients){
			if (!p.getSeenByDoctorStatus()){
				sortedPatients.add(p);
			}
		}
		Collections.sort(sortedPatients, new Comparator<Patient>() {
	        @Override public int compare(Patient p1, Patient p2) {
	            return p1.getUrgency()- p2.getUrgency();//ascending
	        }
		});
		Collections.reverse(sortedPatients);
		return sortedPatients;
	}
	

	/**
	 * Find a patient by healthcardNumber
	 * 
	 * @param hCardNum the health card number used to access a patient
	 * @return Patient object, or null if no patient
	 */
	public Patient getPatientByHCNum(String hCardNum) {
		return patients.get(hCardNum);
	}
	
	/**
	 * Updates the given patient
	 * Necessary because .putExtra() passes a copy of Patient not reference
	 * 
	 * @param patient the patient being updated
	 */
	public void updatePatient(Patient patient){
		//
		patients.put(patient.getHealthCardNum(), patient);
		
	}

	/**
	*Calculates the urgency rating of the patient and adds it to the patient
	*
	*@param patient The patient that is having their urgency calculate.
	*/
	public void calcUrgency(Patient patient){
		int urgency = 0;
		String[] birthDate = patient.getBirthDate().split("-");
		int birthDay = Integer.parseInt(birthDate[0]) + Integer.parseInt(birthDate[1]) * 30 + Integer.parseInt(birthDate[2]) * 365;
		String[] currentDate = sdfNoTime.format(new Date()).split("-");
		int currentDay = Integer.parseInt(currentDate[0]) * 365 + Integer.parseInt(currentDate[1]) * 30 + Integer.parseInt(currentDate[2]);
		float age = ((float) (currentDay - birthDay)) / 365;
		if (age < 2){
			urgency++;
		}
		
		Vitals vitals = patient.getVitals();
		TreeMap <Date, String[]> allVitals = vitals.getAllVitals();
		try{
			Log.d("Vitals", vitals.toString() + "empty");
			String[] mostRecentVitals = allVitals.get(allVitals.firstKey());
			if (mostRecentVitals[0] != null && mostRecentVitals[0] != "N/A"){
				int temp = Integer.parseInt(mostRecentVitals[0]);
				if (temp >= 39){
					urgency++;
				}
			}
			if (mostRecentVitals[1] != null && mostRecentVitals[1] != "N/A"){
				int diastolic = Integer.parseInt(mostRecentVitals[1]);
				if (diastolic >= 90){
					urgency++;
				}
				else if (mostRecentVitals[2] != null && mostRecentVitals[2] != "N/A"){
					int systolic = Integer.parseInt(mostRecentVitals[2]);
					if (systolic >= 140){
						urgency++;
					}
				}
			}
			if (mostRecentVitals[3] != null && mostRecentVitals[3] != "N/A"){
				int heartRate = Integer.parseInt(mostRecentVitals[3]);
				if (heartRate >= 100 || heartRate <= 50){
					urgency++;
				}
			}
		}
		catch (NoSuchElementException e){	
		}
		patient.addUrgency(urgency);
		//do we want to save here?
	}
	
	/**
	 * Populates patients Map from text file
	 * 
	 * @param patients_stream inputStream from .txt file containing patients info
	 * @throws FileNotFoundException
	 */
	private void populate(InputStream patients_stream)
			throws FileNotFoundException {
		Scanner scanner = new Scanner((patients_stream));
		String[] patient_on_file;
		while (scanner.hasNextLine()) {

			patient_on_file = scanner.nextLine().split(",");
			String hcn = patient_on_file[0];
			String birthdate = patient_on_file[2];
			String[] name = patient_on_file[1].split(" ");
			if (patient_on_file.length == 3) {
				patients.put(hcn, new Patient(name, birthdate, hcn,
						new Vitals(),new TreeMap<Date,String>()));
			} else {
				String[] vitalInfo = patient_on_file[3].split("&");
				patients.put(hcn, new Patient(name, birthdate, hcn, new Vitals(
						vitalInfo),new TreeMap<Date,String>()));
			}
			
		}
		scanner.close();
	}
	/**
	 * Deprecated - Uses SQLiteDatabase instead
	 * Saves patient information to .txt file
	 * 
	 * @param context needed to save patients.text in the assets folder
	 */
	public void savePatientData(Context context) {
		try {
			OutputStream outStream = getOutputStream("patient_records.txt");
			for (String hc : patients.keySet()) {
				Log.d("PATIENTSTOSTRING",patients.get(hc).toString());
				outStream.write((patients.get(hc).toString() + "\n")
						.getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves patient data to SQLite database
	 * 
	 * @param patient to be saved
	 */
	public void savePatient(Patient patient){
		updatePatient(patient);
		ContentValues patientValues = new ContentValues();
		patientValues.put("health_card_number", patient.getHealthCardNum());
		patientValues.put("name", patient.getName());
		patientValues.put("seen_by_doctor", "false");
		patientValues.put("date_of_birth", patient.getBirthDate());
		patientValues.put("vitals", patient.getVitals().toString());
		patientValues.put("prescriptions", patient.getPrescriptionString());
		
		if(dbManager.rowExists("patient_records","health_card_number = '" + patient.getHealthCardNum() + "'")){
			//Modify row
			String whereClause = "health_card_number = " + patient.getHealthCardNum();
			dbManager.modifyRow(patientTable,patientValues,whereClause);
		}else{
			//Add patient
			dbManager.addRow(patientValues, patientTable);
		}
	}
	
	/**
	 * Checks the validity of the given username and password
	 * If the loginTable does not exist in the database, creates the table 
	 *  and copies the information from passwords.txt to it.
	 * 
	 * @param username username to be checked
	 * @param password password to be checked
	 * @return whether the username and password combination is valid
	 */
	public boolean logIn(String username, String password){
		if(dbManager.tableExists(loginTable)){
			//Check database for row with given username and password
			String sqlWhere = "username = '" + username + "' AND password = '" + password + "'";
			if(dbManager.rowExists(loginTable,sqlWhere)){
				//Login successful
				setUserType(sqlWhere);
				return true;
			}else{
				//Login failed
				return false;
			}	
		}else{
			//First time app has been run, username password info in .txt file
			initLoginInfoDB();
			return logIn(username,password);	
		}
	}
	
	
	/**
	 * Loads the passwords, usernames, and usertypes from passwords.txt
	 * and copies to local SQLite database for future use
	 */
	private void initLoginInfoDB(){
		Scanner scanner = new Scanner(getInputStream(passwordsTxtFileName));
		ArrayList<String[]> usersLoginInfo = new ArrayList<String[]>();
		
		while(scanner.hasNextLine()){
			usersLoginInfo.add(scanner.nextLine().split(","));
		}
		//Create new table
		String[] columns = {"'username'","'password'","'user_type'"};
		String[] columnTypes = {"TEXT","TEXT","TEXT"};
		dbManager.createTable(loginTable,columns,columnTypes);
		
		//Stores login information in database for future use
		for(String[] userInfo: usersLoginInfo){
			ContentValues cv = new ContentValues();
			cv.put("username", userInfo[0]);
			cv.put("password", userInfo[1]);
			cv.put("user_type",userInfo[2]);
			dbManager.addRow(cv, loginTable);
		}
		
	}
	
	/**
	 * Sets the usertype given a successful login 
	 * @param sqlWhere
	 */
	private void setUserType(String sqlWhere){
		String[] columns = {"user_type"};
		Cursor c = dbManager.getRow(loginTable,sqlWhere,columns);
		c.moveToFirst();
		String userType = c.getString(0);
		this.userType = userType;
		
	}
	
	public String getUserType() {
		return this.userType;
	}
	
	/**
	 * Loads the patients from the database to field patients
	 */
	private void loadPatientsFromDB(){
		Cursor c = dbManager.getAllRows(patientTable);
		if (c.moveToFirst()){
			do{
				Vitals currentVitals;
				//Create vitals from string representation
				if(c.getString(4) != ""){
					currentVitals = new Vitals(c.getString(4).split("&"));
				}else{
					currentVitals = new Vitals();
				}
				
				TreeMap<Date, String> allPrescriptions = new TreeMap<Date, String>();
				
				//Parse prescription record string
				if(!TextUtils.isEmpty(c.getString(5))){
					String[] prescriptionDBString = c.getString(5).split("[\\x7C]");
					for(String s: prescriptionDBString){
						Date date = null;
						String scriptInfo = "";
						String[] currentScript = s.split(Pattern.quote("*"));
						try {
							date = sdfNoTime.parse(currentScript[0]);
							scriptInfo = currentScript[1];
							Log.d("scriptInfo",scriptInfo + "Empty");
							allPrescriptions.put(date, scriptInfo);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}

				}
				
				Patient currentPatient = new Patient(c.getString(1).split(" "),c.getString(2),c.getString(0),currentVitals,allPrescriptions);
				patients.put(c.getString(0), currentPatient);
				c.moveToNext();
			}while(!c.isAfterLast());
		}
		
	}
	
	}
	


