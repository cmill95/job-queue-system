package com.cole.api.jobs;

import com.cole.common.dto.GetJobResponse;
import com.cole.common.dto.SubmitJobRequest;
import com.cole.common.dto.SubmitJobResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService service;

    public JobController(JobService service) {
        this.service = service;
    }

    @PostMapping
    public SubmitJobResponse submit(@RequestBody SubmitJobRequest req) {
        return service.submit(req);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetJobResponse> get(@PathVariable String id) {
        try {
            return ResponseEntity.ok(service.get(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}