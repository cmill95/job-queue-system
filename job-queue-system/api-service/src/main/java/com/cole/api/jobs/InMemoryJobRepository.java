package com.cole.api.jobs;

import com.cole.common.job.Job;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryJobRepository {
    private final Map<String, Job> jobs = new ConcurrentHashMap<>();

    public Job save(Job job) {
        jobs.put(job.id(), job);
        return job;
    }

    public Optional<Job> findById(String id) {
        return Optional.ofNullable(jobs.get(id));
    }
}