package defaultPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import android.util.Log;
/**
 * This class represents patients in a Hospital emergency Room.
 * The class stores patients personal information, as well as vital information,
 * and prescription information.
 */
public class Patient implements Serializable{
	
	
	private static final long serialVersionUID = 1280942052796823017L;
	private String[] name;
	private String birthDate;
	private String healthCardNumber;
	private Date seenByDoctor;
	private boolean seenByDoctorStatus;
	private Vitals vitals;
	private int urgency;
	private TreeMap<Date, String> allPrescriptions;
	
	/** Constructor for Patient class.
	 * 
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
		
		if(!vitals.isEmpty){
			//Was causing crashes, not sure if due to user generated vitals???
			EmergencyRoom.getInstance().calcUrgency(this);
		}	
	}
	
	/**
	 * Sets the time that the patient has been seen by a doctor
	 * 
	 * @param timeSeen date object of the time patient was seen by a doctor
	 */
	public void setSeenByDoctor(Date timeSeen){
		this.seenByDoctorStatus = true;
		this.seenByDoctor = timeSeen;
		EmergencyRoom.getInstance().savePatient(this);
		
	}
	
	/**
	 * Gets the patient's name
	 * 
	 *@return the patients name as a string
	 */
	public String getName(){
		String nameString = "";
		for(String n: name){
			nameString = nameString + n + " ";
		}
		return nameString;
	}
	
	/**
	 * Gets this patient's birthdate
	 * 
	 *@return This patient's birth date formatted as a string.
	 */
	public String getBirthDate(){
		return birthDate;
	}
	
	/**
	 * Returns this patient's health card number.
	 * 
	 * @return This patient's health card number.
	 */
	public String getHealthCardNum(){
		return healthCardNumber;
	}
	
	/**
	* Adds new vitals (vital signs and symptoms) to this Patient and updates the Emergency Room.
	* 
	* @param newVitals New vitals to be added to this patient.
	*/
	public void addVitals(String[] newVitals){
		this.vitals.add(newVitals);
		EmergencyRoom.getInstance().savePatient(this);
		EmergencyRoom.getInstance().calcUrgency(this);
		
	}
	/**
	 *Gets this pateint's vitals and symptoms 
	 * 
	 *@return vitals object that belongs to this patient
	 */
	public Vitals getVitals(){
		return vitals;
	}
	
	/**
	 * Adds and urgency rating to this patient
	 * 
	 * @param urgency rating to be assigned to patient
	 */
	public void addUrgency(int urgency){
		this.urgency = urgency;
	}
	
	/**
	*Gets this patient's urgency
	*
	*@return urgency The urgency rating of this patient.
	*/
	public int getUrgency(){
		return urgency;
	}
	
	/**
	 * Gets the status of whether this patient has been seen by a doctor.
	 * 
	 * @return boolean if patient has been seen by a doctor
	 */
	public boolean getSeenByDoctorStatus(){
		return this.seenByDoctorStatus;
	}
	
	/**
	 * Returns the time of when the patient had been seen by a doctor.
	 * 
	 * @return Date that patient has been seen by a doctor.
	 */
	public Date getSeenByDoctor(){
		return this.seenByDoctor;
	}
	/**
	 * Adds prescription information to the patient.
	 * 
	 * @param scriptInfo String representing the prescription
	 */
	public void addPrescription(String scriptInfo) {
		Date date = new Date();
		allPrescriptions.put(date, scriptInfo);
		EmergencyRoom.getInstance().savePatient(this);	
	}
	
	/**
	 * Generates a string representation of TreeMap<Date, String> allPrescriptions
	 * 
	 * @return String representation of TreeMap<Date, String> allPrescriptions
	 */
	public String getPrescriptionString(){
		String scriptString = "";
		ArrayList<Date> keys = new ArrayList<Date>(allPrescriptions.keySet());
		
		for(int i =0; i< keys.size(); i++){
			Date this_date = keys.get(i);
			scriptString = scriptString + EmergencyRoom.sdf.format(this_date) + "*";
			scriptString = scriptString + allPrescriptions.get(keys.get(i));
			if(!(i == keys.size() - 1)){
				//If not the last prescription entry
				scriptString = scriptString + "|";
			}
		}
		return scriptString;
	}
	
	
	/**
	 * Returns the String representation of this Patient object.
	 * 
	 * @return The string representation of this Patient object
	 */
	@Override
	public String toString(){
		return this.healthCardNumber + "," + getName() + "," + birthDate + ","+ vitals.toString();
	}
		
}
	
		

