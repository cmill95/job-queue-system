# Job Queue System (Spring Boot)

A small distributed job queue system built with Spring Boot, demonstrating service separation (API + worker), shared contracts, and asynchronous job execution.

## Features

- API service for submitting jobs and checking status
- Worker service for executing jobs asynchronously
- Shared `common` module for DTOs and contracts
- Health endpoints for service monitoring

## Architecture

This project is split into three modules:

- **api-service**: HTTP entry point for clients (request validation, job submission, status queries)
- **worker-service**: background job execution (processes queued jobs)
- **common**: shared DTOs / request & response models used by both services

### Data flow (high-level)

Client → `api-service` → (queue/persistence layer) → `worker-service` → job status/result updates

## Tech Stack

- Java 17+
- Spring Boot
- Gradle multi-module build

## Getting Started

### Prerequisites

- Java 17+ installed (`java -version`)
- Gradle wrapper (`./gradlew`)

### Run locally

Start the services in separate terminals from the repo root.

Terminal 1: API service  
./gradlew :api-service:bootRun

Terminal 2: Worker service  
./gradlew :worker-service:bootRun

### Verify services

API health check:  
`curl http://localhost:8080/health`

Worker health check:  
`curl http://localhost:8081/health`

## Endpoints

### Worker health

Request:  
curl http://localhost:8081/health

Example response:

    {
      "status": "ok",
      "service": "worker-service",
      "time": "2025-12-19T18:48:49Z"
    }

## Project Structure

    job-queue-system/
    ├── api-service/       # HTTP entry point
    ├── worker-service/    # background job execution
    ├── common/            # shared domain & DTOs
    ├── build.gradle
    └── settings.gradle

## Configuration

Default ports:

- api-service: 8080
- worker-service: 8081

Configuration files live under each service:  
`src/main/resources/application.yml`

## Testing

Run all tests:

    ./gradlew test

## Roadmap

- [ ] Add job submission endpoint (POST /jobs)
- [ ] Persist jobs (Postgres or Redis)
- [ ] Worker executes jobs asynchronously
- [ ] Job status transitions (QUEUED → RUNNING → DONE / FAILED)
- [ ] Docker Compose for local development
