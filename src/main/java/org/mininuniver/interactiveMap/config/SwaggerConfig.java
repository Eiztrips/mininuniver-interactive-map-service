package org.mininuniver.interactiveMap.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Интерактивная карта Mininuniver API")
                        .description("API для взаимодействия с интерактивной картой")
                        .version("0.1.1")
                        .contact(new Contact()
                                .name("Eiztrips")
                                .url("https://github.com/Eiztrips"))
                        .license(new License()
                                .name("GNU Affero General Public License v3")
                                .url("https://www.gnu.org/licenses/agpl-3.0.html")));

    }
}