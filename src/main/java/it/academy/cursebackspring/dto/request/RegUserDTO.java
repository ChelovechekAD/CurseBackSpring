package it.academy.cursebackspring.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DialectOverride;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegUserDTO implements Serializable {

    @NotBlank(message = Constants.EMAIL_CANNOT_BE_BLANK_OR_NULL_VALIDATION_EXCEPTION)
    @Pattern(regexp = Constants.REGEXP_EMAIL)
    @Schema(name = "email", description = "User email", type = "string", defaultValue = "test@test.test")
    private String email;

    @Schema(name = "name", description = "User name", type = "string", defaultValue = "Name")
    @NotBlank(message = Constants.NAME_CANNOT_BE_BLANK_VALIDATION_EXCEPTION)
    private String name;

    @Schema(name = "surname", description = "User surname", type = "string", defaultValue = "Surname")
    @NotBlank(message = Constants.SURNAME_CANNOT_BE_BLANK_VALIDATION_EXCEPTION)
    private String surname;

    @Schema(name = "password", description = "New user password", type = "string", defaultValue = "1234")
    @NotBlank(message = Constants.PASSWORD_CANNOT_BE_BLANK_OR_NULL_VALIDATION_EXCEPTION)
    private String password;

    @Schema(name = "passwordConfirm", description = "Password confirmation", type = "string", defaultValue = "1234")
    @NotBlank(message = Constants.PASSWORD_CANNOT_BE_BLANK_OR_NULL_VALIDATION_EXCEPTION)
    private String passwordConfirm;

    @Schema(name = "city", description = "User city", requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "City")
    private String city;

    @Schema(name = "street", description = "User street", requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "Street")
    private String street;

    @Schema(name = "building", description = "User building", requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "123")
    @Min(value = Constants.MIN_BUILDING, message = Constants.BUILDING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    @Max(value = Constants.MAX_BUILDING, message = Constants.BUILDING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    private Integer building;

    @Schema(name = "phoneNumber", description = "User phone number", defaultValue = "+123 12 23234")
    @NotBlank(message = Constants.PHONE_NUMBER_CANNOT_BE_BLANK_OR_NULL_VALIDATION_EXCEPTION)
    @Pattern(regexp = Constants.REG_EXP_PHONE_NUMBER, message = Constants.PHONE_NUMBER_VALIDATION_EXCEPTION)
    private String phoneNumber;

}
