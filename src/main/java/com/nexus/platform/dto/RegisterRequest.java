package com.nexus.platform.dto;
import com.nexus.platform.entity.*; import jakarta.validation.constraints.*; import lombok.*; import java.util.Set;
@Getter @Setter public class RegisterRequest { @NotBlank String name; @Email @NotBlank String email; @NotBlank @Size(min=6,max=100) String password; @NotNull Role role; @NotNull Domain domain; Set<Domain> domains; String phone; String city; }
