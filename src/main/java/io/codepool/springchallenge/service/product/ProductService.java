package io.codepool.springchallenge.service.product;

import io.codepool.springchallenge.common.pojo.product.CreateUpdateProductRequest;
import io.codepool.springchallenge.common.pojo.product.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO createProduct(CreateUpdateProductRequest productDTO);

    List<ProductDTO> getProducts();

    ProductDTO deleteProduct(Long productId);

    ProductDTO getById(Long productId);

    ProductDTO updateProduct(Long productId, CreateUpdateProductRequest updateProductRequest);
}
