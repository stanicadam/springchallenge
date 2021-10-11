package io.codepool.springchallenge.controller;

import io.codepool.springchallenge.common.enums.AuthorityEnum;
import io.codepool.springchallenge.common.pojo.auth.BaseUserAuthDetails;
import io.codepool.springchallenge.common.pojo.auth.CreateUpdateUserRequest;
import io.codepool.springchallenge.common.pojo.auth.LoginResponse;
import io.codepool.springchallenge.common.pojo.auth.UserDTO;
import io.codepool.springchallenge.dao.model.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The User controller test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = SecurityTestConfig.class
)
public class UserControllerTest extends BaseControllerTest {

    /**
     * Successful registration test.
     *
     * @throws Exception the exception
     */
    @Test
    public void successfulRegistrationTest() throws Exception{

        CreateUpdateUserRequest registrationRequest = new CreateUpdateUserRequest();
        registrationRequest.setPassword(sellerPassword);
        registrationRequest.setUsername(sellerUsername);
        registrationRequest.setRole(AuthorityEnum.SELLER.getValue());

        String jsonRequest = objectMapper.writeValueAsString(registrationRequest);

        //teach user repository to return our user when saving
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(sellerUserEntity);
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);

        //test if good 201 response
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/user/register").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        //test if user repo save was called during request
        verify(userRepository, times(1)).save(Mockito.any(UserEntity.class));

