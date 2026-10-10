package com.nexus.platform.entity;
import com.fasterxml.jackson.annotation.JsonProperty; import jakarta.persistence.*; import lombok.*; import java.time.LocalDateTime;
@Entity @Table(name="posts") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Post {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
 @Column(nullable=false) String title;
 @Column(nullable=false,length=5000) String content;
 @Enumerated(EnumType.STRING) Domain domain;
 String type;
 String authorName;
 String authorEmail;
 @Lob @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String audioData;
 String audioMimeType;
 LocalDateTime createdAt;
 @JsonProperty("hasVoice") public boolean hasVoice(){return audioData!=null&&!audioData.isBlank();}
 @PrePersist void prePersist(){createdAt=LocalDateTime.now();}
}
