package ru.timurnav.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.timurnav.domain.User;
import ru.timurnav.domain.UserRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@PathVariable("id") Long id) {

        return userRepository.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Scheduled()
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @RequestMapping(value = "/{id}/upload", method = RequestMethod.POST)
    public ResponseEntity upload(@RequestParam("pic") MultipartFile pic,
                                 @PathVariable("id") Long id) {

        if (!pic.isEmpty()) {
            if (pic.getContentType().startsWith("image/")) {
                String fileName = pic.getOriginalFilename();
                File file = new File("/pic/user_avatar_" + id + fileName.substring(fileName.lastIndexOf(".")));
                try {
                    pic.transferTo(file);
                } catch (IOException e) {
                    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return new ResponseEntity<>(file.getAbsolutePath(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity(HttpStatus.RESET_CONTENT);
            }
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

}
