**Sleep in Eesti**

**Introduction**

This is a simple web store project made using Spring Boot. Our web application, SleepinEesti, offers a seamless housing booking experience across every town in Estonia. Customers can effortlessly reserve accommodations through our user-friendly platform. Additionally, users are empowered to contribute by adding their own accommodations, which are displayed on the website upon publication. Our app provides users with the convenience of accessing their booking history, viewing published accommodations, and leaving reviews for the places they've stayed in.

**Authors**

Vadim Filonov

Ida Tuule Jõgi

Edwin Smagin

Katarina Zemljanski


**Technologies Used**

Java 17+

Spring Boot 3.1.5

PostgreSQL 16

MapStruct 1.5.5

Spring Security 6.2.0

Liquibase 4.25.0

Lombok 1.18.30

Angular

SonarLint


**Setting up the development environment**

These instructions will help you set up the project on your local machine for development and testing purposes.

**Prerequisites**

Java version 17 or later

PostgreSQL version 16 or later (Database running)


**Setup**

Clone the repository:

git clone https://gitlab.cs.taltech.ee/vafilo/iti0302-2023.git

Navigate to the project directory:

cd iti0302-2023

Locate application.properties in the src/main/resources/  directory and fill it with the correct database connection data and other necessary configurations.

Build the project (this will also download the necessary dependencies):

./mvn clean install

Run the application:

./mvn package
