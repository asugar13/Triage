package defaultPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import android.content.Context;
import android.util.Log;

public class EmergencyRoom {
	private List<Patient> patients;
	
	public EmergencyRoom(Context context, String fileName){
		this.patients = new ArrayList<Patient>();
		try{
			InputStream patients_stream = context.getAssets().open("patient_records.txt");
			populate(patients_stream);
		}catch(Exception e){ //Change to specific exception
			Log.d("Exception",e.getMessage());
			e.printStackTrace();
		}
	}
	
	public List<Patient> getPatients(){
		return patients;
	}
	
	public void populate(InputStream patients_stream) throws FileNotFoundException {
	Scanner scanner = new Scanner((patients_stream));
	String[] patient_on_file;
	while (scanner.hasNextLine()) {
		patient_on_file = scanner.nextLine().split(",");
		String health_number = patient_on_file[0];
		String[] birthdate = patient_on_file[2].split("-");
		String[] name = patient_on_file[1].split(" ");
		patients.add(new Patient(name, Integer.parseInt(birthdate[2]), Integer
				.parseInt(birthdate[1]), Integer.parseInt(birthdate[0]), Integer.parseInt(health_number)));
	}
	scanner.close();
	}
	
}
