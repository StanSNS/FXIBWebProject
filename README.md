# Project Overview

## Backend

- **Spring Boot**: Version 3.1.3 project based on the `spring-boot-starter-parent` for configuration.
- **Spring Framework**: Includes various Spring dependencies like:
    - `spring-boot-starter-data-jpa`
    - `spring-boot-starter-security`
    - `spring-boot-starter-validation`
    - `spring-boot-starter-web`
- **Database**: MySQL database with the `mysql-connector-j` dependency.
- **Security**: Utilizes Spring Security and JWT (JSON Web Tokens) with:
    - `jjwt-api`
    - `jjwt-impl`
    - `jjwt-jackson`
- **Project Lombok**: A library that simplifies the creation of Java classes by reducing boilerplate code.
- **Model Mapping**: Facilitates object-to-object mapping.
- **Stripe Integration**: Implements Stripe payment integration with the `stripe-java` dependency.
- **Email Sending**: Uses `spring-boot-starter-mail` for email functionality.
- **Gson**: A library for JSON serialization and deserialization.
- **IP geolocation**:  Provides functionalities to query and retrieve geolocation data based on IP addresses.
- **JUnit and AssertJ**: Used for testing and test assertions.

### Testing

- Below, you will find the coverage percentage obtained from the JUnit testing.
  
![test coverage](https://i.imgur.com/25XXRtk.png)

## Frontend

- **React**: The frontend is built using the `React framework`.
- **React Router**: Utilizes `React Router` for client-side routing.
- **Styling**: `Bootstrap 4.6` and CSS are used for styling.
- **HTTP Requests**: `Axios` is used for making HTTP requests.
- **Icons**: Various icon libraries like Font `Awesome` and `React Icons` are used, with dependencies including
    - @fortawesome/fontawesome-svg-core
    - @fortawesome/free-solid-svg-icons
    - @fortawesome/react-fontawesome
    - @react-icons/all-files
    - and react-icons
- **jQuery**: The project depends on the jQuery library.
- **Module System**: Utilizes the esm package for ECMAScript Modules (ESM) support in Node.js.

### Other Dependencies are also part of the project:

- react-scripts
- web-vitals
- react-ts-tradingview-widgets

## Conclusion

### This project demonstrates a robust and well-structured system encompassing both the **backend** and **frontend** components.

This project is a testament to effective development practices and the utilization of cutting-edge tools, resulting
in a comprehensive and well-rounded software solution.




