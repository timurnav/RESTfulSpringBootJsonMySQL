package ru.timurnav.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.timurnav.domain.User;
import ru.timurnav.domain.UserRepository;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final String URI_TEMPLATE = "/images/user_avatar_%d%s";
    private final File DIRECTORY = new File("/images");

    @Autowired
    UserRepository userRepository;

    /**
     * This method returns an User entity with assigned id
     *
     * @param id - id of user in database
     * @return user
     */

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@PathVariable("id") Long id) {
        return userRepository.findOne(id);
    }

    /**
     * The request parameters are optional. Parameters are used to filter the list
     * of users in response body.
     *
     * @param online filters users by status online/offline
     * @param id     is the Timestamp, in case it presents, the request contains only
     *               users whose status is changed after this timestamp
     * @return List of users
     */

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

    /**
     * Method is used to save a new User entity to database
     *
     * @param user - user serialized in JSON
     * @return ResponseEntity, contains an HttpStatus and ID of the created user
     */

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@Valid @RequestBody User user) {
        User u = userRepository.saveAndFlush(user);
        return new ResponseEntity<>(u.getId(), HttpStatus.CREATED);
    }

    /**
     * The method changes the status of users,
     *
     * @param id     - to identify the user in database
     * @param online - new status of user, should be true/false
     * @return ResponseEntity with an HttpStatus and contains in ResponseBody
     * id - id of user,
     * new - new status,
     * old - old status
     */

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity changeStatus(@RequestParam("id") Long id,
                                       @RequestParam("online") boolean online) {

        Timestamp updated = new Timestamp((new Date()).getTime());

        /**
         * Here we should create a request to outer API.
         * We send a request, and this thread is falling asleep,
         * when we receive a response we create a new Callable.
         *
         * Но как это сделать я пока еще не разобрался..
         */

        User user = userRepository.findOne(id);
        boolean old = user.isOnline();
        user.setOnline(online);
        user.setStatusTimestamp(updated);
        userRepository.saveAndFlush(user);
        //TODO refactor it!
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("new", online);
        map.put("old", old);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * Uploading images.
     *
     * @param id  - user's id
     * @param pic - the image
     * @return the ResponseEntity with HttpStatus and the String with
     */
    @RequestMapping(value = "/{id}/upload", method = RequestMethod.POST)
    public ResponseEntity upload(@PathVariable("id") Long id,
                                 @RequestParam("pic") MultipartFile pic) {

        if (!pic.isEmpty()) {
            if (pic.getContentType().startsWith("image/")) {

                String originalFilename = pic.getOriginalFilename();
                String imageName = String.format(URI_TEMPLATE,
                        id,
                        originalFilename.substring(originalFilename.lastIndexOf(".")));

                File file = new File(imageName);

                try {
                    if (!DIRECTORY.exists()) DIRECTORY.mkdirs();
                    pic.transferTo(file);
                } catch (IOException e) {
                    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
                }

                return new ResponseEntity<>(file.getAbsolutePath(), HttpStatus.CREATED);

            } else
                return new ResponseEntity(HttpStatus.RESET_CONTENT);

        } else
            return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

}
