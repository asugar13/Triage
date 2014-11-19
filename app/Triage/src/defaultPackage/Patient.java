package defaultPackage;

import java.io.Serializable;
import java.util.Date;

import android.util.Log;

public class Patient implements Serializable{
	
	
	private static final long serialVersionUID = 1280942052796823017L;
	private String[] name;
	private String birthDate;
	private String healthCardNumber;
	private Date seenByDoctor;
	private boolean seenByDoctorStatus;
	private Vitals vitals;
	private int urgency;
	
	/** Constructor for Patient class.
	 * 
	 * @param name This patient's name.
	 * @param birthdate This Patient's date of birth.
	 * @param hcn This Patient's health card number.
	 * @param vitals This patient's vitals (vital signs and symptoms).
	 */
	public Patient(String[] name, String birthdate, String hcn, Vitals vitals) {
		this.name = name.clone();
		this.birthDate = birthdate;
		this.healthCardNumber = hcn;
		this.vitals = vitals;
		this.seenByDoctorStatus = false;
	}
	
	/**
	 * Sets the time that the patient has been seen by a doctor
	 */
	public void setSeenByDoctor(Date timeSeen){
		this.seenByDoctor = timeSeen;
	}
	
	/**
	*Returns this patient's name.
	*
	*@return This patient's name
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
	*
	*@return This patient's birth date.
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
		EmergencyRoom.updatePatient(this);
		
	}
	/**
	*Returns the vitals of this patient.
	*
	*@return vitals The vitals of this patient
	*/
	public Vitals getVitals(){
		return vitals;
	}
	
	/**
	*Adds an urgency rating to this patient
	*/
	public void addUrgency(int urgency){
		this.urgency = urgency;
	}
	
	/**
	*Gets this patient's urgency
	*/
	public int getUrgency(){
		return urgency;
	}
	
	/**
	*Updates this patient's record to having been seen by a doctor
	*/
	public void setSeenByDoctor(){
		this.seenByDoctorStatus = true;
		this.seenByDoctor = new Date();
	}
	
	public boolean getSeenByDoctorStatus(){
		return this.seenByDoctorStatus;
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
	
		

