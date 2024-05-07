package com.learningSpringBoot.FirstSpring.job;

import java.util.List;

public interface JobService {

    List<Job> findAll();
    void createJob(Job job);
    Job getJobById(Long id);
    boolean deleteJobByID(Long id);
    boolean updateJob(Long id, Job updatedJob);
}
