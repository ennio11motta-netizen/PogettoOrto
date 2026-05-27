package mainprova;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {
        "mainprova",
        "controller",
        "model",
        "repository",
        "service",
        "simulation",
        "rdf",
        "util",
        "config",
        "dto_mapper",
        "exception"
})
@EntityScan(basePackages = "model")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}