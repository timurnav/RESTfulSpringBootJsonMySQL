package ru.timurnav;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;

@SpringBootApplication
public class RESTfulSpringBootMySqlServerApplication {

    private static final LoggerWrapper LOG = LoggerWrapper.get(RESTfulSpringBootMySqlServerApplication.class);

    private static String directory;

    public static String getDirectory() {
        return directory;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(RESTfulSpringBootMySqlServerApplication.class, args);
        LOG.debug("Test directory for images");
        MultipartProperties p = ctx.getBean(MultipartProperties.class);
        File imagesDirectory = new File(p.getLocation());
        if (imagesDirectory.isDirectory() && imagesDirectory.canWrite()) {
            directory = imagesDirectory.getAbsolutePath() + File.separator;
        } else {
            LOG.error("Incorrect directory for uploaded images");
            throw new IllegalArgumentException("Please specify a correct multipart.location in " +
                    "application.properties. It should be an existing directory for uploading images");
        }
        LOG.debug("Test directory is ok!");
    }

}
