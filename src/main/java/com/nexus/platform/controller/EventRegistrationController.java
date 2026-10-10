package com.nexus.platform.controller;

import com.nexus.platform.entity.Event;
import com.nexus.platform.entity.EventRegistration;
import com.nexus.platform.entity.User;
import com.nexus.platform.repository.EventRegistrationRepository;
import com.nexus.platform.repository.EventRepository;
import com.nexus.platform.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/events/{eventId}/registrations")
public class EventRegistrationController {
    private final EventRepository events;
    private final EventRegistrationRepository registrations;
    private final UserRepository users;

    public EventRegistrationController(EventRepository events, EventRegistrationRepository registrations, UserRepository users) {
        this.events = events;
        this.registrations = registrations;
        this.users = users;
    }

    @GetMapping("/me")
    public Map<String, Object> mine(@PathVariable Long eventId, Authentication auth) {
        Event event = events.findById(eventId).orElseThrow();
        User user = users.findByEmailIgnoreCase(auth.getName()).orElseThrow();
        long count = registrations.countByEvent_Id(eventId);
        return Map.of("registered", registrations.findByEvent_IdAndUser_Id(eventId, user.getId()).isPresent(),
                "registeredCount", count, "availableSeats", Math.max(0, event.getSeats() - count));
    }

    @PostMapping
    public Map<String, Object> register(@PathVariable Long eventId, Authentication auth) {
        Event event = events.findById(eventId).orElseThrow();
        User user = users.findByEmailIgnoreCase(auth.getName()).orElseThrow();
        if (event.getRegistrationFee() != null && event.getRegistrationFee().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("This paid event uses the organizer's registration link.");
        }
        if (registrations.findByEvent_IdAndUser_Id(eventId, user.getId()).isPresent()) {
            throw new IllegalArgumentException("You are already registered for this event.");
        }
        long registeredCount = registrations.countByEvent_Id(eventId);
        if (event.getSeats() <= 0 || registeredCount >= event.getSeats()) {
            throw new IllegalArgumentException("This event has no seats remaining.");
        }

        EventRegistration registration = new EventRegistration();
        registration.setEvent(event);
        registration.setUser(user);
        registrations.save(registration);
        long updatedCount = registeredCount + 1;
        return Map.of("registered", true, "registeredCount", updatedCount,
                "availableSeats", Math.max(0, event.getSeats() - updatedCount));
    }

    @DeleteMapping
    public Map<String, Object> cancel(@PathVariable Long eventId, Authentication auth) {
        Event event = events.findById(eventId).orElseThrow();
        User user = users.findByEmailIgnoreCase(auth.getName()).orElseThrow();
        EventRegistration registration = registrations.findByEvent_IdAndUser_Id(eventId, user.getId()).orElseThrow();
        registrations.delete(registration);
        long updatedCount = Math.max(0, registrations.countByEvent_Id(eventId));
        return Map.of("registered", false, "registeredCount", updatedCount,
                "availableSeats", Math.max(0, event.getSeats() - updatedCount));
    }
}
