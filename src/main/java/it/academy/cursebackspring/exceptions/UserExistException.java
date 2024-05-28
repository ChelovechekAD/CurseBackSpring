package it.academy.cursebackspring.exceptions;


import it.academy.cursebackspring.utilities.Constants;

public class UserExistException extends ExistException {

    public UserExistException() {
        super(Constants.USER_ALREADY_EXIST);
    }

}
