package io.codepool.springchallenge.common.pojo.interaction;

import io.codepool.springchallenge.common.pojo.product.ProductDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class BuyProductsResponse {

    private BigDecimal totalSpent;
    private ProductDTO product;
    private String change;
}
