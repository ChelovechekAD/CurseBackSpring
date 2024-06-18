package it.academy.cursebackspring.utilities;

public class Constants {

    //JWT AUTH DATA
    public static final String TOKEN_PATTERN = "Bearer ";
    public static final String TOKEN_HEADER = "Authorization";
    //END

    public static final String REQUESTED_CATALOG_NOT_EXIST = "Requested catalog not exist.";
    public static final String PRODUCT_NOT_FOUND = "Product not found.";
    public static final String USER_NOT_FOUND = "User not found!";
    public static final String ORDER_NOT_FOUND = "Order not found!";
    public static final String ENTITY_NOT_FOUND = "Entity not found!";
    public static final String ORDER_ITEM_NOT_FOUND = "Order item not found!";
    public static final String ORDER_ITEM_EXIST = "Order item exist!";
    public static final String PASSWORD_MATCH_EXCEPTION = "Password and password confirmation do not match!";
    public static final String USER_ALREADY_EXIST = "User already exist!";

    public static final String TOKEN_NOT_FOUND = "Token not found!";
    public static final String WRONG_PASSWORD = "Wrong password!";
    public static final String CART_ITEM_NOT_FOUND = "Cart item not found!";
    public static final String REVIEW_NOT_FOUND = "Review not found!";
    public static final String CART_ITEM_ALREADY_EXIST = "Cart item already exist!";
    public static final String REVIEW_ALREADY_EXIST = "Review already exist!";
    public static final String TOKEN_INVALID = "Refresh token invalid!";
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String ACCESS_DENIED = "Access denied!";
    public static final String REQUIRED_REQUEST_PARAMETERS_DOESNT_EXIST_OR_INVALID = "Required request parameters doesn't exist or invalid!";
    public static final String CATEGORY_PRODUCT_EXIST_EXCEPTION_MESSAGE = "Products with this category exist! You need to use root flag to delete all!";
    public static final String CATEGORY_ALREADY_EXIST_EXCEPTION_MESSAGE = "Category already exist!";
    public static final String THIS_PRODUCT_ALREADY_USED_IN_ORDER_S = "This product already used in order(s)";
    public static final String EXIST_EXCEPTION_MESSAGE = "Item already exist!";

