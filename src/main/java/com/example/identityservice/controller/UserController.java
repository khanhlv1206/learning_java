package com.example.identityservice.controller;

import com.example.identityservice.dto.request.ApiResponse;
import com.example.identityservice.dto.request.UserCreationRequest;
import com.example.identityservice.dto.request.UserUpdateRequest;
import com.example.identityservice.dto.response.UserResponse;
import com.example.identityservice.entity.User;
import com.example.identityservice.service.UserService;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
@JsonInclude(JsonInclude.Include.NON_NULL)
    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }
@GetMapping
    List<User> getUsers(){
        return userService.getUsers();
}
@GetMapping("/{userId}")
UserResponse getUser(@PathVariable("userId")  String userId){
        return userService.getUser(userId);
}
@PutMapping("/{userId}")
    UserResponse updateUser(@RequestBody UserUpdateRequest request, @PathVariable("userId")  String userId){
        return userService.updateUser(userId, request);
    }
@DeleteMapping("/{userId}")
String deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return "User deleted";
}
}
