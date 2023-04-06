package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.model.entity.UserRoleEntity;
import bg.softuni.ecommerce.model.entity.enums.UserRoleEnum;
import bg.softuni.ecommerce.model.error.UserNotFoundException;
import bg.softuni.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opentest4j.AssertionFailedError;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EcommerceUserDetailsServiceTest {
    private final String EXISTING_USERNAME = "admin";
    private final String NOT_EXISTING_USERNAME = "notExisting";

    private EcommerceUserDetailsService toTest;

    @Mock
    private UserRepository testUserRepository;


    @BeforeEach
    void setUp() {
        toTest = new EcommerceUserDetailsService(testUserRepository);
    }

    @Test
    void testUserFound() {
        UserRoleEntity testAdminRole = new UserRoleEntity(UserRoleEnum.ADMIN);
        UserRoleEntity testUserRole = new UserRoleEntity(UserRoleEnum.USER);

        UserEntity testUserEntity = new UserEntity();
        testUserEntity.setUsername(EXISTING_USERNAME);
        testUserEntity.setPassword("12345");
        testUserEntity.setUserRoles(Set.of(testAdminRole, testUserRole));

        when(testUserRepository.findByUsername(EXISTING_USERNAME))
                .thenReturn(Optional.of(testUserEntity));

        UserDetails adminDetails = toTest.loadUserByUsername(EXISTING_USERNAME);

        Assertions.assertNotNull(adminDetails);
        Assertions.assertEquals(EXISTING_USERNAME, adminDetails.getUsername());
        Assertions.assertEquals(testUserEntity.getPassword(), adminDetails.getPassword());
        Assertions.assertEquals(2, adminDetails.getAuthorities().size());
        assertRole(adminDetails.getAuthorities(), "ROLE_ADMIN");
        assertRole(adminDetails.getAuthorities(), "ROLE_USER");
    }

    private void assertRole(Collection<? extends GrantedAuthority> authorities, String role) {
        authorities.stream()
                .filter(a -> role.equals(a.getAuthority()))
                .findAny()
                .orElseThrow(() -> new AssertionFailedError("Role " + role + " not found"));
    }


    @Test
    void testUserNotFound() {
        assertThrows(UsernameNotFoundException.class,
                () -> {
            toTest.loadUserByUsername("non-existent@example.com");
                });
    }
}