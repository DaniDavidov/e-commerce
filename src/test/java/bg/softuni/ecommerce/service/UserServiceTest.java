package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.dto.user.UserDetailsDto;
import bg.softuni.ecommerce.model.dto.user.UserProfileDto;
import bg.softuni.ecommerce.model.dto.user.UserRegisterDto;
import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.model.entity.UserRoleEntity;
import bg.softuni.ecommerce.model.entity.enums.GenderEnum;
import bg.softuni.ecommerce.model.entity.enums.UserRoleEnum;
import bg.softuni.ecommerce.repository.UserRepository;
import bg.softuni.ecommerce.repository.UserRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opentest4j.AssertionFailedError;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.TemplateEngine;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final String TEST_USERNAME = "testUsername";
    private static final String TEST_FIRST_NAME = "Test";
    private static final String TEST_LAST_NAME = "Testov";
    private static final String TEST_PASSWORD = "12345";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PHONE_NUMBER = "089089089";
    private static final String TEST_ADDRESS = "address";
    private static final GenderEnum TEST_GENDER = GenderEnum.MALE;
    private static final Long TEST_ID = 1L;
    private static final UserRoleEnum MODERATOR_ROLE = UserRoleEnum.MODERATOR;
    private static final UserRoleEnum USER_ROLE = UserRoleEnum.USER;


    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private UserRoleRepository mockUserRoleRepository;

    @Captor
    private ArgumentCaptor<UserEntity> userEntityArgumentCaptor;

    @Mock
    private UserRepository mockUserRepository;

    private UserService toTest;

    private UserEntity testUserEntity;

    @Mock
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        this.emailService = new EmailService(new JavaMailSenderImpl(), new TemplateEngine());
        toTest = new UserService(mockUserRepository, mockUserRoleRepository, mockPasswordEncoder, emailService);
        this.testUserEntity = new UserEntity();
        testUserEntity.setId(TEST_ID);
        testUserEntity.setUsername(TEST_USERNAME);
        testUserEntity.setFirstName(TEST_FIRST_NAME);
        testUserEntity.setLastName(TEST_LAST_NAME);
        testUserEntity.setPassword(TEST_PASSWORD);
        testUserEntity.setEmail(TEST_EMAIL);
        testUserEntity.setPhoneNumber(TEST_PHONE_NUMBER);
        testUserEntity.setAddress(TEST_ADDRESS);
        testUserEntity.setUserRoles(Set.of(new UserRoleEntity(UserRoleEnum.USER)));
    }

    @Test
    void testUserRegistration() {
        String encodedPassword = "encodedPassword";

        UserRegisterDto testRegisterDto = new UserRegisterDto(
                TEST_FIRST_NAME,
                TEST_LAST_NAME,
                TEST_EMAIL,
                TEST_USERNAME,
                TEST_PHONE_NUMBER,
                TEST_GENDER,
                TEST_PASSWORD,
                TEST_PASSWORD);

        when(mockPasswordEncoder.encode(testRegisterDto.getPassword()))
                .thenReturn(encodedPassword);
        when(emailService.generateEmailText(TEST_USERNAME)).thenReturn(null);

        toTest.register(testRegisterDto);

        Mockito.verify(mockUserRepository).save(userEntityArgumentCaptor.capture());

        UserEntity savedUserEntity = userEntityArgumentCaptor.getValue();

        Assertions.assertEquals(testRegisterDto.getUsername(), savedUserEntity.getUsername());
        Assertions.assertEquals(encodedPassword, savedUserEntity.getPassword());
    }

    @Test
    void testGetUserProfileSuccess() {

        UserProfileDto userProfile = toTest.getUserProfile(testUserEntity);

        Assertions.assertEquals(TEST_USERNAME, userProfile.getUsername());
        Assertions.assertEquals(TEST_EMAIL, userProfile.getEmail());
    }

    @Test
    void testGetUserDetails() {
        UserDetailsDto userDetails = toTest.getUserDetails(testUserEntity);

        Assertions.assertEquals(testUserEntity.getUsername(), userDetails.getUsername());
        Assertions.assertEquals(testUserEntity.getFirstName(), userDetails.getFirstName());
        Assertions.assertEquals(testUserEntity.getLastName(), userDetails.getLastName());
        Assertions.assertEquals(testUserEntity.getEmail(), userDetails.getEmail());
    }

    @Test
    void testPromoteToModeratorSuccess() {
        when(mockUserRepository.findById(TEST_ID)).thenReturn(Optional.of(testUserEntity));
        when(mockUserRoleRepository.findByName(MODERATOR_ROLE)).thenReturn(new UserRoleEntity(MODERATOR_ROLE));

        toTest.promoteToModerator(TEST_ID);

        Mockito.verify(mockUserRepository).save(userEntityArgumentCaptor.capture());
        UserEntity savedUserEntity = userEntityArgumentCaptor.getValue();

        UserRoleEntity roleEntity = savedUserEntity.getUserRoles()
                .stream()
                .filter(userRole -> userRole.getName().equals(MODERATOR_ROLE))
                .findAny()
                .orElseThrow(() -> new AssertionFailedError("No such Role"));

        Assertions.assertEquals(MODERATOR_ROLE, roleEntity.getName());
    }

    @Test
    void testDemoteToUserSuccess() {
        when(mockUserRepository.findById(TEST_ID)).thenReturn(Optional.of(testUserEntity));
        when(mockUserRoleRepository.findByName(USER_ROLE)).thenReturn(new UserRoleEntity(USER_ROLE));

        toTest.demoteToUser(TEST_ID);

        Mockito.verify(mockUserRepository).save(userEntityArgumentCaptor.capture());
        UserEntity savedUserEntity = userEntityArgumentCaptor.getValue();

        UserRoleEntity roleEntity = savedUserEntity.getUserRoles()
                .stream()
                .filter(userRole -> userRole.getName().equals(USER_ROLE))
                .findAny()
                .orElseThrow(() -> new AssertionFailedError("No such Role"));

        Assertions.assertEquals(USER_ROLE, roleEntity.getName());
    }
}