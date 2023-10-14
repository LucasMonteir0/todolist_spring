package com.example.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity<String> create(@RequestBody UserModel newUser) {
        var user = userRepository.findByUsername(newUser.getUsername());
        if (user != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já cadastrado");
        }

        var cryptoPassword = BCrypt.withDefaults().hashToString(12, newUser.getPassword().toCharArray());
        newUser.setPassword(cryptoPassword);
        this.userRepository.save(newUser);
        return ResponseEntity.ok().body("Usuário criado com sucesso!");
    }
}
