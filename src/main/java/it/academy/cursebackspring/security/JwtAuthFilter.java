package it.academy.cursebackspring.security;

import io.jsonwebtoken.JwtException;
import it.academy.cursebackspring.components.JwtUtils;
import it.academy.cursebackspring.exceptions.UnauthorizedException;
import it.academy.cursebackspring.services.UserService;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader(Constants.TOKEN_HEADER);
            if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, Constants.TOKEN_PATTERN)) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = authHeader.substring(Constants.TOKEN_PATTERN.length());
            if (!jwtUtils.validateAccessToken(jwt)) {
                throw new UnauthorizedException();
            }
            String email = jwtUtils.getAccessClaims(jwt).getSubject();
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService
                        .userDetailsService()
                        .loadUserByUsername(email);
                log.info("User details: " + userDetails);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            } else {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (!authentication.getPrincipal().equals(email)) {
                    log.warn(String.format(Constants.TOKEN_WAS_STOLEN_LOG_MESSAGE, email, authentication));
                    throw new JwtException(Constants.TOKEN_WAS_STOLEN_EXCEPTION_MESSAGE);
                }
            }

            filterChain.doFilter(request, response);
        } catch (UnauthorizedException | JwtException e) {
            log.warn("During jwt filter execution, successfully catch exception: {} with message \"{}\"", e.getClass(), e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(e.getMessage());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.contains("/api/v1/auth/");
    }
}
