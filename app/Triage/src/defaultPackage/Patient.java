package defaultPackage;

import java.io.Serializable;

import android.util.Log;

public class Patient implements Serializable{
	
	
	private static final long serialVersionUID = 1280942052796823017L;
	private String[] name;
	private String birthDate;
	private String healthCardNumber;
	private Vitals vitals;
	
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
	*/
	public Vitals getVitals(){
		return vitals;
	}
	/**
	 * Returns the String representation of this Patient object.
	 */
	@Override
	public String toString(){
		return this.healthCardNumber + "," + getName() + "," + birthDate + ","+ vitals.toString();
	}
		
}
	
		

