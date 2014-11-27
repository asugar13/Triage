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
 */
public class EmergencyRoom {
	/**Singleton storage of instance.*/
	private static EmergencyRoom instance = null;
	/**Constant used for passing patients by intents.*/
	public static final String PATIENT_TAG = "patient_tag";
	/**Map of all patients mapped by health card number.*/
	private Map<String, Patient> patients;
	/**Instance of DatabaseManager used to read/write to SQLiteDatabase.*/
	private DatabaseManager dbManager;
	/**UserType specified by a log in.*/
	private String userType;
	/**Context of the application used for reading from assets.*/
	private Context context;
	/**Constant, name of patient table in database.*/
	public static final String PATIENT_TABLE = "patient_records";
	/**Constant, name of patient records .txt file.*/
	public static final String PATIENT_TXT_FILE_NAME = "patient_records.txt";
	/**Constant name of passwords .txt file.*/
	public static final String PASSWORDS_TXT_FILE_NAME = "passwords.txt";
	/**Constant name of the login table (stores usernames,passwords,usertype) in the database.*/
	public static final String LOGIN_TABLE = "login_information";
	/**Constant a SimpleDateFormat object for the format "25-02-2014".*/
	public static final SimpleDateFormat SDF_NOTIME = new SimpleDateFormat("dd-MM-yyyy");
	/**Constant a SimpleDateFormat object for the format "25-02-2014 12:30 AM".*/
	public static final SimpleDateFormat SDF_TIME = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	
	/**
	 * Singleton, constructor protected.
	 */
	protected EmergencyRoom(){	
		patients = new TreeMap<String, Patient>();
	}
	/**
	 * Returns the instance of EmergencyRoom, instantiates it if doesn't exist.
	 * @return this instance of the Emergency Room.
	 */
	public static EmergencyRoom getInstance(){
		if(instance == null){
			instance = new EmergencyRoom();
		}
		return instance;	
	}
	/**
	 * Pass emergencyRoom application context for reading files from assets.
	 * @param context used to access the application's assets.
	 */
	public void setContext(Context context){
		this.context = context;
		//After context is set we can initialize the database
		dbManager = new DatabaseManager(context);
		dbManager.open();
		loadPatients(context,PATIENT_TXT_FILE_NAME);
	}
	/**
	 * Loads patients from database. If database doesn't exist loads from .txt file,
	 *  and copies to new database for future use.
	 * @param context used to access the applications assets.
	 * @param fileName name of file to open.
	 */
	private void loadPatients(Context context, String fileName) {
		if(dbManager.tableExists(PATIENT_TABLE)){
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
	 * Save all patients data to database for future use, 
	 * only executed first time the app is run.
	 */
	private void initPatientsDB(){
		Log.d("init","patients");
		//Creates patients table
		String[] patientColumns = {"'health_card_number'","'name'",
								"'date_of_birth'",
								"'vitals'","'prescriptions'"};
		String[] patientColumnTypes = {"TEXT","TEXT","TEXT","TEXT","TEXT"};
		dbManager.createTable(PATIENT_TABLE,patientColumns,patientColumnTypes);
		//Copies patients to database for future use.
		for(Patient p: getPatients()){
			savePatient(p);
		}
	}
	
	
	/**
	 * Gets the inputstream for a given filename in assets.
	 * @param context used to access the applications assets.
	 * @param fileName name of file to open.
	 * @return input stream of file for reading.
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
	 * Deprecated - uses SQLite database instead.
	 * Gets the outputstream for writing to a given file.
	 * 
	 * @param context used to write to apps internal storage.
	 * @param fileName name of file to be opened for writing.
	 * @return outputstream of given fileName.
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
	 * Returns a list of all of the patients.
	 * @return ArrayList of all patients.
	 */
	public ArrayList<Patient> getPatients() {
		ArrayList<Patient> allPatients = new ArrayList<Patient>();
		for (String key : patients.keySet()) {
			allPatients.add(patients.get(key));
		}

		return allPatients;
	}
	
	/**
	 * Returns sorted list of unseen patients sorted by urgency rating.
	 * @return ArrayList of sorted patients.
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
	 * Find a patient by healthcardNumber.
	 * @param hCardNum the health card number used to access a patient.
	 * @return Patient object, or null if no patient.
	 */
	public Patient getPatientByHCNum(String hCardNum) {
		return patients.get(hCardNum);
	}
	
	/**
	 * Updates the given patient.
	 * Necessary because .putExtra() passes a copy of Patient not reference.
	 * @param patient the patient being updated.
	 */
	public void updatePatient(Patient patient){
		//
		patients.put(patient.getHealthCardNum(), patient);
		
	}

	/**
	*Calculates the urgency rating of the patient and adds it to the patient.
	*@param patient The patient that is having their urgency calculate.
	*/
	public void calcUrgency(Patient patient){
		int urgency = 0;
		if (patient.getAge() < 2){
			urgency++;
		}
		
		Vitals vitals = patient.getVitals();
		TreeMap <Date, String[]> allVitals = vitals.getAllVitals();
		String temp = mostRecentVital(allVitals, 0);
		String diastolic = mostRecentVital(allVitals, 1);
		String systolic = mostRecentVital(allVitals, 2);
		String heartRate = mostRecentVital(allVitals, 3);
		try{
			if (!temp.equals("N/A")){
				if (Integer.parseInt(temp) >= 39){
					urgency++;
				}
			}
			boolean added = false;
			if (!diastolic.equals("N/A")){
				if (Integer.parseInt(diastolic) >= 90){
					urgency++;
					added = true;
				}
			if (!added){
				if (!systolic.equals("N/A")){
					if (Integer.parseInt(systolic) >= 140){
						urgency++;
					}
				}
			}
			
			}
			if (!heartRate.equals("N/A")){
				int HR = Integer.parseInt(heartRate);
				if (HR >= 100 || HR <= 50){
					urgency++;
				}
			}
		}
		catch (NoSuchElementException e){	
		}
		
		Log.d("URGENCY CALCULATED for " + patient.getName(),Integer.toString(urgency));
		patient.addUrgency(urgency);
		//do we want to save here?
	}
	
	/**
	 * Returns the most recently recorded specified vital
	 * 
	 * @param pos The specified vital by position
	 * @param allVitals A TreeMap of all of the Vitals recorded for a patient
	 * arranged by date
	 * @return currentVital The most recently recorded specified vital
	 * @return "N/A" Returns if there has been no recording of the specified vital
	 * in the past day.
	 */
	public String mostRecentVital(TreeMap<Date, String[]> allVitals, int pos){
		for (Date key: allVitals.descendingKeySet()){
			if (!past24Hours(key, allVitals.lastKey())){
				break;
			}
			String currentVital = allVitals.get(key)[pos];
			if ( (currentVital != null) && !currentVital.equals("N/A")){
				return currentVital;
			}
		}
		return "N/A";
	}
	
	/**
	 * Calculates whether two dates are within 24 hours of each other
	 * 
	 * @param d1 The first date
	 * @param d2 The second date
	 * @return true If the dates are within 24 hours of each other
	 * @return false if the dates are not within 24 hours of each other
	 */
	public boolean past24Hours(Date d1, Date d2){
		String[] dateString1 = SDF_TIME.format(d1).split(" ");
		String[] dateString2 = SDF_TIME.format(d2).split(" ");
		String [] dateSplit1 = dateString1[0].split("-");
		String [] dateSplit2 = dateString2[0].split("-");
		int day1 = Integer.parseInt(dateSplit1[0]) + 30 * Integer.parseInt(dateSplit1[1]);
		int day2 = Integer.parseInt(dateSplit2[0]) + 30 * Integer.parseInt(dateSplit2[1]);
		String[] time1 = dateString1[1].split(":");
		String[] time2 = dateString2[1].split(":");
		int date1 = day1*24*60 + Integer.parseInt(time1[0])*60 + Integer.parseInt(time1[1]);
		int date2 = day2*24*60 + Integer.parseInt(time2[0])*60 + Integer.parseInt(time2[1]);
		System.out.println(date1);
		System.out.println(date2);
		if (Math.abs(date1 - date2) <= 24*60){
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * Populates patients Map from text file.
	 * @param patients_stream inputStream from .txt file containing patients info.
	 * @throws FileNotFoundException.
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
	 * Deprecated - Uses SQLiteDatabase instead.
	 * Saves patient information to .txt file.
	 * @param context needed to save patients.text in the assets folder.
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
	 * Saves patient data to SQLite database.
	 * @param patient to be saved.
	 */
	public void savePatient(Patient patient){
		updatePatient(patient);
		ContentValues patientValues = new ContentValues();
		patientValues.put("health_card_number", patient.getHealthCardNum());
		patientValues.put("name", patient.getName());
		patientValues.put("date_of_birth", patient.getBirthDate());
		patientValues.put("vitals", patient.getVitals().toString());
		patientValues.put("prescriptions", patient.getPrescriptionString());
		
		if(dbManager.rowExists("patient_records","health_card_number = '" + patient.getHealthCardNum() + "'")){
			//Modify row
			String whereClause = "health_card_number = " + patient.getHealthCardNum();
			dbManager.modifyRow(PATIENT_TABLE,patientValues,whereClause);
		}else{
			//Add patient
			dbManager.addRow(patientValues, PATIENT_TABLE);
		}
	}
	
	/**
	 * Checks the validity of the given username and password
	 * If the loginTable does not exist in the database, creates the table 
	 *  and copies the information from passwords.txt to it.
	 * 
	 * @param username username to be checked.
	 * @param password password to be checked.
	 * @return whether the username and password combination is valid.
	 */
	public boolean logIn(String username, String password){
		if(dbManager.tableExists(LOGIN_TABLE)){
			//Check database for row with given username and password
			String sqlWhere = "username = '" + username + "' AND password = '" + password + "'";
			if(dbManager.rowExists(LOGIN_TABLE,sqlWhere)){
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
	 * Checks if the provided health card number already exists.
	 * @param healthCardNumber
	 */
	public boolean healthCardExists(String healthCardNum){
		return patients.keySet().contains(healthCardNum);
	}
	
	
	/**
	 * Loads the passwords, usernames, and usertypes from passwords.txt
	 * and copies to local SQLite database for future use.
	 */
	private void initLoginInfoDB(){
		Scanner scanner = new Scanner(getInputStream(PASSWORDS_TXT_FILE_NAME));
		ArrayList<String[]> usersLoginInfo = new ArrayList<String[]>();
		
		while(scanner.hasNextLine()){
			usersLoginInfo.add(scanner.nextLine().split(","));
		}
		//Create new table
		String[] columns = {"'username'","'password'","'user_type'"};
		String[] columnTypes = {"TEXT","TEXT","TEXT"};
		dbManager.createTable(LOGIN_TABLE,columns,columnTypes);
		
		//Stores login information in database for future use
		for(String[] userInfo: usersLoginInfo){
			ContentValues cv = new ContentValues();
			cv.put("username", userInfo[0]);
			cv.put("password", userInfo[1]);
			cv.put("user_type",userInfo[2]);
			dbManager.addRow(cv, LOGIN_TABLE);
		}
		
	}
	
	/**
	 * Sets the usertype given a successful login .
	 * @param sqlWhere which row to get userType from.
	 */
	private void setUserType(String sqlWhere){
		String[] columns = {"user_type"};
		Cursor c = dbManager.getRow(LOGIN_TABLE,sqlWhere,columns);
		c.moveToFirst();
		String userType = c.getString(0);
		this.userType = userType;
		
	}
	/**
	 * Gets the specified userType.
	 * @return String usertype of logged in user.
	 */
	public String getUserType() {
		return this.userType;
	}
	
	/**
	 * Loads the patients from the database to field patients.
	 */
	private void loadPatientsFromDB(){
		Cursor c = dbManager.getAllRows(PATIENT_TABLE);
		if (c.moveToFirst()){
			do{
				Vitals currentVitals;
				//Create vitals from string representation
				if(c.getString(3) != ""){
					currentVitals = new Vitals(c.getString(3).split("&"));
				}else{
					currentVitals = new Vitals();
				}
				
				TreeMap<Date, String> allPrescriptions = new TreeMap<Date, String>();
				
				//Parse prescription record string
				if(!TextUtils.isEmpty(c.getString(4))){
					String[] prescriptionDBString = c.getString(4).split("[\\x7C]");
					Log.d("ScriptInfo",Arrays.toString(prescriptionDBString));

					for(String s: prescriptionDBString){
						Date date = null;
						String scriptInfo = "";
						String[] currentScript = s.split(Pattern.quote("*"));

						Log.d("CURRENTSCTIPT",Arrays.toString(currentScript));
						try {
							date = SDF_NOTIME.parse(currentScript[0]);
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
	


