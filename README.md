# Electricity Bill Calculator

## Student Details

|Field|Details|
|-|-|
|Name|Naveed Ul Haq Manikpuri|
|USN|2BL23CS192|
|Branch|Computer Science \& Engineering|
|Semester|VI Semester|
|Subject|Advanced Java Programming|
|Problem No.|Problem 14|

## Problem Statement

This is an Electricity Bill Calculator built using Java Servlets. The user enters their Name, Previous Meter Reading, and Current Meter Reading in an HTML form. The Servlet calculates units consumed, applies a three-slab rate structure (first 100 units at ₹2/unit, next 100 units at ₹3/unit, above 200 units at ₹5/unit), adds a fixed monthly charge of ₹50, and displays a fully itemised bill.

## Technologies Used

* Java (Servlets)
* HTML, CSS (inline)
* Apache Tomcat 10
* Eclipse IDE

## How to Run This Project

1. Clone this repository or download the ZIP.
2. Import the project into Eclipse as a Dynamic Web Project.
3. Add Apache Tomcat 10 as the server in Eclipse.
4. Right-click project → Run As → Run on Server.
5. Open browser and go to: `http://localhost:8080/ElectricityBillCalc/index.html`

## Screenshots

### Input Form

!\[Input Form](screenshots/screenshot1.png)

### Output / Result Page

!\[Output Page](screenshots/screenshot2.png)

## Servlet Concept Practiced

doGet / doPost, Input Validation, Slab-wise Calculation, Dynamic HTML Response

