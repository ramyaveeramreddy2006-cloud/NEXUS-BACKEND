package com.nexus.platform.controller;

import com.nexus.platform.entity.Domain;
import com.nexus.platform.entity.Post;
import com.nexus.platform.entity.User;
import com.nexus.platform.repository.PostRepository;
import com.nexus.platform.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostRepository repo;
    private final UserRepository users;

    public PostController(PostRepository repo, UserRepository users) { this.repo = repo; this.users = users; }

    @GetMapping
    public List<Post> all(@RequestParam(required = false) Domain domain) {
        return domain == null ? repo.findAllByOrderByCreatedAtDesc() : repo.findByDomainOrderByCreatedAtDesc(domain);
    }

    @GetMapping("/{id}/voice")
    public Map<String, String> voice(@PathVariable Long id) {
        Post post = repo.findById(id).orElseThrow();
        if (post.getAudioData() == null || post.getAudioData().isBlank()) throw new IllegalArgumentException("This post has no voice message.");
        return Map.of("audioData", post.getAudioData(), "audioMimeType", post.getAudioMimeType() == null ? "audio/webm" : post.getAudioMimeType());
    }

    @PostMapping
    public Post create(@RequestBody Post post, Authentication auth) {
        validateVoice(post);
        User user = users.findByEmailIgnoreCase(auth.getName()).orElseThrow();
        post.setId(null);
        post.setAuthorName(user.getName());
        post.setAuthorEmail(user.getEmail());
        return repo.save(post);
    }

    @PutMapping("/{id}")
    public Post update(@PathVariable Long id, @RequestBody Post post, Authentication auth) {
        validateVoice(post);
        Post current = repo.findById(id).orElseThrow();
        requireOwnerOrAdmin(current.getAuthorEmail(), auth);
        current.setTitle(post.getTitle());
        current.setContent(post.getContent());
        current.setDomain(post.getDomain());
        current.setType(post.getType());
        current.setAudioData(post.getAudioData());
        current.setAudioMimeType(post.getAudioMimeType());
        return repo.save(current);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Authentication auth) {
        Post current = repo.findById(id).orElseThrow();
        requireOwnerOrAdmin(current.getAuthorEmail(), auth);
        repo.delete(current);
    }

    private void requireOwnerOrAdmin(String email, Authentication auth) {
        boolean admin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!auth.getName().equalsIgnoreCase(email) && !admin) throw new AccessDeniedException("You can only manage your own posts.");
    }

    private void validateVoice(Post post) {
        String audio = post.getAudioData();
        if (audio == null || audio.isBlank()) {
            post.setAudioData(null);
            post.setAudioMimeType(null);
            return;
        }
        if (audio.length() > 7_000_000 || !audio.startsWith("data:audio/")) {
            throw new IllegalArgumentException("Voice recordings must be audio files smaller than 5 MB.");
        }
    }
}
