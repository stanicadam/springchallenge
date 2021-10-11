package io.codepool.springchallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.codepool.springchallenge.common.enums.AuthorityEnum;
import io.codepool.springchallenge.dao.model.ProductEntity;
import io.codepool.springchallenge.dao.model.UserEntity;
import io.codepool.springchallenge.dao.repository.ProductRepository;
import io.codepool.springchallenge.dao.repository.UserRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * The Base controller test.
 * Here we define all the mock repositories our tests will use.
 * We also define the entities/dto's that will be used across all our tests.
 */
public abstract class BaseControllerTest {

    @Value("${TOKEN_BEARER_PREFIX}")
    protected String tokenBearerPrefix;

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PasswordEncoder passwordEncoder;


    //mock the repositories
    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected ProductRepository productRepository;


    //user entities.
    //our user entity represents the user that we are simulating being logged in with.
    //we will be logged in with the role SELLER
    protected UserEntity sellerUserEntity;
    //other user entity represents some other user that is not us
    //important for testing deletion of other user's db property
    //role is BUYER
    protected UserEntity buyerUserEntity;


    //properties that belong to our user entity
    protected final String sellerUsername = "seller";
    protected final String sellerPassword = "strongpass";


    //properties that belong to some other user
    protected final String buyerUsername = "buyer";
    protected final String buyerUserPassword = "weakpass";
    protected final BigDecimal buyerUserDeposit = new BigDecimal(20);


    //products
    protected ProductEntity primaryProduct;
    protected ProductEntity otherProduct;

    protected final String primaryProductName = "Product One";
    protected final Integer primaryProductAmount = 999;
    protected final BigDecimal primaryProductPrice = new BigDecimal(100);

    protected final String otherProductName = "Product Two";
    protected final Integer otherProductAmount = 999;
    protected final BigDecimal otherProductPrice = new BigDecimal(50);



    /**
     * Setup.
     * This will be called before all other tests are executed.
     * Here we instantiate our entities and assign them their properties.
     */
    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

        initAllUserRelated();
        initAllProductRelated();
    }


    /**
     * init user related entities
     */
    private void initAllUserRelated(){
        //init our log in user
        sellerUserEntity = new UserEntity();
        sellerUserEntity.setUsername(sellerUsername);
        sellerUserEntity.setPassword(passwordEncoder.encode(sellerPassword));
        sellerUserEntity.setActive(true);
        sellerUserEntity.setRole(AuthorityEnum.SELLER.getValue());

        //init the other user that is not us
        buyerUserEntity = new UserEntity();
        buyerUserEntity.setUsername(buyerUsername);
        buyerUserEntity.setPassword(passwordEncoder.encode(buyerUserPassword));
        buyerUserEntity.setActive(true);
        buyerUserEntity.setDeposit(buyerUserDeposit);
        buyerUserEntity.setRole(AuthorityEnum.BUYER.getValue());

    }


    /**
     * init product related entities and dto's
     */
    private void initAllProductRelated(){

        primaryProduct = new ProductEntity();
        primaryProduct.setName(primaryProductName);
        primaryProduct.setAmountAvailable(primaryProductAmount);
        primaryProduct.setCost(primaryProductPrice);
        primaryProduct.setActive(true);
        primaryProduct.setSeller(sellerUserEntity);


        otherProduct = new ProductEntity();
        otherProduct.setName(otherProductName);
        otherProduct.setAmountAvailable(otherProductAmount);
        otherProduct.setCost(otherProductPrice);
        otherProduct.setActive(true);
        otherProduct.setSeller(buyerUserEntity);

    }

}

