Where to find Code:
 app -> Triage -> src -> defaultPackage: This contains all of the non-activity classes.
 app -> Triage -> src -> com -> Example -> triage: Contains all of the android activity classes.

Description of Code:
- EmergencyRoom manages reading/writing and storing patients during runtime.
- Each patient is represented by a Patient object. Each Patient object has a vitals field which represents all of the vital signs and symptoms for this patient.
- Vitals stores all vital signs and symptoms sorted and mapped by time of recording.
- LoginActivity is the LAUNCHER activity and allows nurses to log into the app (Any user/password combination works for now).
- PatientDisplayActivity displays all of the patients in a list view and allows for searching individual patients by health card number.
- PatientInfoActivity displays the most recent information about a specific patient, and links to EnterVitalsActivity and ViewAllRecordsActivity.
- EnterVitalsActivity allows a nurse to add new vital signs and symptom descriptions to a patient.
- ViewAllRecordsActivity allows a nurse to view all previous vital signs and symptoms descriptions of a patient.

Some commits were done together on one group members computer. Commit logs may not match meetings.txt exactly.
