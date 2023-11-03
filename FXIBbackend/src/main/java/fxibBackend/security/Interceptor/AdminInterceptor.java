package fxibBackend.security.Interceptor;

import fxibBackend.entity.UserEntity;
import fxibBackend.security.JWT.JwtTokenProvider;
import fxibBackend.util.ValidateData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class AdminInterceptor implements HandlerInterceptor {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final JwtTokenProvider jwtTokenProvider;
    private final ValidateData validateData;

    /**
     * Interceptor for handling admin-related requests.
     * This interceptor performs validation for admin access by parsing and validating JWT tokens.
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // Extract the query string from the request
        String queryString = request.getQueryString();

        if (!queryString.isEmpty()) {
            // Split the query string into parts
            String[] parts = queryString.split("&");
            String username = parts[0].split("=")[1];
            String jwtToken = parts[1].split("=")[1];

            // Validate the user with the JWT token
            UserEntity userEntity = validateData.validateUserWithJWT(username, jwtToken);

            // Check if the user is an admin and if the JWT token is valid
            if (userEntity != null && validateData.isUserAdmin(userEntity.getRoles()) && jwtTokenProvider.validateToken(jwtToken)) {
                return true;
            }
        }
        return false;
    }
}


