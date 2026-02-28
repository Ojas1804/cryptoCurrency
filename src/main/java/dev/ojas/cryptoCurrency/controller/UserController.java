package dev.ojas.cryptoCurrency.controller;

import dev.ojas.cryptoCurrency.dto.NewUserInboundDto;
import dev.ojas.cryptoCurrency.dto.NewUserResponseDto;
import dev.ojas.cryptoCurrency.model.User;
import dev.ojas.cryptoCurrency.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    @ResponseBody
    public NewUserResponseDto createNewUser(@RequestBody NewUserInboundDto newUserInboundDto) throws Exception {
        return this.userService.saveUser(newUserInboundDto);
    }

    @GetMapping("/user/{id}")
    @ResponseBody
    public Optional<User> getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    // Get users with pagination
    @GetMapping("/user")
    @ResponseBody
    public Page<User> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsers(page, size);
    }
}
