package bg.softuni.ecommerce.model.dto.user;

import bg.softuni.ecommerce.model.dto.offer.OfferDetailsDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailsDto {

    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String roles;


    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public UserDetailsDto(Long id, String username, String email, String firstName, String lastName, String phoneNumber, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.roles = String.join(", ", roles);
    }
}
