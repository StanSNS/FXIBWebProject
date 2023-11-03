package SecurityTest;

import fxibBackend.security.JWT.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        jwtTokenProvider.setJwtSecret("daf66e01593f61a15b857cf433aae03a005812b31234e149036bcc8dee755dbb");
        jwtTokenProvider.setJwtExpirationDate(3600000);
    }

    @Test
    public void testGenerateToken() {
        Authentication authentication = new Authentication() {

            @Override
            public String getName() {
                return "testUser";
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }
        };

        String token = jwtTokenProvider.generateToken(authentication);
        assertNotNull(token);
    }

    @Test
    public void testGetUsername() {
        String token = jwtTokenProvider.generateToken(new TestAuthentication("testUser"));

        String username = jwtTokenProvider.getUsername(token);
        assertEquals("testUser", username);
    }

    @Test
    public void testValidateToken() {
        String validToken = jwtTokenProvider.generateToken(new TestAuthentication("testUser"));

        jwtTokenProvider.setJwtExpirationDate(-1);
        String expiredToken = jwtTokenProvider.generateToken(new TestAuthentication("testUser"));

        assertTrue(jwtTokenProvider.validateToken(validToken));
        assertFalse(jwtTokenProvider.validateToken(expiredToken));
    }

    private record TestAuthentication(String username) implements Authentication {

        @Override
        public String getName() {
            return username;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getDetails() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return null;
        }

        @Override
        public boolean isAuthenticated() {
            return false;
        }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

        }
    }

    @Test
    public void testPrivateFields() {
        assertEquals(jwtTokenProvider.getJwtSecret(), "daf66e01593f61a15b857cf433aae03a005812b31234e149036bcc8dee755dbb");
        assertEquals(jwtTokenProvider.getJwtExpirationDate(), 3600000);
    }

}
