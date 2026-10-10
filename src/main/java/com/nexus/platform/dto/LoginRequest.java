package com.nexus.platform.dto;
import jakarta.validation.constraints.*; import lombok.*;
@Getter @Setter public class LoginRequest { @Email @NotBlank String email; @NotBlank String password; }
