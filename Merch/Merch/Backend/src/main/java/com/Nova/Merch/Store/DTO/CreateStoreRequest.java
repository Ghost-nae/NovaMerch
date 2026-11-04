package com.Nova.Merch.Store.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateStoreRequest(

        @NotBlank
        @Size(max = 150)
        String name,

        @NotBlank
        @Size(max = 50)
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "Store code must be lowercase and may contain hyphens"
        )
        String code
) {
}
