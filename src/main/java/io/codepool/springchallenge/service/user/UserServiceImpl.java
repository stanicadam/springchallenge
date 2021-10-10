package io.codepool.springchallenge.service.user;

import io.codepool.springchallenge.common.exception.EntityAlreadyExistsException;
import io.codepool.springchallenge.common.exception.EntityNotFoundException;
import io.codepool.springchallenge.common.exception.ForbiddenUpdateDeleteException;
import io.codepool.springchallenge.common.mapper.MapperUtil;
import io.codepool.springchallenge.common.pojo.auth.UserCreateUpdateRequest;
import io.codepool.springchallenge.common.pojo.UserDTO;
import io.codepool.springchallenge.common.services.ContextHolderService;
import io.codepool.springchallenge.dao.model.UserEntity;
import io.codepool.springchallenge.dao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MapperUtil mapperUtil;

    @Autowired
    private ContextHolderService contextHolderService;


    @Override
    @Transactional
    public UserDTO registerNewUser(UserCreateUpdateRequest registrationRequest){
        //check if such user already exists
        if (userRepository.findByUsername(registrationRequest.getUsername()) != null)
            throw new EntityAlreadyExistsException("User", "Username");

        UserEntity userEntity = mapperUtil.map(registrationRequest, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        userEntity.setDeposit(BigDecimal.ZERO);

        return mapperUtil.map(userRepository.save(userEntity), UserDTO.class);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long userId, UserCreateUpdateRequest updateRequest){

        //if we are trying to update a user that is not us
        if(contextHolderService.getCurrentUser().getId() != userId)
            throw new ForbiddenUpdateDeleteException("User");


        //if we are trying to update a non existent user
        UserEntity userEntity = userRepository.findOne(userId);
        if (userEntity == null)
            throw new EntityNotFoundException("User", "Id");


        //if we changed the username, check if new username is available
        if (!userEntity.getUsername().equals(updateRequest.getUsername()) &&
                userRepository.findByUsername(updateRequest.getUsername()) != null)
            throw new EntityAlreadyExistsException("User", "Username");


        userEntity.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        userEntity.setUsername(updateRequest.getUsername());

        return mapperUtil.map(userRepository.save(userEntity), UserDTO.class);
    }


    @Override
    public List<UserDTO> getUsers(){
        return userRepository.findAll().stream().map(x->mapperUtil.map(x,UserDTO.class)).collect(Collectors.toList());
    }

}
