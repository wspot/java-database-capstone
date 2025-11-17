package com.project.back_end.controllers;



import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller.
//    - Use `@RequestMapping("/appointments")` to set a base path for all appointment-related endpoints.
//    - This centralizes all routes that deal with booking, updating, retrieving, and canceling appointments.


    // 2. Autowire Dependencies:
//    - Inject `AppointmentService` for handling the business logic specific to appointments.
//    - Inject the general `Service` class, which provides shared functionality like token validation and appointment checks.
    private final Service service;
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(Service service, AppointmentService appointmentService) {
        this.service = service;
        this.appointmentService = appointmentService;
    }


// 3. Define the `getAppointments` Method:
//    - Handles HTTP GET requests to fetch appointments based on date and patient name.
//    - Takes the appointment date, patient name, and token as path variables.
//    - First validates the token for role `"doctor"` using the `Service`.
//    - If the token is valid, returns appointments for the given patient on the specified date.
//    - If the token is invalid or expired, responds with the appropriate message and status code.

    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<Map<String, Object>> getAppointments(@PathVariable("date") String date, @PathVariable("patientName") String patientName, @PathVariable("token") String token) {
        if (this.service.validateToken(token, "doctor").getStatusCode().is2xxSuccessful()) {
            Map<String, Object> appointments = this.appointmentService.getAppointment(patientName, LocalDate.parse(date), token);
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    // 4. Define the `bookAppointment` Method:
//    - Handles HTTP POST requests to create a new appointment.
//    - Accepts a validated `Appointment` object in the request body and a token as a path variable.
//    - Validates the token for the `"patient"` role.
//    - Uses service logic to validate the appointment data (e.g., check for doctor availability and time conflicts).
//    - Returns success if booked, or appropriate error messages if the doctor ID is invalid or the slot is already taken.
    @PostMapping("/{token}")
    public ResponseEntity<String> bookAppointment(@RequestBody Appointment appointment, @PathVariable("token") String token) {
        if (this.service.validateToken(token, "patient").getStatusCode().is2xxSuccessful()) {
            if (this.service.validateAppointment(appointment).equals(1)) {
                Integer result = this.appointmentService.bookAppointment(appointment);
                if (result == 1) {
                    return new ResponseEntity<>("The appointment is booked successfully", HttpStatus.OK);
                }
                return new ResponseEntity<>("The appointment couldn't be booked", HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                return new ResponseEntity<>("The doctor is no present is no available", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("The token is not valid", HttpStatus.UNAUTHORIZED);
    }


// 5. Define the `updateAppointment` Method:
//    - Handles HTTP PUT requests to modify an existing appointment.
//    - Accepts a validated `Appointment` object and a token as input.
//    - Validates the token for `"patient"` role.
//    - Delegates the update logic to the `AppointmentService`.
//    - Returns an appropriate success or failure response based on the update result.

    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateAppointment(@RequestBody Appointment appointment, @PathVariable("token") String token) {
        if (this.service.validateToken(token, "patient").getStatusCode().is2xxSuccessful()) {
            return this.appointmentService.updateAppointment(appointment);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


// 6. Define the `cancelAppointment` Method:
//    - Handles HTTP DELETE requests to cancel a specific appointment.
//    - Accepts the appointment ID and a token as path variables.
//    - Validates the token for `"patient"` role to ensure the user is authorized to cancel the appointment.
//    - Calls `AppointmentService` to handle the cancellation process and returns the result.

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(@PathVariable("id") String id, @PathVariable("token") String token) {
        if (this.service.validateToken(token, "patient").getStatusCode().is2xxSuccessful()) {
            Appointment a = new Appointment();
            a.setId(Long.parseLong(id));
            return this.appointmentService.deleteAppointment(a);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
