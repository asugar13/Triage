package defaultPackage;

public class Vitals {
	
	//The date and the time that the vital signs and symptoms were recorded.
	private String dateandTime;
	//Patient's blood pressure at a current time.
	private String bloodPressure;
	//Patient's heart rate at a current time.
	private String heartRate;
	//Patient's symptoms description at a current time.
	private String symptomsDescription;
	//Patient's temperature at a current time
	private String temperature;
	
	/**
	 * @param dateandTime is the date and time that the vital signs and the symptoms were recorded. 
	 * @param bloodPressure Patient's blood pressure at a current time.
	 * @param heartRate Patient's heart rate at a current time.
	 * @param symptomsDescription Patient's symptoms' description at a current time
	 */
	
	public Vitals(int day, int month, int year, int hour, int minutes, int bloodPressure, int heartRate, int temperature, String symptomsDescription) {
		this.dateandTime = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year) + "//" + String.valueOf(hour) + ":" + String.valueOf(minutes);
		this.bloodPressure = String.valueOf(bloodPressure);
		this.heartRate = String.valueOf(heartRate);
		this.symptomsDescription = symptomsDescription;
		this.temperature = String.valueOf(temperature);
		
	}
	
	/**
	 * Returns date and time that the vital signs and the symptoms were recorded.
	 */
	public String getdateandTime() {
		return dateandTime;
	}
	
	/**
	 * Returns Patient's blood pressure at a current time.
	 */
	public String getbloodPressure() {
		return bloodPressure;
	}
	
	/**
	 * Returns Patient's heart rate at a current time.
	 */
	public String getheartRate() {
		return heartRate;
	}
	
	/**
	 * Returns the description of symptoms at a current time.
	 */
	public String getsymptomsDescription() {
		return symptomsDescription;
	}
	
	/**
	 * Returns the String description of this object.
	 */
	public String toString() {
		return dateandTime + "\\" + bloodPressure + "\\" + heartRate + "\\" + symptomsDescription;
	}
}
