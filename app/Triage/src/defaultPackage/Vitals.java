package defaultPackage;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Date;
import java.util.TreeMap;
import java.util.regex.Pattern;

import android.util.Log;
/**
 * Stores current and previous vitals and symptoms for a patient 
 * Vitals and symptom descriptions are mapped by time of recording.
 */
public class Vitals implements Serializable{

	private static final long serialVersionUID = 2939547818890631643L;
	/**Simple date format, specifies string format of dates */
	public static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	/**Stores patient's vitals based on the time that they are written. */
	private TreeMap<Date, String[]> vitSymps;
	public boolean isEmpty = true;
	
	/**
	 * Constructs a Vitals object with no vital signs or symptoms yet recorded. 
	 */
	public Vitals(){
		vitSymps = new TreeMap<Date, String[]>();
	}
	
	/**
	 * Constructs a Vitals object with vital signs and symptoms sorted by time.
	 * 
	 * @param inputVital Contains all the vital signs and symptoms to be instantiated.
	 */
	public Vitals(String[] inputVital){
		//Log.d("inputVital",Arrays.toString(inputVital));
		isEmpty = false;
		this.vitSymps = new TreeMap<Date, String[]>();
		
		for(String s: inputVital){
			if(!s.equals("")){
				String[] dateVitalSplit = s.split(Pattern.quote("*"));
				Log.d("dateVital",Arrays.toString(dateVitalSplit));

				try {
					Date date = sdf.parse(dateVitalSplit[0]);
					String[] vitalReadings = dateVitalSplit[1].split("[\\x7C]");
					Log.d("vitalReadings",Arrays.toString(vitalReadings));

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
	 * Adds new vital signs and symptoms to the Vitals object.
	 * 
	 * @param newVitSympt The new vital signs and symptoms that are being recorded.
	 */
	public void add(String[] newVitSymp){
		isEmpty = false;
		Date date = new Date();
		Log.d("Before",vitSymps.toString());
		vitSymps.put(date, newVitSymp);
		Log.d("After",vitSymps.toString());

	}
	/**
	 * Returns all vitals mapped by date.
	 * 
	 * @return TreeMap mapping dates to vitals
	 */
	public TreeMap<Date, String[]> getAllVitals(){
		return vitSymps;
	}
	
	
	
	/**
	 * Returns a Map of the history of a patient's vitals arranged by time.
	 * 
	 * @return vitSymps The vitals signs and symptoms of this patient mapped by Date.
	 */
	public Map<Date, String[]> getVitSymps(){
		return this.vitSymps;
	}
	/**
	 * Returns a string representation of the vitals
	 * For storing the vitals in a text file, not for outputting a meaningful
	 * Representation of the vitals.
	 * 
	 * @return String representation of vitals for writing to internal storage
	 */
	@Override
	public String toString(){
		String vitSympString = "";
		ArrayList<Date> keys = new ArrayList<Date>(vitSymps.keySet());
		
		for(int i = 0;i < keys.size(); i ++){
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
	
