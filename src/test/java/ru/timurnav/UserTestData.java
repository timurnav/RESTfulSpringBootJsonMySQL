package ru.timurnav;

import ru.timurnav.domain.User;
import ru.timurnav.matcher.ModelMatcher;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserTestData {
    private static final LoggerWrapper LOG = LoggerWrapper.get(UserTestData.class);

    public static final long USER_ID = 1;

    public static final String REST_USER_URL = "/users/";

    public static final User USER = new User()
            .setId(1l)
            .setName("Kris")
            .setEmail("kroskross@gmail.com")
            .setUrl("/pic/ava1")
            .setOnline(true)
            .setStatusTimestamp(Timestamp.valueOf("2013-08-25 14:30:27"));

    public static final User CREATED_USER = new User()
            .setName("Timur")
            .setEmail("timur@gmail.com")
            .setUrl("/myPhotos/myPhoto")
            .setOnline(true)
            .setStatusTimestamp(new Timestamp(123456789000l));

    public static final List<User> ALL_USERS = new ArrayList<>(Arrays.asList(
            USER,
            new User().setId(2l).setName("Josh").setEmail("joshlong@gmail.com")
                    .setUrl("/pic/ava2").setOnline(false)
                    .setStatusTimestamp(Timestamp.valueOf("2014-08-25 14:30:27"))
    ));

    public static final ModelMatcher<User, String> MATCHER = new ModelMatcher<>(Object::toString, User.class);
}
