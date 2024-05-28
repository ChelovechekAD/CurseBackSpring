package it.academy.cursebackspring.exceptions;


import it.academy.cursebackspring.utilities.Constants;

public class ReviewNotFoundException extends NotFoundException {

    public ReviewNotFoundException() {
        super(Constants.REVIEW_NOT_FOUND);
    }
}
