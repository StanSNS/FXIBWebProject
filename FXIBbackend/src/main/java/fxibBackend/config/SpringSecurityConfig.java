package fxibBackend.config;

import fxibBackend.security.Interceptor.AdminInterceptor;
import fxibBackend.security.JWT.JwtAuthenticationEntryPoint;
import fxibBackend.security.JWT.JwtAuthenticationFilter;
import fxibBackend.security.JWT.JwtTokenProvider;
import fxibBackend.util.ValidateData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static fxibBackend.constants.MappingConstants.TWO_FACTOR_AUTH_CONTROLLER_MAPPING_LOGIN;
import static fxibBackend.constants.RoleConst.ADMIN_C;
import static fxibBackend.constants.URLAccessConst.*;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig implements WebMvcConfigurer {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter authenticationFilter;
    private final JwtTokenProvider jwtTokenProvider;
    private final ValidateData validateData;

    /**
     * Creates a BCryptPasswordEncoder instance for password hashing and validation.
     *
     * @return A BCryptPasswordEncoder instance for password encoding.
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures security filters and policies for HTTP requests using Spring Security.
     *
     * @param http The HttpSecurity object to configure.
     * @return A SecurityFilterChain with the defined security rules.
     * @throws Exception if there's an issue configuring security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF protection
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests((authorize) -> {
            // Define request matchers and permissions
            authorize.requestMatchers(
                    ALL_PATHS,
                    CUSTOM_LOGOUT_PATHS,
                    ADMIN_PATH,
                    AUTH_PATH,
                    ABOUT_PATH,
                    PRICING_PATH,
                    ACCOUNTS_PATH,
                    PARTNERS_PATH,
                    COMMUNITY_PATH,
                    CHANGE_PASSWORD_PATH,
                    RESET_PASSWORD_PATH,
                    RESET_PASSWORD_UPDATE_PATH,
                    TWO_FACTOR_AUTH_CONTROLLER_MAPPING_LOGIN
            ).permitAll();
            authorize.requestMatchers("/admin").authenticated();
            authorize.requestMatchers("/admin").hasRole(ADMIN_C);
            authorize.anyRequest().authenticated();
        });
        http.httpBasic(Customizer.withDefaults());
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint));
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Creates an AuthenticationManager using the provided AuthenticationConfiguration.
     *
     * @param configuration The AuthenticationConfiguration object to configure the manager.
     * @return An AuthenticationManager instance.
     * @throws Exception if there's an issue creating the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Configures interceptors for specific URL paths.
     *
     * @param registry The InterceptorRegistry to register interceptors.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(jwtTokenProvider, validateData))
                .addPathPatterns("/admin").addPathPatterns("/community/{action}");
    }

}
