package io.codepool.springchallenge.service.product;

import io.codepool.springchallenge.common.exception.EntityAlreadyExistsException;
import io.codepool.springchallenge.common.exception.IllegalArgumentOnCreateUpdateException;
import io.codepool.springchallenge.common.mapper.MapperUtil;
import io.codepool.springchallenge.common.pojo.CreateProductRequest;
import io.codepool.springchallenge.common.pojo.ProductDTO;
import io.codepool.springchallenge.common.services.ContextHolderService;
import io.codepool.springchallenge.dao.model.ProductEntity;
import io.codepool.springchallenge.dao.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MapperUtil mapperUtil;

    @Autowired
    private ContextHolderService contextHolderService;

    @Override
    @Transactional
    public ProductDTO createNewProduct(CreateProductRequest createProductRequest){

        //validations... this could have been done on lombok/jackson side but i like to keep it here, centralized.
        if (createProductRequest.getAmountAvailable() == null ||
        createProductRequest.getAmountAvailable() < 0)
            throw new IllegalArgumentOnCreateUpdateException("Amount Available");

        if (createProductRequest.getCost() == null ||
                createProductRequest.getCost().compareTo(BigDecimal.ZERO) < 1)
            throw new IllegalArgumentOnCreateUpdateException("Cost");

        if (createProductRequest.getName() == null)
            throw new IllegalArgumentOnCreateUpdateException("Name");

        if (productRepository.findByName(createProductRequest.getName()) != null)
            throw new EntityAlreadyExistsException("Product", "Name");


        ProductEntity productEntity = mapperUtil.map(createProductRequest, ProductEntity.class);
        productEntity.setSeller(contextHolderService.getCurrentUser());

        return mapperUtil.map(productRepository.save(productEntity), ProductDTO.class);
    }


    @Override
    @Transactional
    public List<ProductDTO> getProducts(){
        return productRepository.findByActive(true).stream().map(x->mapperUtil.map(x,ProductDTO.class)).collect(Collectors.toList());
    }


}
