package fxibBackend.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.ipgeolocation.api.Geolocation;
import io.ipgeolocation.api.IPGeolocationAPI;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static fxibBackend.constants.ConfigConst.*;

@Component
public class AppConfig {

    /**
     * Creates and configures a Gson instance for JSON serialization and deserialization.
     *
     * @return A Gson instance with default configuration.
     */
    @Bean
    public Gson gson() {
        return new GsonBuilder().create();
    }

    /**
     * Creates and configures a ModelMapper instance with a custom LocalDateTime to String converter.
     * The converter formats LocalDateTime objects to a custom date format.
     *
     * @return A ModelMapper instance with custom configuration.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(new AbstractConverter<LocalDateTime, String>() {
            @Override
            protected String convert(LocalDateTime source) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CUSTOM_DATE_FORMAT);
                return source.format(formatter);
            }
        });
        return modelMapper;
    }

    /**
     * Creates and configures a JavaMailSenderImpl instance for sending email.
     * It sets the host, port, username, and password for the email server and other properties.
     *
     * @return A JavaMailSenderImpl instance configured for sending emails.
     */
    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(EMAIL_HOST);
        javaMailSender.setPort(EMAIL_PORT);
        javaMailSender.setUsername(EMAIL_ORIGIN);
        javaMailSender.setPassword(EMAIL_PASSWORD);
        Properties props = javaMailSender.getJavaMailProperties();
        props.put(EMAIL_PROPS_KEY_ONE, EMAIL_PROPS_VALUE_ONE);
        props.put(EMAIL_PROPS_KEY_TWO, EMAIL_PROPS_VALUE_TWO);
        props.put(EMAIL_PROPS_KEY_THREE, EMAIL_PROPS_VALUE_THREE);
        return javaMailSender;
    }
}
