package com.nexus.platform.dto;
import com.nexus.platform.entity.*; import lombok.*; import java.util.Set;
@Getter @AllArgsConstructor public class AuthResponse { String token; Long id; String name; String email; Role role; Domain domain; Set<Domain> domains; }
