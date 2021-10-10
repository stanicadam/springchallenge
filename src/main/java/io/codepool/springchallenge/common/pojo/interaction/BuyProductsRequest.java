package io.codepool.springchallenge.common.pojo.interaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BuyProductsRequest {

    private Long productId;
    private Integer amount;
}
