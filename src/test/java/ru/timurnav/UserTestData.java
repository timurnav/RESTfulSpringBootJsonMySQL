package ru.timurnav;

import ru.timurnav.domain.User;

import java.sql.Timestamp;

public class UserTestData {
    private static final LoggerWrapper LOG = LoggerWrapper.get(UserTestData.class);
    
    public static final long USER_ID = 1;

    public static final String REST_USER_URL = "/users/";

    public static final User USER = new User()
            .setId(1l)
            .setName("Kris")
            .setEmail("kroskross@gmail.com")
            .setInternalAvatarURL("/pic/ava1")
            .setOnline(true)
            .setStatusTimestamp(Timestamp.valueOf("2013-08-25 14:30:27"));

    public static final User CREATED_USER = new User()
            .setId(4l)
            .setName("Timur")
            .setEmail("timur@gmail.com")
            .setInternalAvatarURL("/myPhotos/myPhoto")
            .setOnline(true)
            .setStatusTimestamp(new Timestamp(1234567890l));

    private static boolean compareUser(User actual, User expected) {
        return actual.getId() == expected.getId() &&
                actual.getName().equals(expected.getName()) &&
                actual.getEmail().equals(expected.getEmail()) &&
                actual.getInternalAvatarURL().equals(expected.getInternalAvatarURL()) &&
                actual.getStatusTimestamp().equals(expected.getStatusTimestamp());
    }
}
