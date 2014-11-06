package defaultPackage;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.HashMap;
import java.lang.Object;

public class Patient implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1280942052796823017L;
	//Patient's name.
	private String[] name;
	//Patient's birth date.
	private String birthdate;
	//Patient's health card number.
	private String healthCardNumber;
	//Patient's arrival time to the hospital.
	private String arrivalTimeToHospital;
	//Patient's current symptoms.
	private String[] symptoms;
	//Patient's current vital signs.
	private String[] vitalSigns;
	//Patient's current urgency level according to the hospital policy.
	private int urgencyLevel;
	//If patient was seen by a doctor 'Yes' unless 'No'.
	private String seenByDoctor;
	
	/** @param name This patient's name.
	 * @param day This Patient's day of birth.
	 * @param month This Patient's month of birth.
	 * @param year This Patient's year of birth.
	 * @param healthcardnumber This Patient's health card number.
	 * @param ArrivalTimeToHospital This Patient's arrival time to the hospital.
	 * @param Symptoms This Patient's current symptoms.
	 * @param VitalSigns This Patient's current vital signs.
	 * @param UrgencyLevel is This Patient's current urgency level according to the hospital policy.
	 * @param SeenByDoctor is This Patient's current situation of waiting to see a doctor unless was not seen by a doctor.
	 */
	public Patient(String[] name, int day, int month, int year, int hcn) {
		this.name = name.clone();
		this.birthdate = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
		this.healthCardNumber = String.valueOf(hcn);
		
	}
	/**
	*Returns this patient's name.
	*/
	public String getName(){
		String nameString = "";
		for(String n: name){
			nameString = nameString + n + " ";
		}
		return nameString;
	}
	
	/**
	*Returns this patient's birth date.
	*/
	public String getBirthDate(){
		return birthdate;
	}
	
	/**
	 * Returns health card number.
	 */
	public String getHealthCardNum(){
		return healthCardNumber;
	}
	
	/**
	*Returns the current symptoms of the patient.
	*/
	public String[] getSymptoms() {
		return symptoms;
	}
	
	/**
	 * Sets to Patient's symptoms.
	 */
	public void setSymptoms(String[] symptoms) {
		this.symptoms = symptoms;
	}
	
	/**
	*Returns the current urgency level of the patient.
	*/
	public int getUrgencyLevel() {
		return urgencyLevel;
	}
	
	/**
	 * Sets to Patient's urgency level
	 */
	public void setUrgencyLevel(int urgencyLevel) {
		this.urgencyLevel = urgencyLevel;
	}
	
	/**
	*Returns the current arrival time to the hospital.
	*/
	public String getArrivalTimeToHospital() {
		return arrivalTimeToHospital;
	}

	/**
	 * Sets to Patient's arrival time to the hospital.
	 */
	public void setArrivalTimeToHospital(String arrivalTimeToHospital) {
		this.arrivalTimeToHospital = arrivalTimeToHospital;
	}
	
	/**
	*Returns if the patient was seen by a doctor as 'Yes' and 'No'.
	*/
	public String getSeenByDoctor() {
		return seenByDoctor;
	}

	/**
	 * Sets to the patient was seen by a doctor as 'Yes' and 'No'.
	 */
	public void setSeenByDoctor(String seenByDoctor) {
		this.seenByDoctor = seenByDoctor;
	}
	
	/**
	*Returns the current vital signs of the patient.
	*/
	public String[] getVitalSigns() {
		return vitalSigns;
	}

	/**
	 * Sets to Patient's vital signs.
	 */
	public void setVitalSigns(String[] vitalSigns) {
		this.vitalSigns = vitalSigns;
	}

	public String toString(){
		String patientName = "";
		for(String n: name){
			patientName += n+ " ";
		}
		return patientName.trim() + " " + birthdate + healthCardNumber + arrivalTimeToHospital + symptoms + vitalSigns + 
				urgencyLevel + seenByDoctor;
	}
	
	
	
	
		
		
		
		
}
	
		

