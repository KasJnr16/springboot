package com.firstapp.reviewMs.reviews;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;

    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam Long companyId){
        return new ResponseEntity<>(reviewService.getAllReviews(companyId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addReview(@RequestParam Long companyId, @RequestBody Review review){
        boolean isReviewSaved = reviewService.createReview(companyId, review);
        if(isReviewSaved)
            return new ResponseEntity<>("Review Added Successfully",HttpStatus.CREATED);
        return new ResponseEntity<>("Review Not Saved", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long reviewId) {
        Review review = reviewService.getReviewById(reviewId);
        if (review != null) {
            return new ResponseEntity<>(review, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId, @RequestBody Review review){
        boolean isUpdated = reviewService.updateReview(reviewId, review);
        if(isUpdated){
            return new ResponseEntity<>("Review Updated Successfully", HttpStatus.OK);
        }
        else
            return new  ResponseEntity<>("Review Not Updated",HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId){
        boolean isDeleted = reviewService.deleteReview(reviewId);
        if(isDeleted){
            return new ResponseEntity<>("Review Deleted Successfully", HttpStatus.OK);
        }
        else
            return new  ResponseEntity<>("Review Not Deleted",HttpStatus.NOT_FOUND);
    }

}
