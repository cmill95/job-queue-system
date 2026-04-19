package com.cole.worker.jobs;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JobRepository extends CrudRepository<JobEntity, String> {
    List<JobEntity> findByStatus(String status);
}
