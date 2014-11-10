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


public class EmergencyRoom {
	private static Map<String, Patient> patients;

	/**
	 * Constructor for the static instantiation of Emergency Room.
	 * The Emergency Room gets populated with Patient objects.
	 * 
	 * @param context the context we are loading patients from.
	 * @param fileName the name of the file we are loading patients from.
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
	 * Gets the InputStream for a given filename.
	 * 
	 * @param context the context where the file is stored.
	 * @param fileName the name of the file to be opened.
	 * @return InputStream for reading.
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
	 * Gets the OutputStream for writing to a given file.
	 * 
	 * @param context the context where the file is stored.
	 * @param fileName the name of the file to be opened.
	 * @return OutputStream for writing.
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
	 * Returns a list of all of the patients.
	 * 
	 * @return ArrayList containing all the Patient objects in the Emergency Room.
	 */
	public static ArrayList<Patient> getPatients() {
		ArrayList<Patient> allPatients = new ArrayList<Patient>();
		for (String key : patients.keySet()) {
			allPatients.add(patients.get(key));
		}

		return allPatients;
	}

	/**
	 * Find a patient using a health card number.
	 * 
	 * @param hCardNum a health card number
	 * @return Patient object, or null if there is no patient with given health card number
	 */
	public static Patient getPatientByHCNum(String hCardNum) {
		return patients.get(hCardNum);
	}
	
	/**
	 * Updates the Patient object whose vitals have been edited in the Emergency Room.
	 * 
	 * @param patient a Patient object
	 */
	public static void updatePatient(Patient patient){
		//Necessary because .putExtra() passes a copy of Patient, not a reference
		patients.put(patient.getHealthCardNum(), patient);
		
	}
	
	/**
	 * Populates the patients Map attribute in EmergencyRoom with all the patients from a given InputStream.
	 *  
	 * @param patients_stream InputStream from .txt file containing patients info
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
	 * 
	 * @param context the context where the .txt file is stored.
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
