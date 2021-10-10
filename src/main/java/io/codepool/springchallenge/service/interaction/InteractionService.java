package io.codepool.springchallenge.service.interaction;

import io.codepool.springchallenge.common.pojo.interaction.BuyProductsRequest;
import io.codepool.springchallenge.common.pojo.interaction.BuyProductsResponse;

public interface InteractionService {

    void depositAmount(Integer amount);

    void resetDeposit();

    BuyProductsResponse buyProducts(BuyProductsRequest buyProductsRequest);
}
