package com.nexus.platform.entity;
import jakarta.persistence.*; import lombok.*; import java.time.LocalDateTime;
@Entity @Table(name="opportunities") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Opportunity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
 String title; String organization; String type; String location; String applyUrl;
 @Enumerated(EnumType.STRING) Domain domain; @Column(length=2000) String description; LocalDateTime deadline;
 String createdBy;
}
