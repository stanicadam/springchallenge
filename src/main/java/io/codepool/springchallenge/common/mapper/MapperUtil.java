package io.codepool.springchallenge.common.mapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.*;


/**
 * The type Mapper util.
 * We are implementing the ModelMapper library which is a simple library to map from one class type to another similar one.
 * We are extending its functionality to be able to dynamically convert from entity type to the
 * respective dto type, singularly or in bulk as part of a Page, for example.
 */
@Component
public class MapperUtil {

    /**
     * Inject the ModelMapper
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Init method.
     * We are able to map entities to DTO's
     * while hiding important properties and without exposing too much information to the front end/mobile app,
     * And reducing the amount of data transferred (important for both mobile and front end apps).
     */
    @PostConstruct
    public void init() {
    }


    /**
     * Method to dynamically map a Page of Entities fetched from db into page of DTO's
     *
     * @param <S> - type of DTO objects
     * @param <T> - type of entity pulled from db
     * @return page - List of DTO objects of type <S>.
     */
    public <S,T> Page<S> mapPageOfEntitiesToPageOfDTO(Page<T> entities, Class<S> clazz) {
        return entities.map(objectEntity -> modelMapper.map(objectEntity, clazz));
    }

    /**
     * Map object of type T into object of type S.
     *
     * @param <S>      the desired type
     * @param <T>      the incoming type
     * @return the result of mapping the object of type T into type S.
     */
    public <S,T> S map(T object, Class<S> clazz){
        return modelMapper.map(object, clazz);
    }
}


