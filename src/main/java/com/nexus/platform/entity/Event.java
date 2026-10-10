package com.nexus.platform.entity;
import com.fasterxml.jackson.annotation.JsonIgnore; import jakarta.persistence.*; import lombok.*; import java.time.*; import java.math.BigDecimal; import java.util.*;
@Entity @Table(name="events") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Event {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
 String title; @Column(length=3000) String description; String location; LocalDateTime eventDate;
 @Enumerated(EnumType.STRING) Domain domain; String organizer; int seats;
 String createdBy;
 @Column(precision=10,scale=2) BigDecimal registrationFee;
 String registrationUrl;
 @JsonIgnore @OneToMany(mappedBy="event",cascade=CascadeType.ALL,orphanRemoval=true) List<EventRegistration> registrations=new ArrayList<>();
}
