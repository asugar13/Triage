package defaultPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import android.content.Context;
import android.util.Log;

/**
 * 
 * @author Asier
 * 
 */
public class EmergencyRoom {
	private static Map<String, Patient> patients;

	/**
	 * 
	 * @param context
	 * @param fileName
	 */

	public static void loadPatients(Context context, String fileName) {
		patients = new TreeMap<String, Patient>();
		try {
			// InputStream patients_stream = context.getAssets().open(
			// "patient_records.txt");
			populate(openFile(context, fileName));
		} catch (Exception e) { // Change to specific exception
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the inputstream for a given filename
	 * @param context
	 * @param fileName
	 * @return input stream for reading
	 */
	private static InputStream openFile(Context context, String fileName) {
		InputStream is = null;
		try {
			//Tries to read from data/data/Files directory
			is = context.openFileInput(fileName);
		} catch (FileNotFoundException e) {
			//If file not found, get .txt from assets
			try {
				return context.getAssets().open(fileName);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return is;
	}
	/**
	 * Gets the outputstream for writing to a given file
	 * @param context
	 * @param fileName
	 * @return
	 */
	private static FileOutputStream getOutputStream(Context context, String fileName) {
		try {
			//Tries to open file from /data/data/Files for reading
			return context.openFileOutput("patient_records.txt",
					context.MODE_PRIVATE);
			
		} catch (FileNotFoundException e) {
			//If file does not exist, creates it
			boolean fileCreated = new File(context.getFilesDir()+ "/patient_records.txt").mkdir();
			if (fileCreated) {
				try {
					return context.openFileOutput("patient_records.txt",
							context.MODE_PRIVATE);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * Returns a list of all of the patients
	 * @return ArrayList of all patients
	 */
	public static ArrayList<Patient> getPatients() {
		ArrayList<Patient> allPatients = new ArrayList<Patient>();
		for (String key : patients.keySet()) {
			allPatients.add(patients.get(key));
		}

		return allPatients;
	}

	/**
	 * Find a patient by healthcardNumber
	 * @param hCardNum
	 * @return Patient object, or null if no patient
	 */
	public static Patient getPatientByHCNum(String hCardNum) {
		return patients.get(hCardNum);
	}
	
	/**
	 * Update the patient in emergency room, vitals have been added
	 * @param patient
	 */
	public static void updatePatient(Patient patient){
		//Necessary becuase .putExtra() passes a copy of Patient not reference
		patients.put(patient.getHealthCardNum(), patient);
		
	}
	
	/**
	 *  Populates patients Map 
	 * @param patients_stream inputStream from .txt file containing patients info
	 * @throws FileNotFoundException
	 */
	public static void populate(InputStream patients_stream)
			throws FileNotFoundException {
		Scanner scanner = new Scanner((patients_stream));
		String[] patient_on_file;
		while (scanner.hasNextLine()) {

			patient_on_file = scanner.nextLine().split(",");
			String hcn = patient_on_file[0];
			String birthdate = patient_on_file[2];
			String[] name = patient_on_file[1].split(" ");
			if (patient_on_file.length == 3) {
				patients.put(hcn, new Patient(name, birthdate, hcn,
						new Vitals()));
			} else {
				String[] vitalInfo = patient_on_file[3].split("&");
				patients.put(hcn, new Patient(name, birthdate, hcn, new Vitals(
						vitalInfo)));
			}
		}
		scanner.close();
	}
	/**
	 * Saves patient information to .txt file
	 * @param context
	 */
	public static void savePatientData(Context context) {
		try {
			OutputStream outStream = getOutputStream(context,"patient_records.txt");
			for (String hc : patients.keySet()) {
				Log.d("PATIENTSTOSTRING",patients.get(hc).toString());
				outStream.write((patients.get(hc).toString() + "\n")
						.getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
