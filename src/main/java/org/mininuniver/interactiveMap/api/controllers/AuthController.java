/*
 * This file is part of mininuniver-interactive-map-service.
 *
 * Copyright (C) 2025 Eiztrips
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.mininuniver.interactiveMap.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.mininuniver.interactiveMap.api.dto.auth.Request;
import org.mininuniver.interactiveMap.api.dto.auth.Response;
import org.mininuniver.interactiveMap.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "API для аутентификации пользователей")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Аутентификация пользователя и получение JWT токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная аутентификация",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные", content = @Content),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Request request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Неверное имя пользователя или пароль", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new Response(jwt));
    }

    @Operation(summary = "Проверка валидности токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Токен действителен"),
            @ApiResponse(responseCode = "401", description = "Токен недействителен или истек",
                    content = @Content)
    })
    @GetMapping("/checkToken")
    public ResponseEntity<Map<String, Object>> checkToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        response.put("valid", true);
        response.put("username", auth.getName());

        return ResponseEntity.ok(response);
    }
}