package com.Nova.Merch.Tenant.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateTenantRequest(

    @NotBlank(message = "Tenant name is required")
    @Size(max = 150, message = "Tenant name cannot exceed 150 characters")
    String name,

    @NotBlank(message = "Tenant code is required")
    @Pattern(
        regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
        message = "Tenant code must contain lowercase letters, numbers or hyphens"
    )
    @Size(max = 50, message = "Tenant code cannot exceed 50 characters")
    String code

) {}
