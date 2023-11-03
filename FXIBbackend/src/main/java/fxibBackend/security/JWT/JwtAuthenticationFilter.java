package fxibBackend.security.JWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static fxibBackend.constants.JWTConst.*;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    /**
     * Custom filter for processing JWT authentication.
     * Extracts and validates a JWT token from the request and sets the user's authentication if valid.
     * Then, it passes the request to the next filter in the filter chain.
     *
     * @param request       The incoming HTTP request.
     * @param response      The HTTP response.
     * @param filterChain   The filter chain to continue processing the request.
     * @throws ServletException if an error occurs during the filter processing.
     * @throws IOException if an I/O error occurs during the filter processing.
     */
    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request,
                                 @NonNull HttpServletResponse response,
                                 @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Extract the JWT token from the request
        String token = getTokenFromRequest(request);

        // Validate the token and set user authentication if it's valid
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Continue processing the request with the next filter in the chain
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the request's Authorization header.
     *
     * @param request The incoming HTTP request.
     * @return The JWT token if found in the request header, or null if not present.
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_TYPE)) {
            return bearerToken.substring(BEGIN_INDEX_TO_RAW_TOKEN);
        }
        return null;
    }


}
