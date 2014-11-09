package defaultPackage;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Date;
import java.util.TreeMap;
import java.util.regex.Pattern;

import android.util.Log;

public class Vitals implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2939547818890631643L;
	/**Simple date format, specifies string format of dates */
	public static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	/**Stores a patient's symptoms based on the time that they are written. */
	private TreeMap<Date, String[]> vitSymps;
	public boolean isEmpty = true;
	
	
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
		isEmpty = false;
		this.vitSymps = new TreeMap<Date, String[]>();
		
		for(String s: inputVital){
			if(!s.equals("")){
				String[] dateVitalSplit = s.split(Pattern.quote("*"));
				try {
					Date date = sdf.parse(dateVitalSplit[0]);
					String[] vitalReadings = dateVitalSplit[1].split("|");
					vitSymps.put(date, vitalReadings);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}else{
				//Do nothing
			}
	
		}
	}
	
	/**
	 * Adds a String of symptoms to a patient's history of recorded symptoms.
	 * @param t The time which the new symptoms are recorded.
	 * @param newSympt The new symptoms that are recorded.
	 */
	public void add(String[] newVitSymp){
		isEmpty = false;
		Date date = new Date();
		Log.d("Before",vitSymps.toString());
		vitSymps.put(date, newVitSymp);
		Log.d("After",vitSymps.toString());

	}
	
	public TreeMap<Date, String[]> getAllVitals(){
		return vitSymps;
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
		ArrayList<Date> keys = new ArrayList<Date>(vitSymps.keySet());
		
		for(int i = 0;i < keys.size(); i ++ ){
			Date this_date = keys.get(i);
			vitSympString = vitSympString + sdf.format(this_date) + "*";
			
			for(int j = 0 ; j < vitSymps.get(this_date).length; j ++){
				if(j == vitSymps.get(this_date).length - 1){
					vitSympString = vitSympString + vitSymps.get(this_date)[j] + "&";
				}
				else {vitSympString = vitSympString + vitSymps.get(this_date)[j] + "|";	
				
				}
			}	
		}
		return vitSympString;
	}
	
	
}
	
