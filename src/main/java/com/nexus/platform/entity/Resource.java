package com.nexus.platform.entity;
import jakarta.persistence.*; import lombok.*; import java.time.LocalDateTime;
@Entity @Table(name="resources") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Resource {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
 String title; @Column(length=3000) String description; String url; String tags;
 @Enumerated(EnumType.STRING) Domain domain; String uploadedBy; LocalDateTime createdAt;
 @PrePersist void prePersist(){createdAt=LocalDateTime.now();}
}
