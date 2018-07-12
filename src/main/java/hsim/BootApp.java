package hsim;

import hsim.checkpoint.init.InitCheckPoint;
import hsim.checkpoint.util.AnnotationScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Configuration
@PropertySource("classpath:app.properties")
public class BootApp {

    public static void main(String[] args) {
        SpringApplication.run(BootApp.class, args);
    }
}
