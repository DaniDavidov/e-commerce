package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.entity.BlacklistEntity;
import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.repository.BlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlacklistService {
    private final BlacklistRepository blacklistRepository;

    @Autowired
    public BlacklistService(BlacklistRepository blacklistRepository) {
        this.blacklistRepository = blacklistRepository;
    }

//    public boolean isBlacklistedByIpAddress(String ipAddress) {
//
//        Optional<BlacklistEntity> blacklistedUser = this.blacklistRepository.findByIpAddress(ipAddress);
//
//        return blacklistedUser.isPresent();
//    }

    public boolean isUserBlacklisted(String username) {
        Optional<BlacklistEntity> blacklistedUser = this.blacklistRepository.findByUserUsername(username);
        return blacklistedUser.isPresent();
    }

    public void addToBlacklist(UserEntity userEntity) {
        BlacklistEntity blacklistEntity = new BlacklistEntity(userEntity);

        this.blacklistRepository.save(blacklistEntity);
    }

    public void removeFromBlacklist(UserEntity userEntity) {
        this.blacklistRepository.deleteByUserUsername(userEntity.getUsername());
    }
}
