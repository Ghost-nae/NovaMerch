package com.Nova.Merch.Identity.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompleteMerchantSetupRequest(

        @NotBlank(message = "Setup token is required")
        String token,

        @NotBlank(message = "Password is required")
        @Size(
            min = 12,
            max = 128,
            message = "Password must contain between 12 and 128 characters"
        )
        String password

) {}
