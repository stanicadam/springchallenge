package io.codepool.springchallenge.service.product;

import io.codepool.springchallenge.common.exception.EntityAlreadyExistsException;
import io.codepool.springchallenge.common.exception.EntityNotFoundException;
import io.codepool.springchallenge.common.exception.ForbiddenUpdateDeleteException;
import io.codepool.springchallenge.common.exception.IllegalArgumentOnCreateUpdateException;
import io.codepool.springchallenge.common.mapper.MapperUtil;
import io.codepool.springchallenge.common.pojo.product.CreateUpdateProductRequest;
import io.codepool.springchallenge.common.pojo.product.ProductDTO;
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
    public ProductDTO createProduct(CreateUpdateProductRequest createProductRequest){

        productInputParamsValidations(createProductRequest);

        if (productRepository.findByName(createProductRequest.getName()) != null)
            throw new EntityAlreadyExistsException("Product", "Name");


        ProductEntity productEntity = mapperUtil.map(createProductRequest, ProductEntity.class);
        productEntity.setSeller(contextHolderService.getCurrentUser());

        return mapperUtil.map(productRepository.save(productEntity), ProductDTO.class);
    }


    @Override
    @Transactional
    public List<ProductDTO> getProducts(){
        return productRepository.findAll().stream().map(x->mapperUtil.map(x,ProductDTO.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId){

        ProductEntity productEntity = productRepository.findOne(productId);

        if (productEntity == null)
            throw new EntityNotFoundException("Product", productId != null ? productId : "null");

        //we have overridden the default equals method
        if (productEntity.getSeller() != contextHolderService.getCurrentUser())
            throw new ForbiddenUpdateDeleteException("Product");

        //TODO review this with specification provider, do we really want to delete or just set as inactive ?
        //watch for cascading deletion issues, if we actually want to completely remove the rows
        productRepository.delete(productEntity);
    }

    @Override
    @Transactional
    public ProductDTO getById(Long productId){
        ProductEntity productEntity = productRepository.findOne(productId);

        if (productEntity == null)
            throw new EntityNotFoundException("Product", productId != null ? productId : "null");

        return mapperUtil.map(productEntity, ProductDTO.class);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long productId, CreateUpdateProductRequest updateProductRequest){
        ProductEntity productEntity = productRepository.findOne(productId);

        if (productEntity == null)
            throw new EntityNotFoundException("Product", productId != null ? productId : "null");

        //we have overridden the default equals method
        if (productEntity.getSeller() != contextHolderService.getCurrentUser())
            throw new ForbiddenUpdateDeleteException("Product");

        productInputParamsValidations(updateProductRequest);

        if (!productEntity.getName().equals(updateProductRequest.getName()) &&
                productRepository.findByName(updateProductRequest.getName()) != null)
            throw new EntityAlreadyExistsException("Product", "Name");

        productEntity.setName(updateProductRequest.getName());
        productEntity.setAmountAvailable(updateProductRequest.getAmountAvailable());
        productEntity.setCost(updateProductRequest.getCost());

        return mapperUtil.map(productRepository.save(productEntity), ProductDTO.class);
    }




    //validations... this could have been done on lombok/jackson side but i like to keep it here.
    //and have all the non null and more complex validations in the same place
    private void productInputParamsValidations(CreateUpdateProductRequest createProductRequest){
        if (createProductRequest.getAmountAvailable() == null ||
                createProductRequest.getAmountAvailable() < 0)
            throw new IllegalArgumentOnCreateUpdateException("Amount Available");

        if (createProductRequest.getCost() == null ||
                createProductRequest.getCost().compareTo(BigDecimal.ZERO) < 1)
            throw new IllegalArgumentOnCreateUpdateException("Cost cannot be null or less than zero");

        if (createProductRequest.getName() == null)
            throw new IllegalArgumentOnCreateUpdateException("Name cannot be null");

    }

}
