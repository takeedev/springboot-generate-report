package takee.dev.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringbootGenerateReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootGenerateReportApplication.class, args);
	}

}
