package com.learningSpringBoot.FirstSpring.reviews;

import java.util.List;

public interface ReviewService {

    List<Review> getAllReviews(Long companyId);
    boolean createReview(Long companyId, Review review);
    Review getReviewById(Long companyId, Long reviewId);
    boolean updateReview(Long companyId, Long reviewId, Review review);
    boolean deleteReview(Long companyId, Long reviewId);
}
