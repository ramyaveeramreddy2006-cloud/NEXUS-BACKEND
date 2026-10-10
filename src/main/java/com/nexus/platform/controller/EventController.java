package com.nexus.platform.controller;

import com.nexus.platform.entity.Event;
import com.nexus.platform.repository.EventRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventRepository repo;
    public EventController(EventRepository repo) { this.repo = repo; }

    @GetMapping public List<Event> all() { return repo.findAllByOrderByEventDateAsc(); }

    @PostMapping
    public Event create(@RequestBody Event event, Authentication auth) {
        event.setId(null);
        event.setCreatedBy(auth.getName());
        if (event.getRegistrationFee() == null) event.setRegistrationFee(java.math.BigDecimal.ZERO);
        return repo.save(event);
    }

    @PutMapping("/{id}")
    public Event update(@PathVariable Long id, @RequestBody Event event, Authentication auth) {
        Event current = repo.findById(id).orElseThrow();
        requireOwnerOrAdmin(current, auth);
        current.setTitle(event.getTitle()); current.setDescription(event.getDescription());
        current.setLocation(event.getLocation()); current.setEventDate(event.getEventDate());
        current.setDomain(event.getDomain()); current.setOrganizer(event.getOrganizer()); current.setSeats(event.getSeats());
        current.setRegistrationFee(event.getRegistrationFee() == null ? java.math.BigDecimal.ZERO : event.getRegistrationFee());
        current.setRegistrationUrl(event.getRegistrationUrl());
        return repo.save(current);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Authentication auth) {
        Event event = repo.findById(id).orElseThrow();
        requireOwnerOrAdmin(event, auth);
        repo.delete(event);
    }

    private void requireOwnerOrAdmin(Event event, Authentication auth) {
        boolean admin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!admin && (event.getCreatedBy() == null || !event.getCreatedBy().equalsIgnoreCase(auth.getName()))) {
            throw new AccessDeniedException("You can only manage events you created.");
        }
    }
}
