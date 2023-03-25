package bg.softuni.ecommerce.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String address;

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }


}
