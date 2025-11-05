# TODO List Application - Microservices Architecture

## Overview

A scalable, distributed TODO List application built using microservices architecture. This application allows users to register, authenticate, and manage their personal tasks through a robust set of independently deployable services. The system supports complete CRUD operations for task management while providing secure user authentication and seamless service communication.​

## Service Components

Cloud Gateway: API Gateway routing requests and handling cross-cutting concerns

Service Discovery: Service registry for dynamic service location and load balancing

Auth Service: JWT-based authentication and authorization management

User Service: User registration, profile management, and user data operations

Task Service: Complete CRUD operations for TODO items and task management

Frontend Client: User interface for interaction with backend services​

## Built With Backend Technologies:

Java/Spring Boot - Microservice framework

Spring Cloud Gateway - API Gateway implementation

Spring Cloud Netflix Eureka - Service discovery

Spring Security + JWT - Authentication & authorization

Spring Data JPA - Data persistence layer

MySQL - Database storage

Maven - Dependency management
