package com.liciot.usermanagementms.repository;

import com.liciot.usermanagementms.entity.Device;
import com.liciot.usermanagementms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository  extends JpaRepository<Device, UUID> {

    public List<Device> findByAdminUser(User adminUser);
}
