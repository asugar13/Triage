Where to find Code:
 app -> Triage -> src -> defaultPackage: This contains all of the non-Activity classes
 app -> Triage -> src -> com -> Example -> triage: Contains all of the android activity classes

Description of Code:
- EmergencyRoom manages reading/writing and storing patients during runtime
- Each patient is represented by a patient object
- Each patient object has a vitals field which represents all of the vitals for this patient
- Vitals stores all vitals and symptoms mapped by time of recording
- LoginActivity is the LAUNCHER activity and allows nurses to log into the app (Any usr/password works for now)
- PatientDisplayActivity displays all of the patients in a list and allows for searching by health card number
- PatientInfoActivity displays all information about a specific patient, and links to EnterVitalsActivity and ViewAllRecordsActivity
- EnterVitalsActivity allows a nurse to add new vitals and symptom descriptions to a patient
- ViewAllRecordsActivity allows a nurse to view all previous vital and symptom descriptions of a patient 