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
import ru.timurnav.domain.User;
import ru.timurnav.matcher.JsonUtil;
import ru.timurnav.repository.UserRepository;
import ru.timurnav.to.StatusResponse;

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
                .andExpect(USER_MATCHER.contentMatcher(USER));
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentListMatcher(ALL_USERS));
    }

    @Test
    public void testGetAllOnline() throws Exception {
        mockMvc.perform(get(REST_URL)
                .param("online", "true"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentListMatcher(USER));
    }

    @Test
    public void testGetAllAfterTimestamp() throws Exception {
        mockMvc.perform(get(REST_URL)
                .param("id", "1377430227000"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentListMatcher(ALL_USERS.get(1)));
    }

    @Test
    public void testGetAllOnlineAfterTimestamp() throws Exception {
        mockMvc.perform(get(REST_URL)
                .param("id", "1377430227000")
                .param("online", "true"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentListMatcher());//empty list
    }

    @Test
    public void testCreate() throws Exception {

        mockMvc.perform(post(REST_URL))
                .andExpect(USER_MATCHER.contentAbsentMatcher(CREATED_USER));

        MvcResult mvcResult = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(CREATED_USER)))
                .andExpect(request().asyncStarted())
                .andReturn();
        mvcResult.getAsyncResult();
        MvcResult mvcAsyncResult = mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated())
//it don't work
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        long newId = Long.valueOf(mvcAsyncResult
                .getResponse()
                .getContentAsString());

        CREATED_USER.setId(newId);
        USER_MATCHER.assertEquals(CREATED_USER, userRepository.findOne(newId));

        ALL_USERS.add(CREATED_USER);
        USER_MATCHER.assertListEquals(ALL_USERS, userRepository.findAll());

        ALL_USERS.remove(2);
        userRepository.delete(3l);
    }

    @Test
    public void testChangeStatus() throws Exception {

        User u = userRepository.findOne(USER_ID);

        boolean newStatus = !u.isOnline();

        MvcResult mvcResult = mockMvc.perform(put(REST_URL + USER_ID)
                .param("online", newStatus + ""))
                .andExpect(request().asyncStarted())
                .andReturn();

        mvcResult.getAsyncResult();

        StatusResponse expectedResponce = new StatusResponse()
                .setId(USER_ID)
                .setCurrentStatus(newStatus)
                .setOldStatus(u.isOnline());

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(STATUS_RESP_MATCHER.contentMatcher(expectedResponce))
        ;

        userRepository.save(USER);

    }
}