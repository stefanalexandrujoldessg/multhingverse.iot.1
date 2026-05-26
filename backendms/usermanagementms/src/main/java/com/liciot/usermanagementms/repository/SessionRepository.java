package com.liciot.usermanagementms.repository;

import com.liciot.usermanagementms.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    public Optional<Session> getByToken(UUID token);

    @Transactional
    @Modifying
    @Query("delete from Session where expiration_date_time <= ?1")//daca p[ui natibve query exact ce scrii tu qjunge in db altfel  scrie numele entitati de aici
            void removeExpiredTokens(LocalDateTime localDateTime);
}
