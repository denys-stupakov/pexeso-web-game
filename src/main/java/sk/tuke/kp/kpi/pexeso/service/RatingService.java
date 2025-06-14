package sk.tuke.kp.kpi.pexeso.service;

import sk.tuke.kp.kpi.pexeso.entity.Rating;

public interface RatingService {
    void setRating(Rating rating) throws RatingException;

    int getAverageRating(String game) throws RatingException;

    int getRating(String player, String game) throws RatingException;

    void reset() throws RatingException;
}
