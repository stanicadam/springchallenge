package io.codepool.springchallenge.service.user;

import io.codepool.springchallenge.common.enums.AuthorityEnum;
import io.codepool.springchallenge.common.exception.EntityAlreadyExistsException;
import io.codepool.springchallenge.common.exception.EntityNotFoundException;
import io.codepool.springchallenge.common.exception.IllegalArgumentOnCreateUpdateException;
import io.codepool.springchallenge.common.mapper.MapperUtil;
import io.codepool.springchallenge.common.pojo.auth.CreateUpdateUserRequest;
import io.codepool.springchallenge.common.pojo.auth.UserDTO;
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

    @Override
    @Transactional
    public UserDTO createUser(CreateUpdateUserRequest registrationRequest){
        //check if such user already exists
        if (userRepository.findByUsername(registrationRequest.getUsername()) != null)
            throw new EntityAlreadyExistsException("User", "Username");

        //validate new input
        userInputParamsValidations(registrationRequest);

        UserEntity userEntity = mapperUtil.map(registrationRequest, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        userEntity.setDeposit(BigDecimal.ZERO);

        return mapperUtil.map(userRepository.save(userEntity), UserDTO.class);
    }

    @Override
    @Transactional
    public UserDTO getById(Long id){

        UserEntity userEntity = userRepository.findOne(id);

        if (userEntity == null)
            throw new EntityNotFoundException("User", id != null ? id : "null");

        return mapperUtil.map(userEntity, UserDTO.class);
    }


    @Override
    @Transactional
    public void deleteUser(Long userId){

        UserEntity userEntity = userRepository.findOne(userId);

        if (userEntity == null)
            throw new EntityNotFoundException("User", userId.toString());


        //TODO review this with specification provider, do we really want to delete or just set as inactive ?
        //watch for cascading deletion issues, if we actually want to completely remove the rows
        userRepository.delete(userEntity);
    }


    @Override
    @Transactional
    public UserDTO updateUser(Long userId, CreateUpdateUserRequest updateRequest){

        //if we are trying to update a non existent user
        UserEntity userEntity = userRepository.findOne(userId);
        if (userEntity == null)
            throw new EntityNotFoundException("User", "Id");


        //if we changed the username, check if new username is available
        if (!userEntity.getUsername().equals(updateRequest.getUsername()) &&
                userRepository.findByUsername(updateRequest.getUsername()) != null)
            throw new EntityAlreadyExistsException("User", "Username");


        //validate new input
        userInputParamsValidations(updateRequest);


        userEntity.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        userEntity.setUsername(updateRequest.getUsername());
        userEntity.setRole(updateRequest.getRole());

        return mapperUtil.map(userRepository.save(userEntity), UserDTO.class);
    }


    @Override
    @Transactional
    public List<UserDTO> getUsers(){
        return userRepository.findAll().stream().map(x->mapperUtil.map(x,UserDTO.class)).collect(Collectors.toList());
    }


    private void userInputParamsValidations(CreateUpdateUserRequest userAuthDetails){
        if (userAuthDetails.getUsername() == null)
            throw new IllegalArgumentOnCreateUpdateException("Username cannot be null");

        if (userAuthDetails.getPassword() == null)
            throw new IllegalArgumentOnCreateUpdateException("Password cannot be null");

        if (userAuthDetails.getRole() == null)
            throw new IllegalArgumentOnCreateUpdateException("Role cannot be null");

        try {
            AuthorityEnum.valueOf(userAuthDetails.getRole());
        }catch (Exception e) {
            throw new IllegalArgumentOnCreateUpdateException("Please use SELLER or BUYER as role name");
        }

    }
}
