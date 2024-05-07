package com.firstapp.jobms.job;

import com.firstapp.jobms.job.dto.JobDTO;

import java.util.List;

public interface JobService {

    List<JobDTO> findAll();
    void createJob(Job job);
    JobDTO getJobById(Long id);
    boolean deleteJobByID(Long id);
    boolean updateJob(Long id, Job updatedJob);
}
