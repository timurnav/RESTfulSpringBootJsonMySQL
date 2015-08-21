package ru.timurnav.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.timurnav.domain.User;
import ru.timurnav.domain.UserRepository;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public List<User> getAll(@RequestParam(value = "online", required = false) Boolean online,
                             @RequestParam(value = "id", required = false) Long id
//                             @RequestParam(value = "id", required = false) Timestamp timestamp
    ) {
//        if (timestamp != null) {
        if (id != null) {
            Timestamp timestamp = new Timestamp(id);
            if (online != null) {
                return userRepository.findByOnlineAndStatusTimestampAfter(online, timestamp);
            }
            return userRepository.findByStatusTimestampAfter(timestamp);
        } else if (online != null) {
            return userRepository.findByOnline(online);
        }
        return userRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody User user) {
        User u = userRepository.saveAndFlush(user);
        return new ResponseEntity<>(u.getId(), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity changeStatus(@RequestParam("id") Long id,
                                       @RequestParam("online") boolean online) {

//        FutureTask<>
//        ExecutorService
//        ThreadPoolTaskExecutor
        User user = userRepository.findOne(id);
        boolean old = user.isOnline();
        user.setOnline(online);
        userRepository.saveAndFlush(user);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("new", online);
        map.put("old", old);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/upload", method = RequestMethod.POST)
    public ResponseEntity upload(@PathVariable("id") Long id,
                                 @RequestParam("pic") MultipartFile pic) {

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
