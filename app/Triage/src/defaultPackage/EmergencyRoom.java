package defaultPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
	private Map<String, Patient> patients;
	/**
	 * 
	 * @param context
	 * @param fileName
	 */
	public EmergencyRoom(Context context, String fileName){
		this.patients = new TreeMap<String, Patient>();
		try{
			InputStream patients_stream = context.getAssets().open("patient_records.txt");
			populate(patients_stream);
		}catch(Exception e){ //Change to specific exception
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @return
	 */
	public Set<Map.Entry<String, Patient>> getPatients(){
		return patients.entrySet();
	}
	
	public Patient getPatientByHCNum(String hCardNum){
		return patients.get(hCardNum);
	}
	
	/**
	 * 
	 * @param patients_stream
	 * @throws FileNotFoundException
	 */
	public void populate(InputStream patients_stream) throws FileNotFoundException {
	Scanner scanner = new Scanner((patients_stream));
	String[] patient_on_file;
	while (scanner.hasNextLine()) {
		patient_on_file = scanner.nextLine().split(",");
		String hcn = patient_on_file[0];
		String birthdate = patient_on_file[2];
		String[] name = patient_on_file[1].split(" ");
		if (patient_on_file.length == 2){
			patients.put(hcn, new Patient(name, birthdate, hcn, new Vitals()));
		}
		else{
			String[] vitalInfo = patient_on_file[3].split("&");
			patients.put(hcn, new Patient(name, birthdate, hcn, new Vitals(vitalInfo)));
		}
	}
	scanner.close();
	}
	public void savePatientData(FileOutputStream outputStreamPatients){
		try{
			for (String hc: patients.keySet()){
				outputStreamPatients.write((patients.get(hc).toString() + "\n").getBytes());
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}