        //test if response can be parsed to dto and that the user is the same
        UserDTO userDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(userDTO.getUsername(), registrationRequest.getUsername());
    }


    /**
     * Unsuccessful registration test. Missing role
     *
     * @throws Exception the exception
     */
    @Test
    public void unsuccessfulRegistrationTestForMissingRole() throws Exception{

        CreateUpdateUserRequest registrationRequest = new CreateUpdateUserRequest();
        registrationRequest.setPassword(sellerPassword);
        registrationRequest.setUsername(sellerUsername);
        registrationRequest.setRole(null);
        String jsonRequest = objectMapper.writeValueAsString(registrationRequest);

        //teach user repository to return our user when saving
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(sellerUserEntity);
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);

        //test if good response
        mockMvc.perform(post("/api/v1/user/register").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity()).andReturn();

        //test if user repo save was called during request
        verify(userRepository, times(0)).save(Mockito.any(UserEntity.class));
    }


    /**
     * Unsuccessful duplicate registration test.
     * Simulates trying to register new user with existing email/username.
     *
     * @throws Exception the exception
     */
    @Test
    public void unsuccessfulDuplicateRegistrationTest() throws Exception{

        CreateUpdateUserRequest registrationRequest = new CreateUpdateUserRequest();
        registrationRequest.setPassword(sellerPassword);
        registrationRequest.setUsername(sellerUsername);
        registrationRequest.setRole(AuthorityEnum.SELLER.getValue());

        String jsonRequest = objectMapper.writeValueAsString(registrationRequest);

        //teach user repo to simulate that a duplicate user exists
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(sellerUserEntity);

        //test if good conflict response
        mockMvc.perform(post("/api/v1/user/register").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()).andReturn();

        //test if user repo save was called during request
        verify(userRepository, times(0)).save(Mockito.any(UserEntity.class));
    }

    /**
     * Login test.
     * Successful 200
     *
     * @throws Exception the exception
     */
    @Test
    public void loginTest() throws Exception{

        BaseUserAuthDetails loginRequest = new BaseUserAuthDetails();
        loginRequest.setUsername(sellerUsername);
        loginRequest.setPassword(sellerPassword);

        String jsonRequest = objectMapper.writeValueAsString(loginRequest);

        //teach user repo to return our user when we log in with our credentials
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(sellerUserEntity);

        //test if good response
        MvcResult result = mockMvc.perform(post("/api/v1/user/login").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        //make sure that the response can be parsed
        LoginResponse loginResponse = objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class);

        //make sure that a token is returned for this user
        assertEquals(loginResponse.getUsername(), sellerUserEntity.getUsername());
        assertTrue(loginResponse.getJWTToken().contains(tokenBearerPrefix));
    }

    /**
     * Login Failed test.
     * Unsuccessful, no user found with this username.
     *
     *
     * @throws Exception the exception
     */
    @Test
    public void loginFailedTest() throws Exception {
        BaseUserAuthDetails loginRequest = new BaseUserAuthDetails();
        loginRequest.setUsername(sellerUsername);
        loginRequest.setPassword(sellerPassword);
        String jsonRequest = objectMapper.writeValueAsString(loginRequest);

        //teach user repo to return null when logging in
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(null);

        //test if good 401 response
        mockMvc.perform(post("/api/v1/login").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()).andReturn();
    }

    /**
     * Get by id test.
     * Successful.
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "testUserDetailsService")
    public void getByIdTest() throws Exception{

        when(userRepository.findOne(Mockito.any(Long.class))).thenReturn(sellerUserEntity);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/user/get/3"))
                .andExpect(status().isOk()).andReturn();

        verify(userRepository, times(1)).findOne(Mockito.any(Long.class));

        //test if response can be parsed to dto and that the user is the same
        UserDTO userDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(userDTO.getUsername(), sellerUserEntity.getUsername());
        assertEquals(userDTO.getRole(), sellerUserEntity.getRole());
    }

    /**
     * Get by id failed test.
     * No user found with this id.
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "testUserDetailsService")
    public void getByIdFailedTest() throws Exception{

        when(userRepository.findOne(Mockito.any(Long.class))).thenReturn(null);

        mockMvc.perform(get("/api/v1/user/get/3"))
                .andExpect(status().isNotFound()).andReturn();

        verify(userRepository, times(1)).findOne(Mockito.any(Long.class));
    }


    /**
     * Delete user test.
     * Successful.
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "testUserDetailsService")
    public void deleteTest() throws Exception{

        when(userRepository.findOne(Mockito.any(Long.class))).thenReturn(sellerUserEntity);
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(sellerUserEntity);

        mockMvc.perform(delete("/api/v1/user/delete/3"))
                .andExpect(status().isOk()).andReturn();

        verify(userRepository, times(1)).findOne(Mockito.any(Long.class));
        verify(userRepository, times(1)).delete(Mockito.any(UserEntity.class));
    }

    /**
     * Delete non existent user test.
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "testUserDetailsService")
    public void deleteFailedTest() throws Exception{

        when(userRepository.findOne(Mockito.any(Long.class))).thenReturn(null);

        mockMvc.perform(delete("/api/v1/user/delete/3"))
                .andExpect(status().isNotFound()).andReturn();

        verify(userRepository, times(1)).findOne(Mockito.any(Long.class));
        verify(userRepository, times(0)).save(Mockito.any(UserEntity.class));
    }


    /**
     * Update user test.
     * Test changing role of our user.
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "testUserDetailsService")
    public void updateTest() throws Exception{

        sellerUserEntity.setRole(AuthorityEnum.SELLER.getValue());

        UserEntity updatedSellerUserEntity = new UserEntity();
        updatedSellerUserEntity.setRole(AuthorityEnum.BUYER.getValue());
        updatedSellerUserEntity.setUsername(sellerUsername);
        updatedSellerUserEntity.setPassword(sellerPassword);

        CreateUpdateUserRequest updateUserRequest = new CreateUpdateUserRequest();
        updateUserRequest.setRole(AuthorityEnum.BUYER.getValue());
        updateUserRequest.setUsername(sellerUsername);
        updateUserRequest.setPassword(sellerPassword);

        String jsonRequest = objectMapper.writeValueAsString(updateUserRequest);

        when(userRepository.findOne(Mockito.any(Long.class))).thenReturn(sellerUserEntity);
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(updatedSellerUserEntity);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/user/update/3").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        verify(userRepository, times(1)).findOne(Mockito.any(Long.class));
        verify(userRepository, times(1)).save(Mockito.any(UserEntity.class));

        //test if response can be parsed to dto and that the user is the same
        UserDTO userDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(userDTO.getUsername(), sellerUserEntity.getUsername());
        assertEquals(userDTO.getRole(), updatedSellerUserEntity.getRole());

        sellerUserEntity.setRole(AuthorityEnum.SELLER.getValue());
    }

}
