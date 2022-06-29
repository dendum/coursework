package ua.lviv.iot.Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ua.lviv.iot.Server.controllers",
        "ua.lviv.iot.Server.services"})

public class App {
    public static void main(final String[] args) {
        SpringApplication.run(App.class, args);
    }
}
