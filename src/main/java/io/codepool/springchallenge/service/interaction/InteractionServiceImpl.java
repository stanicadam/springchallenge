package io.codepool.springchallenge.service.interaction;

import io.codepool.springchallenge.common.exception.EntityNotFoundException;
import io.codepool.springchallenge.common.exception.IllegalArgumentOnCreateUpdateException;
import io.codepool.springchallenge.common.mapper.MapperUtil;
import io.codepool.springchallenge.common.pojo.interaction.BuyProductsRequest;
import io.codepool.springchallenge.common.pojo.interaction.BuyProductsResponse;
import io.codepool.springchallenge.common.pojo.product.ProductDTO;
import io.codepool.springchallenge.common.services.ContextHolderService;
import io.codepool.springchallenge.dao.model.ProductEntity;
import io.codepool.springchallenge.dao.model.UserEntity;
import io.codepool.springchallenge.dao.repository.ProductRepository;
import io.codepool.springchallenge.dao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class InteractionServiceImpl implements InteractionService{

    @Autowired
    private ContextHolderService contextHolderService;

    @Autowired
    private MapperUtil mapperUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    //ideally this could be externalized and picked up from the db
    //in case anything changes with our vending machine
    //we could allow for other amounts to be inserted without updating the code
    //this is why i did not write if/else statements to check if the amount fits, but rather
    //pull the comma separated values and check if it works.
    protected final int[] acceptableDenominations = {5,10,20,50,100};


    @Override
    @Transactional
    public void depositAmount(Integer amount){

        validateDepositAmount(amount);

        UserEntity userEntity = contextHolderService.getCurrentUser();
        userEntity.setDeposit(
                userEntity.getDeposit().add(new BigDecimal(amount))
        );

        userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void resetDeposit(){

        UserEntity userEntity = contextHolderService.getCurrentUser();
        userEntity.setDeposit(BigDecimal.ZERO);

        userRepository.save(userEntity);
    }


    @Override
    @Transactional
    public BuyProductsResponse buyProducts(BuyProductsRequest buyProductsRequest){

        ProductEntity productEntity = productRepository.findByIdAndActive(buyProductsRequest.getProductId(), true);
        UserEntity userEntity = contextHolderService.getCurrentUser();

        if (productEntity == null)
            throw new EntityNotFoundException("Product", buyProductsRequest.getProductId() != null ? buyProductsRequest.getProductId() : "null");

        if (buyProductsRequest.getAmount() == null || buyProductsRequest.getAmount() <= 0)
            throw new IllegalArgumentOnCreateUpdateException("Please insert amount of product you'd like");
        if (buyProductsRequest.getAmount() > productEntity.getAmountAvailable())
            throw new IllegalArgumentOnCreateUpdateException("Currently available amount of product is " + productEntity.getAmountAvailable());

        BigDecimal totalPrice = productEntity.getCost().multiply(new BigDecimal(buyProductsRequest.getAmount()));
        BigDecimal remainder = userEntity.getDeposit().subtract(totalPrice);

        if (remainder.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentOnCreateUpdateException("You don't have enough funds for this transaction");


        productEntity.setAmountAvailable(
                productEntity.getAmountAvailable() - buyProductsRequest.getAmount()
        );
        productRepository.save(productEntity);


        userEntity.setDeposit(remainder);
        userRepository.save(userEntity);

        String changeInCoins = calculateCoinChange(remainder.setScale(0, RoundingMode.FLOOR).intValue(), acceptableDenominations);

        BuyProductsResponse response = new BuyProductsResponse();
        response.setProduct(mapperUtil.map(productEntity, ProductDTO.class));
        response.setChange(changeInCoins);
        response.setTotalSpent(totalPrice);

        return response;
    }



    //solved using dynamic programming....
    private String calculateCoinChange(int amount, int[] currencies) {
        /*
         * dp array will contain the number of ways 'i'
         * amount can be paid using the given currencies,
         * therefore, we made dp of size amount+1 to have
         * an index = amount.
         */
        int[] dp = new int[amount + 1];
        ArrayList<String>[] payments = new ArrayList[amount+1];
        for(int i=0;i<payments.length; i++)
        {
            payments[i] = new ArrayList<>();
        }

        /*
         * positive basecase, when we have remaining amount = 0,
         * this means that we have found one way of paying the
         * initial amount.
         */
        dp[0] = 1;

        for (int currency : currencies) {
            for (int amt = 1; amt < dp.length; amt++) {
                if(amt-currency >=0 && dp[amt - currency] != 0)
                {
                    dp[amt] +=1;
                    /*  we have made an array of arraylist of strings to
                     * store all the ways of paying the current amount,
                     *  therefore, the payments of current amount =
                     *  payments of (amt - currency) concatenated
                     *  with the current currency*/
                    payments[amt].add(payments[amt-currency].size()>0?
                            (payments[amt-currency].get(payments[amt-currency].size()-1) + currency + " ")
                            : Integer.toString(currency) + " ");
                }
            }
        }

        /*number of ways of paying given amount = dp[amount]*/
        System.out.println(dp[amount] + "\n" + payments[amount]);
        ArrayList<String> listOfCoins = payments[amount];

        Collections.sort(listOfCoins, (a, b)->Integer.compare(a.length(), b.length()));
        System.out.println("optimal solution is " + listOfCoins.get(0));
        return listOfCoins.get(0);
    }

    private void validateDepositAmount(Integer amount){
        if (amount == null || Arrays.stream(acceptableDenominations).noneMatch(x-> x == amount))
            throw new IllegalArgumentException("Please insert one of the acceptable denominations " +
                   Arrays.toString(acceptableDenominations));
    }

}
