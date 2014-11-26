Where to find Code:
 app -> Triage -> src -> defaultPackage: This contains all of the non-activity classes.
 app -> Triage -> src -> com -> Example -> triage: Contains all of the android activity classes.

Running application:
Launch application, login to loginActivity using a nurse or physician. Takes you to PatientDisplayActivity. From here you can add patients, view patients information, or look up a patient by health card number. You can also select how you wish to display the patients from the dropdown menu. If you wish to view a patients info click the item in the list or search by health card number, this will take you to PatientInfoActivity. From here you can add to a patients record, add prescriptions, view all previous prescriptions vital records, and set the time a patient was visited by a doctor. Some features will not be allowed depending on your user type.

Note: Some commits were done together on one group members computer. Commit logs may not match meetings.txt exactly.

(OPTIONAL)
Brief Description of all classes
Description of Activity classes:
- LoginActivity is the LAUNCHER activity and allows nurses/physicians to log into the app.
- PatientDisplayActivity displays all of the patients in a list view and allows for searching individual patients by health card number. Links to AddNewPatientActivity
	- The way the patients are sorted can be specified by a dropdown menu
- AddNewPatientActivity asks for personal information, and first vital readings to add a new patient
- AddPrescriptionActivity allows doctors to add prescription information to a patient.
- PatientInfoActivity displays the most recent information about a specific patient, and links to EnterVitalsActivity,ViewAllRecordsActivity,
- EnterVitalsActivity allows a nurse to add new vital signs and symptom descriptions to a patient.
- ViewAllRecordsActivity allows a nurse to view all previous vital signs,symptoms, and prescription records.
- TimeDialogActivity allows nurses to specify a time and date that a patient was seen by a doctor

Description of non-activity classes:
- DataBaseManager reads and writes to an SQLiteDatabase.
	- The database contains two tables: patient_records (for all patient info) and login_information (for username,passwords and user type)
- EmergencyRoom works with DataBaseManager to load and save patients. EmergencyRoom also manages patients during run time (calculating urgency, adding new patients), and logging in to the app.
	- The first time the app is run, the login information and patient info is read from the .txt files, and copied to a new database for future use.
- Each patient is represented by a Patient object. Each Patient object has a vitals field which represents all of the vital signs and symptoms for this patient.
- Vitals stores all vital signs and symptoms sorted and mapped by time of recording.

