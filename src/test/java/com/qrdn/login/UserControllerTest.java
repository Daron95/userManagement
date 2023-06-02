package com.qrdn.login;

import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qrdn.login.controller.UserController;
import com.qrdn.login.entity.User;
import com.qrdn.login.entity.UserPassword;
import com.qrdn.login.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.http.MediaType;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest({ UserController.class })
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    User user1, user2, user3;
    UserPassword userPassword;

    @BeforeEach
    public void setup() {

        user1 = new User();
        user1.setUserName("user1");
        user1.setPassword("pass1");
        user1.setPasswordFrequency("n/a");
        user1.setFirstName("userfirstname");
        user1.setLastName("userlastname");

        user3 = new User();
        user3.setUserName("user3");
        user3.setPassword("");

        userPassword = new UserPassword();
        userPassword.setUserName("user");
        userPassword.setOldPassword("pass");
    }

    @Test
    public void loginUserTest() throws Exception {

      
        when(userService.findUserByID(any(User.class))).thenReturn(user1);

        mockMvc.perform(post("/loginUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.user_name", Matchers.is("user1")))
                .andExpect(jsonPath("$.response.password_frequency", Matchers.is("n/a")))
                .andDo(MockMvcResultHandlers.print());
                
        Mockito.verify(userService, Mockito.times(0)).findUserByID(user1);

    }

    @Test
    public void createUserTest() throws Exception {

        when(userService.createUser(any(User.class))).thenReturn(user2);

        mockMvc.perform(post("/createUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteUserTest() throws Exception {

        when(userService.deleteUser(anyString())).thenReturn(user3);

        mockMvc.perform(delete("/deleteUser")
                .contentType(MediaType.APPLICATION_JSON)
                .param("user_name", user3.getUserName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.user_name", Matchers.is("user3")))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(userService, Mockito.times(1)).deleteUser(user3.getUserName());

    }

    @Test
    public void updateUserTest() throws Exception {

        when(userService.updateUser(any(User.class))).thenReturn(user1);

        mockMvc.perform(put("/updateUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.user_name", Matchers.is("user1")))
                .andExpect(jsonPath("$.response.first_name", Matchers.is("userfirstname")))
                .andExpect(jsonPath("$.response.last_name", Matchers.is("userlastname")))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(userService, Mockito.times(0)).updateUser(user1);

    }

    @Test
    public void changePasswordTest() throws Exception {

        when(userService.changePassword(any(UserPassword.class))).thenReturn(user1);

        mockMvc.perform(put("/changePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPassword)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.user_name", Matchers.is("user1")))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(userService, Mockito.times(1)).changePassword(userPassword);

    }
}
