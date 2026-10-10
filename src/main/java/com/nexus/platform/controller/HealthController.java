package com.nexus.platform.controller; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api") public class HealthController{ @GetMapping("/health") public Map<String,Object> health(){return Map.of("status","UP","service","NEXUS Platform","time",java.time.Instant.now().toString());}}
