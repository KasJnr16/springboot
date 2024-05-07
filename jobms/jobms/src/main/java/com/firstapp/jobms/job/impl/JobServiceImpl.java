package com.firstapp.jobms.job.impl;

import com.firstapp.jobms.job.Job;
import com.firstapp.jobms.job.JobRepository;
import com.firstapp.jobms.job.JobService;
import com.firstapp.jobms.job.clients.CompanyClient;
import com.firstapp.jobms.job.clients.ReviewClient;
import com.firstapp.jobms.job.dto.JobDTO;
import com.firstapp.jobms.job.external.Company;
import com.firstapp.jobms.job.external.Review;
import com.firstapp.jobms.job.mapper.JobMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
//    private List<Job> jobs = new ArrayList<>();
    JobRepository jobRepository;
    @Autowired
    RestTemplate restTemplate;
    private CompanyClient companyClient;
    private ReviewClient reviewClient;
    private Long nextId = 1L;
    int attempt= 0;

    public JobServiceImpl(JobRepository jobRepository, CompanyClient companyClient, ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }

    @Override
//    @CircuitBreaker(name = "companyBreaker",
//            fallbackMethod = "companyBreakerFallback")
//    @Retry(name = "companyBreaker",
//            fallbackMethod = "companyBreakerFallback")
    @RateLimiter(name = "companyLimiter",
           fallbackMethod = "companyLimiterFallback")
    public List<JobDTO> findAll() {
//        System.out.println("Attempts :"+ ++attempt);

        List<Job> jobs = jobRepository.findAll();
        return jobs.stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public List<String> companyBreakerFallback(Exception e){
        List<String> list = new ArrayList<>();
        list.add("Dummy");
        return list;
    }
    public List<String> companyLimiterFallback(Exception e){
        List<String> list = new ArrayList<>();
        list.add("API Limit Reached");
        return list;
    }

    private JobDTO convertToDto(Job job){
        Company company = companyClient.getCompany(job.getCompanyId());
        List<Review> reviews = reviewClient.getReviews(job.getCompanyId());
        return JobMapper.
                mapToJobWithCompanyDTO(job, company, reviews);
    }

    @Override
    public void createJob(Job job) {
        job.setId(nextId++);
        jobRepository.save(job);
    }
    @Override
    public JobDTO getJobById(Long id) {

        Job job = jobRepository.findById(id).orElse(null);
        if(job == null)
            return null;
        return convertToDto(job);
    }

    @Override
    public boolean deleteJobByID(Long id) {
        try {
            jobRepository.deleteById(id);
            return true;
        } catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean updateJob(Long id, Job updatedJob) {

        Optional<Job> jobOptional = jobRepository.findById(id);
            if (jobOptional.isPresent()) {
                Job job = jobOptional.get();
                job.setTitle(updatedJob.getTitle());
                job.setDescription(updatedJob.getDescription());
                job.setMaxSalary(updatedJob.getMaxSalary());
                job.setMinSalary(updatedJob.getMinSalary());
                job.setLocation(updatedJob.getLocation());
                jobRepository.save(job);

                return true;
            }
        return false;
    }


}
