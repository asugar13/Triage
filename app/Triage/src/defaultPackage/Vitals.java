package defaultPackage;

public class Vitals {
	
	//The date and the time that the vital signs and symptoms were recorded.
	private String dateandTime;
	//Patient's blood pressure at a current time.
	private int bloodPressure;
	//Patient's heart rate at a current time.
	private int heartRate;
	//Patient's symptoms description at a current time.
	private String symptomsDescription;
	
	/**
	 * @param dateandTime is the date and time that the vital signs and the symptoms were recorded. 
	 * @param bloodPressure Patient's blood pressure at a current time.
	 * @param heartRate Patient's heart rate at a current time.
	 * @param symptomsDescription Patient's symptoms' description at a current time
	 */
	
	public Vitals(int day, int month, int year, int hour, int minutes, int bloodPressure, int heartRate, String symptomsDescription) {
		this.dateandTime = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year) + "//" + String.valueOf(hour) + ":" + String.valueOf(minutes);
		this.bloodPressure = bloodPressure;
		this.heartRate = heartRate;
		this.symptomsDescription = symptomsDescription;
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
	public int getbloodPressure() {
		return bloodPressure;
	}
	
	/**
	 * Returns Patient's heart rate at a current time.
	 */
	public int getheartRate() {
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
