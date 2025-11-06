/*
  This script handles the admin dashboard functionality for managing doctors:
  - Loads all doctor cards
  - Filters doctors by name, time, or specialty
  - Adds a new doctor via modal form


  Attach a click listener to the "Add Doctor" button
  When clicked, it opens a modal form using openModal('addDoctor')


  When the DOM is fully loaded:
    - Call loadDoctorCards() to fetch and display all doctors


  Function: loadDoctorCards
  Purpose: Fetch all doctors and display them as cards

    Call getDoctors() from the service layer
    Clear the current content area
    For each doctor returned:
    - Create a doctor card using createDoctorCard()
    - Append it to the content div

    Handle any fetch errors by logging them


  Attach 'input' and 'change' event listeners to the search bar and filter dropdowns
  On any input change, call filterDoctorsOnChange()


  Function: filterDoctorsOnChange
  Purpose: Filter doctors based on name, available time, and specialty

    Read values from the search bar and filters
    Normalize empty values to null
    Call filterDoctors(name, time, specialty) from the service

    If doctors are found:
    - Render them using createDoctorCard()
    If no doctors match the filter:
    - Show a message: "No doctors found with the given filters."

    Catch and display any errors with an alert


  Function: renderDoctorCards
  Purpose: A helper function to render a list of doctors passed to it

    Clear the content area
    Loop through the doctors and append each card to the content area


  Function: adminAddDoctor
  Purpose: Collect form data and add a new doctor to the system

    Collect input values from the modal form
    - Includes name, email, phone, password, specialty, and available times

    Retrieve the authentication token from localStorage
    - If no token is found, show an alert and stop execution

    Build a doctor object with the form values

    Call saveDoctor(doctor, token) from the service

    If save is successful:
    - Show a success message
    - Close the modal and reload the page

    If saving fails, show an error message
*/
import {openModal} from "./components/modals.js";
import {getDoctors, filterDoctors, saveDoctor} from "./services/doctorServices";
import {createDoctorCard} from "./components/doctorCard";


document.getElementById('addDocBtn').addEventListener('click', () => {
    openModal('addDoctor');
});

function loadDoctorCards() {
    const contentDiv = document.getElementById("content");
    contentDiv.innerHTML = "";
    getDoctors().then(doctors => {
        doctors.forEach(doctor => {
            const card = createDoctorCard(doctor);
            contentDiv.appendChild(card);
        })
    })
}

function renderDoctorCards(doctors) {
    const contentDiv = document.getElementById("content");
    contentDiv.innerHTML = "";

    doctors.forEach(doctor => {
        const card = createDoctorCard(doctor);
        contentDiv.appendChild(card);
    });
}

function filterDoctorsOnChange(event) {
    // The element that triggered the event
    const element = event.target;

    // Get its id
    const id = element.id;

    // Get its value
    const value = element.value;

    // Example: do something based on which filter changed
    if (id === "searchBar") {
        console.log("Filtering doctors by name:", value);
        filterDoctors(value, "", "").then((docs) => {
            renderDoctorCards(docs);
        });
    } else if (id === "filterTime") {
        console.log("Filtering doctors by available time:", value);
        filterDoctors("", value, "").then((docs) => {
            renderDoctorCards(docs);
        });
    } else if (id === "filterSpecialty") {
        console.log("Filtering doctors by specialty:", value);
        filterDoctors("", "", value).then((docs) => {
            renderDoctorCards(docs);
        });
    }
}

window.onload = () => {
    loadDoctorCards();
    document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
    document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
    document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);
}