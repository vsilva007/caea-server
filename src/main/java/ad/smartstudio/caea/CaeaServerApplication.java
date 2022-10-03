package ad.smartstudio.caea;

import com.sendgrid.SendGrid;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@SpringBootApplication
public class CaeaServerApplication {
	private static final String CAEA_APIKEY = "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";

	public static void main(String[] args) {
		SpringApplication.run(CaeaServerApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().components(new Components()).info(new Info().title("CAEA").description("Classificació d'Activitats Econòmiques Andorrana"));
	}

	@Bean
	public SendGrid sendGrid() {
		return new SendGrid(CAEA_APIKEY);
	}
}
