package fxibBackend;

import fxibBackend.exception.AccessDeniedException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(info = @Info(title = "FXIB - Backend API",version = "1.0"))
public class FxibBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(FxibBackendApplication.class, args);
    }
}
