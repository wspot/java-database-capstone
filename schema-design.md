
## MySQL Database Design

### Table: patients
- id: INT, Primary Key, Auto Increment
- name: VARCHAR , Not Null
- username: VARCHAR , Not Null
- password: VARCHAR , Not Null

### Table: doctors
- id: INT, Primary Key, Auto Increment
- name: VARCHAR , Not Null
- username: VARCHAR , Not Null
- password: VARCHAR , Not Null

### Table: admins
- id: INT, Primary Key, Auto Increment
- name: VARCHAR , Not Null
- username: VARCHAR , Not Null
- password: VARCHAR , Not Null

### Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- patient_id: INT, Foreign Key → patients(id)
- appointment_time: DATETIME, Not Null
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)



## MongoDB Collection Design

### Table: doctors
- id: INT, Primary Key, Auto Increment
- name: VARCHAR , Not Null
- username: VARCHAR , Not Null
- password: VARCHAR , Not Null