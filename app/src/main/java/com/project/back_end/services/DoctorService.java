package com.project.back_end.services;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;

@Service
public class DoctorService {

// 1. **Add @Service Annotation**:
//    - This class should be annotated with `@Service` to indicate that it is a service layer class.
//    - The `@Service` annotation marks this class as a Spring-managed bean for business logic.
//    - Instruction: Add `@Service` above the class declaration.

    // 2. **Constructor Injection for Dependencies**:
//    - The `DoctorService` class depends on `DoctorRepository`, `AppointmentRepository`, and `TokenService`.
//    - These dependencies should be injected via the constructor for proper dependency management.
//    - Instruction: Ensure constructor injection is used for injecting dependencies into the service.
    AppointmentRepository appointmentRepository;
    TokenService tokenService;
    PatientRepository patientRepository;
    DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(AppointmentRepository appointmentRepository, TokenService tokenService, PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

// 3. **Add @Transactional Annotation for Methods that Modify or Fetch Database Data**:
//    - Methods like `getDoctorAvailability`, `getDoctors`, `findDoctorByName`, `filterDoctorsBy*` should be annotated with `@Transactional`.
//    - The `@Transactional` annotation ensures that database operations are consistent and wrapped in a single transaction.
//    - Instruction: Add the `@Transactional` annotation above the methods that perform database operations or queries.

// 4. **getDoctorAvailability Method**:
//    - Retrieves the available time slots for a specific doctor on a particular date and filters out already booked slots.
//    - The method fetches all appointments for the doctor on the given date and calculates the availability by comparing against booked slots.
//    - Instruction: Ensure that the time slots are properly formatted and the available slots are correctly filtered.

    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        ArrayList<String> availability = new ArrayList<>();
        Optional<Doctor> doctor = this.doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            List<Appointment> appointments = this.appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, date.atStartOfDay(), LocalDateTime.of(date, LocalTime.MAX));

            for (String time : doctor.get().getAvailableTimes()) {
                String[] availableTimes = time.split("-");
                LocalTime availableStartTime = LocalTime.parse(availableTimes[0]);
                LocalTime availableEndTime = LocalTime.parse(availableTimes[1]);
                for (Appointment appointment : appointments) {
                    if (!appointment.getAppointmentTime().isBefore(ChronoLocalDateTime.from(availableStartTime)) && !appointment.getAppointmentTime().isAfter(ChronoLocalDateTime.from(availableEndTime))) {
                        availability.add(time);
                    }
                }
            }
        }

        return availability;
    }

