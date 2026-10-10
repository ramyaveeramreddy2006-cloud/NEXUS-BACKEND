package com.nexus.platform.repository;
import com.nexus.platform.entity.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface EventRepository extends JpaRepository<Event,Long>{ List<Event> findAllByOrderByEventDateAsc(); long countByDomain(Domain domain); }
