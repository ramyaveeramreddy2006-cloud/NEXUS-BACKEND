package com.nexus.platform.controller;

import com.nexus.platform.entity.Resource;
import com.nexus.platform.repository.ResourceRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {
    private final ResourceRepository repo;
    public ResourceController(ResourceRepository repo) { this.repo = repo; }

    @GetMapping public List<Resource> all() { return repo.findAllByOrderByCreatedAtDesc(); }

    @PostMapping
    public Resource create(@RequestBody Resource resource, Authentication auth) {
        resource.setId(null);
        resource.setUploadedBy(auth.getName());
        return repo.save(resource);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Authentication auth) {
        Resource resource = repo.findById(id).orElseThrow();
        boolean admin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!admin && (resource.getUploadedBy() == null || !resource.getUploadedBy().equalsIgnoreCase(auth.getName()))) {
            throw new AccessDeniedException("You can only manage your own resources.");
        }
        repo.delete(resource);
    }
}
