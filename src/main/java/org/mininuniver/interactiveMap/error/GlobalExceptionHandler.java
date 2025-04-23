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

package org.mininuniver.interactiveMap.error;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex, WebRequest request) {
        ApiError apiError = createApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Произошла внутренняя ошибка сервера",
                ex.getMessage(),
                request
        );
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ApiResponse(responseCode = "401", description = "Ошибка аутентификации",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        ApiError apiError = createApiError(
                HttpStatus.UNAUTHORIZED,
                "Ошибка аутентификации",
                ex.getMessage(),
                request
        );
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ApiResponse(responseCode = "401", description = "Неверные учетные данные",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        ApiError apiError = createApiError(
                HttpStatus.UNAUTHORIZED,
                "Неверные учетные данные",
                "Неверное имя пользователя или пароль",
                request
        );
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ApiResponse(responseCode = "403", description = "Доступ запрещен",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ApiError apiError = createApiError(
                HttpStatus.FORBIDDEN,
                "Доступ запрещен",
                "Недостаточно прав для выполнения операции",
                request
        );
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(responseCode = "400", description = "Ошибка валидации данных",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Ошибка валидации данных")
                .path(request.getDescription(false).replace("uri=", ""))
                .validationErrors(validationErrors)
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(responseCode = "404", description = "Ресурс не найден",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<ApiError> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
        ApiError apiError = createApiError(
                HttpStatus.NOT_FOUND,
                "Ресурс не найден",
                "Запрашиваемый ресурс не существует: " + ex.getRequestURL(),
                request
        );
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(responseCode = "404", description = "Сущность не найдена",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        ApiError apiError = createApiError(
                HttpStatus.NOT_FOUND,
                "Сущность не найдена",
                ex.getMessage(),
                request
        );
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    private ApiError createApiError(HttpStatus status, String error, String message, WebRequest request) {
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
    }
}