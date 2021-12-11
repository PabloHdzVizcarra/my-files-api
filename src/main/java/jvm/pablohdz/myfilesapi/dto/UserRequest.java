package jvm.pablohdz.myfilesapi.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * Representation of request data to save a new user.
 */
public class UserRequest {
    @NotNull(message = "username is required")
    @Size(min = 3, max = 55,
        message = "the username be greater than 3 characters")
    private String username;

    @Pattern(regexp = "^[a-zA-Z ]+$", message = "firstname must be only have characters string")
    @Size(min = 3, max = 55,
        message = "the firstname be greater than 3 characters")
    private String firstName;

    @Size(min = 3, max = 55,
        message = "the lastname be greater than 3 characters")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "lastname must be only have characters string")
    private String lastName;

    @Positive
    @Digits(integer = 8, fraction = 0,
        message = "employee number must be length equal to eight digits")
    @Size(min = 8, max = 8, message = "employee number must be length equal to eight digits")
    private String numEmployee;

    @Email(message = "the email must be valid")
    private String email;

    public UserRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNumEmployee() {
        return numEmployee;
    }

    public void setNumEmployee(String numEmployee) {
        this.numEmployee = numEmployee;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
