package defaultPackage;

public class Patient {
	
	//Patient's name.
	public String[] name;
	//Patient's birth date.
	public String birthdate;
	//Patient's health card number.
	public String healthcardnumber;
	//Patient's arrival time to the hospital.
	public String ArrivalTimeToHospital;
	//Patient's current symptoms.
	public String[] Symptoms;
	//Patient's current vital signs.
	public String[] VitalSigns;
	//Patient's current urgency level according to the hospital policy.
	public int UrgencyLevel;
	//If patient was seen by a doctor 'Yes' unless 'No'.
	public String SeenByDoctor;
	
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
	public Patient(String[] name, int day, int month, int year, int hcn, int hour, int minutes, String[] symptoms, 
			String[] vitalsigns, int urgencylevel, String seenbydoctor){
		this.name = name.clone();
		this.birthdate = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
		this.healthcardnumber = String.valueOf(hcn);
		this.ArrivalTimeToHospital = String.valueOf(hour) + ":" + String.valueOf(minutes);
		this.Symptoms = symptoms;
		this.VitalSigns = vitalsigns;
		this.UrgencyLevel = urgencylevel;
		this.SeenByDoctor = seenbydoctor;
	}
	
	//Returns this patient's name.
	public String[] getName() {
		return name.clone();
	}
		
	//Sets to Patient's name.
	public void setName(String[] name){
		this.name = name.clone(); //To avoid unintended side-effects.
	}
	
	//Returns this patient's birth date.
	public String getbirthdate(){
		return birthdate;
	}
	
	/**
	 * Returns health card number.
	 */
	public String gethealthcardnumber(){
		return healthcardnumber;
	}
	
	//Returns the current arrival time to the hospital.
	public String getatth(){
		return ArrivalTimeToHospital;
	}
	
	//Returns the current symptoms of the patient.
	public String[] getsymptoms(){
		return Symptoms;
	}
		
	//Returns the current vital signs of the patient.
	public String[] getvitalsigns(){
		return VitalSigns;
	}
	
	//Returns the current urgency level of the patient.
	public int geturgencylevel(){
		return UrgencyLevel;
	}
	
	//Returns if the patient was seen by a doctor as 'Yes' and 'No'.
	public String getseenbydoctor(){
		return SeenByDoctor;
	}
	
	public String toString(){
		String patientName = "";
		for(String n: name){
			patientName += n+ " ";
		}
		return patientName.trim() + " " + birthdate + healthcardnumber + ArrivalTimeToHospital + Symptoms + VitalSigns + 
				UrgencyLevel + SeenByDoctor;
	}
}
