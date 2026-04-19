package com.cole.worker.jobs;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("jobs")
public class JobEntity implements Persistable<String> {

    @Id
    private final String id;
    private final String type;
    private final String status;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String payload;
    private final String result;
    private final String errorMessage;

    @Transient
    private final boolean isNew;

    // Used by Spring Data JDBC when loading rows from the DB
    @PersistenceCreator
    public JobEntity(String id, String type, String status, Instant createdAt, Instant updatedAt,
                     String payload, String result, String errorMessage) {
        this(id, type, status, createdAt, updatedAt, payload, result, errorMessage, false);
    }

    public JobEntity(String id, String type, String status, Instant createdAt, Instant updatedAt,
                     String payload, String result, String errorMessage, boolean isNew) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.payload = payload;
        this.result = result;
        this.errorMessage = errorMessage;
        this.isNew = isNew;
    }

    @Override public String getId() { return id; }
    @Override public boolean isNew() { return isNew; }

    public String getType() { return type; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getPayload() { return payload; }
    public String getResult() { return result; }
    public String getErrorMessage() { return errorMessage; }
}
