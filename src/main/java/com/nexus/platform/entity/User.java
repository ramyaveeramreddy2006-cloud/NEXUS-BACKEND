package com.nexus.platform.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity @Table(name="users") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private String name;
  @Column(nullable=false, unique=true) private String email;
  @Column(nullable=false) private String password;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private Role role;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private Domain domain;
  @ElementCollection(fetch=FetchType.EAGER) @CollectionTable(name="user_domains",joinColumns=@JoinColumn(name="user_id"))
  @Enumerated(EnumType.STRING) @Column(name="domain") @Builder.Default private Set<Domain> domains=new HashSet<>();
  private String phone;
  private String city;
  private String bio;
  private LocalDateTime createdAt;
  @PrePersist void prePersist(){ if(createdAt==null) createdAt=LocalDateTime.now(); if(domains==null) domains=new HashSet<>(); if(domain!=null) domains.add(domain); }
}
