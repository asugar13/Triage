package defaultPackage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.TreeMap;

public class Vitals {
	/**Simple date format, specifies string format of dates */
	public static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	/**Stores a patient's symptoms based on the time that they are written. */
	private Map<Date, String[]> vitSymps;
	
	/**Stores a patient's vital signs based on the time that they are written. */
	
	/**
	 * Constructs a Vitals object with no vitals and symptoms yet recorder. 
	 */
	public Vitals(){
		vitSymps = new TreeMap<Date, String[]>();
	}
	
	/**
	 * Constructs a Vitals object with a patient's vital signs and symptoms
	 * arranged by time.
	 * @param t1 A list of times of when a patient's symptoms were recorded.
	 * @param symptoms A list of a patient's recorded symptoms.
	 * @param t2 A list of times of when a patient's vital signs were recorded.
	 * @param vitals A list of a patient's recorded vital signs
	 */
	public Vitals(String[] inputVital){
		this.vitSymps = new TreeMap<Date, String[]>();
		
		for(String s: inputVital){
			String[] dateVitalSplit = s.split("*");
			try {
				Date date = sdf.parse(dateVitalSplit[0]);
				String[] vitalReadings = dateVitalSplit[1].split("|");
				vitSymps.put(date, vitalReadings);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	/**
	 * Adds a String of symtpoms to a patient's history of recorded symptoms.
	 * @param t The time which the new symptoms are recorded.
	 * @param newSympt The new symptoms that are recorded.
	 */
	public void add(String[] newVitSymp){
		Date date = new Date();
		vitSymps.put(date, newVitSymp);
	}
	
	/**
	 * Returns a list of the history of a patient's vitals arranged by time.
	 */
	public Map<Date, String[]> getVitSymps(){
		return this.vitSymps;
	}
	/**
	 * @return String representation of vitals for writing to internal storage
	 */
	@Override
	public String toString(){
		String vitSympString = "";
		for(Date date : vitSymps.keySet()){
			//Add date string
			vitSympString = vitSympString + date.toString() + "*";
			//Add all vital strings separated by "|"
			for(String vital : vitSymps.get(date)){
				vitSympString = vitSympString + "|";
			}
			vitSympString = vitSympString.substring(0,-1);
		
		vitSympString = vitSympString + "&";
		}
		vitSympString = vitSympString.substring(0,-1);
		
		return vitSympString;
	}
	
	
}
	
