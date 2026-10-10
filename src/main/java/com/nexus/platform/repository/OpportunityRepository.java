package com.nexus.platform.repository;
import com.nexus.platform.entity.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface OpportunityRepository extends JpaRepository<Opportunity,Long>{ List<Opportunity> findAllByOrderByDeadlineAsc(); long countByDomain(Domain domain); }
