package io.codepool.springchallenge.controller;


import io.codepool.springchallenge.common.pojo.auth.UserDTO;
import io.codepool.springchallenge.common.pojo.interaction.BuyProductsRequest;
import io.codepool.springchallenge.common.pojo.interaction.BuyProductsResponse;
import io.codepool.springchallenge.dao.model.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The Interaction controller test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = SecurityTestConfig.class
)
public class InteractionControllerTest extends BaseControllerTest {

    /**
     * Successful new deposit.
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "buyer", userDetailsServiceBeanName = "testUserDetailsService")
    public void createDeposit() throws Exception{

        when(userRepository.findOne(Mockito.any(Long.class))).thenReturn(buyerUserEntity);
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(buyerUserEntity);

        BigDecimal oldDeposit = buyerUserEntity.getDeposit();
        BigDecimal additionalDeposit = new BigDecimal(100);
        BigDecimal total = oldDeposit.add(additionalDeposit);

        mockMvc.perform(post("/api/v1/interaction/deposit/" + additionalDeposit.intValue()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        verify(userRepository, times(1)).save(Mockito.any(UserEntity.class));

        when(userRepository.findOne(Mockito.any(Long.class))).thenReturn(buyerUserEntity);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/user/get/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        UserDTO userDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(userDTO.getDeposit(), total);
    }


    /**
     * Not authorized to make deposit.
     * User is not a buyer
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "testUserDetailsService")
    public void createDepositFailTest() throws Exception{

        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(sellerUserEntity);

        mockMvc.perform(post("/api/v1/interaction/deposit/" + 30).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()).andReturn();

        verify(userRepository, times(0)).save(Mockito.any(UserEntity.class));
    }


    /**
     * Successful deposit reset.
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "buyer", userDetailsServiceBeanName = "testUserDetailsService")
    public void resetDeposit() throws Exception{

        BigDecimal oldDeposit = new BigDecimal(30);

        buyerUserEntity.setDeposit(oldDeposit);
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(buyerUserEntity);
        when(userRepository.findOne(Mockito.any(Long.class))).thenReturn(buyerUserEntity);

        mockMvc.perform(post("/api/v1/interaction/deposit/reset"))
                .andExpect(status().isOk()).andReturn();

        verify(userRepository, times(1)).save(Mockito.any(UserEntity.class));

        when(userRepository.findOne(Mockito.any(Long.class))).thenReturn(buyerUserEntity);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/user/get/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        UserDTO userDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(userDTO.getDeposit(), BigDecimal.ZERO);
    }


    /**
     * Successfully buying products.
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "buyer", userDetailsServiceBeanName = "testUserDetailsService")
    public void buyProducts() throws Exception{

        BigDecimal oldDeposit = new BigDecimal(1000);
        buyerUserEntity.setDeposit(oldDeposit);

        BigDecimal singleProductCost = new BigDecimal(30);
        primaryProduct.setCost(singleProductCost);

        int amountOfProducts = 12;
        BuyProductsRequest buyProductsRequest = new BuyProductsRequest();
        buyProductsRequest.setAmount(amountOfProducts);
        //whatever id, we will teach product repo to return primary product
        buyProductsRequest.setProductId(3l);

        String jsonRequest = objectMapper.writeValueAsString(buyProductsRequest);

        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(buyerUserEntity);
        when(userRepository.findOne(Mockito.any(Long.class))).thenReturn(buyerUserEntity);

        when(productRepository.findOne(Mockito.any(Long.class))).thenReturn(primaryProduct);

        MvcResult buyResult = mockMvc.perform(post("/api/v1/interaction/buy").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        //we updated deposit
        verify(userRepository, times(1)).save(Mockito.any(UserEntity.class));

        MvcResult userResult = mockMvc.perform(get("/api/v1/user/get/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        BuyProductsResponse buyProductsResponse = objectMapper.readValue(buyResult.getResponse().getContentAsString(), BuyProductsResponse.class);
        UserDTO userDTO = objectMapper.readValue(userResult.getResponse().getContentAsString(), UserDTO.class);

        //we have correctly calculated the remaining deposit
        assertEquals(oldDeposit.subtract(singleProductCost.multiply(new BigDecimal(amountOfProducts))), userDTO.getDeposit());
        //we have correctly calculated the total spent
        assertEquals(oldDeposit.subtract(buyProductsResponse.getTotalSpent()), userDTO.getDeposit());

        assertThat(buyProductsResponse.getChange(), containsString("20 20 100 100 100 100 100 100"));
    }


    /**
     * unsuccessfully buying products.
     * product with this ID does not exist
     *
     * @throws Exception the exception
     */
    @Test
    @WithUserDetails(value = "buyer", userDetailsServiceBeanName = "testUserDetailsService")
    public void buyProductsFailNonExistentProductTest() throws Exception{

        BuyProductsRequest buyProductsRequest = new BuyProductsRequest();

        String jsonRequest = objectMapper.writeValueAsString(buyProductsRequest);

        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(buyerUserEntity);
        when(productRepository.findOne(Mockito.any(Long.class))).thenReturn(null);

        mockMvc.perform(post("/api/v1/interaction/buy").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity()).andReturn();
    }

}
