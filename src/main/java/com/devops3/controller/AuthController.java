package com.devops3.controller;

import com.devops3.dto.ErrorDTO;
import com.devops3.exception.TokenRefreshException;
import com.devops3.model.AuthenticationRequest;
import com.devops3.model.RefreshToken;
import com.devops3.payload.request.TokenRefreshRequest;
import com.devops3.payload.response.JwtResponse;
import com.devops3.payload.response.TokenRefreshResponse;
import com.devops3.security.UserDetailsImpl;
import com.devops3.security.service.RefreshTokenService;
import com.devops3.service.RoleRepository;
import com.devops3.service.UserRepository;
import com.devops3.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RefreshTokenService refreshTokenService;


    @Operation(summary = "Login user, return JWT if the user exists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged in",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class))}),
            @ApiResponse(responseCode = "401", description = "User does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody(required = true) AuthenticationRequest authenticationRequest) throws Exception {

        Authentication authentication;
        try {
             authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());


        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @Operation(summary = "Refresh expired token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully refreshed the token",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenRefreshResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Cannot refresh token",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtTokenUtil.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

}
