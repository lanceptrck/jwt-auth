package com.devops3.controller;

import com.devops3.dto.ErrorDTO;
import com.devops3.dto.GenericDTO;
import com.devops3.dto.PayloadDTO;
import com.devops3.dto.Status;
import com.devops3.payload.response.TokenRefreshResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Operation(summary = "Endpoint that can be accessed by all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Public content",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PayloadDTO.class))})
    })
    @GetMapping("/all")
    public ResponseEntity<?> allAccess() {
        PayloadDTO dto = new PayloadDTO();
        dto.setResponseCode(200);
        dto.setStatus(Status.SUCCESS);
        dto.setData("Public content.");

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Endpoint that can be accessed by authenticated users", security = { @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated user content",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PayloadDTO.class))})
    })
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> userAccess() {
        PayloadDTO dto = new PayloadDTO();
        dto.setResponseCode(200);
        dto.setStatus(Status.SUCCESS);
        dto.setData("User content.");

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Endpoint that can only be accessed by moderators")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Content for moderators only",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PayloadDTO.class))})
    })
    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> moderatorAccess() {
        PayloadDTO dto = new PayloadDTO();
        dto.setResponseCode(200);
        dto.setStatus(Status.SUCCESS);
        dto.setData("Moderator board.");

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Endpoint that can only be accessed by admins")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrator board",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PayloadDTO.class))})
    })
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminAccess() {
        PayloadDTO dto = new PayloadDTO();
        dto.setResponseCode(200);
        dto.setStatus(Status.SUCCESS);
        dto.setData("Admin board.");

        return ResponseEntity.ok(dto);
    }
}