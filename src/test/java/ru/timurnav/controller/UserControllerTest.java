package ru.timurnav.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.timurnav.RESTfulSpringBootMySqlServerApplication;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RESTfulSpringBootMySqlServerApplication.class)
@WebAppConfiguration
public class UserControllerTest {

//    @Autowired
//    UserRepository userRepository;

    @Test
    public void testGet() throws Exception {
        System.out.println(123);
    }

    @Test
    public void testGetAll() throws Exception {

    }

    @Test
    public void testCreate() throws Exception {

    }

    @Test
    public void testChangeStatus() throws Exception {

    }
}