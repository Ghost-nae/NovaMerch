package com.Nova.Merch.Access.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateMerchantOwnerRequest(

    @NotBlank(message = "Merchant email is required")
    @Email(message = "A valid email address is required")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    String email

) {}
