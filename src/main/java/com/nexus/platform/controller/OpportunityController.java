package com.nexus.platform.controller;

import com.nexus.platform.entity.Opportunity;
import com.nexus.platform.repository.OpportunityRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {
    private final OpportunityRepository repo;
    public OpportunityController(OpportunityRepository repo) { this.repo = repo; }

    @GetMapping public List<Opportunity> all() { return repo.findAllByOrderByDeadlineAsc(); }

    @PostMapping
    public Opportunity create(@RequestBody Opportunity opportunity, Authentication auth) {
        opportunity.setId(null);
        opportunity.setCreatedBy(auth.getName());
        return repo.save(opportunity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Authentication auth) {
        Opportunity opportunity = repo.findById(id).orElseThrow();
        boolean admin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!admin && (opportunity.getCreatedBy() == null || !opportunity.getCreatedBy().equalsIgnoreCase(auth.getName()))) {
            throw new AccessDeniedException("You can only manage opportunities you created.");
        }
        repo.delete(opportunity);
    }
}
