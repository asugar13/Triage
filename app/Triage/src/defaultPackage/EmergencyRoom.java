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

	private static InputStream openFile(Context context, String fileName) {
		InputStream is = null;
		try {
			is = context.openFileInput(fileName);
			Log.d("IS",is.toString());
		} catch (FileNotFoundException e) {
			Log.d("ERROR","FILENOTFOUND");
			try {
				return context.getAssets().open(fileName);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return is;
	}

	private static FileOutputStream getOutputStream(Context context, String fileName) {
		try {
			return context.openFileOutput("patient_records.txt",
					context.MODE_PRIVATE);
		} catch (FileNotFoundException e) {

			boolean fileCreated = new File(context.getFilesDir()
					+ "/patient_records.txt").mkdir();
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
	 * 
	 * @return
	 */
	public static List<Patient> getPatients() {
		ArrayList<Patient> allPatients = new ArrayList<Patient>();
		for (String key : patients.keySet()) {
			allPatients.add(patients.get(key));
		}

		return allPatients;
	}

	public static Patient getPatientByHCNum(String hCardNum) {
		return patients.get(hCardNum);
	}
	
	public static void updatePatient(Patient patient){
		patients.put(patient.getHealthCardNum(), patient);
		
	}
	

	/**
	 * 
	 * @param patients_stream
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
