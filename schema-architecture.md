Section 1: Architecture summary

The Smart Clinic Management System follows a three-tier web architecture, which separates the system into three distinct layers:

- Presentation Tier – The user interface, consisting of Thymeleaf templates and REST API consumers
- Application Tier – The Spring Boot backend that contains the controllers, services, and business logic
- Data Tier – The databases: MySQL for structured data and MongoDB for flexible, document-based data

Section 2: Numbered flow of data and control

- User Interface Layer
  1. User accesses AdminDashboard or Appointment pages.
  2. The action is routed to the appropriate Thymeleaf or REST controller.
  3. Controllers delegate logic to the Service Layer, which acts as the heart of the backend system
  4. The service layer communicates with the Repository Layer to perform data access operations
  5. Once data is retrieved from the database, it is mapped into Java model classes that the application can work with
  6. Finally, the bound models are used in the response layer
