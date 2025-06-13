package com.example.tinyhouse.api;

import com.example.tinyhouse.business.abstracts.UserService;
import com.example.tinyhouse.core.utilities.results.DataResult;
import com.example.tinyhouse.core.utilities.results.Result;
import com.example.tinyhouse.entities.dtos.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UsersController {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getall")
    public ResponseEntity<DataResult<List<UserListDto>>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/getbyid")
    public ResponseEntity<DataResult<UserDto>> getById(@RequestParam int id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<DataResult<UserDto>> add(@Valid @RequestBody UserCreateDto userDto)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return ResponseEntity.ok(userService.add(userDto));
    }

    @PutMapping("/update")
    public ResponseEntity<DataResult<UserDto>> update(@Valid @RequestBody UserUpdateDto userDto) {
        return ResponseEntity.ok(userService.update(userDto));
    }

    @DeleteMapping("/deletebyid")
    public ResponseEntity<Result> deleteById(@RequestParam int id) {
        return ResponseEntity.ok(userService.delete(id));
    }

    @PostMapping("/login")
    public ResponseEntity<DataResult<UserDto>> login(@Valid @RequestBody UserLoginDto dto)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        return ResponseEntity.ok(userService.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<DataResult<UserRegisterDto>> register(@Valid @RequestBody UserRegisterDto dto)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return ResponseEntity.ok(userService.register(dto));
    }
}
