package com.preethy.moviebooking.exception;

public class UserNotAllowedException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public UserNotAllowedException(String message) {
        super(message);
    }

}
