package com.example.board.service;

import com.example.board.exception.follow.FollowAlreadyExistsException;
import com.example.board.exception.follow.FollowNotFoundException;
import com.example.board.exception.follow.InvalidFollowException;
import com.example.board.exception.user.UserAlreadyExistsException;
import com.example.board.exception.user.UserNotAllowedException;
import com.example.board.exception.user.UserNotFoundException;
import com.example.board.model.entity.FollowEntity;
import com.example.board.model.entity.UserEntity;
import com.example.board.model.user.*;
import com.example.board.repository.FollowEntityRepository;
import com.example.board.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private FollowEntityRepository followEntityRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        return userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public User signUp(String username, String password) {

        userEntityRepository
                .findByUsername(username)
                .ifPresent(
                        user -> {
                            throw new UserAlreadyExistsException();
                        }
                );

        var userEntity = userEntityRepository.save(UserEntity.of(username, passwordEncoder.encode(password)));

        return User.from(userEntity);
    }

    public UserAuthenticationResponse authenticate(String username, String password) {
        var userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (passwordEncoder.matches(password, userEntity.getPassword())) {

            var accessToken = jwtService.generateAccessToken(userEntity);
            return new UserAuthenticationResponse(accessToken);
        } else {

            throw new UserNotFoundException(username);
        }

    }

    public List<User> getUsers(String query, UserEntity currentUser) {

        List<UserEntity> userEntities;

        if (query != null && !query.isBlank()) {

            userEntities = userEntityRepository.findByUsernameContaining(query);
        } else {
            userEntities = userEntityRepository.findAll();
        }

        return userEntities.stream().map(userEntity ->
                getUserWithFollowingStatus(userEntity, currentUser)).toList();
    }

    public User getUser(String username, UserEntity currentUser) {
        var userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return getUserWithFollowingStatus(userEntity, currentUser);
    }

    private  User getUserWithFollowingStatus(UserEntity userEntity, UserEntity currentUser) {

        var isFollowing =   followEntityRepository
                .findByFollowerAndFollowing(currentUser, userEntity).isPresent();
        return User.from(userEntity,isFollowing);
    }


    public User upDateUser(
            String username, UserPatchRequestBody userPatchRequestBody, UserEntity currentUserEntity) {

        var userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (!userEntity.equals(currentUserEntity)) {
            throw new UserNotAllowedException();
        }

        if (userPatchRequestBody.description() != null) {
            userEntity.setDescription(userPatchRequestBody.description());
        }

        return User.from(userEntityRepository.save(userEntity));
    }

    @Transactional
    public User follow(String username, UserEntity currentUser) {
        var following = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (following.equals(currentUser)) {
            throw new InvalidFollowException("A user cannot follow themselves.");
        }

        followEntityRepository.findByFollowerAndFollowing(currentUser, following)
                .ifPresent(follow -> {
                    throw new FollowAlreadyExistsException(currentUser, following);
                });

        followEntityRepository.save(FollowEntity.of(currentUser, following));

        following.setFollowersCount(Math.max(0, following.getFollowersCount() + 1));
        currentUser.setFollowingsCount(Math.max(0, following.getFollowingsCount() + 1));
        userEntityRepository.saveAll(List.of(following, currentUser));
        return User.from(following,true);
    }


    @Transactional
    public User unFollow(String username, UserEntity currentUser) {
        var following = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (following.equals(currentUser)) {
            throw new InvalidFollowException("A user cannot follow themselves.");
        }

        var followEntity = followEntityRepository.findByFollowerAndFollowing(currentUser, following)
                .orElseThrow(() ->
                        new FollowNotFoundException(currentUser, following));

        followEntityRepository.delete(followEntity);

        following.setFollowersCount(Math.max(0, following.getFollowersCount() - 1));
        currentUser.setFollowingsCount(Math.max(0, following.getFollowingsCount() - 1));
        userEntityRepository.saveAll(List.of(following, currentUser));
        return User.from(following, false);
    }

    public List<User> getFollowersByUsername(String username, UserEntity currentUser) {
        var following = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        var followEntities = followEntityRepository.findByFollowing(following);

        return followEntities.stream().map(follow ->
                getUserWithFollowingStatus(follow.getFollower(), currentUser)).toList();
    }

    public List<User> getFollowingsByUsername(String username, UserEntity currentUser) {
        var follower = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        var followEntities = followEntityRepository.findByFollower(follower);

        return followEntities.stream().map(follow ->
                getUserWithFollowingStatus(follow.getFollowing(), currentUser)).toList();
    }
}
