package bg.softuni.ecommerce.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {

    @NotEmpty
    @Size(min = 2, max = 20)
    private String firstName;

    @NotEmpty
    @Size(min = 2, max = 20)
    private String lastName;

    @NotEmpty(message = "Email is necessary.")
    @Email(message = "User email should be valid.")
//    @UniqueUserEmail(message = "User email should be unique.")
    private String email;

    @NotEmpty
    private String username;

    @NotEmpty
    private String phoneNumber;

    @NotEmpty
    @Size(min = 5)
    private String password;

    private String confirmPassword;
}
