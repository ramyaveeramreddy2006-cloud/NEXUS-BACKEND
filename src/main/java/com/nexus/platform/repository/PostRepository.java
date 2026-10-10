package com.nexus.platform.repository;
import com.nexus.platform.entity.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface PostRepository extends JpaRepository<Post,Long>{ List<Post> findAllByOrderByCreatedAtDesc(); List<Post> findByDomainOrderByCreatedAtDesc(Domain domain); long countByDomain(Domain domain); }
