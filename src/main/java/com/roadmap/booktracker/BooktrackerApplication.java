package com.roadmap.booktracker;

import com.roadmap.booktracker.auth.config.properties.CookieProperties;
import com.roadmap.booktracker.auth.config.properties.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({
        JwtProperties.class,
        CookieProperties.class
})
@EnableScheduling
public class BooktrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooktrackerApplication.class, args);
    }

}
