package com.nexus.platform.repository;
import com.nexus.platform.entity.User; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface UserRepository extends JpaRepository<User,Long>{ Optional<User> findByEmailIgnoreCase(String email); boolean existsByEmailIgnoreCase(String email); }
