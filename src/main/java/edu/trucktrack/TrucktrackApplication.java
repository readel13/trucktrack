package edu.trucktrack;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrucktrackApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(TrucktrackApplication.class, args);
    }

}
