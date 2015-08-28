package ru.timurnav.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.timurnav.RESTfulSpringBootMySqlServerApplication;
import ru.timurnav.matcher.JsonUtil;
import ru.timurnav.repository.UserRepository;

import javax.annotation.PostConstruct;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static ru.timurnav.UserTestData.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RESTfulSpringBootMySqlServerApplication.class)
@WebAppConfiguration
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    void postConstruct() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    private final String REST_URL = "/users/";

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + USER_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(USER));
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentListMatcher(ALL_USERS));
    }

    @Test
    public void testCreate() throws Exception {

        mockMvc.perform(get(REST_URL))
                .andExpect(MATCHER.contentAbsentMatcher(CREATED_USER));

        MvcResult mvcResult = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(CREATED_USER)))
                .andExpect(request().asyncStarted())
                .andReturn();
// http://stackoverflow.com/questions/18053703/spring-mvc-test-framework-returning-inconsistent-results-for-async-controller-te
        mvcResult.getAsyncResult();

        MvcResult mvcAsyncResult = mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        long newId = Long.valueOf(mvcAsyncResult
                .getResponse()
                .getContentAsString());

        CREATED_USER.setId(newId);
        MATCHER.assertEquals(CREATED_USER, userRepository.findOne(newId));

        ALL_USERS.add(CREATED_USER);
        MATCHER.assertListEquals(ALL_USERS, userRepository.findAll());

    }

    public void testChangeStatus() throws Exception {

    }
}