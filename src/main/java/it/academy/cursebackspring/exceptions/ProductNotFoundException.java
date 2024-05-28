package it.academy.cursebackspring.exceptions;


import it.academy.cursebackspring.utilities.Constants;

public class ProductNotFoundException extends NotFoundException {

    public ProductNotFoundException() {
        super(Constants.PRODUCT_NOT_FOUND);
    }
}
