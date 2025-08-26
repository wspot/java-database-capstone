# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]

## Admin User Stories

As an Admin , I want to Log into the portal with my username and password  , so that i can manage the platform securely

**Acceptance Criteria:**

1. redirect admin to login page when he is not authenticated
2. redirect admin to admin page when password and username are correct
3. show error message and redirect admin to login page when password and username are not correct

**Priority:** High
**Story Points:** 1000


As an Admin , I want to Log out of the portal, so that i can protect system access

**Acceptance Criteria:**

1. show logout button for authenticated users
2. redirect admin to login page when logout button is clicked

**Priority:** High
**Story Points:** 1000

As an Admin , I want to Add doctors to the portal , so that i can create new doctors

**Acceptance Criteria:**

1. add new doctors o the portals

**Priority:** High
**Story Points:** 1000

As an Admin , I want to Delete doctor's profile from the portal

**Acceptance Criteria:**

1. delete doctors from the portals

**Priority:** High
**Story Points:** 1000


As an Admin , I want to Run a stored procedure in MySQL CLI , so that i can get the number of appointments per month and track usage statistics

**Acceptance Criteria:**

1. Run a stored procedure in MySQL CLI

**Priority:** High
**Story Points:** 800

## Patient User Stories

As a patient , I want to View a list of doctors without logging in , so that i can explore options before registering

**Acceptance Criteria:**

1. access a list of doctors without logging in

**Priority:** High
**Story Points:** 600

As a patient , I want to Sign up using my email and password , so that i can book appointments

**Acceptance Criteria:**

1. get redirected to signup form if i click on signup button
2. fill the information and signup
3. show success message if signup succeeds
4. show error message if signup fails

**Priority:** High
**Story Points:** 1000

As a patient , I want Log into the portal, so that i can manage my bookings

**Acceptance Criteria:**

1. get redirected to login form
2. fill form and show success message if login succeeds
3. fill form and show error message if login fails

**Priority:** High
**Story Points:** 1000

As a patient , I want to Log out of the portal, so that i can secure my account

**Acceptance Criteria:**

1. show logout button for authenticated users
2. redirect patient to login page when logout button is clicked

**Priority:** High
**Story Points:** 1000

As a patient , I want to Log in and book an hour-long appointment, so that i can consult with a doctor

**Acceptance Criteria:**

1. log in and book an hour-long appointment

**Priority:** High
**Story Points:** 1000
 

## Doctor User Stories

As a doctor , I want Log into the portal, so that i can manage my appointments

**Acceptance Criteria:**

1. get redirected to login form
2. fill form and show success message if login succeeds
3. fill form and show error message if login fails

**Priority:** High
**Story Points:** 1000

As a doctor , I want to Log out of the portal, so that i can protect my data

**Acceptance Criteria:**

1. show logout button for authenticated users
2. redirect doctor to login page when logout button is clicked

**Priority:** High
**Story Points:** 1000

As a doctor , I want to View my appointment calendar , so that i can stay organized

**Acceptance Criteria:**

1. show a page with appointment calendar

**Priority:** High
**Story Points:** 800

As a doctor , I want to Mark your unavailability , so that i can inform patients only the available slots

**Acceptance Criteria:**

1. update my availability using a calendar

**Priority:** High
**Story Points:** 800

As a doctor , I want to Update your profile with specialization and contact information , so that patients have up-to-date information

**Acceptance Criteria:**

1. update my profile

**Priority:** High
**Story Points:** 800