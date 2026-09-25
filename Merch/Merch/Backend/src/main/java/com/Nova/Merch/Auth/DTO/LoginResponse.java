package com.Nova.Merch.Auth.DTO;

public record LoginResponse (String accessToken, String tokenType, long expiresIn) {}
