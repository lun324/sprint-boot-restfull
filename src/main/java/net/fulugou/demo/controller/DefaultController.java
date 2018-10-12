package net.fulugou.demo.controller;

import net.fulugou.demo.util.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    @GetMapping("/401")
    public ResponseEntity<?> _401() {
        ApiResponse response = new ApiResponse("1", "无权访问");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/403")
    public ResponseEntity<?> _403() {
        ApiResponse response = new ApiResponse("1", "禁止访问");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
