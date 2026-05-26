package com.liciot.usermanagementms.service.security;


import com.liciot.usermanagementms.entity.Authority;
import com.liciot.usermanagementms.repository.UserRepository;
import com.liciot.usermanagementms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JWTUserDetailsService implements UserDetailsService {
     @Autowired
    UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username)  {

       com.liciot.usermanagementms.entity.User user = userService.findUserWithAuthoritiesByUsername(username);
        if (user != null) {
            if (user.getUsername().equals(username)) {
                //BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                Set<Authority> authorityList = user.getAuthoritySet();

                List<GrantedAuthority> grantedAuthorities = authorityList.stream().map(
                        (authority)->{
                            return new SimpleGrantedAuthority( authority.getAuthority());
                        }
                ).collect(Collectors.toList());

                return new User(user.getUsername(),  user.getPassword(),
                        grantedAuthorities);// nu aici hashuim aici trebuie sa fie deja hashuita
            } else {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
        }
        else {
            throw new UsernameNotFoundException("User with username: "+ username);
        }
    }

}
