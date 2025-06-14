package com.example.tinyhouse.business.concretes;

import com.example.tinyhouse.business.abstracts.UserService;
import com.example.tinyhouse.business.constants.UserMessages;
import com.example.tinyhouse.core.utilities.results.*;
import com.example.tinyhouse.core.utilities.security.hashing.HashingHelper;
import com.example.tinyhouse.dataAccess.abstracts.UserDao;
import com.example.tinyhouse.entities.concretes.User;
import com.example.tinyhouse.entities.dtos.*;
import com.example.tinyhouse.entities.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserManager implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserManager(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public DataResult<List<UserListDto>> getAll(int requesterId) {
        DataResult<User> requester = getValidRequester(requesterId, UserRole.ADMIN);
        if (!requester.isSuccess()) {
            return new ErrorDataResult<>(null, requester.getMessage());
        }

        List<User> users = userDao.findAll();
        List<UserListDto> dtoList = users.stream()
                .map(user -> new UserListDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole(), user.isActive()))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(dtoList, UserMessages.USER_LISTED);
    }

    @Override
    public DataResult<UserDto> getById(int userId) {
        return userDao.findById(userId)
                .<DataResult<UserDto>>map(user -> new SuccessDataResult<>(
                        new UserDto(
                                user.getId(),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getEmail(),
                                user.getRole(),
                                user.isActive()
                        ),
                        UserMessages.USER_FOUND
                ))
                .orElseGet(() -> new ErrorDataResult<>(null, UserMessages.USER_NOT_FOUND));
    }

    @Override
    public DataResult<UserDto> add(UserCreateDto userDto) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (userDao.existsByEmail(userDto.getEmail())) {
            return new ErrorDataResult<>(UserMessages.EMAIL_ALREADY_EXISTS);
        }

        String passwordHash = HashingHelper.createPasswordHash(userDto.getPassword());

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPasswordHash(passwordHash);
        user.setRole(userDto.getRole());

        userDao.save(user);

        return new SuccessDataResult<>(
                new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole(), user.isActive()),
                UserMessages.USER_ADDED
        );
    }

    @Override
    public Result delete(int userId, int requesterId) {
        DataResult<User> requester = getValidRequester(requesterId, UserRole.ADMIN);
        if (!requester.isSuccess()) {
            return new ErrorResult(requester.getMessage());
        }

        if (!userDao.existsById(userId)) {
            return new ErrorResult(UserMessages.USER_NOT_FOUND);
        }

        userDao.deleteById(userId);
        return new SuccessResult(UserMessages.USER_DELETED);
    }

    @Override
    public DataResult<UserDto> update(UserUpdateDto dto, int requesterId) {
        DataResult<User> requester = getValidRequester(requesterId, UserRole.ADMIN);
        if (!requester.isSuccess()) {
            return new ErrorDataResult<>(requester.getMessage());
        }

        Optional<User> userOpt = userDao.findById(dto.getId());
        if (userOpt.isEmpty()) {
            return new ErrorDataResult<>(UserMessages.USER_NOT_FOUND);
        }

        User user = userOpt.get();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());

        userDao.save(user);

        return new SuccessDataResult<>(
                new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole(), user.isActive()),
                UserMessages.USER_UPDATED
        );
    }

    @Override
    public DataResult<UserDto> login(UserLoginDto dto)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        User user = userDao.getByEmail(dto.getEmail());
        if (user == null) {
            return new ErrorDataResult<>(UserMessages.USER_NOT_FOUND);
        }

        if (!HashingHelper.verifyPasswordHash(dto.getPassword(), user.getPasswordHash())) {
            return new ErrorDataResult<>(UserMessages.PASSWORD_INCORRECT);
        }

        UserDto resultDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole(), user.isActive());
        return new SuccessDataResult<>(resultDto, UserMessages.LOGIN_SUCCESSFUL);
    }

    @Override
    public DataResult<UserRegisterDto> register(UserRegisterDto dto)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        
        if (userDao.existsByEmail(dto.getEmail())) {
            return new ErrorDataResult<>(dto, UserMessages.EMAIL_ALREADY_EXISTS);
        }

        String hash = HashingHelper.createPasswordHash(dto.getPassword());

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(dto.getPassword());
        user.setPasswordHash(hash);
        user.setRole(dto.getRole());

        userDao.save(user);

        return new SuccessDataResult<>(dto, UserMessages.USER_REGISTERED);
    }

    private DataResult<User> getValidRequester(int requesterId, UserRole... allowedRoles) {
        Optional<User> userOpt = userDao.findById(requesterId);

        if (userOpt.isEmpty()) {
            return new ErrorDataResult<>(null, UserMessages.USER_NOT_FOUND);
        }

        User user = userOpt.get();

        boolean hasAccess = false;
        for (UserRole role : allowedRoles) {
            if (user.getRole() == role) {
                hasAccess = true;
                break;
            }
        }

        if (!hasAccess) {
            return new ErrorDataResult<>(null, UserMessages.UNAUTHORIZED_ACTION);
        }

        return new SuccessDataResult<>(user);
    }
}
