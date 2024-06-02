package it.academy.cursebackspring.controllers;

import it.academy.cursebackspring.dto.request.LoginUserDTO;
import it.academy.cursebackspring.dto.request.RegUserDTO;
import it.academy.cursebackspring.dto.response.LoginUserJwtDTO;
import it.academy.cursebackspring.services.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginUserDTO dto) {
        LoginUserJwtDTO out = authService.loginUser(dto);
        ResponseCookie cookie = ResponseCookie.from("refresh-token", out.getRefreshToken())
                .httpOnly(true)
                .path("/api/v1/auth/refresh")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(out);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("refresh-token")
                .value(null)
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/registration")
    public ResponseEntity<?> reg(@RequestBody @Valid RegUserDTO regUserDTO) {
        authService.regUser(regUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("refresh-token") String token) {
        LoginUserJwtDTO loginUserJwtDTO = authService.reLoginUser(token);
        ResponseCookie cookie = ResponseCookie.from("refresh-token", loginUserJwtDTO.getRefreshToken())
                .httpOnly(true)
                .path("/api/v1/auth/refresh")
                .build();
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, cookie.toString()).body(loginUserJwtDTO);
    }

}
