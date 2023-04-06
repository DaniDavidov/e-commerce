package bg.softuni.ecommerce.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestUserDataService implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals("admin")) {
            return new User(username,
                    "12345",
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        }

        return new User(username,
                "12345",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
