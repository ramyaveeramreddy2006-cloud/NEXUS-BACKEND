package com.nexus.platform.controller;
import com.nexus.platform.repository.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/dashboard") public class DashboardController{
 private final UserRepository u; private final PostRepository p; private final EventRepository e; private final ResourceRepository r; private final OpportunityRepository o;
 public DashboardController(UserRepository u,PostRepository p,EventRepository e,ResourceRepository r,OpportunityRepository o){this.u=u;this.p=p;this.e=e;this.r=r;this.o=o;}
 @GetMapping public Map<String,Object> summary(){return Map.of("users",u.count(),"posts",p.count(),"events",e.count(),"resources",r.count(),"opportunities",o.count(),"domains",List.of("EDUCATION","AGRICULTURE","HEALTHCARE","BUSINESS","TECHNOLOGY","GOVERNMENT","COMMUNITY"));}
}
