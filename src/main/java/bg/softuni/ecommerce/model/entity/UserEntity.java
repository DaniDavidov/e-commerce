package bg.softuni.ecommerce.model.entity;

import bg.softuni.ecommerce.model.entity.enums.GenderEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private String address;

    @ManyToMany
    private Set<UserRoleEntity> userRoles;

    @OneToMany
    private List<OfferEntity> offers;

    public UserEntity(String username,
                      String email,
                      String password,
                      String firstName,
                      String lastName,
                      String phoneNumber,
                      GenderEnum gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public UserEntity(String username,
                      String email,
                      String password,
                      String firstName,
                      String lastName,
                      String phoneNumber,
                      GenderEnum gender, Set<UserRoleEntity> userRoles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.userRoles = userRoles;
    }
}
