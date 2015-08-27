package ru.timurnav;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.File;

@SpringBootApplication
@EnableJpaRepositories
@ComponentScan
public class RESTfulSpringBootMySqlServerApplication {

    private static final LoggerWrapper LOG = LoggerWrapper.get(RESTfulSpringBootMySqlServerApplication.class);

    //TODO refactor with @Autowired
    private static String directory;

    public static String getDirectory() {
        return directory;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(RESTfulSpringBootMySqlServerApplication.class, args);

        MultipartProperties p = ctx.getBean(MultipartProperties.class);
        File imagesDirectory = new File(p.getLocation());

        if (!imagesDirectory.isDirectory() || !imagesDirectory.canWrite()) {
            LOG.error("Incorrect directory for uploaded images");
            throw new IllegalArgumentException("Please specify a correct multipart.location in " +
                    "application.properties. It should be an existing directory for uploading images");
        }
        directory = imagesDirectory.getAbsolutePath() + File.separator;
        System.out.println("ok, let's work!");
    }

}
