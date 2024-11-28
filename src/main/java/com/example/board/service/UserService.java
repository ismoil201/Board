package com.example.board.service;

import com.example.board.exception.user.UserAlreadyExistsException;
import com.example.board.exception.user.UserNotFoundException;
import com.example.board.model.entity.UserEntity;
import com.example.board.model.user.User;
import com.example.board.model.user.UserUpdateRequestBody;
import com.example.board.model.user.UserUserRequestBody;
import com.example.board.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        return userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public List<User> getUsers() {

        var userList = userEntityRepository.findAll();


        return userList.stream().map(User::from).toList();
    }

    public User save(UserUserRequestBody userUserRequestBody) {
        var user = new UserEntity();

        user.setUsername(userUserRequestBody.username());
        user.setProfile(userUserRequestBody.profile());
        user.setDescription(userUserRequestBody.description());

        UserEntity save = userEntityRepository.save(user);

        return User.from(save);
    }

    public void delete(String username, String password) {

        Optional<UserEntity> byUsername = userEntityRepository.findByUsername(username);

        if (byUsername.isPresent()) {
            if (password.equals(byUsername.get().getPassword())) {
                userEntityRepository.delete(byUsername.get());
            }
        } else {
            throw new UserNotFoundException(username);
        }
    }

    public User Update(String username, String password, UserUpdateRequestBody userUpdateRequestBody) {

        Optional<UserEntity> byUsername = userEntityRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            if (password.equals(byUsername.get().getPassword())) {
                UserEntity userEntity = byUsername.get();
                userEntity.setDescription(userUpdateRequestBody.description());
                userEntity.setProfile(userUpdateRequestBody.profile());
                userEntity.setUsername(userUpdateRequestBody.username());
                userEntityRepository.save(userEntity);
                return User.from(userEntity);
            }
        } else {
            throw new UserNotFoundException(username);
        }
        return null;
    }

    public User signUp(String username, String password) {

        userEntityRepository
                .findByUsername(username)
                .ifPresent(
                        user -> {
                            throw new UserAlreadyExistsException();
                        }
                );

        var userEntity = userEntityRepository.save(UserEntity.of(username, bCryptPasswordEncoder.encode(password)));

        return User.from(userEntity);
    }
}
