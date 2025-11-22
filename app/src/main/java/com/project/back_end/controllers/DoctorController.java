package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST controller that serves JSON responses.
//    - Use `@RequestMapping("${api.path}doctor")` to prefix all endpoints with a configurable API path followed by "doctor".
//    - This class manages doctor-related functionalities such as registration, login, updates, and availability.


    // 2. Autowire Dependencies:
//    - Inject `DoctorService` for handling the core logic related to doctors (e.g., CRUD operations, authentication).
//    - Inject the shared `Service` class for general-purpose features like token validation and filtering.
    private final DoctorService doctorService;
    private final Service service;

    @Autowired
    public DoctorController(DoctorService doctorService, Service service) {
        this.doctorService = doctorService;
        this.service = service;
    }

    // 3. Define the `getDoctorAvailability` Method:
//    - Handles HTTP GET requests to check a specific doctor’s availability on a given date.
//    - Requires `user` type, `doctorId`, `date`, and `token` as path variables.
//    - First validates the token against the user type.
//    - If the token is invalid, returns an error response; otherwise, returns the availability status for the doctor.
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(@PathVariable("date") String date, @PathVariable("user") String user, @PathVariable("doctorId") Long doctorId, @PathVariable("token") String token) {
        if (this.service.validateToken(token, user).getStatusCode().is2xxSuccessful()) {
            List<String> availability = this.doctorService.getDoctorAvailability(doctorId, LocalDate.parse(date));
            HashMap<String, Object> response = new HashMap<>();
            response.put("availability", availability);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // 4. Define the `getDoctor` Method:
//    - Handles HTTP GET requests to retrieve a list of all doctors.
//    - Returns the list within a response map under the key `"doctors"` with HTTP 200 OK status.
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctor() {
        List<Doctor> doctors = this.doctorService.getDoctors();
        HashMap<String, Object> response = new HashMap<>();
        response.put("doctors", doctors);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


// 5. Define the `saveDoctor` Method:
//    - Handles HTTP POST requests to register a new doctor.
//    - Accepts a validated `Doctor` object in the request body and a token for authorization.
//    - Validates the token for the `"admin"` role before proceeding.
//    - If the doctor already exists, returns a conflict response; otherwise, adds the doctor and returns a success message.

    @PostMapping("/{token}")
    public @ResponseBody ResponseEntity<String> saveDoctor(@PathVariable("token") String token, @RequestBody Doctor doctor) {
        if (this.service.validateToken(token, "admin").getStatusCode().is2xxSuccessful()) {
            int result = this.doctorService.saveDoctor(doctor);
            if (result == 1) {
                return new ResponseEntity<>("Doctor added to db", HttpStatus.OK);
            } else if (result == -1) {
                return new ResponseEntity<>("Doctor already exists", HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>("Some internal error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // 6. Define the `doctorLogin` Method:
//    - Handles HTTP POST requests for doctor login.
//    - Accepts a validated `Login` DTO containing credentials.
//    - Delegates authentication to the `DoctorService` and returns login status and token information.
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login login) {
        return this.doctorService.validateDoctor(login);
    }


    // 7. Define the `updateDoctor` Method:
//    - Handles HTTP PUT requests to update an existing doctor's information.
//    - Accepts a validated `Doctor` object and a token for authorization.
//    - Token must belong to an `"admin"`.
//    - If the doctor exists, updates the record and returns success; otherwise, returns not found or error messages.
    @PutMapping("/{token}")
    public ResponseEntity<String> updateDoctor(@PathVariable("token") String token, @RequestBody Doctor doctor) {
        if (this.service.validateToken(token, "admin").getStatusCode().is2xxSuccessful()) {
            int result = this.doctorService.updateDoctor(doctor);
            if (result == 1) {
                return new ResponseEntity<>("Doctor updated", HttpStatus.OK);
            } else if (result == -1) {
                return new ResponseEntity<>("Doctor not found", HttpStatus.OK);
            }
            return new ResponseEntity<>("Some internal error occurred", HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    // 8. Define the `deleteDoctor` Method:
//    - Handles HTTP DELETE requests to remove a doctor by ID.
//    - Requires both doctor ID and an admin token as path variables.
//    - If the doctor exists, deletes the record and returns a success message; otherwise, responds with a not found or error message.
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<String> deleteDoctor(@PathVariable("id") Long id, @PathVariable("token") String token) {
        if (this.service.validateToken(token, "admin").getStatusCode().is2xxSuccessful()) {
            int result = this.doctorService.deleteDoctor(id);
            if (result == 1) {
                return new ResponseEntity<>("Doctor deleted successfully", HttpStatus.OK);
            } else if (result == -1) {
                return new ResponseEntity<>("Doctor not found with id", HttpStatus.OK);
            }
            return new ResponseEntity<>("Some internal error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    // 9. Define the `filter` Method:
//    - Handles HTTP GET requests to filter doctors based on name, time, and specialty.
//    - Accepts `name`, `time`, and `speciality` as path variables.
//    - Calls the shared `Service` to perform filtering logic and returns matching doctors in the response.
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public Map<String, Object> filer(@PathVariable("name") String name, @PathVariable("time") String time, @PathVariable("speciality") String speciality) {
        return this.service.filterDoctor(name, speciality, time);
    }

}
