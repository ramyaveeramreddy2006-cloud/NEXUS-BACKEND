package com.nexus.platform.repository;
import com.nexus.platform.entity.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ResourceRepository extends JpaRepository<Resource,Long>{ List<Resource> findAllByOrderByCreatedAtDesc(); long countByDomain(Domain domain); }
