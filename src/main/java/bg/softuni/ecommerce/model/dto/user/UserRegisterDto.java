package bg.softuni.ecommerce.model.dto.user;

import bg.softuni.ecommerce.model.entity.enums.GenderEnum;
import bg.softuni.ecommerce.model.validation.FieldMatch;
import bg.softuni.ecommerce.model.validation.UniqueUserEmail;
import bg.softuni.ecommerce.model.validation.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(
        first = "password",
        second = "confirmPassword",
        message = "Passwords must match!")
public class UserRegisterDto {

    @NotEmpty
    @Size(min = 2, max = 20)
    private String firstName;

    @NotEmpty
    @Size(min = 2, max = 20)
    private String lastName;

    @NotEmpty(message = "Email is necessary.")
    @Email(message = "User email should be valid.")
    @UniqueUserEmail(message = "User email should be unique.")
    private String email;

    @NotEmpty
    @UniqueUsername(message = "This username is already taken")
    private String username;

    @NotEmpty
    private String phoneNumber;

    @NotNull
    private GenderEnum gender;

    @NotEmpty
    @Size(min = 5)
    private String password;

    private String confirmPassword;
}
