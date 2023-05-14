package edu.trucktrack;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TrucktrackApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(TrucktrackApplication.class, args);
    }

}