    public static final String SELECT_AVG_RATING_FROM_REVIEW = "select avg(rating) from Review where reviewPK.product.id = ?1";
    public static final String TOKEN_WAS_STOLEN_LOG_MESSAGE = "Token was stolen!\n" +
            " User email from token: %s \n" +
            " User email from context: %s";
    public static final String TOKEN_WAS_STOLEN_EXCEPTION_MESSAGE = "Token was stolen!";
    public static final String REG_EXP_PRODUCT_NAME = "^[A-zА-я0-9]{1,40}$";
    public static final String COUNT_PER_PAGE_VALIDATION_EXCEPTION = "Count of elements per page cannot be less than 10.";
    public static final String PAGE_NUM_VALIDATION_EXCEPTION = "Page num cannot be less than 1.";
    public static final String CATEGORY_ID_VALIDATION_EXCEPTION = "Category id cannot be less than 1.";
    public static final String RATING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION = "Rating must be between 0 and 10";
    public static final String PRICE_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION = "Price cannot be less than 1.";
    public static final String PRODUCT_ID_VALIDATION_EXCEPTION = "Product id cannot be less than 1.";
    public static final String USER_ID_VALIDATION_EXCEPTION = "User id cannot be less than 1.";
    public static final String USER_NAME_REGEXP_PATTERN = "^[\\p{L}]+([\\p{L} '-]*[\\p{L}])?$";
    public static final String USER_NAME_VALIDATION_EXCEPTION = "User name must be match this rules:\n" +
            "Only letters (including diacritics), spaces, hyphens, and apostrophes are allowed.\n" +
            "Must be between 1 and 50 characters long.\n" +
            "Cannot have leading or trailing spaces.\n" +
            "No multiple consecutive spaces, hyphens, or apostrophes.";
    public static final String BUILDING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION = "Building must be between 1 and 1000";
    public static final String STREET_CANNOT_BE_BLANK_VALIDATION_EXCEPTION = "Street cannot be blank";
    public static final String CITY_CANNOT_BE_BLANK_VALIDATION_EXCEPTION = "City cannot be blank.";
    public static final String NAME_CANNOT_BE_BLANK_VALIDATION_EXCEPTION = "Name cannot be blank.";
    public static final String REG_EXP_PHONE_NUMBER = "^\\+?(\\d{1,3})?[-.\\s]?\\(?\\d{1,4}\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$";
    public static final String PHONE_NUMBER_VALIDATION_EXCEPTION = """
            Phone number must be match this rules:\s
            Allows optional country codes with a plus sign (e.g., +1, +44).
            Supports various delimiters (spaces, dashes, or dots).
            Allows parentheses for area codes.
            Ensures a minimum of 7 and a maximum of 15 digits (excluding delimiters).""";
    public static final String SURNAME_CANNOT_BE_BLANK_VALIDATION_EXCEPTION = "Surname cannot be blank.";
    public static final String SET_OF_ROLES_CANNOT_BE_EMPTY_VALIDATION_EXCEPTION = "Set of roles cannot be empty. Default role is 'DEFAULT_USER'.";
    public static final String TOKEN_MUST_NOT_BE_BLANK_VALIDATION_EXCEPTION = "Token must not be blank.";
    public static final String QUANTITY_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION = "Quantity cannot be less than 1.";
    public static final String CATEGORY_NAME_VALIDATION_EXCEPTION = "Category name must have at least 1 char.";
    public static final String ORDER_ID_VALIDATION_EXCEPTION = "Order id cannot be less than 1";
    public static final String COUNT_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION = "Count cannot be less than 0.";
    public static final String PRODUCT_NAME_NOT_MATCH_PATTERN_VALIDATION_EXCEPTION = "Product name not match pattern.";
    public static final String IMAGE_LINK_CANNOT_BE_EMPTY_VALIDATION_EXCEPTION = "Image link cannot be blank or null!";
    public static final String PRODUCT_NAME_CANNOT_BE_BLANK_OR_NULL = "Product name cannot be blank or null!";
    public static final String DESCRIPTION_CANNOT_BE_NULL_OR_BLANK = "Description cannot be null or blank!";
    public static final String PRICE_CANNOT_BE_NULL_VALIDATION_EXCEPTION = "Price cannot be null!";
    public static final String CATEGORY_ID_PARAM_KEY = "categoryId";
    public static final String ROOT_PARAM_KEY = "root";
    public static final String PRODUCT_ID_PARAM_KEY = "productId";
    public static final String PRODUCT_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION = "Product id cannot be null!";
    public static final String PRODUCT_ID_PARAM_NOT_FOUND = "Product id param not found!";
    public static final String CATEGORY_NAME_PARAM_KEY = "categoryName";
    public static final String ORDER_ID_PARAM_KEY = "orderId";
    public static final String CART_ITEM_CANNOT_BE_CREATED_WHEN_USER_IS_NULL = "Cart item cannot be created when user is null.";
    public static final String CART_ITEM_CANNOT_BE_CREATED_WHEN_PRODUCT_IS_NULL = "Cart item cannot be created when product is null.";
    public static final String ORDER_ITEM_CANNOT_BE_CREATED_WHEN_ORDER_IS_NULL = "OrderItem cannot be created when order is null.";
    public static final String ORDER_ITEM_CANNOT_BE_CREATED_WHEN_PRODUCT_IS_NULL = "Order item cannot be created when product is null.";
    public static final String REVIEW_CANNOT_BE_CREATED_WHEN_USER_IS_NULL = "Review cannot be created when user is null.";
    public static final String REVIEW_CANNOT_BE_CREATED_WHEN_PRODUCT_IS_NULL = "Review cannot be created when product is null.";
    public static final String CART_ITEM_PRIMARY_KEY_CANNOT_BE_NULL = "Cart item primary key cannot be null.";
    public static final String QUANTITY_OF_ITEM_CANNOT_BE_NULL_VALIDATION_EXCEPTION = "Quantity of item cannot be null.";
    public static final String MIN_PRICE = "0.01";
    public static final int MIN_CATEGORY_ID = 1;
    public static final int MIN_COUNT_OF_ITEMS_IN_CART = 1;
    public static final int MIN_PRODUCT_ID = 1;
    public static final int MIN_USER_ID = 1;
    public static final String MIN_RATING = "0";
    public static final String MAX_RATING = "10";
    public static final int MIN_ORDER_ID = 1;
    public static final int MIN_PAGE_NUM = 1;
    public static final int MIN_COUNT_PER_PAGE = 10;
    public static final String USER_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION = "User id cannot be null.";
    public static final String PAGE_NUM_CANNOT_BE_NULL_VALIDATION_EXCEPTION = "Page num cannot be null.";
    public static final String COUNT_PER_PAGE_CANNOT_BE_NULL_VALIDATION_EXCEPTION = "Count per page cannot be null.";
    public static final String ORDER_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION = "Order id cannot be null.";
    public static final String RATING_CANNOT_BE_NULL_VALIDATION_EXCEPTION = "Rating cannot be null.";
    public static final String CATEGORY_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION = "Category id cannot be null.";
    public static final String EMAIL_CANNOT_BE_BLANK_OR_NULL_VALIDATION_EXCEPTION = "Email cannot be blank or null.";
    public static final String PASSWORD_CANNOT_BE_BLANK_OR_NULL_VALIDATION_EXCEPTION = "Password cannot be blank or null!";
    public static final int MIN_COUNT_OF_ITEM_IN_ORDER = 1;
    public static final String PHONE_NUMBER_CANNOT_BE_BLANK_OR_NULL_VALIDATION_EXCEPTION = "Phone number cannot be blank or null!";
    public static final String ORDER_STATUS_CANNOT_BE_NULL_VALIDATION_EXCEPTION = "Order status cannot be null.";
    public static final String BUILDING_CANNOT_BE_NULL_OR_BLANK_VALIDATION_EXCEPTION = "Building cannot be null or blank.";
    public static final int MIN_BUILDING = 1;
    public static final int MAX_BUILDING = 1000;
    public static final String REGEXP_EMAIL = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final String USER_ID_PARAM_KEY = "userId";
}
