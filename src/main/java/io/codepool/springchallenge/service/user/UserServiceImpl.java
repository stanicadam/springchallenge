package io.codepool.springchallenge.service.user;

import io.codepool.springchallenge.common.exception.EntityAlreadyExistsException;
import io.codepool.springchallenge.common.exception.EntityNotFoundException;
import io.codepool.springchallenge.common.exception.ForbiddenUpdateDeleteException;
import io.codepool.springchallenge.common.exception.IllegalArgumentOnCreateUpdateException;
import io.codepool.springchallenge.common.mapper.MapperUtil;
import io.codepool.springchallenge.common.pojo.auth.BaseUserAuthDetails;
import io.codepool.springchallenge.common.pojo.auth.UserDTO;
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
    public UserDTO createUser(BaseUserAuthDetails registrationRequest){
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

        UserEntity userEntity = userRepository.findByIdAndActive(id, true);

        if (userEntity == null)
            throw new EntityNotFoundException("User", id != null ? id : "null");

        return mapperUtil.map(userEntity, UserDTO.class);
    }


    @Override
    @Transactional
    public UserDTO deleteUser(Long userId){

        UserEntity userEntity = userRepository.findOne(userId);

        if (userRepository.findOne(userId) == null)
            throw new EntityNotFoundException("User", userId.toString());

        userEntity.setActive(false);

        return mapperUtil.map(userRepository.save(userEntity), UserDTO.class);
    }


    @Override
    @Transactional
    public UserDTO updateUser(Long userId, BaseUserAuthDetails updateRequest){

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


        //validate new input
        userInputParamsValidations(updateRequest);


        userEntity.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        userEntity.setUsername(updateRequest.getUsername());

        return mapperUtil.map(userRepository.save(userEntity), UserDTO.class);
    }


    @Override
    @Transactional
    public List<UserDTO> getUsers(){
        return userRepository.findAll().stream().map(x->mapperUtil.map(x,UserDTO.class)).collect(Collectors.toList());
    }


    private void userInputParamsValidations(BaseUserAuthDetails userAuthDetails){
        if (userAuthDetails.getUsername() == null)
            throw new IllegalArgumentOnCreateUpdateException("Username cannot be null");

        if (userAuthDetails.getPassword() == null)
            throw new IllegalArgumentOnCreateUpdateException("Password cannot be null");
    }
}
