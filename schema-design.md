
## MySQL Database Design

### Table: patients
- id: INT, Primary Key, Auto Increment
- name: VARCHAR , Not Null
- email: VARCHAR , Not Null
- password: VARCHAR , Not Null
- phone: VARCHAR , Not Null
- address: VARCHAR , Not Null
- appointments : List<Appointment> 

### Table: doctors
- id: INT, Primary Key, Auto Increment
- name: VARCHAR , Not Null
- specialty: VARCHAR , Not Null
- email: VARCHAR , Not Null
- password: VARCHAR , Not Null
- phone: VARCHAR , Not Null
- availableTimes: List<String>
- appointments : List<Appointment>

### Table: admins
- id: INT, Primary Key, Auto Increment
- username: VARCHAR , Not Null
- password: VARCHAR , Not Null

### Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- patient_id: INT, Foreign Key → patients(id)
- appointment_time: DATETIME, Not Null
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)



## MongoDB Collection Design

### Table: prescription
- id: String, Primary Key
- patientName: String, Not Null
- appointmentId: Long, Not Null
- medication: String, min = 3, max = 100
- dosage: String, Not Null.
- doctorNotes: String, max = 200