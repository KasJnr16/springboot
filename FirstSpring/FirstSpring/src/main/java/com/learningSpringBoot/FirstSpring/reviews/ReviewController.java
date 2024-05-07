package com.learningSpringBoot.FirstSpring.reviews;

import com.learningSpringBoot.FirstSpring.company.CompanyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies/{companyId}/reviews")
public class ReviewController {
    private ReviewService reviewService;
    private CompanyRepository companyRepository;

    public ReviewController(ReviewService reviewService, CompanyRepository companyRepository) {
        this.reviewService = reviewService;
        this.companyRepository = companyRepository;

    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(@PathVariable Long companyId){
        if(companyRepository.existsById(companyId))
            return new ResponseEntity<>(reviewService.getAllReviews(companyId), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<String> addReview(@PathVariable Long companyId, @RequestBody Review review){
        boolean isReviewSaved = reviewService.createReview(companyId, review);
        if(isReviewSaved)
            return new ResponseEntity<>("Review Added Successfully",HttpStatus.CREATED);
        return new ResponseEntity<>("Review Not Saved", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long companyId, @PathVariable Long reviewId){
        Review review = reviewService.getReviewById(companyId, reviewId);
        if(review != null)
            return new ResponseEntity<>(review, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long companyId, @PathVariable Long reviewId, @RequestBody Review review){
        boolean isUpdated = reviewService.updateReview(companyId, reviewId, review);
        if(isUpdated){
            return new ResponseEntity<>("Review Updated Successfully", HttpStatus.OK);
        }
        else
            return new  ResponseEntity<>("Review Not Updated",HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long companyId, @PathVariable Long reviewId){
        boolean isDeleted = reviewService.deleteReview(companyId, reviewId);
        if(isDeleted){
            return new ResponseEntity<>("Review Deleted Successfully", HttpStatus.OK);
        }
        else
            return new  ResponseEntity<>("Review Not Deleted",HttpStatus.NOT_FOUND);
    }

}
