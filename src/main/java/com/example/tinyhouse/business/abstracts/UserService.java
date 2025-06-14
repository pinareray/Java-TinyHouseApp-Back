package com.example.tinyhouse.business.abstracts;

import com.example.tinyhouse.core.utilities.results.DataResult;
import com.example.tinyhouse.core.utilities.results.Result;
import com.example.tinyhouse.entities.dtos.*;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {
    DataResult<List<UserListDto>> getAll(int requesterId);
    DataResult<UserDto> getById(int userId);
    DataResult<UserDto> add(UserCreateDto user) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    Result delete(int userId, int requesterId);
    DataResult<UserDto> update(UserUpdateDto dto, int requesterId);
    DataResult<UserDto> login(UserLoginDto userLoginDto) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException;
    DataResult<UserRegisterDto> register(UserRegisterDto userRegisterDto) throws UnsupportedEncodingException, NoSuchAlgorithmException;
}
