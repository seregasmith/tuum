import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@EntityScan("model.entity")
@EnableJpaRepositories("model.repo")
@ComponentScan(basePackages = {"mq", "model"})
public class AccountPRCServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountPRCServiceApplication.class, args);
    }

}
