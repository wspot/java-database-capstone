/*
  Import getAllAppointments to fetch appointments from the backend
  Import createPatientRow to generate a table row for each patient appointment


  Get the table body where patient rows will be added
  Initialize selectedDate with today's date in 'YYYY-MM-DD' format
  Get the saved token from localStorage (used for authenticated API calls)
  Initialize patientName to null (used for filtering by name)


  Add an 'input' event listener to the search bar
  On each keystroke:
    - Trim and check the input value
    - If not empty, use it as the patientName for filtering
    - Else, reset patientName to "null" (as expected by backend)
    - Reload the appointments list with the updated filter


  Add a click listener to the "Today" button
  When clicked:
    - Set selectedDate to today's date
    - Update the date picker UI to match
    - Reload the appointments for today


  Add a change event listener to the date picker
  When the date changes:
    - Update selectedDate with the new value
    - Reload the appointments for that specific date


  Function: loadAppointments
  Purpose: Fetch and display appointments based on selected date and optional patient name

  Step 1: Call getAllAppointments with selectedDate, patientName, and token
  Step 2: Clear the table body content before rendering new rows

  Step 3: If no appointments are returned:
    - Display a message row: "No Appointments found for today."

  Step 4: If appointments exist:
    - Loop through each appointment and construct a 'patient' object with id, name, phone, and email
    - Call createPatientRow to generate a table row for the appointment
    - Append each row to the table body

  Step 5: Catch and handle any errors during fetch:
    - Show a message row: "Error loading appointments. Try again later."


  When the page is fully loaded (DOMContentLoaded):
    - Call renderContent() (assumes it sets up the UI layout)
    - Call loadAppointments() to display today's appointments by default
*/

import {getAllAppointments} from "./services/appointmentRecordService.js";
import {createPatientRow} from "./components/patientRows.js";


function filterPatientsByName(event, token, patientTable) {
    const filter = event.target.value;
    const date = document.getElementById("datePicker").value;
    const patientName = filter?.trim() !== "" ? filter?.trim() : null;

    loadAppointments(date, patientName, token, patientTable);
}

function filterPatientsByDate(event, token, patientTable) {
    const name = document.getElementById("searchBar").value;
    loadAppointments(event.target.value, name, token, patientTable);
}

function filterPatientsByTodayDate(event, token, patientTable) {
    let selectedDate = new Date().toISOString();
    loadAppointments(selectedDate, null, token, patientTable);
}

function loadAppointments(selectedDate, patientName, token , patientTable) {
    try {
        getAllAppointments(selectedDate, patientName, token).then(({appointments}) => {
            console.log(appointments);
            patientTable.innerHTML = "";
            if (appointments?.length === 0) {
                const tr = document.createElement("tr");
                const td = document.createElement("td");

                // Set the message
                td.textContent = "Error loading appointments. Try again later.";

                // Make the message span all columns
                td.colSpan = 5; // adjust based on number of <th> columns

                tr.appendChild(td);
                patientTable.appendChild(tr);
            }

            if (appointments?.length > 0) {
                appointments.forEach(appointment => {

                    let patient = {
                        id: appointment.patient.id,
                        name: appointment.patient.name,
                        email: appointment.patient.email,
                        phone: appointment.patient.phone
                    }


                    const row = createPatientRow(patient, appointment.id, appointment.doctor.id);
                    patientTable.appendChild(row);
                });
            }
        });
    } catch (error) {
        patientTable.innerHTML = "";

        const tr = document.createElement("tr");
        const td = document.createElement("td");

        // Set the message
        td.textContent = "Error loading appointments. Try again later.";

        // Make the message span all columns
        td.colSpan = 5; // adjust based on number of <th> columns

        tr.appendChild(td);
        patientTable.appendChild(tr);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const patientTable = document.getElementById("patientTableBody");
    const token = localStorage.getItem("token");
    let todayDate = new Date().toISOString().split("T")[0];

    renderContent();
    loadAppointments(todayDate, null, token , patientTable);

    document.getElementById("searchBar")
        .addEventListener("input", (event) => filterPatientsByName(event, token, patientTable));

    document.getElementById("datePicker")
        .addEventListener("input", (event) => filterPatientsByDate(event, token, patientTable));

    document.getElementById("todayButton")
        .addEventListener("click", (event) => filterPatientsByTodayDate(event, token, patientTable));
});


