package ru.timurnav.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.timurnav.LoggerWrapper;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Callable;

import static ru.timurnav.RESTfulSpringBootMySqlServerApplication.getDirectory;

@RestController
@RequestMapping(value = "/images")
public class ImageController {


    private static final LoggerWrapper LOG = LoggerWrapper.get(UserController.class);

    private static final String ERROR_TEMPLATE = "Can not upload file %s. Cause %s";


    /**
     * Uploading images.
     * <p>
     * Before uploading you have to specify in application.property a correct directory for uploaded images
     *
     * @param pic - the image
     * @return the ResponseEntity with HttpStatus and the String with
     */

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Callable<ResponseEntity> upload(@RequestParam("pic") MultipartFile pic) {
        return () -> {
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            Thread.yield();
            String originalFilename = pic.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            if (!pic.isEmpty()) {
                if (pic.getContentType().startsWith("image/")) {

                    File file = new File(UUID.randomUUID().toString() + extension);

                    try {
                        pic.transferTo(file);
                        return new ResponseEntity<>(file.getName(), HttpStatus.CREATED);

                    } catch (IOException e) {
                        LOG.error(String.format(ERROR_TEMPLATE, originalFilename, e.getMessage()));
                        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                } else {
                    LOG.error(String.format(ERROR_TEMPLATE, originalFilename, "type is unsupported"));
                    return new ResponseEntity<>("Images expected, but not " + pic.getContentType(), HttpStatus.RESET_CONTENT);
                }

            } else {
                LOG.error(String.format(ERROR_TEMPLATE, originalFilename, "file is empty"));
                return new ResponseEntity<>("Empty file", HttpStatus.NO_CONTENT);
            }
        };
    }
/*

    @RequestMapping(value = "/{imageName:.+}", method = RequestMethod.GET)
    public ResponseEntity getImage(@PathVariable String imageName){
        File f = new File(getDirectory() + imageName);

        if (f.isFile() && f.canRead()){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
*/
}
