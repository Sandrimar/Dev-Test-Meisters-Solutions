# Backend - Task Management System

This directory contains the Spring Boot application for the task management system.

## Prerequisites

- Java 21
- MySQL

## Setting Up

1. **Enter this directory**:

    ```sh
    cd Dev-Test-Meisters-Solutions/backend
    ```

2. **Configure MySQL**:
    Create a MySQL database and update the `application.properties` file with your database credentials.

3. **Install dependencies and run the application**:

    ```sh
    ./mvnw clean install
    ./mvnw spring-boot:run
    ```

## API Endpoints

- **GET /api/tasks**: Retrieve all tasks.
- **POST /api/tasks**: Create a new task.
- **PUT /api/tasks/{id}**: Update a task.
- **PATCH /api/tasks/{id}?status=STATUS**: Update the status of a task.
- **DELETE /api/tasks/{id}**: Delete a task.
- **POST /email/send?to=<you@email.com>**: Receive daily reminders of pending tasks.
