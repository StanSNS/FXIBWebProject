package SecurityTest;

import fxibBackend.entity.UserEntity;
import fxibBackend.security.Interceptor.AdminInterceptor;
import fxibBackend.security.JWT.JwtTokenProvider;
import fxibBackend.util.ValidateData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminInterceptorTest {

    private AdminInterceptor adminInterceptor;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private ValidateData validateData;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminInterceptor = new AdminInterceptor(jwtTokenProvider, validateData);
    }

    @Test
    void preHandle_AllConditionsMet_ReturnsTrue() {
        when(request.getQueryString()).thenReturn("username=test&jwtToken=validToken");
        when(validateData.validateUserWithJWT("test", "validToken")).thenReturn(new UserEntity());
        when(validateData.isUserAdmin(any())).thenReturn(true);
        when(jwtTokenProvider.validateToken("validToken")).thenReturn(true);

        boolean result = adminInterceptor.preHandle(request, response, new Object());

        assertTrue(result);
    }

    @Test
    void preHandle_InvalidAdminUser_ReturnsFalse() {
        when(request.getQueryString()).thenReturn("username=test&jwtToken=validToken");
        when(validateData.validateUserWithJWT("test", "validToken")).thenReturn(new UserEntity()); // Non-admin user
        when(jwtTokenProvider.validateToken("validToken")).thenReturn(true);

        boolean result = adminInterceptor.preHandle(request, response, new Object());

        assertFalse(result);
    }

    @Test
    void preHandle_InvalidToken_ReturnsFalse() {
        when(request.getQueryString()).thenReturn("username=test&jwtToken=invalidToken");
        when(jwtTokenProvider.validateToken("invalidToken")).thenReturn(false);

        boolean result = adminInterceptor.preHandle(request, response, new Object());

        assertFalse(result);
    }

    @Test
    void preHandle_MissingQueryString_ReturnsFalse() {
        when(request.getQueryString()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> adminInterceptor.preHandle(request, response, new Object()));
    }
}
