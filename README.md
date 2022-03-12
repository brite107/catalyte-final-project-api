# Final Health Project API #

## Description

This project will serve to demonstrate a REST API that provides CRUD operations for two entities
(Patient, Encounter). Data is sent and received in JSON format. This project
uses Maven, Spring boot and Postgres. The application uses a layered architecture for separation of concerns.

## Prerequisites

Spring Boot requires Java 8 and is compatible up to and including Java 16.

### Installation ###

* Fork the repository to your specified work directory. Navigate to the directory in which you will
  store the project, then clone it to your computer using the following command:

```bash
git clone <insert_url_here>
```

* Open the project in your IDE.

### Data

***Starting Spring Application with Postgres***
* Make sure that your postgres database is available and configured with the following options:
    * POSTGRES_USER=postgres
    * POSTGRES_PASSWORD=root
    * PORT=5432

## Usage ##

* Navigate to FinalProjectApiApplication.java located in src/main/java/io/catalyte/training/finalprojectapi.
* If starting in Intellij, right click FinalProjectApiApplication, then click Run 'FinalProjectApiApplication.main()'.
* The application can also be run by clicking the green play buttons in the gutter of the
  FinalProjectApiApplication.java file or in the toolbar in the top right corner.

## Testing ##

### Running Mockito Unit Tests ###
* Right click src.test.java.io.catalyte.training.finalprojectapi.domains.encounters.EncounterServiceImplTest or src.test.java.io.catalyte.training.finalprojectapi.domains.encounters.PatientServiceImplTest and click run tests.
* Code coverage can be viewed by right-clicking the file and selecting 'More run/debug' then Run with Coverage.

### Running Integration Tests ###
* Right click src.test.java.io.catalyte.training.finalprojectapi.domains.encounters.EncounterControllerTest or src.test.java.io.catalyte.training.finalprojectapi.domains.encounters.PatientControllerTest and click run tests.
* Code coverage can be viewed by right-clicking on the file and selecting 'More run/debug' then Run with Coverage.

### Running all tests for Encounter and Patient entities with coverage ###
* Right click src.test.java.io.catalyte.training.finalprojectapi.domains.encounters.EncounterControllerTest or src.test.java.io.catalyte.training.finalprojectapi.domains package, select 'More run/debug' then Run with Coverage.

### Postman ###

A postman collection demonstrates all of the 2XX and 4XX functional requirements. The collection can be found
here:

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/14413928-b037cc31-4b94-4cc8-8709-9bcb05bd8ef2?action=collection%2Ffork&collection-url=entityId%3D14413928-b037cc31-4b94-4cc8-8709-9bcb05bd8ef2%26entityType%3Dcollection%26workspaceId%3D1fb0818b-bc96-4f32-97f6-599cd5998eed#?env%5Blocalhost%3A8080%5D=W3sia2V5IjoiZG9tYWluIiwidmFsdWUiOiJsb2NhbGhvc3Q6ODA4MCIsImVuYWJsZWQiOnRydWV9XQ==)

## Logging
* Log4j2 logs information to the project folder `logs/` as well as the console.
* Logging configuration specified in log4j2.xml file in `src/resources`.
* Application logs any error scenarios.

## Swagger
* Run application
* Go to localhost:8080/swagger-ui.html to see swagger documentation for all endpoints.
* The configuration file for swagger is found in src.test.java.io.catalyte.training.finalprojectapi.config package.

## Coding Style Standards ##

The files in this project are linted according to the  [Google Java Style
Guide](https://google.github.io/styleguide/javaguide.html). To lint the files:

- To lint the whole project: right click on the src/main/java folder in the Project window and select
  'Reformat code' from the drop down list. Repeat with the src/test/java folder.
- To lint an individual file, type **Ctrl Alt L** while focus is on the file you want to lint.

## Contact ##

For questions, comments and concerns, please contact Jen Perry at jperry@catalyte.io.# catalyte-final-project-api
