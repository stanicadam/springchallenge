package io.codepool.springchallenge.service.product;

import io.codepool.springchallenge.common.pojo.CreateProductRequest;
import io.codepool.springchallenge.common.pojo.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO createNewProduct(CreateProductRequest productDTO);

    List<ProductDTO> getProducts();
}
