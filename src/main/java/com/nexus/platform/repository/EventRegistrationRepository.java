package com.nexus.platform.repository;

import com.nexus.platform.entity.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    long countByEvent_Id(Long eventId);
    Optional<EventRegistration> findByEvent_IdAndUser_Id(Long eventId, Long userId);
}
