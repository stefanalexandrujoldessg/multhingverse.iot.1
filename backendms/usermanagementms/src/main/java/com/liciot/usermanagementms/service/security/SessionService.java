package com.liciot.usermanagementms.service.security;

import com.liciot.usermanagementms.entity.Session;
import com.liciot.usermanagementms.entity.User;
import com.liciot.usermanagementms.repository.SessionRepository;
import com.liciot.usermanagementms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional
    public Session createSession(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {


            Session session = new Session();
            session.setUser(userOptional.get());
            session.setExpirationDateTime(LocalDateTime.now().plusHours(24));
            sessionRepository.save(session);
            return session;
        }
        return null;
    }
@Transactional
    public User validateAndGetUserWithAuthoritiesFormSessionToken(String sessionToken) {
        Optional<Session> sessionOptional = sessionRepository.getByToken(UUID.fromString(sessionToken));
        if(sessionOptional.isPresent())
        {
            Session session = sessionOptional.get();
            if( session.getExpirationDateTime().isAfter(LocalDateTime.now()))
            {
                User user = session.getUser();
                user.getAuthoritySet();
                System.out.print( session.getUser().getAuthoritySet().size());
                return user;
            }
            else
            {
                sessionRepository.delete(session);
            }
        }

        return null;
    }
    @Scheduled(fixedDelay = 2000)
    public void removeExpired()
    {
            this.sessionRepository.removeExpiredTokens(LocalDateTime.now());
    }

    public void removeSessionByByToken(String sessionToken)
    {
        Optional<Session> sessionOptional = sessionRepository.getByToken(UUID.fromString(sessionToken));
        if(sessionOptional.isPresent())
        {


                sessionRepository.delete( sessionOptional.get());

        }




    }
}
