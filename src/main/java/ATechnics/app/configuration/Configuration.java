package ATechnics.app.configuration;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public MultipartConfigElement element() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(500));
        factory.setMaxFileSize(DataSize.ofMegabytes(500));
        return factory.createMultipartConfig();
    }
}