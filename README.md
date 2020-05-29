# Todo Project: Common Library

This design and implementation of the Todo Project's service demonstrates familiarity and understanding of modern
Java frameworks and design philosophies.

This is a library of common elements shared between all services in the Todo Project


### Features

* Common components and configurations for Kafka
* Standard inter-service communication objects (DTOs)
* Aspect Oriented Logging Components

## Table of Contents

- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#Installation)
- [Running The Tests](#running-the-tests)
- [Built With](#built-with)
    - [Additional Features](#additional-features)
- [Versioning](#versioning)
- [License](#license)


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.



### Prerequisites

```
Java 8
Maven
Lombok IDE Plugin
```

### Installation

Clone the repository locally, build and install to your local repository or to your private Maven repository.

Include in your project's pom.xml with:

```
    <dependency>
        <groupId>org.example</groupId>
        <artifactId>CommonLibrary</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
```

## Running the tests

All tests for this service are unit tests and run without the Spring Context. 
Test coverage has Lombok generated methods in mind and many of the classes 
where testing wouldn't be appropriate (configuration, security, logging classes) or DAO 
classes have been ignored in Jacoco coverage reports and in SonarQube.

To run the tests if using IntelliJ:

1. Load the `Unit Tests` run configuration and run with code coverage
to execute all unit tests and generate the code coverage report

To run via Maven:

1. Execute `mvn test` and Maven will run the tests with HTML Jacoco code coverage
reports generated in target/site/jacoco

## Built With

* [Spring Boot 2.3.0](https://spring.io/projects/spring-boot) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [Lombok](https://projectlombok.org/) - Used to generate java boilerplate code

### Additional Libraries
* Spring Data Kafka
* Spring AOP (Aspect Oriented Programming)
* Log4j2/SLF4J

### External Tools

List of tools that were used in development but not directly tied to the project 

* [Sonarqube](https://www.sonarqube.org/) - Code quality server
* [Sonarlint](https://www.sonarlint.org/) - Code quality linting tool
* [Nexus Repository](https://www.sonatype.com/nexus-repository-oss) - Shared artifacts repository
* [Jenkins](https://www.jenkins.io/) - Automated build and testing

## Versioning

This project uses [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/jrstubbington/Todo-Common-Library/tags). 

## Authors

* **James Stubbington** - *Initial work* - [jrstubbington](https://github.com/jrstubbington)

See also the list of [contributors](https://github.com/jrstubbington/Todo-Common-Library/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details