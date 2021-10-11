package io.codepool.springchallenge.controller;


import io.codepool.springchallenge.common.pojo.product.CreateUpdateProductRequest;
import io.codepool.springchallenge.common.pojo.product.ProductDTO;
import io.codepool.springchallenge.dao.model.ProductEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;


import java.math.BigDecimal;

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
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "testUserDetailsService")
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

        assertEquals(productDTO.getSellerUsername(), sellerUsername);
    }


    /**
     * Failed new product test. Name already taken
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "testUserDetailsService")
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
    @WithUserDetails(value = "buyer", userDetailsServiceBeanName = "testUserDetailsService")
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
     * Successful 200
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "testUserDetailsService")
    public void getByIdTest() throws Exception{

        when(productRepository.findOne(Mockito.any(Long.class))).thenReturn(primaryProduct);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/product/get/3"))
                .andExpect(status().isOk()).andReturn();

        verify(productRepository, times(1)).findOne(Mockito.any(Long.class));

        ProductDTO productDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProductDTO.class);
        assertEquals(productDTO.getName(), primaryProduct.getName());
    }

    /**
     * Get by id failed test.
     * No result for this id.
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
     * Delete product test.
     * Successful.
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "testUserDetailsService")
    public void deleteTest() throws Exception{

        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(sellerUserEntity);
        when(productRepository.findOne(Mockito.any(Long.class))).thenReturn(primaryProduct);
        when(productRepository.save(Mockito.any(ProductEntity.class))).thenReturn(primaryProduct);

        mockMvc.perform(delete("/api/v1/product/delete/3"))
                .andExpect(status().isOk()).andReturn();

        verify(productRepository, times(1)).findOne(Mockito.any(Long.class));
        verify(productRepository, times(1)).delete(Mockito.any(ProductEntity.class));
    }

    /**
     * Delete non existent product test.
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "testUserDetailsService")
    public void deleteFailedTest() throws Exception{

        when(productRepository.findOne(Mockito.any(Long.class))).thenReturn(null);

        mockMvc.perform(delete("/api/v1/product/delete/3"))
                .andExpect(status().isNotFound()).andReturn();

        verify(productRepository, times(1)).findOne(Mockito.any(Long.class));
        verify(productRepository, times(0)).save(Mockito.any(ProductEntity.class));
    }


    /**
     * Update product test.
     * Test changing name, cost and availability of our product.
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "testUserDetailsService")
    public void updateTest() throws Exception{

        String newName = "New Product Name";
        BigDecimal newCost = new BigDecimal(20);
        Integer newAvailability = 10;

        ProductEntity updatedProductEntity = new ProductEntity();
        updatedProductEntity.setName(newName);
        updatedProductEntity.setCost(newCost);
        updatedProductEntity.setAmountAvailable(newAvailability);

        CreateUpdateProductRequest updateProductRequest = new CreateUpdateProductRequest();
        updateProductRequest.setName(newName);
        updateProductRequest.setCost(newCost);
        updateProductRequest.setAmountAvailable(newAvailability);

        String jsonRequest = objectMapper.writeValueAsString(updateProductRequest);

        when(productRepository.findOne(Mockito.any(Long.class))).thenReturn(updatedProductEntity);
        when(productRepository.save(Mockito.any(ProductEntity.class))).thenReturn(updatedProductEntity);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/product/update/3").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        verify(productRepository, times(1)).findOne(Mockito.any(Long.class));
        verify(productRepository, times(1)).save(Mockito.any(ProductEntity.class));

        ProductDTO productDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProductDTO.class);
        assertEquals(productDTO.getName(), newName);
        assertEquals(productDTO.getAmountAvailable(), newAvailability);
    }


}
