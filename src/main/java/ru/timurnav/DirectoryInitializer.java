package ru.timurnav;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DirectoryInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        File imagesDirectory = new File(mp.getLocation());
        if (!imagesDirectory.isDirectory() || !imagesDirectory.canWrite()) {
            throw new IllegalArgumentException("Please specify a correct multipart.location in " +
                    "application.properties. It should be an existing directory for uploading images");
        }
        System.out.println("directory is ok, let's work!");
    }

    @Autowired
    MultipartProperties mp;
}
