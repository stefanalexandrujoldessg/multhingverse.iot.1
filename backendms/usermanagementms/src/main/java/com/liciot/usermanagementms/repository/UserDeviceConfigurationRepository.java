package com.liciot.usermanagementms.repository;

import com.liciot.usermanagementms.entity.Device;
import com.liciot.usermanagementms.entity.User;
import com.liciot.usermanagementms.entity.UserDeviceConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserDeviceConfigurationRepository extends JpaRepository<UserDeviceConfiguration, UUID> {

    public List<UserDeviceConfiguration> findByUser(User user);
    public Optional<UserDeviceConfiguration> findByUserAndDevice(User user, Device device);
}
