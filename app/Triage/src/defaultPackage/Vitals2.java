package defaultPackage;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.TreeMap;

public class Vitals2 {
	/**Stores a patient's symptoms based on the time that they are written. */
	private Map<Date, String> symptoms;
	
	/**Stores a patient's vital signs based on the time that they are written. */
	private Map<Date, int[]> vitals;
	
	/**
	 * Constructs a Vitals object with no vitals and symptoms yet recorder. 
	 */
	public Vitals2(){
		symptoms = new TreeMap<Date, String>();
		vitals = new TreeMap<Date, int[]>();
	}
	
	/**
	 * Constructs a Vitals object with a patient's vital signs and symptoms
	 * arranged by time.
	 * @param t1 A list of times of when a patient's symptoms were recorded.
	 * @param symptoms A list of a patient's recorded symptoms.
	 * @param t2 A list of times of when a patient's vital signs were recorded.
	 * @param vitals A list of a patient's recorded vital signs
	 */
	public Vitals2(List<Date> t1, List<String> symptoms, List<Date> t2, List<int[]> vitals){
		this.symptoms = new TreeMap<Date, String>();
		this.vitals = new TreeMap<Date, int[]>();
		for (int i = 0; i < t1.size(); i++){
			this.symptoms.put(t1.get(i), symptoms.get(i));
			this.vitals.put(t2.get(i), vitals.get(i));		
		}
	}
	
	/**
	 * Adds a String of symtpoms to a patient's history of recorded symptoms.
	 * @param t The time which the new symptoms are recorded.
	 * @param newSympt The new symptoms that are recorded.
	 */
	public void addSymptoms(Date t, String newSympt){
		this.symptoms.put(t, newSympt);
	}
	
	/**
	 * Returns a list of the history of a patient's symptoms arranged by time.
	 */
	public Map<Date, String> getSymptoms(){
		return this.symptoms;
	}
	
	/**
	 * Adds a set of vital signs to a patient's history of recorded vitals.
	 * @param t The time which the new vitals are recorded.
	 * @param new The new vitals that are recorded.
	 */
	public void addVitals(Date t, int[] newVitals){
		this.vitals.put(t, newVitals);
	}
	
	/**
	 * Returns a list of the history of a patient's vitals arranged by time.
	 */
	public Map<Date, int[]> getVitals(){
		return this.vitals;
	}
}
	
