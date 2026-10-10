package com.nexus.platform.controller;

import com.nexus.platform.entity.Domain;
import com.nexus.platform.entity.User;
import com.nexus.platform.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final UserRepository users;
    public ProfileController(UserRepository users) { this.users = users; }

    @PutMapping("/domains")
    public Map<String, Set<Domain>> updateDomains(@RequestBody Map<String, List<String>> body, Authentication authentication) {
        List<String> requested = body.get("domains");
        if (requested == null || requested.isEmpty()) throw new IllegalArgumentException("Select at least one field.");
        if (requested.size() > Domain.values().length) throw new IllegalArgumentException("Too many fields selected.");

        User user = users.findByEmailIgnoreCase(authentication.getName()).orElseThrow();
        Set<Domain> selected = new HashSet<>();
        for (String value : requested) selected.add(Domain.valueOf(value));
        selected.add(user.getDomain());
        user.setDomains(selected);
        users.save(user);
        return Map.of("domains", selected);
    }
}
