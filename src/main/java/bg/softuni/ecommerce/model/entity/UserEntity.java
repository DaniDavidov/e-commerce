package bg.softuni.ecommerce.model.entity;

import bg.softuni.ecommerce.model.entity.enums.GenderEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private String address;


}
