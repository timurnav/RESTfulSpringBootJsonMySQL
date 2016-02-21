package ru.timurnav.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.timurnav.LoggerWrapper;
import ru.timurnav.domain.User;
import ru.timurnav.exception.ExceptionUtil;
import ru.timurnav.repository.UserRepository;
import ru.timurnav.to.StatusResponse;
import ru.timurnav.util.OuterRequest;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private static final LoggerWrapper LOG = LoggerWrapper.get(UserController.class);

    @Autowired
    UserRepository userRepository;

    /**
     * This method returns an User entity with assigned id
     *
     * @param id - id of user in database
     * @return user
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public User get(@PathVariable("id") Long id) {
        return ExceptionUtil.check(userRepository.findOne(id), id);
    }

    /**
     * The request parameters are optional. Parameters are used to filter the list
     * of users in response body.
     *
     * @param online filters users by status online/offline
     * @param id     is the Timestamp, in case it presents, the request contains
     *               users whose status is changed after this timestamp only
     * @return List of users
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public List<User> getAll(@RequestParam(value = "online", required = false) Boolean online,
                             @RequestParam(value = "id", required = false) Long id
    ) {
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
     * @param user receive user as Json
     * @return ResponseEntity, contains an HttpStatus and ID of the created user
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Order
    public Callable<ResponseEntity> create(@RequestBody User user) {
        return () -> {
            try {
                User u = userRepository.save(user);
                return new ResponseEntity<>(u.getId(), HttpStatus.CREATED);
            } catch (Exception e) {
                LOG.error("abort creating new user. Cause " + e.getMessage());
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        };
    }

    /**
     * The method changes the status of users,
     *
     * @param id      - to identify the user in database
     * @param current - new status of user, should be true/false
     * @return ResponseEntity with an HttpStatus and contains in ResponseBody
     * id - id of user,
     * new - new status,
     * old - old status
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Order
    public Callable<ResponseEntity> changeStatus(@PathVariable("id") Long id,
                                                 @RequestParam("online") Boolean current) {
        return () -> {
            Timestamp updateTime = new Timestamp((new Date()).getTime());

            OuterRequest.sendOuterRequest();

            try {
                User user = ExceptionUtil.check(userRepository.findOne(id), id);
                boolean old = user.isOnline();
                user.setOnline(current);
                user.setStatusTimestamp(updateTime);
                userRepository.save(user);
                StatusResponse sr = new StatusResponse()
                        .setId(id)
                        .setOldStatus(old)
                        .setCurrentStatus(current);

                return new ResponseEntity<>(sr, HttpStatus.OK);
            } catch (Exception e) {
                LOG.error("abort status changing. Cause " + e.getMessage());
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        };
    }

}