// 5. **saveDoctor Method**:
//    - Used to save a new doctor record in the database after checking if a doctor with the same email already exists.
//    - If a doctor with the same email is found, it returns `-1` to indicate conflict; `1` for success, and `0` for internal errors.
//    - Instruction: Ensure that the method correctly handles conflicts and exceptions when saving a doctor.

    @Transactional
    public int saveDoctor(Doctor doctor) {
        try {
            Doctor doctor1 = this.doctorRepository.findByEmail(doctor.getEmail());

            if (doctor1 != null) {
                return -1;
            } else {
                this.doctorRepository.save(doctor);
                return 1;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    // 6. **updateDoctor Method**:
//    - Updates an existing doctor's details in the database. If the doctor doesn't exist, it returns `-1`.
//    - Instruction: Make sure that the doctor exists before attempting to save the updated record and handle any errors properly.
    @Transactional
    public int updateDoctor(Doctor doctor) {
        try {
            Optional<Doctor> doctor1 = this.doctorRepository.findById(doctor.getId());

            if (doctor1.isPresent()) {
                this.doctorRepository.save(doctor);
                return 1;
            } else {
                return -1;
            }
        } catch (Exception e) {
            return 0;
        }
    }

// 7. **getDoctors Method**:
//    - Fetches all doctors from the database. It is marked with `@Transactional` to ensure that the collection is properly loaded.
//    - Instruction: Ensure that the collection is eagerly loaded, especially if dealing with lazy-loaded relationships (e.g., available times).

    @Transactional
    public List<Doctor> getDoctors() {
        return this.doctorRepository.findAll();
    }

// 8. **deleteDoctor Method**:
//    - Deletes a doctor from the system along with all appointments associated with that doctor.
//    - It first checks if the doctor exists. If not, it returns `-1`; otherwise, it deletes the doctor and their appointments.
//    - Instruction: Ensure the doctor and their appointments are deleted properly, with error handling for internal issues.

    public int deleteDoctor(long id) {
        try {
            List<Doctor> doctors = this.doctorRepository.findAllById(Collections.singleton(id));
            if (!doctors.isEmpty()) {
                this.doctorRepository.deleteById(id);
                return 1;
            } else {
                return -1;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    // 9. **validateDoctor Method**:
//    - Validates a doctor's login by checking if the email and password match an existing doctor record.
//    - It generates a token for the doctor if the login is successful, otherwise returns an error message.
//    - Instruction: Make sure to handle invalid login attempts and password mismatches properly with error responses.
    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {
        try {
            Doctor doctor = doctorRepository.findByEmail(login.getEmail());
            if (doctor != null && doctor.getPassword().equals(login.getPassword())) {
                Map<String, String> responseMap = new HashMap<>();
                responseMap.put("token", this.tokenService.generateToken(login.getEmail()));
                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

// 10. **findDoctorByName Method**:
//    - Finds doctors based on partial name matching and returns the list of doctors with their available times.
//    - This method is annotated with `@Transactional` to ensure that the database query and data retrieval are properly managed within a transaction.
//    - Instruction: Ensure that available times are eagerly loaded for the doctors.

    @Transactional
    public Map<String, Object> findDoctorByName(String name) {
        try {
            Map<String, Object> responseMap = new HashMap<>();
            List<Doctor> doctors = this.doctorRepository.findByNameLike(name);
            responseMap.put("doctors", doctors);
            return responseMap;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }


    private List<Doctor> filterDoctorByTime(String amOrPm, List<Doctor> doctors) {

        return doctors.stream().filter(doctor -> {
            List<String> availableTimes = doctor.getAvailableTimes();
            LocalTime filterTime = LocalTime.parse(amOrPm);
            for (String time : availableTimes) {
                String[] times = time.split("-");
                LocalTime availableStartTime = LocalTime.parse(times[0]);
                LocalTime availableEndTime = LocalTime.parse(times[1]);
                if (availableStartTime.isBefore(filterTime) && availableEndTime.isAfter(filterTime)) {
                    return true;
                }
            }
            return false;
        }).toList();
    }

// 11. **filterDoctorsByNameSpecialityandTime Method**:
//    - Filters doctors based on their name, specialty, and availability during a specific time (AM/PM).
//    - The method fetches doctors matching the name and specialty criteria, then filters them based on their availability during the specified time period.
//    - Instruction: Ensure proper filtering based on both the name and specialty as well as the specified time period.

    public Map<String, Object> filterDoctorsByNameSpecilityandTime(String name, String specialty, String amOrPm) {
        try {
            Map<String, Object> responseMap = new HashMap<>();
            List<Doctor> doctors = this.doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
            List<Doctor> filteredDoctors = filterDoctorByTime(amOrPm, doctors);
            responseMap.put("doctors", filteredDoctors);
            return responseMap;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

// 12. **filterDoctorByTime Method**:
//    - Filters a list of doctors based on whether their available times match the specified time period (AM/PM).
//    - This method processes a list of doctors and their available times to return those that fit the time criteria.
//    - Instruction: Ensure that the time filtering logic correctly handles both AM and PM time slots and edge cases.


// 13. **filterDoctorByNameAndTime Method**:
//    - Filters doctors based on their name and the specified time period (AM/PM).
//    - Fetches doctors based on partial name matching and filters the results to include only those available during the specified time period.
//    - Instruction: Ensure that the method correctly filters doctors based on the given name and time of day (AM/PM).

    public Map<String, Object> filterDoctorByNameAndTime(String name, String amOrPm) {
        try {
            Map<String, Object> responseMap = new HashMap<>();
            List<Doctor> doctors = this.doctorRepository.findByNameLike(name);
            List<Doctor> filteredDoctors = filterDoctorByTime(amOrPm, doctors);
            responseMap.put("doctors", filteredDoctors);
            return responseMap;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

// 14. **filterDoctorByNameAndSpecility Method**:
//    - Filters doctors by name and specialty.
//    - It ensures that the resulting list of doctors matches both the name (case-insensitive) and the specified specialty.
//    - Instruction: Ensure that both name and specialty are considered when filtering doctors.

    public Map<String, Object> filterDoctorByNameAndSpecility(String name, String specialty) {
        try {
            Map<String, Object> responseMap = new HashMap<>();
            List<Doctor> doctors = this.doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
            responseMap.put("doctors", doctors);
            return responseMap;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }


// 15. **filterDoctorByTimeAndSpecility Method**:
//    - Filters doctors based on their specialty and availability during a specific time period (AM/PM).
//    - Fetches doctors based on the specified specialty and filters them based on their available time slots for AM/PM.
//    - Instruction: Ensure the time filtering is accurately applied based on the given specialty and time period (AM/PM).

    public Map<String, Object> filterDoctorByTimeAndSpecility(String specialty, String amOrPm) {
        try {
            Map<String, Object> responseMap = new HashMap<>();
            List<Doctor> doctors = this.doctorRepository.findBySpecialtyIgnoreCase(specialty);
            List<Doctor> filteredDoctors = filterDoctorByTime(amOrPm, doctors);
            responseMap.put("doctors", filteredDoctors);
            return responseMap;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    // 16. **filterDoctorBySpecility Method**:
//    - Filters doctors based on their specialty.
//    - This method fetches all doctors matching the specified specialty and returns them.
//    - Instruction: Make sure the filtering logic works for case-insensitive specialty matching.
    public Map<String, Object> filterDoctorBySpecility(String specialty) {
        try {
            Map<String, Object> responseMap = new HashMap<>();
            List<Doctor> doctors = this.doctorRepository.findBySpecialtyIgnoreCase(specialty);
            responseMap.put("doctors", doctors);
            return responseMap;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    // 17. **filterDoctorsByTime Method**:
//    - Filters all doctors based on their availability during a specific time period (AM/PM).
//    - The method checks all doctors' available times and returns those available during the specified time period.
//    - Instruction: Ensure proper filtering logic to handle AM/PM time periods.
    public Map<String, Object> filterDoctorsByTime(String amOrPm) {
        try {
            Map<String, Object> responseMap = new HashMap<>();
            List<Doctor> doctors = this.doctorRepository.findAll();
            List<Doctor> filteredDoctors = filterDoctorByTime(amOrPm, doctors);
            responseMap.put("doctors", filteredDoctors);
            return responseMap;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

}
