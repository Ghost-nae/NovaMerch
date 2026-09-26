package com.Nova.Merch.Auth.Controller;

import com.Nova.Merch.Auth.DTO.LoginRequest;
import com.Nova.Merch.Auth.DTO.LoginResponse;
import com.Nova.Merch.Auth.Service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthenticationService authenticationService;

  public AuthController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping("/login")
  public RequestEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    LoginResponse response = authenticationService.login(request);

    return ResponseEntity.ok(response);
  }
}
