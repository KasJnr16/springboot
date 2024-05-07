package com.learningSpringBoot.FirstSpring.reviews.impl;

import com.learningSpringBoot.FirstSpring.company.Company;
import com.learningSpringBoot.FirstSpring.company.CompanyService;
import com.learningSpringBoot.FirstSpring.reviews.Review;
import com.learningSpringBoot.FirstSpring.reviews.ReviewRepository;
import com.learningSpringBoot.FirstSpring.reviews.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;
    private CompanyService companyService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, CompanyService companyService) {
        this.reviewRepository = reviewRepository;
         this.companyService = companyService;
    }

    @Override
    public List<Review> getAllReviews(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public boolean createReview(Long companyId, Review review) {
        Company company = companyService.getCompanyById(companyId);
        if(company != null){
            review.setCompany(company);
            reviewRepository.save(review);
            return true;
        }
        return false;
    }

    @Override
    public Review getReviewById(Long companyId, Long reviewId) {
        List<Review> reviews = reviewRepository.findByCompanyId(companyId);
        return reviews.stream()
                .filter(review -> review.getId().equals(reviewId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean updateReview(Long companyId, Long reviewId, Review updatedReview) {
       if(companyService.getCompanyById(companyId) != null) {

           updatedReview.setCompany(companyService.getCompanyById(companyId));
           updatedReview.setId(reviewId);
           reviewRepository.save(updatedReview);
           return true;
       }
       return false;

    }

    @Override
    public boolean deleteReview(Long companyId, Long reviewId) {
        if(companyService.getCompanyById(companyId) != null
                && reviewRepository.existsById(reviewId)) {
            Review review = reviewRepository.findById(reviewId).orElse(null);
            assert review != null;
            Company company = review.getCompany();
            company.getReviews().remove(review);
            companyService.updateCompany(company, companyId);
            reviewRepository.deleteById(reviewId);
            return true;

        }
        return false;
    }
}
