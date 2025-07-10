package org.td024;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpaceReservationApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpaceReservationApplication.class, args);
    }
}
