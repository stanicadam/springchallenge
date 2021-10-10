package io.codepool.springchallenge.service.user;

import io.codepool.springchallenge.common.exception.EntityAlreadyExistsException;
import io.codepool.springchallenge.common.mapper.MapperUtil;
import io.codepool.springchallenge.common.pojo.auth.RegistrationRequest;
import io.codepool.springchallenge.common.pojo.UserDTO;
import io.codepool.springchallenge.dao.model.UserEntity;
import io.codepool.springchallenge.dao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
    public UserDTO registerNewUser(RegistrationRequest registrationRequest){
        //check if such user already exists
        if (!userRepository.findDuplicateUserDetails(registrationRequest.getEmail(), registrationRequest.getUsername()).isEmpty())
            throw new EntityAlreadyExistsException("User");

        UserEntity userEntity = mapperUtil.map(registrationRequest, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        userEntity.setDeposit(BigDecimal.ZERO);

        return mapperUtil.map(userRepository.save(userEntity), UserDTO.class);
    }
}
