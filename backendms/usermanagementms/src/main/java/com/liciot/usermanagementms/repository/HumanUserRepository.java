package com.liciot.usermanagementms.repository;

import com.liciot.usermanagementms.entity.HumanUser;
 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HumanUserRepository extends JpaRepository<HumanUser, UUID> {
    public Optional<HumanUser> findByUsername(String username);

}
