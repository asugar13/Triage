package defaultPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class EmergencyRoom {


	private List<Patient> patients;
	
	public EmergencyRoom(File dir, String fileName) throws IOException {
		this.patients = new ArrayList<Patient>();
		File file = new File(dir, fileName);
		if (file.exists()) {
			this.populate(file.getPath());
		} else {
			file.createNewFile();
		}
	}
	
	public void populate(String filePath) throws FileNotFoundException {
	Scanner scanner = new Scanner(new FileInputStream(filePath));
	String[] patient_on_file;
	while (scanner.hasNextLine()) {
		patient_on_file = scanner.nextLine().split(",");
		String health_number = patient_on_file[0];
		String[] birthdate = patient_on_file[2].split("-");
		String[] name = patient_on_file[1].split(" ");
		patients.add(new Patient(name, Integer.parseInt(birthdate[3]), Integer
				.parseInt(birthdate[1]), Integer.parseInt(birthdate[0]), Integer.parseInt(health_number)));
	}
	scanner.close();
	}
	
	
	
	public static void main (String[] args) {
		File di = new File("/Triage/src/defaultPackage/patient_records.txt");
		EmergencyRoom r = new EmergencyRoom(di, "patient_records.txt");
		
		
	}
}
