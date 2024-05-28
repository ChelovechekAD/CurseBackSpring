package it.academy.cursebackspring.exceptions;

import it.academy.cursebackspring.utilities.Constants;

public class ReviewExistException extends ExistException {

    public ReviewExistException() {
        super(Constants.REVIEW_ALREADY_EXIST);
    }

}
