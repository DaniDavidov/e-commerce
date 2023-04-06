package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.entity.BlacklistEntity;
import bg.softuni.ecommerce.model.entity.BrandEntity;
import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.model.entity.enums.GenderEnum;
import bg.softuni.ecommerce.repository.BlacklistRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlacklistServiceTest {

    private BlacklistService toTest;

    private UserEntity testUserEntity;

    private BlacklistEntity testBlacklistEntity;

    @Captor
    private ArgumentCaptor<BlacklistEntity> blacklistEntityArgumentCaptor;

    @Mock
    private BlacklistRepository mockBlacklistRepository;

    @BeforeEach
    void setUp() {
        this.toTest = new BlacklistService(mockBlacklistRepository);
        this.testUserEntity = new UserEntity(
                "test",
                "test@example.com",
                "12345",
                "Test",
                "Testov",
                "089",
                GenderEnum.MALE);
        this.testBlacklistEntity = new BlacklistEntity(testUserEntity);
    }

    @Test
    void testIsUserBlacklistedReturnsTrue() {
        when(mockBlacklistRepository.findByUserUsername(testUserEntity.getUsername()))
                .thenReturn(Optional.of(testBlacklistEntity));
        boolean userBlacklisted = toTest.isUserBlacklisted(testUserEntity.getUsername());
        Assertions.assertTrue(userBlacklisted);
    }

    @Test
    void testAddToBlacklistSuccess() {
        toTest.addToBlacklist(testUserEntity);

        Mockito.verify(mockBlacklistRepository).save(blacklistEntityArgumentCaptor.capture());

        BlacklistEntity savedUserInBlacklist = blacklistEntityArgumentCaptor.getValue();

        Assertions.assertEquals(testUserEntity.getUsername(), savedUserInBlacklist.getUser().getUsername());
        Assertions.assertEquals(testUserEntity.getEmail(), savedUserInBlacklist.getUser().getEmail());

    }
}