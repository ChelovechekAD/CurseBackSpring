package it.academy.cursebackspring.exceptions;

import it.academy.cursebackspring.utilities.Constants;

public class CategoryNotFoundException extends NotFoundException {

    public CategoryNotFoundException() {
        super(Constants.REQUESTED_CATALOG_NOT_EXIST);
    }

}
