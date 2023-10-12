package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.compositeservice.model.SetupTestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SetupTestController {

    @GetMapping("/setup-test")
    public SetupTestResponse response(String id, String input) {
        return new SetupTestResponse(id, "Hello, world! " + input);
    }
}
