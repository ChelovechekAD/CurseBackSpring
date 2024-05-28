package it.academy.cursebackspring.exceptions;


import it.academy.cursebackspring.utilities.Constants;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException() {
        super(Constants.USER_NOT_FOUND);
    }
}
