package defaultPackage;

public class Patient {
	
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
	public Patient(String[] name, int day, int month, int year, int hcn, int hour, int minutes, String seenbydoctor){
		this.name = name.clone();
		this.birthdate = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
		this.healthCardNumber = String.valueOf(hcn);
		this.arrivalTimeToHospital = String.valueOf(hour) + ":" + String.valueOf(minutes);
		this.seenByDoctor = seenbydoctor;
	}
	

	
	public String[] getSymptoms() {
		return symptoms;
	}



	public void setSymptoms(String[] symptoms) {
		this.symptoms = symptoms;
	}



	public String[] getVitalSigns() {
		return vitalSigns;
	}



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
