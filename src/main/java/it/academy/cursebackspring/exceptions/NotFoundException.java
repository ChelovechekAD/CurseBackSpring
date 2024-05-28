package it.academy.cursebackspring.exceptions;


import it.academy.cursebackspring.utilities.Constants;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super(Constants.ENTITY_NOT_FOUND);
    }

    public NotFoundException(String message) {
        super(message);
    }

}
