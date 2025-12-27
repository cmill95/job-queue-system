# Job Queue System (Spring Boot)

A small distributed job queue system built with Spring Boot that handles asynchronous job execution using
a separate API and worker service. The system decouples job submission from execution, allowing long-running
or failure-prone work to be processed safely outside the HTTP request lifecycle.

The project focuses on clean system design rather than scale, modeling explicit job state transitions,
failure handling, and service separation in a way that mirrors real-world backend architectures.


## Features

- Asynchronous job processing that decouples HTTP requests from background execution
- Explicit job lifecycle modeling with observable state transitions
- Independent worker service for fault isolation and non-blocking API requests
- Shared domain contracts to ensure consistent data models across services
- Health endpoints for verifying independent service availability

## Architecture

The system is split into three modules with clearly defined responsibilities:

- **api-service**  
  Acts as the HTTP entry point for clients. Responsible for request validation, job creation, and job status queries. The API service never executes jobs directly.

- **worker-service**  
  Executes jobs asynchronously in the background. Workers poll for queued jobs, process them, and update job state and results.

- **common**  
  Shared module containing DTOs, enums, and request/response contracts used by both services to ensure consistent data models.

  
### Data Flow (High-Level)

1. A client submits a job request to the API service.
2. The API service validates the request, creates a job record, and marks it as `QUEUED`.
3. The worker service polls for queued jobs and claims a job for execution.
4. The worker updates the job state to `RUNNING`, executes the job, and records the result.
5. Upon completion, the job transitions to either `SUCCESS` or `FAILED`.


## Tech Stack


- Java 17+
- Spring Boot
- Gradle (multi-module build)

## Getting Started

### Prerequisites

To run the system locally, you will need:

- Java 17 or newer
- The Gradle wrapper (`./gradlew`)


### Run locally


The API and worker services are run as separate processes and should be started in different terminals from the repository root.


Terminal 1: API service  
./gradlew :api-service:bootRun

Terminal 2: Worker service  
./gradlew :worker-service:bootRun

### Verify services

Each service exposes a health endpoint to confirm it is running independently.

API health check:  
`curl http://localhost:8080/health`

Worker health check:  
`curl http://localhost:8081/health`

## API Endpoints

### Submit a Job

`POST /jobs`

Creates a new job and returns a unique job identifier immediately, 
allowing the client to poll for status updates while the job executes asynchronously.

Example response:
```json
{
  "id": "606e5bc5-cb3b-4551-892a-2b9703ceed3b",
  "status": "QUEUED"
}
```

### Get Job Status

`GET /jobs/{id}`

Returns the current state and result of a job.

Example response:
```json
{
  "id": "606e5bc5-cb3b-4551-892a-2b9703ceed3b",
  "status": "SUCCESS",
  "result": {
    "message": "Job completed successfully"
  }
}
```



## Project Structure

The repository is organized as a Gradle multi-module project:

    job-queue-system/
    ├── api-service/        # HTTP entry point and job management
    ├── worker-service/     # background job execution
    ├── common/             # shared domain models and DTOs
    ├── build.gradle
    └── settings.gradle


## Configuration

Default ports:

- api-service: 8080
- worker-service: 8081

Configuration is defined per service in:
`src/main/resources/application.yml`


## Testing

All tests can be run from the repository root using:


`./gradlew test`

