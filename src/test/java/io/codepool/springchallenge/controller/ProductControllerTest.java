package io.codepool.springchallenge.controller;

import io.codepool.springchallenge.common.enums.AuthorityEnum;
import io.codepool.springchallenge.common.pojo.auth.CreateUpdateUserRequest;
import io.codepool.springchallenge.common.pojo.auth.LoginResponse;
import io.codepool.springchallenge.common.pojo.auth.UserDTO;
import io.codepool.springchallenge.common.pojo.product.CreateUpdateProductRequest;
import io.codepool.springchallenge.common.pojo.product.ProductDTO;
import io.codepool.springchallenge.dao.model.ProductEntity;
import io.codepool.springchallenge.dao.model.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The Product controller test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = SecurityTestConfig.class
)
public class ProductControllerTest extends BaseControllerTest {

    /**
     * Successful new product test.
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "testUserDetailsService")
    public void createProductTest() throws Exception{
        CreateUpdateProductRequest createProductRequest = new CreateUpdateProductRequest();
        createProductRequest.setName(primaryProductName);
        createProductRequest.setAmountAvailable(primaryProductAmount);
        createProductRequest.setCost(primaryProductPrice);

        String jsonRequest = objectMapper.writeValueAsString(createProductRequest);

        when(productRepository.save(Mockito.any(ProductEntity.class))).thenReturn(primaryProduct);

        //test if good 201 response
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/product/create").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        verify(productRepository, times(1)).save(Mockito.any(ProductEntity.class));

        ProductDTO productDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProductDTO.class);
        assertEquals(productDTO.getName(), primaryProduct.getName());
        assertEquals(productDTO.getAmountAvailable(), primaryProduct.getAmountAvailable());
        assertEquals(productDTO.getCost(), primaryProduct.getCost());

        assertEquals(productDTO.getSellerUsername(), primaryUserUsername);
    }


    /**
     * Failed new product test. Name already taken
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "testUserDetailsService")
    public void createProductConflictTest() throws Exception{
        CreateUpdateProductRequest createProductRequest = new CreateUpdateProductRequest();
        createProductRequest.setName(primaryProductName);
        createProductRequest.setAmountAvailable(primaryProductAmount);
        createProductRequest.setCost(primaryProductPrice);

        String jsonRequest = objectMapper.writeValueAsString(createProductRequest);

        when(productRepository.findByName(Mockito.any(String.class))).thenReturn(primaryProduct);

        mockMvc.perform(post("/api/v1/product/create").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()).andReturn();

        verify(productRepository, times(0)).save(Mockito.any(ProductEntity.class));
    }

    /**
     * Failed new product test. Role is not SELLER
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "otheruser", userDetailsServiceBeanName = "testUserDetailsService")
    public void createProduct403Test() throws Exception{
        CreateUpdateProductRequest createProductRequest = new CreateUpdateProductRequest();
        createProductRequest.setName(primaryProductName);
        createProductRequest.setAmountAvailable(primaryProductAmount);
        createProductRequest.setCost(primaryProductPrice);

        String jsonRequest = objectMapper.writeValueAsString(createProductRequest);

        mockMvc.perform(post("/api/v1/product/create").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()).andReturn();

        verify(productRepository, times(0)).save(Mockito.any(ProductEntity.class));
    }


    /**
     * Get by id test.
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "testUserDetailsService")
    public void getByIdTest() throws Exception{

        when(productRepository.findByIdAndActive(Mockito.any(Long.class), Mockito.any(Boolean.class))).thenReturn(primaryProduct);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/product/get/3"))
                .andExpect(status().isOk()).andReturn();

        verify(productRepository, times(1)).findByIdAndActive(Mockito.any(Long.class), Mockito.any(Boolean.class));

        ProductDTO productDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProductDTO.class);
        assertEquals(productDTO.getName(), primaryProduct.getName());
    }

    /**
     * Get by id failed test.
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "testUserDetailsService")
    public void getByIdFailedTest() throws Exception{

        when(userRepository.findByIdAndActive(Mockito.any(Long.class), Mockito.any(Boolean.class))).thenReturn(null);

        mockMvc.perform(get("/api/v1/user/get/3"))
                .andExpect(status().isNotFound()).andReturn();

        verify(userRepository, times(1)).findByIdAndActive(Mockito.any(Long.class), Mockito.any(Boolean.class));
    }


}
