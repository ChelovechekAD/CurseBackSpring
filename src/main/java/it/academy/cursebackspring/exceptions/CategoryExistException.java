package it.academy.cursebackspring.exceptions;


import it.academy.cursebackspring.utilities.Constants;

public class CategoryExistException extends ExistException {

    public CategoryExistException() {
        super(Constants.CATEGORY_ALREADY_EXIST_EXCEPTION_MESSAGE);
    }

}
