package com.tyuhgce.LoweAlpine.utils;

import spark.Request;

/**
 * Params wrapper
 *
 * @version 1.0
 * @since 1.0
 */
public class RequestParams {
    private static final String CINEMA = "cinema";
    private static final String CINEMA_HALL = "hall";
    private static final String CINEMA_HALL_PLACE = "place";

    private final String cinema;
    private final String hall;
    private int place;
    private final String key;

    private void validate(String cinema, String hall, int place) {
        if (cinema == null) {
            throw new IllegalArgumentException(CINEMA + " is null");
        }
        if (hall == null) {
            throw new IllegalArgumentException(CINEMA_HALL + " is null");
        }
        if (place < 0) {
            throw new IllegalArgumentException(CINEMA_HALL_PLACE + " is negative");
        }
    }

    public RequestParams(String cinema, String hall, int place) {
        this.cinema = cinema;
        this.hall = hall;
        this.place = place;
        validate(this.cinema, this.hall, this.place);
        this.key = this.cinema + "|" + this.hall;
    }

    public RequestParams(Request request) {
        this.cinema = request.params(CINEMA);
        this.hall = request.params(CINEMA_HALL);
        if (request.params(CINEMA_HALL_PLACE) != null) {
            this.place = Integer.parseInt(request.params(CINEMA_HALL_PLACE));
        }
        validate(this.cinema, this.hall, this.place);

        this.key = this.cinema + "|" + this.hall;
    }

    public String getCinema() {
        return cinema;
    }

    public String getHall() {
        return hall;
    }

    public int getPlace() {
        return place;
    }

    public String getKey() {
        return key;
    }
}
