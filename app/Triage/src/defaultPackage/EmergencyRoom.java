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
	private static EmergencyRoom instance = null;
	private Map<String, Patient> patients;
	private DatabaseManager dbManager;
	private String userType;
	private Context context;
	private class InvalidTypeException extends Exception{};
	
	public static final String patientTable = "patient_records";
	public static final String patientsTxtFileName = "patient_records.txt";
	public static final String passwordsTxtFileName = "passwords.txt";
	public static final String loginTable = "login_information";
	public static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	/**
	 * Signleton, constructor protected
	 */
	protected EmergencyRoom(){	
		patients = new TreeMap<String, Patient>();
	}
	/**
	 * Returns the instance of EmergencyRoom
	 * @return
	 */
	public static EmergencyRoom getInstance(){
		if(instance == null){
			instance = new EmergencyRoom();
		}
		return instance;	
	}
	/**
	 * Pass emergencyRoom application context for reading files from assets.
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
	 * @param context
	 * @param fileName
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
	 * Gets the inputstream for a given filename
	 * @param context
	 * @param fileName
	 * @return input stream for reading
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
	 * Gets the outputstream for writing to a given file
	 * @param context
	 * @param fileName
	 * @return
	 */
	private FileOutputStream getOutputStream(String fileName) {
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
	public ArrayList<Patient> getPatients() {
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
	public ArrayList<Patient> getUnseenSortedPatients(){
		ArrayList<Patient> sortedPatients = new ArrayList<Patient>();
		ArrayList<Patient> unsortedPatients = getPatients();
		for (Patient p: unsortedPatients){
			if (p.getSeenByDoctorStatus()){
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
	 * @param hCardNum
	 * @return Patient object, or null if no patient
	 */
	public Patient getPatientByHCNum(String hCardNum) {
		return patients.get(hCardNum);
	}
	
	/**
	 * Update the patient in emergency room, vitals have been added
	 * @param patient
	 */
	public void updatePatient(Patient patient){
		//Necessary becuase .putExtra() passes a copy of Patient not reference
		patients.put(patient.getHealthCardNum(), patient);
		
	}

	/**
	*Calculates the urgency rating of the patient and adds to the patient's record
	*
	*@param patient The patient that is having their urgency calculate.
	*/
	public void calcUrgency(Patient patient){
		int urgency = 0;
		String[] birthDate = patient.getBirthDate().split("-");
		int birthDay = Integer.parseInt(birthDate[0]) + Integer.parseInt(birthDate[1]) * 30 + Integer.parseInt(birthDate[2]) * 365;
		String[] currentDate = sdf.format(new Date()).split("-");
		int currentDay = Integer.parseInt(currentDate[0]) * 365 + Integer.parseInt(currentDate[1]) * 30 + Integer.parseInt(currentDate[2]);
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
	 *  Populates patients Map from text file
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
	public void savePatient(Patient patient){
		updatePatient(patient);
		ContentValues patientValues = new ContentValues();
		patientValues.put("health_card_number", patient.getHealthCardNum());
		patientValues.put("name", patient.getName());
		patientValues.put("seen_by_doctor", "false");
		patientValues.put("date_of_birth", patient.getBirthDate());
		patientValues.put("vitals", patient.getVitals().toString());
		//patientValues.put("prescriptions", patient.getPrescriptions());
		
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
	 * @param username
	 * @param password
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
	 * Loads the passwords,usernames, and usertypes from passwords.txt
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
	/**
	 * Loads the patients from the database to field patients
	 */
	private void loadPatientsFromDB(){
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
	


