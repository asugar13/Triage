package defaultPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.TreeMap;

import android.util.Log;
/**
 * This class represents patients in a Hospital emergency Room.
 * The class stores patients personal information, as well as vital information,
 * and prescription information.
 */
public class Patient implements Serializable{
	/**SerialVersion Id used for passing Patient in Intents.*/
	private static final long serialVersionUID = 1280942052796823017L;
	/**Name of patient.*/
	private String[] name;
	/**BirthDate of patient.*/
	private String birthDate;
	/**HealthCard number of patient.*/
	private String healthCardNumber;
	/**Patient seen by doctor time.*/
	private Date seenByDoctor;
	/**Patient seen by doctor status.*/
	private boolean seenByDoctorStatus;
	/**Patients vitals, stores all readings.*/
	private Vitals vitals;
	/**Patients assigned urgency.*/
	private int urgency = -1;
	/**All of patients prescriptions past and present.*/
	private TreeMap<Date, String> allPrescriptions;
	
	/** 
	 * Constructor for Patient class, instantiates the patient.
	 * @param name This patient's name.
	 * @param birthdate This Patient's date of birth.
	 * @param hcn This Patient's health card number.
	 * @param vitals This patient's vitals (vital signs and symptoms).
	 */
	public Patient(String[] name, String birthdate, String hcn, Vitals vitals,TreeMap<Date,String> allPrescriptions) {
		this.name = name.clone();
		this.birthDate = birthdate;
		this.healthCardNumber = hcn;
		this.vitals = vitals;
		this.seenByDoctorStatus = false;
		this.allPrescriptions = allPrescriptions;
		
		Log.d("Allscript",Arrays.asList(allPrescriptions).toString());
		if(!vitals.isEmpty){
			EmergencyRoom.getInstance().calcUrgency(this);
		}else{
			Log.d("HERE vitals empty",Integer.toString(getAge()));
			if(getAge() < 2){
				urgency = 1;
				}			
		}	
	}
	
	/**
	 * Sets the time that the patient has been seen by a doctor.
	 * @param timeSeen date object of the time patient was seen by a doctor.
	 */
	public void setSeenByDoctor(Date timeSeen){
		this.seenByDoctorStatus = true;
		this.seenByDoctor = timeSeen;
		EmergencyRoom.getInstance().savePatient(this);
	}
	
	/**
	 * Gets the patient's name.
	 * @return the patients name as a string.
	 */
	public String getName(){
		String nameString = "";
		for(String n: name){
			nameString = nameString + n + " ";
		}
		return nameString;
	}
	
	/**
	 * Gets this patient's birthdate.
	 * @return This patient's birth date formatted as a string.
	 */
	public String getBirthDate(){
		return birthDate;
	}
	
	/**
	 * Gets the patients age in years
	 * @return int patients age in years
	 */
	public int getAge(){
		String[] birthDateSplit = birthDate.split("-");
		int birthDay = Integer.parseInt(birthDateSplit[0])*365 + Integer.parseInt(birthDateSplit[1]) * 30 + Integer.parseInt(birthDateSplit[2]);
		String[] currentDate = EmergencyRoom.SDF_NOTIME.format(new Date()).split("-");
		int currentDay = Integer.parseInt(currentDate[0])+ Integer.parseInt(currentDate[1]) * 30 + Integer.parseInt(currentDate[2]) * 365 ;
		int age = ((currentDay - birthDay)) / 365;
		return age;
	}
	
	
	/**
	 * Returns this patient's health card number.
	 * @return This patient's health card number.
	 */
	public String getHealthCardNum(){
		return healthCardNumber;
	}
	
	/**
	* Adds new vitals (vital signs and symptoms) to this Patient and updates the Emergency Room.
	* @param newVitals New vitals to be added to this patient.
	*/
	public void addVitals(String[] newVitals){
		this.vitals.add(newVitals);
		EmergencyRoom.getInstance().calcUrgency(this);
		EmergencyRoom.getInstance().savePatient(this);		
	}
	
	/**
	 *Gets this pateint's vitals and symptoms. 
	 *@return vitals object that belongs to this patient.
	 */
	public Vitals getVitals(){
		return vitals;
	}
	
	/**
	 * Adds and urgency rating to this patient.
	 * @param urgency rating to be assigned to patient.
	 */
	public void addUrgency(int urgency){
		this.urgency = urgency;
	}
	
	/**
	*Gets this patient's urgency.
	*@return urgency The urgency rating of this patient.
	*/
	public int getUrgency(){
		return urgency;
	}
	
	/**
	 * Gets the status of whether this patient has been seen by a doctor.
	 * @return boolean if patient has been seen by a doctor.
	 */
	public boolean getSeenByDoctorStatus(){
		return this.seenByDoctorStatus;
	}
	
	/**
	 * Returns the time of when the patient had been seen by a doctor.
	 * @return Date that patient has been seen by a doctor.
	 */
	public Date getSeenByDoctor(){
		return this.seenByDoctor;
	}
	/**
	 * Adds prescription information to the patient.
	 * @param scriptInfo String representing the prescription.
	 */
	public void addPrescription(String scriptInfo) {
		Date date = new Date();
		allPrescriptions.put(date, scriptInfo);
		EmergencyRoom.getInstance().savePatient(this);	
	}
	
	/**
	 * Generates a string representation of TreeMap<Date, String> allPrescriptions.
	 * @return String representation of TreeMap<Date, String> allPrescriptions.
	 */
	public String getPrescriptionString(){
		String scriptString = "";
		ArrayList<Date> keys = new ArrayList<Date>(allPrescriptions.keySet());
		
		for(int i =0; i< keys.size(); i++){
			Date this_date = keys.get(i);
			scriptString = scriptString + EmergencyRoom.SDF_TIME_SEC.format(this_date) + "*";
			scriptString = scriptString + allPrescriptions.get(keys.get(i));
			if(!(i == keys.size() - 1)){
				//If not the last prescription entry
				scriptString = scriptString + "|";
			}
		}
		return scriptString;
	}
	
	/**
	 * Returns list of all prescriptions returned in the format:
	 * 25-04-2014 + \n  + Medication Name and Info.
	 * @return String representing prescription entries.
	 */
	public String[] getPrescriptions(){
		ArrayList<String> allScripts = new ArrayList<String>();
		ArrayList<Date> keys = new ArrayList<Date>(allPrescriptions.keySet());
		for(int i = 0; i< allPrescriptions.size();i++){
			allScripts.add(EmergencyRoom.SDF_TIME.format(keys.get(i)) + "\n" +
						allPrescriptions.get(keys.get(i)));
		}
		Collections.reverse(allScripts);
		return allScripts.toArray(new String[allScripts.size()]);
	}
	
	/**
	 * Returns most recent prescription returned in format:
	 * 25-04-2014 + \n  + Medication Name and Info.
	 * @return String representing prescription entries.
	 */
	public String getMostRecentPrescription(){
		return EmergencyRoom.SDF_NOTIME.format(allPrescriptions.firstKey()) + "\n" +  allPrescriptions.firstEntry(); 
	}
	
	/**
	 * Generates string representation of patient.
	 * @return The string representation of this Patient object.
	 */
	@Override
	public String toString(){
		return this.healthCardNumber + "," + getName() + "," + birthDate + ","+ vitals.toString();
	}	
}